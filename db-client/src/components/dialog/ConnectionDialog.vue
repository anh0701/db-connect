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

watch(form, () => {

    tested.value = false;

    message.value = "";

}, { deep: true });

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

    <div class="overlay">

        <div class="dialog">

            <h2>{{ dialogTitle }}</h2>

            <label>
            Database
                <select
                    :value="form.type"
                    @change="changeType(($event.target as HTMLSelectElement).value as DatabaseType)"
                >
                    <option>POSTGRES</option>
                    <option>MYSQL</option>
                    <option>ORACLE</option>
                    <option>SQLSERVER</option>
                    <option>SQLITE</option>

                </select>
            </label>

            <label>
                Connection Name
                <input v-model="connectionName" />
            </label>

            <label>
            Host
                <input v-model="form.host"/>
            </label>

            <label>
            Port
                <input
                type="number"
                v-model="form.port"
                />
            </label>

            <label>
            Database
                <input
                v-model="form.database"
                />
            </label>

            <label>
            Username
                <input
                v-model="form.username"
                />
            </label>

            <label>
            Password
                <input
                type="password"
                v-model="form.password"
                />
            </label>

            <label class="checkbox">
                <input
                    type="checkbox"
                    v-model="saveConnectionChecked"
                />
                Save connection
            </label>

            <p>{{ message }} </p>
            <div class="buttons">

                <button
                    @click="$emit('close')">
                    Cancel
                </button>

                <button
                @click="onTest"
                :disabled="testing"
                >
                Test
                </button>

                <button
                    @click="onSubmit"
                    :disabled="!tested || connecting"
                >
                    {{ submitText }}
                </button>

            </div>
        </div>
    </div>

</template>

<style scoped>

.overlay {
    position: fixed;
    inset: 0;
    background: rgba(0, 0, 0, .4);

    display: flex;
    justify-content: center;
    align-items: center;

    padding: 20px;
    box-sizing: border-box;
}

.dialog {
    width: min(450px, 100%);
    background: white;
    border-radius: 12px;
    padding: 24px;

    display: flex;
    flex-direction: column;
    gap: 14px;

    box-sizing: border-box;

    max-height: 90vh;
    overflow-y: auto;
}

label {
    display: flex;
    flex-direction: column;
    gap: 6px;
}

input,
select {
    width: 100%;
    padding: 10px;
    box-sizing: border-box;
}

.buttons {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
    flex-wrap: wrap;
}

.checkbox {
    flex-direction: row;
    align-items: center;
    gap: 8px;
}

.checkbox input {
    width: auto;
}

</style>
