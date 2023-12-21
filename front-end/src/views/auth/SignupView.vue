<template>
  <el-form :model="form" label-width="120px" class="signupForm" style="max-width: 80%" status-icon>
    <el-form-item label="email">
      <el-input v-model="form.email" placeholder="이메일을 입력하세요." />
    </el-form-item>
    <el-form-item label="password">
      <el-input v-model="form.password" type="password" placeholder="비밀번호를 입력하세요." />
    </el-form-item>
    <el-form-item label="nickname">
      <el-input v-model="form.nickname" placeholder="닉네임을 입력하세요." />
    </el-form-item>

    <el-form-item>
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
