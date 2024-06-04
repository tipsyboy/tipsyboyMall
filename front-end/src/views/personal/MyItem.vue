<template>
  <CenterLayout>
    <ProfileMenu />
    <el-table :data="items" @row-click="showItemDetail" style="width: 100%" :row-class-name="rowClassName">
      <el-table-column prop="itemId" label="No" width="50" />
      <el-table-column label="카테고리">게시글 상태</el-table-column>
      <el-table-column prop="itemName" label="상품명" width="180"> </el-table-column>
      <el-table-column prop="status" label="상태" width="180" />
      <el-table-column :prop="'createdDate'" label="날짜">
        <template v-slot="{ row }">
          {{ formatDateTime(row.createdDate) }}
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :hide-on-single-page="true"
      class="paging-bar"
      background
      layout="prev, pager, next"
      @current-change="handlePageChange"
      :default-page-size="pageSize"
      :total="totalCount"
    />
  </CenterLayout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import moment from 'moment'
import ProfileMenu from './ProfileMenu.vue'

const requestUrl = 'http://localhost:8080/items/my-items'
const router = useRouter()
const items = ref([])
const currentPage = ref(1)
const pageSize = ref(12)
const totalCount = ref(0)

onMounted(async () => {
  fetchData()
})

const fetchData = async () => {
  const queryParams = {
    page: currentPage.value,
    size: pageSize.value,
  }
  await axios
    .get(requestUrl, { params: queryParams, withCredentials: true })
    .then((response) => {
      console.log(response)
      items.value = response.data.content
    })
    .catch((error) => {
      console.log(error)
    })
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchData()
}

const formatDateTime = (dateTime: any) => {
  return moment(dateTime).format('YYYY-MM-DD HH:mm')
}

const showItemDetail = (row: any) => {
  router.push({ name: 'itemDetail', params: { itemId: row.itemId } })
}

const rowClassName = (row: any, index: number) => {
  return 'hovered-row'
}
</script>

<style scoped></style>
