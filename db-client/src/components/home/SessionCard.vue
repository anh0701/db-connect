<script setup lang="ts">
import type { ConnectionSession } from "../../types/connection/connection";

defineProps<{
    session: ConnectionSession;
}>();
</script>

<template>

<div class="card">

    <h3>{{ session.database }}</h3>

    <p>{{ session.type }}</p>

    <small>

        {{ session.host }}:{{ session.port }}

    </small>

</div>

</template>

<style scoped>

.card{
    border:1px solid #ddd;
    border-radius:12px;
    padding:16px;
    margin-bottom:12px;
    cursor:pointer;
}

.card:hover{
    background:#f5f5f5;
}

</style>
