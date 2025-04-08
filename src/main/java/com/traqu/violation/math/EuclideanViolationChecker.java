package com.traqu.violation.math;

import com.traqu.config.Config;
import com.traqu.model.Position3D;
import com.traqu.model.Violation;
import com.traqu.violation.ViolationReporter;

import java.util.*;

public final class EuclideanViolationChecker {
    private EuclideanViolationChecker() {
    }

    public static void checkForViolations(Map<String, Set<Position3D>> playerPositions, Map<String, String> playerNames) {
        playerPositions.forEach((playerId, positionsSet) -> {
            String playerName = playerNames.get(playerId);
            List<Position3D> positions = new ArrayList<>(positionsSet);
            List<Violation> newViolations = new ArrayList<>();

            for (int i = 0; i < positions.size(); i++) {
                for (int j = i + 1; j < positions.size(); j++) {
                    Position3D pos1 = positions.get(i);
                    Position3D pos2 = positions.get(j);

                    if (checkForViolation(pos1, pos2)) {
                        Violation violation = new Violation(playerName, playerId, pos1, pos2);
                        String violationString = violation.get_VIOLATION_STRING();

                        if (!Violation.checkIfViolationExists(violationString)) {
                            newViolations.add(violation);
                        }
                    }
                }
            }

            for (Violation violation : newViolations) {
                Violation.saveViolation(violation.get_VIOLATION_STRING());
            }

            if (!newViolations.isEmpty()) {
                Violation firstViolation = newViolations.get(0);
                ViolationReporter.reportViolation(
                        firstViolation.getPlayerName(),
                        firstViolation.getPlayerId(),
                        firstViolation.getPos1(),
                        firstViolation.getPos2()
                );
            }
        });
    }

    private static boolean checkForViolation(Position3D pos1, Position3D pos2) {
        double distance = calculateDistance2D(pos1, pos2);
        return distance > Double.parseDouble(Config.getConfigData().getDistance());
    }

    private static double calculateDistance2D(Position3D pos1, Position3D pos2) {
        double dx = pos2.x() - pos1.x();
        double dz = pos2.z() - pos1.z();
        return Math.sqrt(dx * dx + dz * dz);
    }
}
