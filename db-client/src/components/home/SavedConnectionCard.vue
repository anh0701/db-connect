<script setup lang="ts">

import type { SavedConnection } from "../../types/connection/SavedConnection";

defineProps<{

    connection: SavedConnection;

}>();

const emit = defineEmits<{

    (e: "connect"): void;

}>();

</script>

<template>

<div
    class="card"
    @click="emit('connect')"
>

    <h3>{{ connection.name }}</h3>

    <p>{{ connection.databaseType }}</p>

    <small>

        {{ connection.databaseName }}@{{ connection.host }}:{{ connection.port }}

    </small>

</div>

</template>

<style scoped>

.card{

    border:1px solid #ddd;

    border-radius:12px;

    padding:16px;

    cursor:pointer;

    transition:.2s;

}

.card:hover{

    background:#f5f5f5;

    transform:translateY(-2px);

}

</style>
