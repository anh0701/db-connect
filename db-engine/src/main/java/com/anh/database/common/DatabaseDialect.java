package com.anh.database.common;

public interface DatabaseDialect {
    String buildJdbcUrl(
            String host,
            int port,
            String database
    );

    String getDriverClassName();
}
