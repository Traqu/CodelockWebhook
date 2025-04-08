package com.traqu.config;

public final class ConfigData {
    private String logPath;
    private String webhookUrl;
    private String adminRoleId;
    private String distance;

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public String getAdminRoleId() {
        return adminRoleId;
    }

    public void setAdminRoleId(String adminRoleId) {
        this.adminRoleId = adminRoleId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}