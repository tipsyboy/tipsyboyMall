<template>
  <CenterLayout>
    <ProfileMenu />
    <div class="my-order-container">
      <el-table
        :data="orders"
        @row-click="showOrderDetail"
        style="width: 100%"
        :row-class-name="rowClassName"
      >
        <el-table-column prop="itemId" label="No" width="50" />
        <el-table-column label="카테고리">게시글 상태</el-table-column>
        <el-table-column prop="itemName" label="상품명" width="180"> </el-table-column>
        <el-table-column prop="status" label="상태" width="180" />
        <el-table-column :prop="'createdDate'" label="날짜"> </el-table-column>
      </el-table>
    </div>
  </CenterLayout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import ProfileMenu from './ProfileMenu.vue'

const requestUrl = 'http://localhost:8080/orders'
const router = useRouter()
const orders = ref([])
const currentPage = ref(1)
const pageSize = ref(12)
const totalCount = ref(0)

onMounted(async () => {
  fetchData()
})

const fetchData = async () => {
  const queryParams = {
    page: currentPage.value,
    size: pageSize.value
  }
  await axios
    .get(requestUrl, { params: queryParams, withCredentials: true })
    .then((response) => {
      console.log(response)
    })
    .catch((error) => {
      console.log(error)
    })
}

const showOrderDetail = (row: any) => {
  router.push({ name: 'orderDetail', params: { orderId: row.orderId } })
}

const rowClassName = (row: any, index: number) => {
  return 'hovered-row'
}
</script>

<style scoped></style>
