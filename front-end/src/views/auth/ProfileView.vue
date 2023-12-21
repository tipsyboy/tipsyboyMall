<template>
  <el-descriptions :title="$route.params.nickname" direction="vertical" :column="2" border>
    <el-descriptions-item label="Nickname">{{ userInfo.nickname }}</el-descriptions-item>
    <el-descriptions-item label="Telephone">//TODO: 전화번호</el-descriptions-item>
    <el-descriptions-item label="E-mail">{{ userInfo.email }}</el-descriptions-item>
    <el-descriptions-item label="Place" :span="2">//TODO: place</el-descriptions-item>
    <el-descriptions-item label="Remarks">
      <el-tag size="small">//TODO: School</el-tag>
    </el-descriptions-item>
    <el-descriptions-item label="Address"
      >// TODO: No.1188, Wuzhong Avenue, Wuzhong District, Suzhou, Jiangsu Province
    </el-descriptions-item>
  </el-descriptions>
</template>

<script setup lang="ts">
import axios from 'axios'
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
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
