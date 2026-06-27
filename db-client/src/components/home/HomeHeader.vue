<script setup lang="ts">
const emit = defineEmits<{
    (e: "new-connection"): void;
}>();
</script>

<template>
    <header class="header">
        <h1>Database Client</h1>

        <button @click="emit('new-connection')">
            + New Connection
        </button>
    </header>
</template>

<style scoped>
.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
}

button {
    padding: 10px 18px;
    border: none;
    border-radius: 8px;
    cursor: pointer;
}
</style>
