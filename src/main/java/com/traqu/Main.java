package com.traqu;

import com.traqu.codelogs.logs.CodeLogs;
import com.traqu.codelogs.parser.CodeLogsParser;
import com.traqu.codelogs.parser.PlayerDataParser;
import com.traqu.config.Config;
import com.traqu.logger.Logger;
import com.traqu.model.Player;
import com.traqu.model.Position3D;
import com.traqu.violation.math.EuclideanViolationChecker;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public final class Main {

    public static void main(String[] args) {

        Config.initialize();
        while (true) {
            try {
                Logger.Log("Parsing logs...");
                List<Player> players = CodeLogsParser.parseLogs(CodeLogs.getInstance());
                Logger.Log("OK");

                PlayerDataParser.PlayerData playerData = PlayerDataParser.parsePlayerData(players);
                Map<String, String> playerNames = playerData.playerNames();
                Map<String, Set<Position3D>> playerPositions = playerData.playerPositions();

                Logger.Log("Checking for violations...");
                EuclideanViolationChecker.checkForViolations(playerPositions, playerNames);
                Logger.Log("OK");

                TimeUnit.HOURS.sleep(1);

            } catch (Exception e) {
                Logger.Log("[ERROR]: Please configure the _absolute_ \"logPath\" generated in [config/config.json]\n\t\t\t\t\twhich will be pointing to your \".../servers/[instanceID]/profiles/CodeLock/Logs/Access\" directory\n\t\t\t\t\tUse single FORWARD slashes!", true);
                break;
            }
        }
    }
}