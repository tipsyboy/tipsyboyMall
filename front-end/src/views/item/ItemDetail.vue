<template>
  <CenterLayout>
    <div>
      <el-empty :image-size="150" v-if="!itemInfo.itemImages || itemInfo.itemImages.length === 0" />
      <el-carousel v-else indicator-position="outside" :pause-on-hover="true" height="430px">
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
          <el-descriptions-item>{{ formatDateTime(itemInfo.createdDate) }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div class="item-info-right">
        <el-descriptions :column="1">
          <el-descriptions-item label="상태">{{ itemInfo.status }}</el-descriptions-item>
          <el-descriptions-item
            ><el-input-number v-model="count" :min="1" :max="itemInfo.stock" label="수량"
          /></el-descriptions-item>
          <el-descriptions-item
            ><el-button type="info" plain @click="addToCart"
              >카트에 담기</el-button
            ></el-descriptions-item
          >
        </el-descriptions>
      </div>
    </div>
    <el-divider />
    <div class="item-description">
      <div>{{ itemInfo.description }}</div>
    </div>
    <el-divider />
    <CommentVue />
  </CenterLayout>
</template>

<script setup lang="ts">
import axios from 'axios'
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import moment from 'moment'
import { markRaw } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Edit } from '@element-plus/icons-vue'
import useMemberStore from '@/stores/memberInfo'
import CommentVue from '../comment/CommentVue.vue'

const route = useRoute()
const router = useRouter()
const itemInfo = ref<any>([])
const memberStore = useMemberStore()
const count = ref(1)
const requestUrl = 'http://localhost:8080/items'

onMounted(() => {
  axios
    .get(`${requestUrl}/${route.params.itemId}`, {
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
  ElMessageBox.confirm('정말로 삭제하시겠습니까?', '삭제', {
    type: 'warning',
    icon: markRaw(Delete)
  })
    .then(async () => {
      await axios
        .delete(`http://localhost:8080/items/${itemInfo.value.itemId}`, {
          withCredentials: true
        })
        .then(() => {
          ElMessage({
            message: '삭제되었습니다.',
            type: 'success'
          })
          router.push({ name: 'itemList' })
        })
        .catch((error) => {
          console.error(error)
          ElMessage.error('삭제할 수 없습니다.')
        })
    })
    .catch(() => {
      console.log('삭제 취소')
    })
}

const toEditPage = () => {
  const itemId = itemInfo.value.itemId
  router.push({ name: 'itemEdit', params: { itemId } })
}

const getImageUrl = (storedName: string) => {
  return `http://localhost:8080/images/${storedName}`
}

const addToCart = () => {
  const cartItem = {
    itemId: itemInfo.value.itemId, // 상품 ID
    count: count.value // 상품 수량
  }
  axios
    .post('http://localhost:8080/api/cartItem', cartItem, {
      withCredentials: true
    })
    .then(() => {
      ElMessageBox.confirm(
        '상품이 장바구니에 추가되었습니다. 장바구니로 이동 하시겠습니까?',
        '알림',
        {
          confirmButtonText: '확인',
          cancelButtonText: '취소',
          type: 'success'
        }
      )
        .then(() => {
          router.replace('/cart')
        })
        .catch(() => {})
    })
    .catch((error) => {
      console.error('카트에 상품을 추가하는 도중 에러 발생:', error)
      ElMessage.error('상품을 장바구니에 추가할 수 없습니다.')
    })
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
