<script setup lang="ts">
import type { SavedConnection } from "../../types/connection/SavedConnection";

defineProps<{
    connection: SavedConnection;
}>();

const emit = defineEmits<{
    (e: "connect"): void;
    (e: "edit"): void;
}>();
</script>

<template>
    <div
        class="flex items-center justify-between rounded-xl border border-gray-300 bg-white px-4 py-3 transition hover:border-gray-400 hover:shadow-sm"
        @click="emit('connect')"
    >
        <div>
            <h3 class="text-base font-medium text-gray-900">
                {{ connection.name }}
            </h3>

            <p class="mt-1 text-sm text-gray-700">
                {{ connection.databaseType }}
            </p>

            <p class="mt-1 text-xs text-gray-500">
                {{ connection.databaseName }}@{{ connection.host }}:{{ connection.port }}
            </p>
        </div>

        <div class="flex items-center gap-2">
            <button
                type="button"
                class="rounded-lg bg-blue-600 px-3 py-2 text-sm font-medium text-white transition hover:bg-blue-700"
                @click.stop="emit('connect')"
            >
                Connect
            </button>

            <button
                type="button"
                class="rounded-lg border border-gray-300 bg-white px-3 py-2 text-sm font-medium text-gray-700 transition hover:bg-gray-50"
                @click.stop="emit('edit')"
            >
                Edit
            </button>
        </div>
    </div>
</template>
