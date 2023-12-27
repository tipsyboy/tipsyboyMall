<template>
  <el-empty :image-size="150" />
  <h1 class="item-name">{{ itemInfo.itemName }}</h1>
  <el-divider />
  <div class="item-info-container">
    <div class="item-info-left">
      <el-descriptions column="1" size="large">
        <el-descriptions-item label="판매자">{{ itemInfo.seller }}</el-descriptions-item>
        <el-descriptions-item label="가격">{{ itemInfo.price }}</el-descriptions-item>
        <el-descriptions-item label="재고">{{ itemInfo.stock }}</el-descriptions-item>
        <el-descriptions-item label="상태">{{ itemInfo.status }}</el-descriptions-item>
      </el-descriptions>
    </div>
    <div class="item-info-right">
      <el-descriptions>
        <el-descriptions-item>{{ formatDateTime(itemInfo.createdDate) }}</el-descriptions-item>
      </el-descriptions>
    </div>
  </div>
  <el-divider />
  <div class="item-description">
    <div>{{ itemInfo.description }}</div>
  </div>
</template>

<script setup lang="ts">
import axios from 'axios'
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import moment from 'moment'

const route = useRoute()
const itemInfo = ref<any>([])
onMounted(() => {
  axios
    .get(`http://localhost:8080/items/${route.params.itemId}`, {
      withCredentials: true
    })
    .then((response) => {
      console.log(response.data)
      itemInfo.value = response.data
    })
    .catch((error) => {
      console.error(error)
    })
})

const formatDateTime = (dateTime: any) => {
  return moment(dateTime).format('YYYY-MM-DD HH:mm')
}
</script>

<style scoped>
.item-info-container {
  display: flex;
  justify-content: space-between;
}

.item-info-left,
.item-info-right {
  flex: 1;
}

.item-description {
  margin-top: 20px;
}
</style>
