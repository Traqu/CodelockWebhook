package com.traqu.codelogs.logs;

import java.util.ArrayList;
import java.util.List;

public final class CodeLogs {

    private static final CodeLogs INSTANCE = new CodeLogs();

    private final List<String> LOGS = new ArrayList<>();

    private CodeLogs() {
    }

    public static CodeLogs getInstance() {
        return INSTANCE;
    }

    public List<String> get_LOGS() {
        return LOGS;
    }
}