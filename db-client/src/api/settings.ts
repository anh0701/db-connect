import { api } from "./client";

export interface Setting {
    key: string;
    value: string;
}

export function getSetting(key: string) {
    return api<Setting>(`/settings/${key}`);
}

export interface UpdateSettingRequest {
    value: string;
}

export function updateSetting(key: string, value: string) {
    return api<void>(`/settings/${key}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            value
        }),
    });
}
