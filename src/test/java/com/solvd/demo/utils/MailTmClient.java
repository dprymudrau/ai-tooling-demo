package com.solvd.demo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Minimal client for the public <a href="https://docs.mail.tm">mail.tm</a> disposable-email API.
 * <p>
 * Used by the successful-login test to read the one-time email verification code that Under Armour's
 * Okta login sends when authenticating from a new/untrusted device (which always happens on the
 * remote Selenium grid). The mail.tm inbox is read over plain HTTPS from the test JVM, so it works
 * regardless of where the browser runs.
 * <p>
 * NOTE: the address/password used here belong to a throwaway test inbox - they are NOT secrets and
 * are unrelated to the Selenium hub credentials.
 */
public class MailTmClient {

    private static final String BASE_URL = "https://api.mail.tm";
    private static final Pattern SIX_DIGIT_CODE = Pattern.compile("\\b(\\d{6})\\b");

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(20))
            .build();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String address;
    private final String password;
    private String token;

    public MailTmClient(String address, String password) {
        this.address = address;
        this.password = password;
    }

    /** Obtain (or refresh) an auth token for the inbox. */
    public void authenticate() throws Exception {
        String payload = mapper.createObjectNode()
                .put("address", address)
                .put("password", password)
                .toString();
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/token"))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(30))
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        token = mapper.readTree(response.body()).path("token").asText();
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Unable to authenticate to mail.tm inbox '" + address
                    + "'. Response: " + response.body());
        }
    }

    private JsonNode listMessages() throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/messages"))
                .header("Authorization", "Bearer " + token)
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readTree(response.body()).path("hydra:member");
    }

    /** Remove every message currently in the inbox so the next code we read is guaranteed to be fresh. */
    public void deleteAllMessages() throws Exception {
        for (JsonNode message : listMessages()) {
            String id = message.path("id").asText();
            if (id.isEmpty()) {
                continue;
            }
            HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/messages/" + id))
                    .header("Authorization", "Bearer " + token)
                    .timeout(Duration.ofSeconds(30))
                    .DELETE()
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        }
    }

    /**
     * Poll the inbox until a message containing a 6-digit verification code arrives.
     *
     * @param timeoutSeconds maximum time to wait
     * @return the 6-digit code
     */
    public String waitForVerificationCode(int timeoutSeconds) throws Exception {
        long deadline = System.currentTimeMillis() + timeoutSeconds * 1000L;
        while (System.currentTimeMillis() < deadline) {
            JsonNode members = listMessages();
            for (JsonNode summary : members) {
                String id = summary.path("id").asText();
                if (id.isEmpty()) {
                    continue;
                }
                HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + "/messages/" + id))
                        .header("Authorization", "Bearer " + token)
                        .timeout(Duration.ofSeconds(30))
                        .GET()
                        .build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                JsonNode message = mapper.readTree(response.body());
                String text = message.path("text").asText("");
                Matcher matcher = SIX_DIGIT_CODE.matcher(text);
                if (matcher.find()) {
                    return matcher.group(1);
                }
            }
            Thread.sleep(3000);
        }
        throw new RuntimeException("No verification code email received within " + timeoutSeconds + " seconds");
    }
}
