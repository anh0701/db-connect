<script setup lang="ts">
const emit = defineEmits<{
    (e: "new-connection"): void;
}>();
</script>

<template>
    <header class="flex items-center justify-between">
        <div>
            <h1 class="text-xl font-semibold tracking-tight text-gray-900">
                Database Client
            </h1>

            <p class="mt-1 text-sm text-gray-500">
                Manage and connect to your databases
            </p>
        </div>

        <button
            type="button"
            class="inline-flex items-center gap-2 rounded-lg border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 transition hover:border-gray-400 hover:bg-gray-50"
            @click="emit('new-connection')"
        >
            <span class="text-base">+</span>
            New Connection
        </button>
    </header>
</template>
