<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from "vue";
import { ConnectionRequest } from "../../types/connection/ConnectionRequest";
import { createConnection, testConnection } from "../../api/connection";
import { DatabaseType } from "../../types/DatabaseType";
import { getSetting, updateSetting } from "../../api/settings";
import { saveConnection } from "../../api/saved-connection";
import { SavedConnection } from "../../types/connection/SavedConnection";
import { DEFAULT_CONFIG } from "../../constants/database-defaults";

type DialogMode = "new" | "edit" | "connect";

const props = defineProps<{
    mode: DialogMode;
    connection?: SavedConnection;
}>();

const emit = defineEmits<{
    (e: "close"): void;
    (e: "created"): void;
}>();

const form = reactive<ConnectionRequest>({
    type: "POSTGRES",
    host: "localhost",
    port: 5432,
    database: "postgres",
    username: "postgres",
    password: ""
});

const testing = ref(false);
const connecting = ref(false);
const tested = ref(false);
const message = ref("");
const saveConnectionChecked = ref(true);
const connectionName = ref("");
const customConnectionName = ref(false);

watch(
    form,
    () => {
        tested.value = false;
        message.value = "";
    },
    { deep: true }
);

watch(
    () => [form.host, form.database],
    () => {
        if (!customConnectionName.value) {
            connectionName.value = generateConnectionName();
        }
    }
);

watch(connectionName, (value) => {
    if (value !== generateConnectionName()) {
        customConnectionName.value = true;
    }
});

watch(
    () => props.connection,
    (connection) => {
        if (!connection) {
            resetForm();
            return;
        }

        form.type = connection.databaseType as DatabaseType;
        form.host = connection.host;
        form.port = connection.port;
        form.database = connection.databaseName;
        form.username = connection.username;
        form.password = "";

        connectionName.value = connection.name;
        customConnectionName.value = true;
    },
    {
        immediate: true
    }
);

const dialogTitle = computed(() => {
    switch (props.mode) {
        case "new":
            return "New Connection";

        case "edit":
            return "Edit Connection";

        case "connect":
            return "Connect";
    }
});

const submitText = computed(() => {
    return props.mode === "edit"
        ? "Save"
        : "Connect";
});

function resetForm() {
    Object.assign(form, DEFAULT_CONFIG.POSTGRES, {
        password: ""
    });

    customConnectionName.value = false;
    connectionName.value = generateConnectionName();
}

function generateConnectionName() {
    const database = form.database.trim() || "database";
    const host = form.host.trim() || "localhost";

    return `${database}@${host}`;
}

onMounted(async () => {
    connectionName.value = generateConnectionName();

    try {
        const setting = await getSetting("save_connection");
        saveConnectionChecked.value = setting.value === "true";
    } catch {
        saveConnectionChecked.value = true;
    }
});

async function onTest() {
    testing.value = true;
    message.value = "";

    try {
        const res = await testConnection(form);
        tested.value = res.success;
        message.value = res.message;
    } catch (e: any) {
        tested.value = false;
        message.value = e.message;
    } finally {
        testing.value = false;
    }
}

async function onSubmit() {
    connecting.value = true;

    try {
        switch (props.mode) {
            case "new":
                await createConnection(form);

                await updateSetting(
                    "save_connection",
                    String(saveConnectionChecked.value)
                );

                if (saveConnectionChecked.value) {
                    await saveConnection({
                        name: connectionName.value,
                        databaseType: form.type,
                        host: form.host,
                        port: form.port,
                        databaseName: form.database,
                        username: form.username
                    });
                }

                break;

            case "edit":
                // TODO:
                // await updateSavedConnection(...)

                break;

            case "connect":
                await createConnection(form);

                break;
        }

        emit("created");
        emit("close");
    } catch (e: any) {
        message.value = e.message;
    } finally {
        connecting.value = false;
    }
}

function changeType(type: DatabaseType) {
    const config = DEFAULT_CONFIG[type];

    form.type = config.type;
    form.host = config.host;
    form.port = config.port;
    form.database = config.database;
    form.username = config.username;
    form.password = "";
}
</script>

<template>
    <div
        class="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4"
        @click.self="emit('close')"
    >
        <div
            class="flex max-h-[90vh] w-full max-w-lg flex-col overflow-y-auto rounded-xl bg-white shadow-xl"
        >
            <!-- Header -->
            <div
                class="flex items-center justify-between border-b border-gray-200 px-6 py-4"
            >
                <h2 class="text-lg font-semibold text-gray-900">
                    {{ dialogTitle }}
                </h2>

                <button
                    type="button"
                    class="rounded-lg p-1.5 text-gray-500 transition hover:bg-gray-100 hover:text-gray-700"
                    @click="emit('close')"
                >
                    ✕
                </button>
            </div>

            <!-- Form -->
            <div class="space-y-4 px-6 py-5">
                <!-- Database Type -->
                <label class="block">
                    <span class="mb-1.5 block text-sm font-medium text-gray-700">
                        Database
                    </span>

                    <select
                        :value="form.type"
                        class="w-full rounded-lg border border-gray-300 bg-white px-3 py-2.5 text-sm text-gray-900 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
                        @change="
                            changeType(
                                ($event.target as HTMLSelectElement)
                                    .value as DatabaseType
                            )
                        "
                    >
                        <option value="POSTGRES">
                            POSTGRES
                        </option>

                        <option value="MYSQL">
                            MYSQL
                        </option>

                        <option value="ORACLE">
                            ORACLE
                        </option>

                        <option value="SQLSERVER">
                            SQLSERVER
                        </option>

                        <option value="SQLITE">
                            SQLITE
                        </option>
                    </select>
                </label>

                <!-- Connection Name -->
                <label class="block">
                    <span class="mb-1.5 block text-sm font-medium text-gray-700">
                        Connection Name
                    </span>

                    <input
                        v-model="connectionName"
                        type="text"
                        class="w-full rounded-lg border border-gray-300 px-3 py-2.5 text-sm text-gray-900 outline-none transition placeholder:text-gray-400 focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
                    />
                </label>

                <!-- Host -->
                <label class="block">
                    <span class="mb-1.5 block text-sm font-medium text-gray-700">
                        Host
                    </span>

                    <input
                        v-model="form.host"
                        type="text"
                        class="w-full rounded-lg border border-gray-300 px-3 py-2.5 text-sm text-gray-900 outline-none transition placeholder:text-gray-400 focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
                    />
                </label>

                <!-- Port -->
                <label class="block">
                    <span class="mb-1.5 block text-sm font-medium text-gray-700">
                        Port
                    </span>

                    <input
                        v-model="form.port"
                        type="number"
                        class="w-full rounded-lg border border-gray-300 px-3 py-2.5 text-sm text-gray-900 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
                    />
                </label>

                <!-- Database Name -->
                <label class="block">
                    <span class="mb-1.5 block text-sm font-medium text-gray-700">
                        Database Name
                    </span>

                    <input
                        v-model="form.database"
                        type="text"
                        class="w-full rounded-lg border border-gray-300 px-3 py-2.5 text-sm text-gray-900 outline-none transition placeholder:text-gray-400 focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
                    />
                </label>

                <!-- Username -->
                <label class="block">
                    <span class="mb-1.5 block text-sm font-medium text-gray-700">
                        Username
                    </span>

                    <input
                        v-model="form.username"
                        type="text"
                        class="w-full rounded-lg border border-gray-300 px-3 py-2.5 text-sm text-gray-900 outline-none transition placeholder:text-gray-400 focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
                    />
                </label>

                <!-- Password -->
                <label class="block">
                    <span class="mb-1.5 block text-sm font-medium text-gray-700">
                        Password
                    </span>

                    <input
                        v-model="form.password"
                        type="password"
                        class="w-full rounded-lg border border-gray-300 px-3 py-2.5 text-sm text-gray-900 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
                    />
                </label>

                <!-- Save connection -->
                <div class="rounded-lg border border-gray-200 bg-gray-50 px-4 py-3">
                    <label class="flex cursor-pointer items-start gap-3">
                        <input
                            v-model="saveConnectionChecked"
                            type="checkbox"
                            class="mt-0.5 h-4 w-4 rounded border-gray-300 text-blue-600 focus:ring-2 focus:ring-blue-500"
                        />

                        <div>
                            <p class="text-sm font-medium text-gray-800">
                                Save connection
                            </p>

                            <p class="mt-0.5 text-xs text-gray-500">
                                Save this connection for quick access later.
                            </p>
                        </div>
                    </label>
                </div>

                <!-- Message -->
                <p
                    v-if="message"
                    class="rounded-lg bg-gray-50 px-3 py-2 text-sm text-gray-600"
                >
                    {{ message }}
                </p>
            </div>

            <!-- Footer -->
            <div
                class="flex flex-wrap justify-end gap-2 border-t border-gray-200 px-6 py-4"
            >
                <button
                    type="button"
                    class="rounded-lg border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 transition hover:bg-gray-50"
                    @click="emit('close')"
                >
                    Cancel
                </button>

                <button
                    type="button"
                    class="rounded-lg border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 transition hover:bg-gray-50 disabled:cursor-not-allowed disabled:opacity-50"
                    :disabled="testing"
                    @click="onTest"
                >
                    {{ testing ? "Testing..." : "Test" }}
                </button>

                <button
                    type="button"
                    class="rounded-lg bg-blue-600 px-4 py-2 text-sm font-medium text-white transition hover:bg-blue-700 disabled:cursor-not-allowed disabled:opacity-50"
                    :disabled="!tested || connecting"
                    @click="onSubmit"
                >
                    {{ connecting ? "Connecting..." : submitText }}
                </button>
            </div>
        </div>
    </div>
</template>
