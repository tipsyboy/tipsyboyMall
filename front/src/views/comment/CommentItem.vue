<template>
  <div class="comment-container" :class="{ 'reply-comment': props.comment.parentCommentId }">
    <div class="comment-header">
      <div class="comment-header_left">{{ props.comment.author }}</div>
      <div class="comment-header_right">{{ props.comment.getFormattedDateTime() }}</div>
    </div>

    <div class="comment-content" :class="{ 'deleted-comment': props.comment.deleted }">
      <div v-if="state.isEditing">
        <el-input
          v-model="state.commentEditForm.content"
          type="textarea"
          resize="none"
          rows="4"
        ></el-input>
        <el-button type="primary" @click="editComment">저장</el-button>
        <el-button @click="changeCommentEdit">취소</el-button>
      </div>
      <div v-else>
        <span v-if="props.comment.parentCommentId" class="parent-badge"
          >@{{ props.comment.parentCommentAuthor }}
        </span>
        {{ props.comment.content }}
      </div>
    </div>

    <div class="comment-util-btn">
      <div>
        <el-button type="info" @click="replyToComment()" :icon="ChatLineRound" circle />
      </div>
      <div v-if="getAuthor() === props.comment.author && !props.comment.deleted">
        <el-button type="primary" @click="changeCommentEdit" :icon="Edit" circle />
        <el-button type="danger" @click="deleteComment" :icon="Delete" circle />
      </div>
    </div>

    <div class="comment-footer">
      <el-button size="small" text @click="likeComment">
        추천
        <span style="margin-left: 10px">{{ props.comment.likeCnt }}</span>
      </el-button>
      <el-button size="small" text @click="dislikeComment">
        비추천
        <span style="margin-left: 10px" v-if="getAuthor() === props.comment.author">
          {{ props.comment.dislikeCnt }}
        </span>
      </el-button>
    </div>

    <!-- 대댓글 입력 -->
    <div v-if="state.recommenting" class="reply-input">
      <el-input
        v-model="state.reCommentCreateForm.content"
        type="textarea"
        resize="none"
        rows="4"
        placeholder="대댓글을 입력하세요."
      ></el-input>
      <div class="re-reply-btn">
        <el-button type="primary" @click="postRecomment">대댓글 등록</el-button>
        <el-button @click="replyToComment">취소</el-button>
      </div>
    </div>
    <div v-if="props.comment.children">
      <CommentItem
        :comment="child"
        v-for="child in props.comment.children"
        :key="child.commentId"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import type Comment from '@/entity/comment/Comment'
import CommentItem from './CommentItem.vue'
import CommentCreateForm from '@/entity/comment/CommentCreateForm'
import CommentEditForm from '@/entity/comment/CommentEditForm'
import CommentRepository from '@/repository/CommentRepository'
import { Delete, Edit, ChatLineRound } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { container } from 'tsyringe'
import { markRaw, reactive } from 'vue'
import { useRouter } from 'vue-router'

const COMMENT_REPOSITORY = container.resolve(CommentRepository)
const router = useRouter()

const props = defineProps<{
  comment: Comment | any // ? 왜
}>()

type StateType = {
  commentEditForm: CommentEditForm
  reCommentCreateForm: CommentCreateForm
  isEditing: Boolean
  recommenting: Boolean
}
const state = reactive<StateType>({
  isEditing: false,
  recommenting: false,
  reCommentCreateForm: new CommentCreateForm(),
  commentEditForm: new CommentEditForm(props.comment.content),
})

const changeCommentEdit = () => {
  state.isEditing = !state.isEditing
}

const editComment = () => {
  COMMENT_REPOSITORY.editComment(props.comment.commentId, state.commentEditForm)
    .then(() => {
      router.go(0)
    })
    .catch((e) => {
      console.error(e)
    })
}

const deleteComment = () => {
  ElMessageBox.confirm('정말로 삭제하시겠습니까?', '댓글 삭제', {
    type: 'warning',
    icon: markRaw(Delete),
  }).then(() => {
    COMMENT_REPOSITORY.deleteComment(props.comment.commentId)
      .then(() => {
        router.go(0)
      })
      .catch((e) => {
        console.error(e)
      })
  })
}

const replyToComment = () => {
  state.recommenting = !state.recommenting
  state.reCommentCreateForm = new CommentCreateForm()
  state.reCommentCreateForm.setItemId(props.comment.itemId)
  state.reCommentCreateForm.setParentCommentId(props.comment.commentId)
}

const postRecomment = () => {
  COMMENT_REPOSITORY.createComment(state.reCommentCreateForm).then(() => {
    router.go(0)
  })
}

const likeComment = () => {
  COMMENT_REPOSITORY.likeComment(props.comment.commentId)
    .then(() => {
      props.comment.likeCnt += 1
      ElMessage.success('댓글 추천 완료')
    })
    .catch((e) => {
      ElMessage.warning(e.message || '댓글 추천 실패.')
    })
}

const dislikeComment = () => {
  COMMENT_REPOSITORY.dislikeComment(props.comment.commentId)
    .then(() => {
      props.comment.dislikeCnt += 1
      ElMessage.success('댓글 비추천 완료')
    })
    .catch((e) => {
      ElMessage.warning(e.message || '댓글 추천 실패.')
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

.comment-util-btn .el-button {
  margin: 4px;
  padding: 8px;
}

.comment-footer {
  padding: 5px 10px;
  display: flex;
  justify-content: flex-end;
  font-size: 12px;
}

.parent-badge {
  color: #3493ff;
  font-size: 15px;
  background-color: #eaf4ff;
  border-radius: 10px;
  padding: 0 5px;
}

.reply-comment {
  margin-left: 20px;
}

.re-reply-btn {
  margin-top: 5px;
  margin-bottom: 50px;
}

.deleted-comment {
  color: red;
  font-style: italic;
}
</style>
