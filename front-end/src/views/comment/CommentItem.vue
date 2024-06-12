<template>
  <div class="comment-container">
    <div class="comment-header">
      <div class="comment-header_left">{{ props.comment.author }}</div>
      <div class="comment-header_right">{{ props.comment.getFormattedDateTime() }}</div>
    </div>

    <div class="comment-content">
      <div v-if="state.isEditing">
        <el-input v-model="state.commentEditForm.content" type="textarea" resize="none" rows="4"></el-input>
        <el-button type="primary" @click="editComment">저장</el-button>
        <el-button @click="changeCommentEdit">취소</el-button>
      </div>
      <div v-else>
        {{ props.comment.content }}
      </div>
    </div>

    <div class="comment-util-btn" v-if="getAuthor() === props.comment.author">
      <el-button type="primary" @click="changeCommentEdit" :icon="Edit" circle />
      <el-button type="danger" @click="deleteComment" :icon="Delete" circle />
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
import type Comment from '@/entity/comment/Comment'
import CommentEditForm from '@/entity/comment/CommentEditForm'
import CommentRepository from '@/repository/CommentRepository'
import { Delete, Edit } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { container } from 'tsyringe'
import { markRaw, reactive, ref, type PropType } from 'vue'
import { useRouter } from 'vue-router'

const COMMENT_REPOSITORY = container.resolve(CommentRepository)
const router = useRouter()

const props = defineProps<{
  comment: Comment | any // ? 왜
}>()

type StateType = {
  commentEditForm: CommentEditForm
  isEditing: Boolean
}
const state = reactive<StateType>({
  isEditing: false,
  commentEditForm: new CommentEditForm(props.comment.content),
})

const changeCommentEdit = () => {
  state.isEditing = !state.isEditing
}

const editComment = () => {
  COMMENT_REPOSITORY.editComment(props.comment.commentId, state.commentEditForm).then(() => {
    console.log('수정')
  })
}

const deleteComment = () => {
  ElMessageBox.confirm('정말로 삭제하시겠습니까?', '댓글 삭제', {
    type: 'warning',
    icon: markRaw(Delete),
  }).then(() => {
    COMMENT_REPOSITORY.deleteComment(props.comment.commentId)
      .then(() => {
        console.log('삭제가 됨.')
        router.go(0)
      })
      .catch((e) => {
        console.error(e)
      })
  })
}

const getAuthor = () => {
  const profileData = localStorage.getItem('profile')
  if (profileData !== null) {
    const profile = JSON.parse(profileData)
    return profile.nickname
  }
  return null
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
