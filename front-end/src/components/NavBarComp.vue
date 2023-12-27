<template>
  <el-header class="navbar">
    <el-menu
      mode="horizontal"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b"
      :ellipsis="false"
      router
    >
      <el-menu-item index="/">Home</el-menu-item>
      <el-menu-item index="/order">Orders</el-menu-item>
      <el-menu-item index="/item/save">item</el-menu-item>
      <el-menu-item index="/item/list">item list</el-menu-item>

      <div class="flex-grow" />
      <template v-if="!memberStore.isLogin">
        <el-menu-item index="/login">Login</el-menu-item>
        <el-menu-item index="/signup">Sign up</el-menu-item>
      </template>
      <template v-else>
        <el-menu-item
          :index="{ name: 'profile', params: { nickname: memberStore.userInfo.nickname } }"
        >
          Profile
        </el-menu-item>
        <el-menu-item @click="logout">Logout</el-menu-item>
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
.flex-grow {
  flex-grow: 1;
}
</style>
