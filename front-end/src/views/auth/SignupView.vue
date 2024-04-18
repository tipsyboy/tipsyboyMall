<template>
  <CenterLayout>
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
  </CenterLayout>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'
import router from '@/router'
import { ElMessage } from 'element-plus'

const requestUrl = 'http://localhost:8080/auth/signup'
const form = ref({
  email: '',
  password: '',
  nickname: ''
})

const signup = () => {
  axios
    .post(requestUrl, {
      email: form.value.email,
      password: form.value.password,
      nickname: form.value.nickname
    })
    .then(() => {
      ElMessage({
        message: '회원가입이 정상적으로 완료되었습니다.',
        type: 'success'
      })
      router.push({ name: 'login' })
    })
    .catch((error) => {
      ElMessage({
        message: '회원가입에 실패하였습니다.',
        type: 'error'
      })
      console.error(error)
    })
}
</script>

<style scoped></style>
