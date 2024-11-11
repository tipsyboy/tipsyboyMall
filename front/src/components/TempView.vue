<template>
  <CenterLayout>
    <button @click="modalOpen">Modal</button>

    <div class="modal-wrap" v-show="state.modalCheck">
      <div class="modal-container">
        <h2>로그인</h2>
        <el-form label-width="90px" status-icon>
          <el-form-item label="email" size="large">
            <el-input placeholder="이메일을 입력하세요." />
          </el-form-item>
          <el-form-item label="password" size="large">
            <el-input type="password" placeholder="비밀번호를 입력하세요." show-password />
          </el-form-item>
          <el-form-item size="large">
            <el-button class="mx-1" type="primary">로그인</el-button>
            <span class="mx-1">아직 회원이 아니라면?</span>
            <router-link to="/signup">회원가입</router-link>
          </el-form-item>
        </el-form>

        <div class="modal-btn">
          <button @click="modalOpen">닫기</button>
          <button @click="modalOpen">확인</button>
        </div>
      </div>
    </div>
  </CenterLayout>
</template>

<script setup lang="ts">
import { reactive } from 'vue'

const state = reactive({
  modalCheck: false,
})

const modalOpen = () => {
  state.modalCheck = !state.modalCheck
}
</script>

<style scoped>
.modal-wrap {
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
}
/* modal or popup */
.modal-container {
  position: relative;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 550px;
  background: #fff;
  border-radius: 10px;
  padding: 20px;
  box-sizing: border-box;
}
</style>
