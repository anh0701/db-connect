import { DatabaseType } from "../DatabaseType";

export interface ConnectionSession {
    sessionId: string;

    type: DatabaseType;

    host: string;

    port: number;

    database: string;

    username: string;
}
