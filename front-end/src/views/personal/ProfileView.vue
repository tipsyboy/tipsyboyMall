<template>
  <CenterLayout>
    <ProfileMenu :user-info="userInfo" />
    <el-empty :image-size="150" />
    <h2>{{ $route.params.nickname }}</h2>
    <el-descriptions direction="vertical" :column="1" border>
      <el-descriptions-item label="E-mail" span="2">{{ userInfo.email }}</el-descriptions-item>

      <el-descriptions-item label="Address">// TODO: 주소 </el-descriptions-item>
    </el-descriptions>
  </CenterLayout>
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
      withCredentials: true,
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

<style scoped>
.profile-container {
  padding: 0 0;
  margin-top: 5%;
  max-width: 70%;
  margin-left: 15%;
}
</style>
