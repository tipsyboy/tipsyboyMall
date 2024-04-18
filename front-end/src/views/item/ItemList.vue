<template>
  <CenterLayout>
    <el-table
      :data="items"
      @row-click="showItemDetail"
      style="width: 100%"
      :row-class-name="rowClassName"
    >
      <el-table-column prop="itemId" label="No" width="50" />
      <el-table-column label="카테고리">카테고리</el-table-column>
      <el-table-column prop="itemName" label="상품명" width="180"> </el-table-column>
      <el-table-column prop="status" label="상태" width="180" />
      <el-table-column prop="seller" label="작성자" width="180" />
      <el-table-column :prop="'createdDate'" label="날짜">
        <template v-slot="{ row }">
          {{ formatDateTime(row.createdDate) }}
        </template>
      </el-table-column>
      <el-table-column prop="xxx" label="조회수" />
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

<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import moment from 'moment'
import { useRouter } from 'vue-router'

const router = useRouter()
const requestUrl = 'http://localhost:8080/items'
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
    size: pageSize.value
  }

  await axios
    .get(requestUrl, { params: queryParams })
    .then((response) => {
      console.log(response)
      items.value = response.data.content
      totalCount.value = response.data.totalElements
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

<style>
.hovered-row {
  cursor: pointer;
}

.paging-bar {
  margin-top: 10px;
  margin-left: 20%;
}
</style>
