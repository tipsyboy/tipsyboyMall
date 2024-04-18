<template>
  <el-form>
    <el-form-item>
      <el-input v-model="commentContent" type="textarea" rows="6" resize="none" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="postComment()">등록</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const commentContent = ref('')

const postComment = () => {
  console.log('댓글 작성 로직을 작성하자.')
}
</script>

<style scoped></style>
