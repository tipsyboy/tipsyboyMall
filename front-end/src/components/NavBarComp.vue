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
      <div class="flex-grow" />
      <el-menu-item index="/login">Login</el-menu-item>
      <el-menu-item index="/signup">Sign-up</el-menu-item>
    </el-menu>
  </el-header>
</template>

<script lang="ts" setup></script>

<style scoped>
.navbar {
  padding: 0;
}
.flex-grow {
  flex-grow: 1;
}
</style>
