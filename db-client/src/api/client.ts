const BASE_URL = "http://localhost:8080";

export async function api<T>(
    path: string,
    options?: RequestInit
): Promise<T> {

    const response = await fetch(`${BASE_URL}${path}`, options);

    if (!response.ok) {
        throw new Error(await response.text());
    }

    const json: ApiResponse<T> = await response.json();

    return json.data;
}

export interface ApiResponse<T> {
    status: number;
    message: string;
    data: T;
}
