<template>
  <CenterLayout>
    <p>{{ message }}</p>
    <el-button @click="router.go(-2)">되돌아가기</el-button>
  </CenterLayout>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()
const { message } = history.state
</script>
