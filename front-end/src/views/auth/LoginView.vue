<template>
  <CenterLayout>
    <h2>로그인</h2>
    <el-form :model="form" class="loginForm" label-width="90px" status-icon>
      <el-form-item label="email" size="large">
        <el-input v-model="form.email" placeholder="이메일을 입력하세요." />
      </el-form-item>
      <el-form-item label="password" size="large">
        <el-input
          v-model="form.password"
          type="password"
          placeholder="비밀번호를 입력하세요."
          show-password
        />
      </el-form-item>
      <el-form-item size="large">
        <el-button class="mx-1" @click="login()" type="primary">로그인</el-button>
        <span class="mx-1">아직 회원이 아니라면?</span>
        <router-link to="/signup">회원가입</router-link>
      </el-form-item>
    </el-form>
  </CenterLayout>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import useMemberStore from '@/stores/memberInfo'

const form = ref({
  email: '',
  password: ''
})

const memberStore = useMemberStore()
const login = () => {
  memberStore.login(form)
}
</script>

<style scoped></style>
