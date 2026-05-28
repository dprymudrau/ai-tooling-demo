package com.solvd.demo.utils;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * Utility for reading emails from a Gmail mailbox using IMAP. Connects to imap.gmail.com using the
 * provided account and app-password and returns the most recent message that matches the supplied
 * to-address and subject filters.
 */
public final class GmailReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(GmailReader.class);

    private static final String IMAP_HOST = "imap.gmail.com";
    private static final int IMAP_PORT = 993;

    private GmailReader() {
    }

    /**
     * Polls the inbox until a message matching the given recipient and subject keyword is found
     * (or the timeout is exceeded).
     *
     * @param mailboxLogin    Gmail account login (e.g. dmitryprimudriv@gmail.com)
     * @param appPassword     Gmail application password (with or without spaces)
     * @param toAddress       expected recipient address (matched with X-Original-To/To/CC)
     * @param subjectContains substring required in the subject line
     * @param timeoutSeconds  maximum amount of seconds to wait for the message to arrive
     * @return raw text of the matching message body (or {@code null} if nothing was found)
     */
    public static String fetchLatestEmailBody(String mailboxLogin,
                                              String appPassword,
                                              String toAddress,
                                              String subjectContains,
                                              int timeoutSeconds) {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.host", IMAP_HOST);
        props.put("mail.imaps.port", String.valueOf(IMAP_PORT));
        props.put("mail.imaps.ssl.enable", "true");
        props.put("mail.imaps.ssl.trust", "*");
        props.put("mail.imaps.ssl.protocols", "TLSv1.2 TLSv1.3");
        props.put("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.imaps.socketFactory.fallback", "false");
        props.put("mail.imaps.socketFactory.port", String.valueOf(IMAP_PORT));

        String normalizedPassword = appPassword == null ? "" : appPassword.replace(" ", "");

        long deadline = System.currentTimeMillis() + (long) timeoutSeconds * 1000L;

        Session session = Session.getInstance(props);

        while (System.currentTimeMillis() < deadline) {
            Store store = null;
            try {
                store = session.getStore("imaps");
                store.connect(IMAP_HOST, IMAP_PORT, mailboxLogin, normalizedPassword);
                Folder inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_ONLY);

                // Gmail's IMAP search of plus-alias recipients is unreliable, so we
                // filter only by subject and then verify the recipient client-side.
                SearchTerm term = new SubjectTerm(subjectContains);
                Message[] messages = inbox.search(term);

                LOGGER.info("Gmail search returned {} message(s) for subject~={}",
                        messages.length, subjectContains);

                // Iterate from newest to oldest
                for (int i = messages.length - 1; i >= 0; i--) {
                    Message msg = messages[i];
                    if (isAddressedTo(msg, toAddress)) {
                        String body = extractText(msg);
                        inbox.close(false);
                        return body;
                    }
                }

                inbox.close(false);
            } catch (Exception e) {
                LOGGER.warn("Unable to fetch email from Gmail: {}", e.getMessage());
            } finally {
                if (store != null) {
                    try {
                        store.close();
                    } catch (Exception ignored) {
                    }
                }
            }

            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        return null;
    }

    private static boolean isAddressedTo(Message message, String expectedAddress) throws Exception {
        Address[] recipients = message.getAllRecipients();
        if (recipients == null) {
            return false;
        }
        String expected = expectedAddress.toLowerCase();
        for (Address addr : recipients) {
            if (addr instanceof InternetAddress) {
                String email = ((InternetAddress) addr).getAddress();
                if (email != null && email.toLowerCase().equals(expected)) {
                    return true;
                }
            } else if (addr.toString().toLowerCase().contains(expected)) {
                return true;
            }
        }
        return false;
    }

    private static String extractText(Message message) throws Exception {
        Object content = message.getContent();
        if (content instanceof String) {
            return (String) content;
        }
        if (content instanceof Multipart) {
            return extractFromMultipart((Multipart) content);
        }
        if (content instanceof InputStream) {
            return new String(((InputStream) content).readAllBytes());
        }
        return content == null ? "" : content.toString();
    }

    private static String extractFromMultipart(Multipart multipart) throws Exception {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart part = multipart.getBodyPart(i);
            Object partContent = part.getContent();
            if (partContent instanceof String) {
                builder.append((String) partContent).append('\n');
            } else if (partContent instanceof Multipart) {
                builder.append(extractFromMultipart((Multipart) partContent));
            } else if (partContent instanceof InputStream) {
                builder.append(new String(((InputStream) partContent).readAllBytes())).append('\n');
            }
        }
        return builder.toString();
    }
}
