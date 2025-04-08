package com.traqu.codelogs.parser;

import com.traqu.model.Player;
import com.traqu.model.Position3D;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class PlayerDataParser {

    private PlayerDataParser() {
    }

    public static PlayerData parsePlayerData(List<Player> playersData) {
        Map<String, Set<Position3D>> playerPositions = playersData
                .stream()
                .collect(Collectors.groupingBy(
                        Player::steamID,
                        Collectors.mapping(Player::position, Collectors.toSet())
                ));

        Map<String, String> playerNames = playersData
                .stream()
                .collect(Collectors.toMap(
                        Player::steamID,
                        Player::name,
                        (original, duplicate) -> original
                ));

        return new PlayerData(playerNames, playerPositions);
    }

    public record PlayerData(Map<String, String> playerNames, Map<String, Set<Position3D>> playerPositions) {
    }
}
