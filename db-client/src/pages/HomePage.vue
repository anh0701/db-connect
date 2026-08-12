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
    <div class="mx-auto w-full max-w-5xl p-8">
        <HomeHeader
            @new-connection="openNewConnection"
        />

        <!-- Has saved connections -->
        <div
            v-if="connections.length > 0 || loading"
            class="mt-10"
        >
            <div class="mb-4 flex items-end justify-between">
                <div>
                    <h2 class="text-base font-semibold text-gray-900">
                        Saved Connections
                    </h2>
                </div>

                <button
                    type="button"
                    class="inline-flex items-center gap-2 rounded-lg border border-gray-300 bg-white px-3 py-2 text-sm font-medium text-gray-700 shadow-sm transition hover:bg-gray-50 hover:text-gray-900"
                    @click="loadConnections"
                >
                    <svg
                        class="h-4 w-4"
                        viewBox="0 0 24 24"
                        fill="none"
                        stroke="currentColor"
                        stroke-width="2"
                    >
                        <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            d="M4 4v5h5"
                        />
                        <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            d="M20 20v-5h-5"
                        />
                        <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            d="M5.5 9A7 7 0 0118.5 6.5"
                        />
                        <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            d="M18.5 15A7 7 0 015.5 17.5"
                        />
                    </svg>

                    Refresh
                </button>
            </div>

            <!-- Loading -->
            <div
                v-if="loading"
                class="rounded-xl border border-gray-200 bg-white py-12 text-center"
            >
                <p class="text-sm text-gray-500">
                    Loading connections...
                </p>
            </div>

            <!-- Connections -->
            <div
                v-else
                class="space-y-3"
            >
                <SavedConnectionCard
                    v-for="connection in connections"
                    :key="connection.id"
                    :connection="connection"
                    @connect="openConnection(connection)"
                    @edit="editConnection(connection)"
                />
            </div>
        </div>

        <!-- Empty state -->
        <div
            v-else
            class="mt-10 rounded-xl border border-dashed border-gray-300 bg-gray-50 px-6 py-12 text-center"
        >
            <p class="text-sm text-gray-500">
                No saved connections yet.
            </p>

            <button
                type="button"
                class="mt-4 inline-flex items-center gap-2 rounded-lg bg-blue-600 px-4 py-2.5 text-sm font-medium text-white shadow-sm transition hover:bg-blue-700 hover:shadow"
                @click="openNewConnection"
            >
                <span class="text-base leading-none">+</span>
                New Connection
            </button>
        </div>

        <ConnectionDialog
            v-if="showDialog"
            :mode="dialogMode"
            :connection="selectedConnection"
            @close="showDialog = false"
            @created="loadConnections"
        />
    </div>
</template>


