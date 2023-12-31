<template>
  <ProfileMenu :user-info="userInfo" />
  <el-empty :image-size="150" />
  <h2>{{ $route.params.nickname }}</h2>
  <el-descriptions direction="vertical" :column="1" border>
    <el-descriptions-item label="E-mail">{{ userInfo.email }}</el-descriptions-item>
    <el-descriptions-item label="Remarks">
      <el-tag size="small">//TODO: School</el-tag>
    </el-descriptions-item>
    <el-descriptions-item label="Address">// TODO: 주소 </el-descriptions-item>
  </el-descriptions>
</template>

<script setup lang="ts">
import axios from 'axios'
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import ProfileMenu from './ProfileMenu.vue'

const userInfo = ref({ email: '', nickname: '' })
const route = useRoute()
onMounted(() => {
  axios
    .get(`http://localhost:8080/members/profile/${route.params.nickname}`, {
      withCredentials: true
    })
    .then((response) => {
      console.log(response.data)
      userInfo.value.email = response.data.email
      userInfo.value.nickname = response.data.nickname
    })
    .catch((error) => {
      console.error(error)
    })
})
</script>
