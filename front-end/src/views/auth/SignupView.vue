<template>
  <h2>회원가입</h2>
  <el-form :model="form" class="signupForm" label-width="90px" status-icon>
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
    <el-form-item label="nickname" size="large">
      <el-input v-model="form.nickname" placeholder="닉네임을 입력하세요." />
    </el-form-item>

    <el-form-item size="large">
      <el-button type="primary" @click="signup()">회원가입</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'
import router from '@/router'

const form = ref({
  email: '',
  password: '',
  nickname: ''
})

const signup = () => {
  axios
    .post('http://localhost:8080/auth/signup', {
      email: form.value.email,
      password: form.value.password,
      nickname: form.value.nickname
    })
    .then(() => {
      router.push({ name: 'home' })
    })
    .catch((error) => {
      console.error(error)
    })
}
</script>

<style scoped></style>
