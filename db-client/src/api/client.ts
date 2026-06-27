const BASE_URL = "http://localhost:8080";

export async function api<T>(
    path: string,
    options?: RequestInit
): Promise<T> {

    try {

        const response = await fetch(`${BASE_URL}${path}`, options);

        if (!response.ok) {
            throw new Error(await response.text());
        }

        return await response.json();

    } catch (e) {

        console.error(e);

        throw e;
    }

}
