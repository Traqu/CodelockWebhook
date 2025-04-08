package com.traqu.model;

import com.traqu.config.Config;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

public final class Violation {
    private final String VIOLATION_STRING;
    private final String playerName;
    private final String playerId;
    private final Position3D pos1;
    private final Position3D pos2;

    public Violation(String playerName, String playerId, Position3D pos1, Position3D pos2) {
        this.playerName = playerName;
        this.playerId = playerId;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.VIOLATION_STRING = generateViolationString(playerName, playerId, pos1, pos2);
    }

    public static boolean checkIfViolationExists(String violationString) {
        Set<String> existingViolations = loadExistingViolations();
        return existingViolations.contains(violationString);
    }

    public String get_VIOLATION_STRING() {
        return VIOLATION_STRING;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public Position3D getPos1() {
        return pos1;
    }

    public Position3D getPos2() {
        return pos2;
    }

    private String generateViolationString(String playerName, String playerId, Position3D pos1, Position3D pos2) {
        String input = playerName + playerId + pos1.toString() + pos2.toString();
        return generateMD5Hash(input);
    }

    private String generateMD5Hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    private static Set<String> loadExistingViolations() {
        Set<String> violations = new HashSet<>();
        File file = new File(Config.VIOLATIONS_FILE);

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    violations.add(line);
                }
            } catch (IOException e) {
                System.err.println("[ERROR] Error reading violations from file: " + e.getMessage());
            }
        }
        return violations;
    }

    public static void saveViolation(String violationString) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Config.VIOLATIONS_FILE, true))) {
            System.out.println("Saving violation: " + violationString);
            writer.write(violationString);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.err.println("[ERROR] Error writing violation to file: " + e.getMessage());
        }
    }
}