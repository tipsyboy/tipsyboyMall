<script setup lang="ts">
import type LoginMember from '@/entity/member/LoginMember'
import MemberRepository from '@/repository/MemberRepository'
import ProfileRepository from '@/repository/ProfileRepository'
import { container } from 'tsyringe'
import { onBeforeMount, reactive } from 'vue'

const MEMBER_REPOSITORY = container.resolve(MemberRepository)
const PROFILE_REPOSITORY = container.resolve(ProfileRepository)

type StateType = {
  profile: LoginMember | null
}

const state = reactive<StateType>({
  profile: null,
})

onBeforeMount(() => {
  MEMBER_REPOSITORY.getProfileByAuth().then((profile) => {
    PROFILE_REPOSITORY.setProfile(profile)
    state.profile = profile
  })
})

const logout = () => {
  PROFILE_REPOSITORY.clearProfile()
  location.href = '/api/logout'
}
</script>

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

      <el-menu-item index="/login" v-if="state.profile === null">Login</el-menu-item>
      <el-menu-item index="/signup" v-if="state.profile === null">Sign up</el-menu-item>

      <el-sub-menu index="forLoginMember" class="mr-3" v-if="state.profile !== null">
        <template #title>{{ state.profile.nickname }}</template>
        <el-menu-item :index="'/profile/' + state.profile.nickname"> 마이페이지 </el-menu-item>
        <el-menu-item index="/cart">장바구니</el-menu-item>
        <el-menu-item index="/item/save">상품 등록</el-menu-item>
        <el-menu-item @click="logout()">로그아웃</el-menu-item>
      </el-sub-menu>
    </el-menu>
  </el-header>
</template>

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
