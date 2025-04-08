package com.traqu.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public final class Config {
    private static final String CTX = getAppDirectory();
    public static final String CONFIG_DIR = CTX + File.separator + "config";
    public static final String CONFIG_JSON = CONFIG_DIR + File.separator + "config.json";
    public static final String VIOLATIONS_DIR = CTX + File.separator + "violations";
    public static final String VIOLATIONS_FILE = VIOLATIONS_DIR + File.separator + "violations";

    private static ConfigData configData;

    private Config() {
    }

    public static void initialize() {
        initializeDirectory(CONFIG_DIR);
        initializeDirectory(VIOLATIONS_DIR);
        loadConfig();
    }

    private static void loadConfig() {
        File jsonFile = new File(CONFIG_JSON);
        if (!jsonFile.exists()) {
            try (FileWriter writer = new FileWriter(jsonFile)) {
                writer.write("""
                        {
                          "logPath": "C:/YOUR_SERVER_DIR/servers/SERVER/profiles/CodeLock/Logs/Access",
                          "webhookUrl": "https://discord.com/api/webhooks/your-webhook-id",
                          "adminRoleId": "your-admin-role-id",
                          "distance": "60"
                    }
                    """);
                System.out.println("Created config.json with placeholder values.");
            } catch (IOException e) {
                throw new RuntimeException("[ERROR] Failed to create config.json", e);
            }
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            configData = mapper.readValue(jsonFile, ConfigData.class);
        } catch (IOException e) {
            throw new RuntimeException("[ERROR] Failed to read config.json", e);
        }
    }

    public static ConfigData getConfigData() {
        return configData;
    }

    private static String getAppDirectory() {
        try {
            return Paths.get(Config.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException("[ERROR] Could not determine where the app is located.", e);
        }
    }

    private static void initializeDirectory(String dirPath) {
        File directory = new File(dirPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                System.err.println("Failed to create " + directory.getPath() + " directory.");
            }
        }
    }
}
