<template>
  <el-main class="content">
    <RouterView />
  </el-main>
</template>

<script lang="ts" setup>
import { RouterView } from 'vue-router'
</script>

<style scoped>
.content {
  height: calc(100vh - 60px);
}
</style>
