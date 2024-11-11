<template>
  <el-form>
    <el-form-item>
      <el-input v-model="state.commentCreateForm.content" type="textarea" rows="6" resize="none" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="postComment()">등록</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import CommentCreateForm from '@/entity/comment/CommentCreateForm'
import CommentRepository from '@/repository/CommentRepository'
import { container } from 'tsyringe'
import { reactive } from 'vue'
import { useRouter } from 'vue-router'

const COMMENT_REPOSITORY = container.resolve(CommentRepository)
const router = useRouter()

const props = defineProps<{
  itemId: number
}>()

type StateType = {
  commentCreateForm: CommentCreateForm
}

const state = reactive<StateType>({
  commentCreateForm: new CommentCreateForm(),
})

const postComment = () => {
  state.commentCreateForm.setItemId(props.itemId)
  console.log(state.commentCreateForm)
  COMMENT_REPOSITORY.createComment(state.commentCreateForm).then(() => {
    router.go(0)
  })
}
</script>

<style scoped></style>
