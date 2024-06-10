<template>
  <h3>{{ comments.length }}개의 댓글</h3>
  <div class="comment-vue">
    <CommentItem :comment="comment" v-for="comment in comments" :key="comment.id" />
  </div>
  <CommentCreate />
</template>

<script setup lang="ts">
import { ref } from 'vue'
import CommentItem from './CommentItem.vue'
import CommentCreate from './CommentCreate.vue'

// Mock comments data
const comments = ref([
  { id: 1, author: 'User1', timestamp: '2024-04-03 00:11:00', content: 'This is a comment.' },
  { id: 2, author: 'User2', timestamp: '2022-03-29 10:05:00', content: 'This is another comment.' },
  { id: 3, author: '간술맨', timestamp: '2024-03-29 10:05:00', content: 'ㅋㅋ' },
])
</script>

<style scoped>
.comment-list {
  margin-bottom: 20px;
}

.single-comment {
  margin-bottom: 10px;
}

.new-comment-form {
  margin-top: 20px;
}
</style>
