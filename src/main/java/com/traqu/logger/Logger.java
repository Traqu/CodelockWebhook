package com.traqu.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Logger {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Logger() {
    }

    public static void Log(String message) {
        System.out.println("[" + LocalDateTime.now().format(formatter) + "] " + message);
        System.out.flush();
    }

    public static void Log(String message, boolean error) {
        System.err.println("[" + LocalDateTime.now().format(formatter) + "] " + message);
        System.out.flush();
    }
}