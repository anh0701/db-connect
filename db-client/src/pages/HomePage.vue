<script setup lang="ts">
import { onMounted, ref } from "vue";

import HomeHeader from "../components/home/HomeHeader.vue";
import SavedConnectionCard from "../components/home/SavedConnectionCard.vue";
import ConnectionDialog from "../components/dialog/ConnectionDialog.vue";

import { getSavedConnections } from "../api/saved-connection";

import type { SavedConnection } from "../types/connection/SavedConnection";

type DialogMode = "new" | "edit" | "connect";

const loading = ref(false);

const connections = ref<SavedConnection[]>([]);

const selectedConnection = ref<SavedConnection>();

const showDialog = ref(false);

const dialogMode = ref<DialogMode>("new");

async function loadConnections() {

    loading.value = true;

    try {

        connections.value = await getSavedConnections();

    } catch (e) {

        console.error(e);

    } finally {

        loading.value = false;

    }

}

function openNewConnection() {

    dialogMode.value = "new";

    selectedConnection.value = undefined;

    showDialog.value = true;

}

function openConnection(connection: SavedConnection) {

    dialogMode.value = "connect";

    selectedConnection.value = connection;

    showDialog.value = true;

}

function editConnection(connection: SavedConnection) {

    dialogMode.value = "edit";

    selectedConnection.value = connection;

    showDialog.value = true;

}

onMounted(loadConnections);
</script>

<template>
    <div class="container">

        <HomeHeader
            @new-connection="openNewConnection"
        />

        <button @click="loadConnections">
            Refresh
        </button>

        <p v-if="loading">
            Loading...
        </p>

        <div
            v-else
            class="connection-list"
        >
            <SavedConnectionCard
                v-for="connection in connections"
                :key="connection.id"
                :connection="connection"
                @connect="openConnection(connection)"
                @edit="editConnection(connection)"
            />
        </div>

        <p v-if="!loading && connections.length === 0">
            No saved connections.
        </p>

        <ConnectionDialog
            v-if="showDialog"
            :mode="dialogMode"
            :connection="selectedConnection"
            @close="showDialog = false"
            @created="loadConnections"
        />

    </div>
</template>

<style scoped>
.container {

    width: min(900px, 100%);

    margin: auto;

    padding: 32px;

    box-sizing: border-box;

}

.connection-list {

    display: grid;

    gap: 16px;

}
</style>
