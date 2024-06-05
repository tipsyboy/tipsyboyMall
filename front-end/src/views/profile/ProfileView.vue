<template>
  <CenterLayout>
    <ProfileMenu :nickname="state.findMember.nickname" />

    <router-view />

    <el-empty :image-size="150" />
    <h2>{{ state.findMember.nickname }}</h2>
    <el-descriptions direction="vertical" :column="1" :border="true">
      <el-descriptions-item label="E-mail" span="2">{{ state.findMember.email }}</el-descriptions-item>

      <el-descriptions-item label="Address">// TODO: 주소 </el-descriptions-item>
    </el-descriptions>
  </CenterLayout>
</template>

<script setup lang="ts">
import { container } from 'tsyringe'
import ProfileMenu from './ProfileMenu.vue'
import MemberRepository from '@/repository/MemberRepository'
import { onMounted, reactive } from 'vue'
import LoginMember from '@/entity/member/LoginMember'
import { useRouter } from 'vue-router'

const MEMBER_REPOSITORY = container.resolve(MemberRepository)
const router = useRouter()

const props = defineProps<{
  nickname: string
}>()

type StateType = {
  findMember: LoginMember
}

const state = reactive<StateType>({
  findMember: new LoginMember(),
})

onMounted(() => {
  MEMBER_REPOSITORY.getProfileByNickname(props.nickname)
    .then((findMember) => {
      state.findMember = findMember
    })
    .catch((e) => {
      router.push({
        name: 'ErrorPage',
        state: { message: e.message },
      })
    })
})
</script>

<style scoped></style>
