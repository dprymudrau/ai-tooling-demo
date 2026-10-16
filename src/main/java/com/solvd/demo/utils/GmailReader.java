package com.solvd.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.AndTerm;
import javax.mail.search.RecipientStringTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to read emails from a Gmail account using IMAP.
 */
public class GmailReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(GmailReader.class);

    private static final String IMAP_HOST = "imap.gmail.com";
    private static final String IMAP_PORT = "993";

    private final String mailboxAddress;
    private final String appPassword;

    public GmailReader(String mailboxAddress, String appPassword) {
        this.mailboxAddress = mailboxAddress;
        this.appPassword = appPassword;
    }

    /**
     * Waits up to {@code timeoutSeconds} for a message that was sent to {@code toAddress}
     * with a subject that contains {@code subjectContains}. Returns the raw message body
     * (HTML or plain text). Returns {@code null} if no matching message is received in time.
     */
    public String waitForEmail(String toAddress, String subjectContains, int timeoutSeconds) {
        long deadline = System.currentTimeMillis() + (timeoutSeconds * 1000L);
        int attempt = 0;

        while (System.currentTimeMillis() < deadline) {
            attempt++;
            LOGGER.info("Attempt #{} to find email to '{}' with subject containing '{}'",
                    attempt, toAddress, subjectContains);

            String body = tryFetch(toAddress, subjectContains);
            if (body != null) {
                return body;
            }

            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        return null;
    }

    private String tryFetch(String toAddress, String subjectContains) {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.host", IMAP_HOST);
        props.put("mail.imaps.port", IMAP_PORT);
        props.put("mail.imaps.ssl.enable", "true");
        // Modern TLS settings - Java 21 disables older protocols by default
        props.put("mail.imaps.ssl.protocols", "TLSv1.2 TLSv1.3");
        props.put("mail.imaps.starttls.enable", "true");
        props.put("mail.imaps.ssl.trust", "*");
        props.put("mail.imaps.ssl.checkserveridentity", "false");
        props.put("mail.imaps.connectiontimeout", "30000");
        props.put("mail.imaps.timeout", "30000");

        Store store = null;
        Folder folder = null;
        try {
            Session session = Session.getInstance(props);
            store = session.getStore("imaps");
            store.connect(IMAP_HOST, mailboxAddress, appPassword);

            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            SearchTerm searchTerm = new AndTerm(
                    new RecipientStringTerm(Message.RecipientType.TO, toAddress),
                    new SubjectTerm(subjectContains)
            );

            Message[] messages = folder.search(searchTerm);
            LOGGER.info("Found {} messages matching search criteria.", messages.length);
            if (messages.length == 0) {
                return null;
            }

            // Pick most recent message
            Message latest = messages[messages.length - 1];
            // double-check recipient (RecipientStringTerm sometimes matches loosely)
            Address[] recipients = latest.getRecipients(Message.RecipientType.TO);
            boolean recipientMatches = false;
            if (recipients != null) {
                for (Address a : recipients) {
                    if (a.toString().toLowerCase().contains(toAddress.toLowerCase())) {
                        recipientMatches = true;
                        break;
                    }
                }
            }
            if (!recipientMatches) {
                LOGGER.info("Recipient mismatch. Skipping.");
                return null;
            }

            return extractBody(latest);
        } catch (Exception e) {
            LOGGER.warn("Error while fetching emails: {}", e.getMessage());
            return null;
        } finally {
            try {
                if (folder != null && folder.isOpen()) {
                    folder.close(false);
                }
                if (store != null) {
                    store.close();
                }
            } catch (Exception ignored) {
                // ignore
            }
        }
    }

    private String extractBody(Part part) throws Exception {
        if (part.isMimeType("text/html")) {
            return (String) part.getContent();
        }
        if (part.isMimeType("text/plain")) {
            return (String) part.getContent();
        }
        if (part.isMimeType("multipart/*")) {
            Multipart multi = (Multipart) part.getContent();
            String htmlBody = null;
            String textBody = null;
            for (int i = 0; i < multi.getCount(); i++) {
                MimeBodyPart bodyPart = (MimeBodyPart) multi.getBodyPart(i);
                if (bodyPart.isMimeType("text/html") && htmlBody == null) {
                    htmlBody = (String) bodyPart.getContent();
                } else if (bodyPart.isMimeType("text/plain") && textBody == null) {
                    textBody = (String) bodyPart.getContent();
                } else if (bodyPart.isMimeType("multipart/*")) {
                    String nested = extractBody(bodyPart);
                    if (nested != null && htmlBody == null) {
                        htmlBody = nested;
                    }
                }
            }
            return htmlBody != null ? htmlBody : textBody;
        }
        try {
            Object content = part.getContent();
            if (content instanceof String) {
                return (String) content;
            }
        } catch (IOException ignored) {
            // ignore
        }
        return null;
    }

    /**
     * Extracts the password reset link from the supplied email body.
     * Returns {@code null} when no link is found.
     */
    public static String extractResetPasswordLink(String body) {
        if (body == null) {
            return null;
        }
        Pattern pattern = Pattern.compile("https://login\\.shop\\.underarmour\\.com/email/verify/[A-Za-z0-9_\\-?=&%.]+");
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            return matcher.group(0).replace("&amp;", "&");
        }
        return null;
    }
}
