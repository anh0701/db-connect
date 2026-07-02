package com.anh.core.configdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public final class MigrationManager {
    private MigrationManager() {
    }

    public static void migrate(Connection connection)
            throws Exception {

        createVersionTable(connection);
        
        List<String> scripts = List.of(

                "sql/configdb/v1_init.sql"
                // "sql/configdb/v2_add_query_history.sql",
                // "sql/configdb/v3_add_setting.sql"

        );

        for (String script : scripts) {

            if (isExecuted(connection, script)) {
                continue;
            }

            SqlScriptRunner.run(
                    connection,
                    script);

            saveExecuted(
                    connection,
                    script);
        }
    }

    private static boolean isExecuted(
            Connection connection,
            String version)
            throws Exception {

        String sql = """
                SELECT 1
                FROM schema_version
                WHERE version = ?
                """;

        try (PreparedStatement statement =
                    connection.prepareStatement(sql)) {

            statement.setString(1, version);

            try (ResultSet rs = statement.executeQuery()) {

                return rs.next();
            }
        }
    }

    private static void saveExecuted(
            Connection connection,
            String version)
            throws Exception {

        String sql = """
                INSERT INTO schema_version(version)
                VALUES(?)
                """;

        try (PreparedStatement statement =
                    connection.prepareStatement(sql)) {

            statement.setString(1, version);

            statement.executeUpdate();
        }
    }

    private static void createVersionTable(Connection connection)
            throws Exception {

        String sql = """
            CREATE TABLE IF NOT EXISTS schema_version (
                version TEXT PRIMARY KEY,
                executed_at TEXT DEFAULT CURRENT_TIMESTAMP
            );
            """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
}
