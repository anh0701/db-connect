import { ConnectionRequest } from "../types/connection/ConnectionRequest";
import { DatabaseType } from "../types/DatabaseType";

export const DEFAULT_CONFIG: Record<DatabaseType, Omit<ConnectionRequest, "password">> = {
    POSTGRES: {
        type: "POSTGRES",
        host: "localhost",
        port: 5432,
        database: "postgres",
        username: "postgres"
    },

    MYSQL: {
        type: "MYSQL",
        host: "localhost",
        port: 3306,
        database: "mysql",
        username: "root"
    },

    ORACLE: {
        type: "ORACLE",
        host: "localhost",
        port: 1521,
        database: "XE",
        username: "system"
    },

    SQLSERVER: {
        type: "SQLSERVER",
        host: "localhost",
        port: 1433,
        database: "master",
        username: "sa"
    },

    SQLITE: {
        type: "SQLITE",
        host: "",
        port: 0,
        database: "",
        username: ""
    }
};
