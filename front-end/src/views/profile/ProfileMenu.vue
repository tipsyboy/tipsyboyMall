<template>
  <el-menu class="profile-menu" ellipsis mode="horizontal" :popper-offset="16" router>
    <el-menu-item :index="`/profile/${props.nickname}`">내 정보</el-menu-item>
    <el-menu-item :index="`/profile/${props.nickname}/items`">내 상품</el-menu-item>
    <el-menu-item :index="`/profile/${props.nickname}/orders`">주문 목록</el-menu-item>
  </el-menu>
</template>

<script setup lang="ts">
const props = defineProps<{
  nickname: string
}>()
</script>

<style scoped>
.profile-menu {
  margin-bottom: 2rem;
}
</style>
