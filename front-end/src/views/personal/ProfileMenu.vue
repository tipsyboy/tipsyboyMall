<template>
  <el-menu ellipsis mode="horizontal" :popper-offset="16" router>
    <el-menu-item :index="`/profile/${memberStore.userInfo.nickname}`">내 정보</el-menu-item>
    <el-menu-item :index="`/profile/${memberStore.userInfo.nickname}/my-item`"
      >내 상품</el-menu-item
    >
    <el-menu-item :index="`/profile/${memberStore.userInfo.nickname}/my-order`"
      >주문 목록</el-menu-item
    >
  </el-menu>
</template>

<script setup lang="ts">
import useMemberStore from '@/stores/memberInfo'

const memberStore = useMemberStore()
</script>

<style scoped></style>
