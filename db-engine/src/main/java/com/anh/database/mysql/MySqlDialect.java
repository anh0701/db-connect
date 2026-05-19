package com.anh.database.mysql;

import com.anh.database.common.DatabaseDialect;

public class MySqlDialect implements DatabaseDialect{
    @Override
    public String buildJdbcUrl(
            String host,
            int port,
            String database
    ) {

        return
                "jdbc:mysql://" +
                host +
                ":" +
                port +
                "/" +
                database;
    }

    @Override
    public String getDriverClassName() {

        return "com.mysql.cj.jdbc.Driver";
    }
}
