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
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to read emails from a Gmail IMAP inbox and extract password
 * reset links sent to a particular email address (which may be a Gmail
 * "+suffix" alias of the inbox owner).
 */
public final class MailReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailReader.class);

    private static final String IMAP_HOST = "imap.gmail.com";
    private static final int IMAP_PORT = 993;

    private static final Pattern RESET_LINK_PATTERN = Pattern.compile(
            "https?://[\\w.-]*underarmour\\.com[^\\s\"'>)]*",
            Pattern.CASE_INSENSITIVE);

    private MailReader() {
    }

    /**
     * Waits for an email delivered to {@code toAddress} whose subject contains
     * {@code subjectContains}, and returns its body (plain text or HTML).
     *
     * @param inboxUser       Gmail account to read from (e.g. dmitryprimudriv@gmail.com)
     * @param inboxPassword   Gmail app password for IMAP access (spaces stripped)
     * @param toAddress       address the email was sent to (may be a + alias)
     * @param subjectContains substring to search for in the subject
     * @param timeoutSeconds  max number of seconds to wait
     * @return raw text body of the most recent matching email
     */
    public static String fetchEmailBody(String inboxUser,
                                        String inboxPassword,
                                        String toAddress,
                                        String subjectContains,
                                        int timeoutSeconds) {
        return fetchEmailBody(inboxUser, inboxPassword, toAddress,
                subjectContains, timeoutSeconds, 0L);
    }

    /**
     * Same as {@link #fetchEmailBody(String,String,String,String,int)} but
     * only matches emails received at or after {@code sinceEpochMillis}.
     * This avoids picking up stale reset emails from previous test runs when
     * the inbox already contains many matching messages.
     */
    public static String fetchEmailBody(String inboxUser,
                                        String inboxPassword,
                                        String toAddress,
                                        String subjectContains,
                                        int timeoutSeconds,
                                        long sinceEpochMillis) {
        // Gmail app passwords are 16 chars without spaces; users often paste
        // them with spaces. Strip whitespace so IMAP login does not fail.
        String cleanPassword = inboxPassword.replaceAll("\\s+", "");

        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.host", IMAP_HOST);
        props.put("mail.imaps.port", String.valueOf(IMAP_PORT));
        props.put("mail.imaps.ssl.enable", "true");
        // Java 21 disables some older TLS ciphers; enable modern ones.
        props.put("mail.imaps.ssl.protocols", "TLSv1.2 TLSv1.3");
        props.put("mail.imaps.ssl.trust", "*");
        props.put("mail.imaps.ssl.checkserveridentity", "false");
        props.put("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.imaps.socketFactory.fallback", "false");
        props.put("mail.imaps.socketFactory.port", String.valueOf(IMAP_PORT));

        Session session = Session.getInstance(props);

        long deadline = System.currentTimeMillis() + timeoutSeconds * 1000L;
        int attempt = 0;
        while (System.currentTimeMillis() < deadline) {
            attempt++;
            Store store = null;
            try {
                store = session.getStore("imaps");
                store.connect(IMAP_HOST, IMAP_PORT, inboxUser, cleanPassword);
                Folder inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_ONLY);
                try {
                    SearchTerm subjectTerm = new SubjectTerm(subjectContains);
                    Message[] messages = inbox.search(subjectTerm);
                    LOGGER.info("Attempt {}: found {} messages matching subject='{}'",
                            attempt, messages.length, subjectContains);
                    // Iterate newest-first and find a recent match addressed
                    // to toAddress and (if specified) received after
                    // sinceEpochMillis.
                    for (int i = messages.length - 1; i >= 0; i--) {
                        Message msg = messages[i];
                        if (!isFreshEnough(msg, sinceEpochMillis)) {
                            continue;
                        }
                        if (matchesRecipient(msg, toAddress)) {
                            return extractTextFromMessage(msg);
                        }
                    }
                    // Fallback: accept latest if the body references the
                    // target address (Gmail sometimes folds the alias).
                    for (int i = messages.length - 1; i >= 0; i--) {
                        Message msg = messages[i];
                        if (!isFreshEnough(msg, sinceEpochMillis)) {
                            continue;
                        }
                        String body = extractTextFromMessage(msg);
                        if (body != null && body.contains(toAddress)) {
                            return body;
                        }
                    }
                } finally {
                    inbox.close(false);
                }
            } catch (Exception e) {
                LOGGER.warn("Attempt {} failed to fetch mail: {}", attempt, e.getMessage());
            } finally {
                if (store != null) {
                    try {
                        store.close();
                    } catch (Exception ignored) {
                    }
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        throw new RuntimeException("Did not receive email to '" + toAddress
                + "' with subject containing '" + subjectContains
                + "' within " + timeoutSeconds + "s");
    }

    /**
     * Extracts the first underarmour.com reset URL found in the email body.
     */
    public static String extractResetLink(String emailBody) {
        Matcher m = RESET_LINK_PATTERN.matcher(emailBody);
        while (m.find()) {
            String url = m.group();
            // Prefer reset/verify token URLs over generic ones
            if (url.contains("verify") || url.contains("reset") || url.contains("token")) {
                return cleanUrl(url);
            }
        }
        Matcher mFallback = RESET_LINK_PATTERN.matcher(emailBody);
        if (mFallback.find()) {
            return cleanUrl(mFallback.group());
        }
        throw new RuntimeException("Reset link not found in email body");
    }

    private static String cleanUrl(String url) {
        // Trim trailing punctuation/quotes/closing brackets and decode HTML
        // ampersand entities that are often present in HTML email bodies.
        return url.replace("&amp;", "&").replaceAll("[\\)\\]\"'>,.]+$", "");
    }

    private static boolean isFreshEnough(Message msg, long sinceEpochMillis) {
        if (sinceEpochMillis <= 0L) {
            return true;
        }
        try {
            java.util.Date received = msg.getReceivedDate();
            if (received == null) {
                received = msg.getSentDate();
            }
            // Allow 10s clock skew between the test host and the mail server.
            return received != null
                    && received.getTime() >= sinceEpochMillis - 10_000L;
        } catch (Exception e) {
            return true;
        }
    }

    private static boolean matchesRecipient(Message msg, String toAddress) {
        try {
            Address[] recipients = msg.getAllRecipients();
            if (recipients == null) {
                return false;
            }
            for (Address a : recipients) {
                if (a != null && a.toString().toLowerCase().contains(toAddress.toLowerCase())) {
                    return true;
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    private static String extractTextFromMessage(Message message) throws Exception {
        Object content = message.getContent();
        if (content instanceof String) {
            return (String) content;
        }
        if (content instanceof Multipart) {
            return extractFromMultipart((Multipart) content);
        }
        return String.valueOf(content);
    }

    private static String extractFromMultipart(Multipart multipart) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < multipart.getCount(); i++) {
            Part part = multipart.getBodyPart(i);
            appendPartText(part, sb);
        }
        return sb.toString();
    }

    private static void appendPartText(Part part, StringBuilder sb) throws Exception {
        if (part.isMimeType("text/plain") || part.isMimeType("text/html")) {
            Object c = part.getContent();
            if (c != null) {
                sb.append(c).append('\n');
            }
        } else if (part.getContent() instanceof Multipart) {
            Multipart mp = (Multipart) part.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                appendPartText(mp.getBodyPart(i), sb);
            }
        } else if (part instanceof MimeBodyPart) {
            try {
                String s = ((MimeBodyPart) part).getContent().toString();
                sb.append(s).append('\n');
            } catch (IOException ignored) {
            }
        }
    }
}
