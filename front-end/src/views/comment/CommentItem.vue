<template>
  <div class="comment-container">
    <div class="comment-header">
      <div class="comment-header_left">{{ comment.author }}</div>
      <div class="comment-header_right">{{ formatTimestamp(comment.timestamp) }}</div>
    </div>
    <div class="comment-content">
      {{ comment.content }}
    </div>
    <div class="comment-util-btn">
      <el-button type="primary" :icon="Edit" circle />
      <el-button type="danger" :icon="Delete" circle />
    </div>
    <div class="comment-footer">
      <el-button size="small" text>
        추천
        <span style="margin-left: 10px">3</span>
      </el-button>
      <el-button size="small" text>비추천</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Delete, Edit } from '@element-plus/icons-vue'

const { comment } = defineProps<{
  comment: {
    id: number
    author: string
    timestamp: string
    content: string
  }
}>()

const formatTimestamp = (timestamp: string) => {
  const currentTime = new Date()
  const commentTime = new Date(timestamp)
  const diffTime = currentTime.getTime() - commentTime.getTime()
  const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24))

  if (diffDays > 30) {
    return commentTime.toISOString().split('T')[0]
  } else if (diffDays > 0) {
    return `${diffDays} 일 전`
  } else {
    const diffHours = Math.floor(diffTime / (1000 * 60 * 60))
    const diffMinutes = Math.floor(diffTime / (1000 * 60))
    const diffSeconds = Math.floor(diffTime / 1000)

    if (diffHours > 0) {
      return `${diffHours} 시간 전`
    } else if (diffMinutes > 0) {
      return `${diffMinutes} 분 전`
    } else {
      return `${diffSeconds} 초 전`
    }
  }
}
</script>

<style scoped>
.comment-container {
  margin-bottom: 8px;
  font-size: 14px;
  font-weight: 400;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  background-color: #e8e8e8;
  border-radius: 5px;
  margin-bottom: 5px;
  padding: 5px 10px;
}
.comment-content {
  margin-left: 10px;
  margin-bottom: 5px;
}

.comment-util-btn {
  display: flex;
  justify-content: flex-end;
  padding: 5px 10px;
}

.comment-footer {
  padding: 5px 10px;
  display: flex;
  justify-content: flex-end;
  font-size: 12px;
}
</style>
