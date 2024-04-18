<template>
  <el-header class="navbar">
    <el-menu
      class="navbar__menu"
      mode="horizontal"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b"
      :ellipsis="false"
      router
    >
      <el-menu-item index="/">Home</el-menu-item>
      <el-menu-item index="/item/list">item list</el-menu-item>

      <div class="flex-grow" />
      <template v-if="!memberStore.isLogin">
        <el-menu-item index="/login">Login</el-menu-item>
        <el-menu-item index="/signup">Sign up</el-menu-item>
      </template>
      <template v-else>
        <el-sub-menu index="" class="mr-3">
          <template #title>{{ memberStore.userInfo.nickname }}</template>
          <el-menu-item :index="`/profile/${memberStore.userInfo.nickname}`">
            마이페이지
          </el-menu-item>
          <el-menu-item index="/cart">장바구니</el-menu-item>
          <el-menu-item index="/item/save">상품 등록</el-menu-item>
          <el-menu-item @click="logout">로그아웃</el-menu-item>
        </el-sub-menu>
      </template>
    </el-menu>
  </el-header>
</template>

<script lang="ts" setup>
import useMemberStore from '@/stores/memberInfo'

const memberStore = useMemberStore()

const logout = () => {
  memberStore.logout()
}
</script>

<style scoped>
.navbar {
  padding: 0;
}
.navbar .navbar__menu {
  border: 0;
}

.flex-grow {
  flex-grow: 1;
}
</style>
