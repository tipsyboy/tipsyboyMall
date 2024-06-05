<template>
  <CenterLayout>
    <h2>로그인</h2>
    <el-form label-width="90px" status-icon>
      <el-form-item label="email" size="large">
        <el-input v-model="state.loginForm.email" placeholder="이메일을 입력하세요." />
      </el-form-item>
      <el-form-item label="password" size="large">
        <el-input
          v-model="state.loginForm.password"
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
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import LoginForm from '@/entity/member/LoginForm'
import type HttpError from '@/http/HttpError'
import { container } from 'tsyringe'
import MemberRepository from '@/repository/MemberRepository'

const router = useRouter()
const state = reactive({
  loginForm: new LoginForm(),
})
const MEMBER_REPOSITORY = container.resolve(MemberRepository)

const login = () => {
  MEMBER_REPOSITORY.login(state.loginForm)
    .then(() => {
      ElMessage({ type: 'success', message: '환영합니다. :)' })
      location.href = '/'
    })
    .catch((e: HttpError) => {
      ElMessage({ type: 'error', message: e.getMessage() })
    })
}
</script>

<style scoped></style>
