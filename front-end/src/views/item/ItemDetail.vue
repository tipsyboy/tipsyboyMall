<template>
  <div>
    <el-empty :image-size="150" v-if="!itemInfo.itemImages || itemInfo.itemImages.length === 0" />
    <el-carousel indicator-position="outside" :pause-on-hover="true" height="430px">
      <el-carousel-item v-for="(itemImage, index) in itemInfo.itemImages" :key="index">
        <img class="item-image" :src="getImageUrl(itemImage.storedName)" />
      </el-carousel-item>
    </el-carousel>
  </div>

  <div class="item-header">
    <h1 class="item-name">{{ itemInfo.itemName }}</h1>
    <div class="item-btn" v-if="isOwner()">
      <el-button type="primary" @click="toEditPage" :icon="Edit" circle />
      <el-button type="danger" @click="deleteItem" :icon="Delete" circle />
    </div>
  </div>
  <el-divider />
  <div class="item-info-container">
    <div class="item-info-left">
      <el-descriptions :column="1" size="large">
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
import { useRoute, useRouter } from 'vue-router'
import moment from 'moment'
import { Delete, Edit } from '@element-plus/icons-vue'
import useMemberStore from '@/stores/memberInfo'

const route = useRoute()
const router = useRouter()
const itemInfo = ref<any>([])
const memberStore = useMemberStore()

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

const isOwner = () => {
  return memberStore.userInfo.nickname === itemInfo.value.seller
}

const formatDateTime = (dateTime: any) => {
  return moment(dateTime).format('YYYY-MM-DD HH:mm')
}

const deleteItem = () => {
  const confirmDelete = window.confirm('정말로 삭제하시겠습니까?')

  if (confirmDelete) {
    axios
      .delete(`http://localhost:8080/items/${itemInfo.value.itemId}`, {
        withCredentials: true
      })
      .then(() => {
        alert('삭제되었습니다.')
        router.push({ name: 'itemList' })
      })
      .catch((error) => {
        console.error(error)
      })
  }
}

const toEditPage = () => {
  const itemId = itemInfo.value.itemId
  router.push({ name: 'itemEdit', params: { itemId } })
}

const getImageUrl = (storedName: string) => {
  // 이미지 파일이 저장된 경로에 따라 수정이 필요할 수 있습니다.
  return `http://localhost:8080/images/${storedName}`
}
</script>

<style scoped>
.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
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
.el-carousel__item .item-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
}
</style>
