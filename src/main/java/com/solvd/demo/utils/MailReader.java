package com.solvd.demo.utils;

import jakarta.mail.Address;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.search.AndTerm;
import jakarta.mail.search.RecipientStringTerm;
import jakarta.mail.search.SearchTerm;
import jakarta.mail.search.SubjectTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility for fetching the Under Armour password-reset link.
 * <p>
 * Resolution order:
 * <ol>
 *     <li>System property / env var {@code RESET_PASSWORD_LINK} - direct link injection.</li>
 *     <li>Gmail IMAP poll using {@code GMAIL_USER} + {@code GMAIL_APP_PASSWORD}.</li>
 * </ol>
 */
public final class MailReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailReader.class);

    private static final String IMAP_HOST = "imap.gmail.com";
    private static final int IMAP_PORT = 993;

    private static final Pattern RESET_LINK_PATTERN =
            Pattern.compile("https://login\\.shop\\.underarmour\\.com/email/verify/[A-Za-z0-9_\\-]+(?:\\?[^\\s\"'<>]+)?");

    private MailReader() {
    }

    /**
     * Fetches the latest password reset link for the given recipient.
     *
     * @param toAddress       recipient email (may contain '+alias')
     * @param subjectContains subject filter, e.g. "Password Reset Request"
     * @param timeoutSeconds  max IMAP wait time
     * @return the password reset URL
     */
    public static String fetchResetPasswordLink(String toAddress,
                                                String subjectContains,
                                                int timeoutSeconds) {
        // 1. Direct link injection - useful for CI runs where IMAP creds aren't available.
        String injected = getProperty("RESET_PASSWORD_LINK");
        if (injected != null && !injected.isEmpty()) {
            LOGGER.info("Using RESET_PASSWORD_LINK provided via env/sysprop: {}", injected);
            Matcher m = RESET_LINK_PATTERN.matcher(injected);
            if (m.find()) {
                return m.group();
            }
            return injected;
        }

        // 2. File-based delivery - useful when an external process (or operator)
        // fetches the email and writes the link to a known path. Path can be
        // overridden via RESET_PASSWORD_LINK_FILE; defaults to /tmp/ua_reset_link.txt.
        String filePath = getProperty("RESET_PASSWORD_LINK_FILE");
        if (filePath == null || filePath.isEmpty()) {
            filePath = "/tmp/ua_reset_link.txt";
        }
        String fromFile = pollForLinkInFile(filePath, timeoutSeconds);
        if (fromFile != null) {
            return fromFile;
        }

        // 3. IMAP fallback.
        return fetchViaImap(toAddress, subjectContains, timeoutSeconds);
    }

    /**
     * Polls the given file path until a reset link is written into it or the
     * timeout expires. The link is matched via {@link #RESET_LINK_PATTERN}.
     * Returns {@code null} if no link is found before the timeout.
     */
    private static String pollForLinkInFile(String filePath, int timeoutSeconds) {
        java.io.File file = new java.io.File(filePath);
        long deadline = System.currentTimeMillis() + timeoutSeconds * 1000L;
        LOGGER.info("Polling {} for reset password link (timeout={}s)", filePath, timeoutSeconds);
        while (System.currentTimeMillis() < deadline) {
            if (file.exists() && file.length() > 0) {
                try {
                    String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
                    Matcher m = RESET_LINK_PATTERN.matcher(content);
                    if (m.find()) {
                        String link = m.group();
                        LOGGER.info("Picked up reset password link from {}: {}", filePath, link);
                        return link;
                    }
                } catch (Exception e) {
                    LOGGER.warn("Failed to read {}: {}", filePath, e.getMessage());
                }
            }
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        LOGGER.info("No reset link found in {} within {}s, falling through to IMAP",
                filePath, timeoutSeconds);
        return null;
    }

    private static String fetchViaImap(String toAddress, String subjectContains, int timeoutSeconds) {
        String user = getRequiredProperty("GMAIL_USER");
        String appPassword = getRequiredProperty("GMAIL_APP_PASSWORD");

        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.host", IMAP_HOST);
        props.put("mail.imaps.port", String.valueOf(IMAP_PORT));
        props.put("mail.imaps.ssl.enable", "true");
        props.put("mail.imaps.connectiontimeout", "10000");
        props.put("mail.imaps.timeout", "10000");

        long deadline = System.currentTimeMillis() + timeoutSeconds * 1000L;
        long pollInterval = 5000L;

        while (System.currentTimeMillis() < deadline) {
            Store store = null;
            Folder inbox = null;
            try {
                Session session = Session.getInstance(props);
                store = session.getStore("imaps");
                store.connect(IMAP_HOST, user, appPassword);

                inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_ONLY);

                SearchTerm term = new AndTerm(
                        new SubjectTerm(subjectContains),
                        new RecipientStringTerm(Message.RecipientType.TO, toAddress)
                );
                Message[] messages = inbox.search(term);
                LOGGER.info("IMAP search returned {} messages for to='{}' subjectContains='{}'",
                        messages.length, toAddress, subjectContains);

                for (int i = messages.length - 1; i >= 0; i--) {
                    Message msg = messages[i];
                    if (!isRecipient(msg, toAddress)) {
                        continue;
                    }
                    String content = extractText(msg);
                    Matcher m = RESET_LINK_PATTERN.matcher(content);
                    if (m.find()) {
                        String link = m.group();
                        LOGGER.info("Found reset password link: {}", link);
                        return link;
                    }
                }
            } catch (Exception e) {
                LOGGER.warn("IMAP poll failed: {}", e.getMessage());
            } finally {
                closeQuietly(inbox);
                closeQuietly(store);
            }

            try {
                Thread.sleep(pollInterval);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        throw new RuntimeException("Reset password email was not received within "
                + timeoutSeconds + " seconds (to=" + toAddress
                + ", subjectContains=" + subjectContains + ")");
    }

    private static boolean isRecipient(Message msg, String toAddress) {
        try {
            Address[] recipients = msg.getAllRecipients();
            if (recipients == null) {
                return false;
            }
            String normalizedExpected = toAddress.toLowerCase();
            for (Address a : recipients) {
                if (a instanceof InternetAddress) {
                    String addr = ((InternetAddress) a).getAddress();
                    if (addr != null && addr.equalsIgnoreCase(normalizedExpected)) {
                        return true;
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return true; // fallback - trust IMAP search filter
    }

    private static String extractText(Part part) throws Exception {
        if (part.isMimeType("text/*")) {
            Object content = part.getContent();
            return content == null ? "" : content.toString();
        }
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mp.getCount(); i++) {
                sb.append(extractText(mp.getBodyPart(i))).append('\n');
            }
            return sb.toString();
        }
        return "";
    }

    private static String getProperty(String name) {
        String value = System.getenv(name);
        if (value == null || value.isEmpty()) {
            value = System.getProperty(name);
        }
        return value;
    }

    private static String getRequiredProperty(String name) {
        String value = getProperty(name);
        if (value == null || value.isEmpty()) {
            throw new RuntimeException("Required env/system property '" + name + "' is not set. "
                    + "Either set GMAIL_USER + GMAIL_APP_PASSWORD for IMAP access, or "
                    + "set RESET_PASSWORD_LINK directly with a fresh reset URL.");
        }
        return value;
    }

    private static void closeQuietly(Folder folder) {
        if (folder != null && folder.isOpen()) {
            try {
                folder.close(false);
            } catch (Exception ignored) {
            }
        }
    }

    private static void closeQuietly(Store store) {
        if (store != null && store.isConnected()) {
            try {
                store.close();
            } catch (Exception ignored) {
            }
        }
    }
}
