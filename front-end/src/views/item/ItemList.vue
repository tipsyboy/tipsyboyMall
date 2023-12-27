<template>
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
    <el-table-column :prop="['createdDate']" label="날짜">
      <template v-slot="{ row }">
        {{ formatDateTime(row.createdDate) }}
      </template>
    </el-table-column>
    <el-table-column prop="xxx" label="조회수" />
  </el-table>
</template>
<script lang="ts" setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import moment from 'moment'
import { useRouter } from 'vue-router'

const router = useRouter()
const items = ref([])

onMounted(async () => {
  await axios
    .get('http://localhost:8080/items')
    .then((response) => {
      console.log(response)
      items.value = response.data
    })
    .catch((error) => {
      console.log(error)
    })
})

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
</style>
