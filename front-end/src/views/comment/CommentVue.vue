<template>
  <h3>{{ state.commentList.contents.length }}개의 댓글</h3>

  <CommentItem :comment="comment" v-for="comment in state.commentList.contents" :key="comment.commentId" />

  <CommentCreate :itemId="props.itemId" />
</template>

<script setup lang="ts">
import { onMounted, reactive } from 'vue'
import CommentItem from './CommentItem.vue'
import CommentCreate from './CommentCreate.vue'
import { container } from 'tsyringe'
import CommentRepository from '@/repository/CommentRepository'
import Paging from '@/entity/data/Paging'
import Comment from '@/entity/comment/Comment'

const COMMENT_REPOSITORY = container.resolve(CommentRepository)
const props = defineProps<{
  itemId: number
}>()

type StateType = {
  commentList: Paging<Comment>
}

const state = reactive<StateType>({
  commentList: new Paging<Comment>(),
})

onMounted(() => {
  getCommentList()
})

const getCommentList = (page = 1) => {
  COMMENT_REPOSITORY.getCommentList(props.itemId, page)
    .then((commentList) => {
      state.commentList = commentList
      console.log(state.commentList)
    })
    .catch((e) => {
      console.error(e)
    })
}
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
