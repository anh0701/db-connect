package com.anh.model;

import com.anh.database.common.DatabaseType;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionSession {
    public String sessionId;

    public DatabaseType type;

    public String host;

    public int port;

    public String database;

    public String username;

    public HikariDataSource dataSource;
}