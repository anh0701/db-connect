package com.anh.database.postgres;

import com.anh.database.common.DatabaseDialect;

public class PostgresDialect implements DatabaseDialect{
    @Override
    public String buildJdbcUrl(
            String host,
            int port,
            String database
    ) {

        return
                "jdbc:postgresql://" +
                host +
                ":" +
                port +
                "/" +
                database;
    }

    @Override
    public String getDriverClassName() {

        return "org.postgresql.Driver";
    }
}
