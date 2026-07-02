package com.anh.core.configdb;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

public final class SqlScriptRunner {
    
    private SqlScriptRunner() {
    }

    public static void run(
            Connection connection,
            String resourcePath) throws Exception {

        InputStream inputStream = SqlScriptRunner.class
                .getClassLoader()
                .getResourceAsStream(resourcePath);

        if (inputStream == null) {
            throw new IOException(
                    "Cannot find resource: " + resourcePath);
        }

        String content = new String(
                inputStream.readAllBytes(),
                StandardCharsets.UTF_8);

        StringBuilder sql = new StringBuilder();

        for (String line : content.split("\\R")) {

            String trimmed = line.trim();

            if (trimmed.isEmpty()) {
                continue;
            }

            if (trimmed.startsWith("--")) {
                continue;
            }

            sql.append(line).append('\n');

            if (trimmed.endsWith(";")) {

                String statementSql = sql.toString().trim();

                statementSql = statementSql.substring(
                        0,
                        statementSql.length() - 1);

                try (Statement statement = connection.createStatement()) {

                    statement.execute(statementSql);
                }

                sql.setLength(0);
            }
        }
    }
}
