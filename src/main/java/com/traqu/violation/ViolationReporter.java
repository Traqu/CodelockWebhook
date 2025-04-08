package com.traqu.violation;

import com.traqu.config.Config;
import com.traqu.model.Position3D;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class ViolationReporter {
    private ViolationReporter() {
    }

    public static void reportViolation(String playerName, String playerId, Position3D pos1, Position3D pos2) {
        String adminRoleId = Config.getConfigData().getAdminRoleId();
        String webhookUrl = Config.getConfigData().getWebhookUrl();
        String message = "<@&" + adminRoleId + "> **Violation detected!**\n";

        sendDiscordWebhook(webhookUrl, message, playerName, playerId, pos1, pos2);
    }

    private static void sendDiscordWebhook(String webhookUrl, String content, String playerName, String playerId, Position3D pos1, Position3D pos2) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonPayload = String.format("{\"content\":\"%s\","
                            + "\"embeds\": [{"
                            + "\"title\": \"Violation Detected\","
                            + "\"description\": \"The violation has been logged.\","
                            + "\"color\": 16711680,"
                            + "\"fields\": ["
                            + "{"
                            + "\"name\": \"Player Name\", \"value\": \"%s\", \"inline\": true"
                            + "},"
                            + "{"
                            + "\"name\": \"Player ID\", \"value\": \"%s\", \"inline\": true"
                            + "},"
                            + "{"
                            + "\"name\": \"\\n\", \"value\": \"\\n\", \"inline\": false"
                            + "},"
                            + "{"
                            + "\"name\": \"Position 1\", \"value\": \"%s\", \"inline\": true"
                            + "},"
                            + "{"
                            + "\"name\": \"Position 2\", \"value\": \"%s\", \"inline\": true"
                            + "}"
                            + "]"
                            + "}]}",
                    escapeJson(content), playerName, playerId, pos1, pos2);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200 || responseCode == 204) {
                System.out.println("Webhook sent successfully (HTTP code: " + responseCode + ")");
            } else {
                System.err.println("[ERROR] Discord webhook failed with response code: " + responseCode);
            }

        } catch (Exception e) {
            System.err.println("[ERROR] Failed to send Discord webhook: " + e.getMessage());
        }
    }

    private static String escapeJson(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}
