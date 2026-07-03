import { api } from "./client";
import type { SavedConnection } from "../types/connection/SavedConnection";

export interface SaveConnectionRequest {

    name: string;

    databaseType: string;

    host: string;

    port: number;

    databaseName: string;

    username: string;

}

export function getSavedConnections() {

    return api<SavedConnection[]>("/saved-connections");

}

export function getSavedConnection(id: number) {

    return api<SavedConnection>(`/saved-connections/${id}`);

}

export function saveConnection(request: SaveConnectionRequest) {

    return api<number>("/saved-connections", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(request)

    });

}
