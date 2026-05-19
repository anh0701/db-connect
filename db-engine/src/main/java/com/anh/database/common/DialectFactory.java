package com.anh.database.common;

import com.anh.database.mysql.MySqlDialect;
import com.anh.database.postgres.PostgresDialect;

public class DialectFactory {
    public static DatabaseDialect getDialect(DatabaseType type) {

        switch (type) {

            case POSTGRES:
                return new PostgresDialect();

            case MYSQL:
                return new MySqlDialect();

            default:
                throw new RuntimeException(
                        "Unsupported database type"
                );
        }
    }
}
