<script setup lang="ts">
import type { ConnectionSession } from "../../types/connection/connection";

defineProps<{
    session: ConnectionSession;
}>();
</script>

<template>
    <div
        class="rounded-xl border border-gray-300 bg-white p-4 transition hover:border-gray-400 hover:shadow-sm"
    >
        <h3 class="text-base font-medium text-gray-900">
            {{ session.database }}
        </h3>

        <p class="mt-1 text-sm text-gray-700">
            {{ session.type }}
        </p>

        <p class="mt-1 text-xs text-gray-500">
            {{ session.host }}:{{ session.port }}
        </p>
    </div>
</template>
