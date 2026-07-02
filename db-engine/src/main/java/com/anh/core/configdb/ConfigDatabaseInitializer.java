package com.anh.core.configdb;

import java.sql.Connection;

public final class ConfigDatabaseInitializer {

    private ConfigDatabaseInitializer() {
    }

    public static void initialize() {

        try (Connection connection = ConfigDatabase.getConnection()) {

            MigrationManager.migrate(connection);

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
}
