package com.solvd.demo.utils;

import jakarta.mail.Address;
import jakarta.mail.BodyPart;
import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.search.AndTerm;
import jakarta.mail.search.RecipientStringTerm;
import jakarta.mail.search.SearchTerm;
import jakarta.mail.search.SubjectTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility for connecting to a Gmail mailbox over IMAP and retrieving the most
 * recent email matching a recipient (e.g. an alias such as user+1@gmail.com)
 * and a subject substring.
 *
 * Authentication uses a Gmail app password.
 *
 * Credentials are read from system properties first (`mail.user`, `mail.password`),
 * with `_config.properties` keys as fallback (resolved by callers).
 */
public class GmailClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(GmailClient.class);

    private static final String IMAP_HOST = "imap.gmail.com";
    private static final int IMAP_PORT = 993;
    private static final String INBOX_FOLDER = "INBOX";

    private final String username;
    private final String appPassword;

    public GmailClient(String username, String appPassword) {
        this.username = username;
        this.appPassword = appPassword;
    }

    /**
     * Poll Gmail INBOX for the most recent email delivered to the supplied
     * recipient (alias) whose subject contains the supplied substring.
     *
     * @param toAddress       full recipient address (alias supported, e.g. user+1@gmail.com)
     * @param subjectContains substring expected in the email subject
     * @param timeoutSeconds  total polling timeout in seconds
     * @return the matched email, or null if nothing arrived in time
     */
    public EmailMessage waitForEmail(String toAddress, String subjectContains, int timeoutSeconds)
            throws MessagingException, IOException, InterruptedException {
        long deadline = System.currentTimeMillis() + (timeoutSeconds * 1000L);

        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.host", IMAP_HOST);
        props.put("mail.imaps.port", String.valueOf(IMAP_PORT));
        props.put("mail.imaps.ssl.enable", "true");

        Session session = Session.getInstance(props);
        Store store = session.getStore("imaps");
        store.connect(IMAP_HOST, IMAP_PORT, username, appPassword);
        try {
            Folder inbox = store.getFolder(INBOX_FOLDER);
            inbox.open(Folder.READ_ONLY);
            try {
                SearchTerm searchTerm = new AndTerm(
                        new RecipientStringTerm(Message.RecipientType.TO, toAddress),
                        new SubjectTerm(subjectContains));

                while (System.currentTimeMillis() < deadline) {
                    Message[] messages = inbox.search(searchTerm);
                    if (messages != null && messages.length > 0) {
                        Message latest = messages[messages.length - 1];
                        return toEmailMessage(latest);
                    }
                    LOGGER.info("No email match yet for to='{}' subject contains '{}'. Sleeping...",
                            toAddress, subjectContains);
                    Thread.sleep(5000L);
                }
                return null;
            } finally {
                inbox.close(false);
            }
        } finally {
            store.close();
        }
    }

    private EmailMessage toEmailMessage(Message message) throws MessagingException, IOException {
        String subject = message.getSubject();
        String from = "";
        Address[] froms = message.getFrom();
        if (froms != null && froms.length > 0) {
            from = froms[0].toString();
        }
        String body = extractBody(message);
        return new EmailMessage(subject, from, body);
    }

    private String extractBody(Message message) throws IOException, MessagingException {
        Object content = message.getContent();
        if (content instanceof String) {
            return (String) content;
        }
        if (content instanceof Multipart) {
            return extractMultipart((Multipart) content);
        }
        return "";
    }

    private String extractMultipart(Multipart multipart) throws MessagingException, IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart part = multipart.getBodyPart(i);
            String disposition = part.getDisposition();
            if (disposition != null
                    && (disposition.equalsIgnoreCase(Part.ATTACHMENT)
                    || disposition.equalsIgnoreCase(Part.INLINE))) {
                continue;
            }
            Object partContent = part.getContent();
            if (partContent instanceof String) {
                sb.append((String) partContent).append("\n");
            } else if (partContent instanceof Multipart) {
                sb.append(extractMultipart((Multipart) partContent));
            }
        }
        return sb.toString();
    }

    private static final Pattern RESET_PASSWORD_LINK_PATTERN = Pattern.compile(
            "https://login\\.shop\\.underarmour\\.com/email/verify/[A-Za-z0-9]+\\?token=[A-Za-z0-9]+(?:&amp;|&)?[^\"'\\s<>]*",
            Pattern.CASE_INSENSITIVE);

    /** Locate the password-reset URL inside the email body. Returns null if not found. */
    public static String extractResetPasswordLink(String body) {
        if (body == null) {
            return null;
        }
        Matcher matcher = RESET_PASSWORD_LINK_PATTERN.matcher(body);
        if (matcher.find()) {
            return matcher.group().replace("&amp;", "&");
        }
        return null;
    }

    /** Minimal holder for the data we assert on. */
    public static final class EmailMessage {
        private final String subject;
        private final String from;
        private final String body;

        public EmailMessage(String subject, String from, String body) {
            this.subject = subject;
            this.from = from;
            this.body = body;
        }

        public String getSubject() {
            return subject;
        }

        public String getFrom() {
            return from;
        }

        public String getBody() {
            return body;
        }
    }

    private static final class Part {
        private static final String ATTACHMENT = "ATTACHMENT";
        private static final String INLINE = "INLINE";
    }

    // Mark message read after assertion if needed (helper hook)
    public void markRead(Message message) throws MessagingException {
        message.setFlag(Flags.Flag.SEEN, true);
    }
}
