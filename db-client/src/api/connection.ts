import { ApiResponse } from "../types/ApiResponse";
import { ConnectionSession } from "../types/connection/connection";
import { ConnectionRequest } from "../types/connection/ConnectionRequest";
import { api } from "./client";

export function getSessions() {
    return api<ConnectionSession[]>("/connect/sessions");
}

export function testConnection(request: ConnectionRequest) {
    return api<ApiResponse>("/connect/test", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(request),
    });
}

export function createConnection(request: ConnectionRequest) {
    return api<ApiResponse>("/connect/create", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(request),
    });
}

export function closeConnection(sessionId: string) {
    return api<void>(`/connect/${sessionId}`, {
        method: "DELETE",
    });
}
