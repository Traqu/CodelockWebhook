package com.traqu.codelogs.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public final class CodeLogsReader {

    public void fillLinesWithData(Path path, List<String> lines){
        readCodeLogs(path, lines);
    }

    private void readCodeLogs(Path path, List<String> lines) {
        try (BufferedReader codeLogsReader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = codeLogsReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println("[ERROR] File not found: " + path);
        } catch (IOException e) {
            System.err.println("[ERROR] An error occurred while reading the file: " + e.getMessage());
        }
    }
}