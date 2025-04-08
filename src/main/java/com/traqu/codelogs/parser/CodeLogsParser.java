package com.traqu.codelogs.parser;

import com.traqu.codelogs.factory.PlayerFactory;
import com.traqu.codelogs.logs.CodeLogs;
import com.traqu.codelogs.reader.CodeLogsReader;
import com.traqu.config.Config;
import com.traqu.model.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CodeLogsParser {
    private static final List<String> LINES = new ArrayList<>();

    private CodeLogsParser() {
    }

    public static List<Player> parseLogs(CodeLogs LOGS_INSTANCE) {
        try {
            LOGS_INSTANCE.get_LOGS().addAll(filterLogs(new CodeLogsReader()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String> accessedCodelocksList = LOGS_INSTANCE.get_LOGS().stream()
                .filter(log -> log.contains("quick accessed a lock!"))
                .collect(Collectors.toCollection(ArrayList::new));

        List<String> commonNames = accessedCodelocksList.stream()
                .filter(log -> log.split(" ").length == 10)
                .toList();
        accessedCodelocksList.removeAll(commonNames);

        Pattern pattern = Pattern.compile("\\(\\d{1,2}\\)");
        List<String> exceptionsRepeatedNames = accessedCodelocksList.stream()
                .filter(log -> pattern.matcher(log).find())
                .toList();
        accessedCodelocksList.removeAll(exceptionsRepeatedNames);

        List<String> exceptionsComplexNames = new ArrayList<>(accessedCodelocksList);

        commonNames = sanitizeLogs(commonNames);
        exceptionsRepeatedNames = sanitizeLogs(exceptionsRepeatedNames);
        exceptionsComplexNames = sanitizeLogs(exceptionsComplexNames);

        LOGS_INSTANCE.get_LOGS().clear();
        LOGS_INSTANCE.get_LOGS().addAll(commonNames);
        LOGS_INSTANCE.get_LOGS().addAll(exceptionsRepeatedNames);
        LOGS_INSTANCE.get_LOGS().addAll(exceptionsComplexNames);

        return LOGS_INSTANCE.get_LOGS().stream()
                .map(PlayerFactory::fromLogLine)
                .toList();
    }

    private static List<String> sanitizeLogs(List<String> logs) {
        List<String> handled = new ArrayList<>();
        Pattern repeatedNamePattern = Pattern.compile(" \\(\\d{1,2}\\)");

        for (String log : logs) {
            String cleanedLog = repeatedNamePattern.matcher(log).replaceAll("");

            int startIndex = cleanedLog.indexOf('(');
            if (startIndex != -1 && startIndex + 1 < cleanedLog.length()) {
                String playerName = cleanedLog.substring(startIndex + 1)
                        .trim()
                        .replace("(<", "")
                        .replace(">)", "")
                        .replace(" quick accessed a lock!", "")
                        .replace(")", ",")
                        .replace(" accessed a lock!", "");
                handled.add(playerName);
            }
        }

        return handled;
    }

    private static List<String> filterLogs(CodeLogsReader codeLogsReader) throws IOException {
        List<String> filteredList = new ArrayList<>();
        Stream<Path> list = Files.list(Path.of(getPathFromConfig()));
        list.forEach(file -> {
            codeLogsReader.fillLinesWithData(file.toAbsolutePath(), LINES);
            filteredList.addAll(LINES.stream().filter(line -> line.contains("accessed")).toList());
        });
        return filteredList;
    }

    private static String getPathFromConfig() {
        return Config.getConfigData().getLogPath();
    }
}