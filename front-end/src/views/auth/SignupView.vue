<template>
  <CenterLayout>
    <h2>회원가입</h2>
    <el-form label-width="90px">
      <el-form-item label="email" size="large">
        <el-input v-model="state.signupForm.email" placeholder="이메일을 입력하세요." />
      </el-form-item>
      <el-form-item label="password" size="large">
        <el-input
          v-model="state.signupForm.password"
          type="password"
          placeholder="비밀번호를 입력하세요."
          show-password
        />
      </el-form-item>
      <el-form-item label="nickname" size="large">
        <el-input v-model="state.signupForm.nickname" placeholder="닉네임을 입력하세요." />
      </el-form-item>

      <el-form-item size="large">
        <el-button type="primary" @click="signup()">회원가입</el-button>
      </el-form-item>
    </el-form>
  </CenterLayout>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import SignupForm from '@/entity/member/SignupForm'
import HttpError from '@/http/HttpError'
import { container } from 'tsyringe'
import MemberRepository from '@/repository/MemberRepository'

const router = useRouter()
const state = reactive({
  signupForm: new SignupForm()
})
const MEMBER_REPOSITORY = container.resolve(MemberRepository)

const signup = () => {
  MEMBER_REPOSITORY.signup(state.signupForm)
    .then(() => {
      ElMessage({ type: 'success', message: '환영합니다. :) 로그인 해주세요!' })
      router.replace('/login')
    })
    .catch((e: HttpError) => {
      ElMessage({ type: 'error', message: e.getMessage() })
    })
}
</script>

<style scoped></style>
