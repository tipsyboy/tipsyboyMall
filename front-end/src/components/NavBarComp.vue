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
      <el-menu-item index="/items">item list</el-menu-item>

      <div class="flex-grow" />
      <el-menu-item index="/login">Login</el-menu-item>
      <el-menu-item index="/signup">Sign up</el-menu-item>
      <el-sub-menu index="" class="mr-3">
        <template #title>메뉴창 - 유저 닉네임</template>
        <el-menu-item index="/profile"> 마이페이지 </el-menu-item>
        <el-menu-item index="/cart">장바구니</el-menu-item>
        <el-menu-item index="/item/save">상품 등록</el-menu-item>
        <el-menu-item>로그아웃</el-menu-item>
      </el-sub-menu>
    </el-menu>
  </el-header>
</template>

<script lang="ts" setup></script>

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
