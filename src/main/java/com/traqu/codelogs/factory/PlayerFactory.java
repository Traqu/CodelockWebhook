package com.traqu.codelogs.factory;

import com.traqu.model.Player;
import com.traqu.model.Position3D;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class PlayerFactory {

    public static Player fromLogLine(String logLine) {
        String[] parts = logLine.split(",");

        if (parts.length < 5) {
            throw new IllegalArgumentException("Invalid log format: " + logLine);
        }

        String name = parts[0].trim();
        String steamID = parts[1].trim();

        try {
            double x = round(Double.parseDouble(parts[2].trim()));
            double y = round(Double.parseDouble(parts[3].trim()));
            double z = round(Double.parseDouble(parts[4].trim()));

            Position3D position = new Position3D(x, y, z);

            return new Player(name, steamID, position);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid coordinate format in log: " + logLine, e);
        }
    }

    private static double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}