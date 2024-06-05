<template>
  <CenterLayout>
    <ProfileMenu :nickname="props.nickname" />
    <p>내 주문 페이지입니다.</p>
  </CenterLayout>
</template>

<script setup lang="ts">
import ProfileMenu from './ProfileMenu.vue'

const props = defineProps<{
  nickname: string
}>()
</script>

<style scoped></style>
