<template>
  <CenterLayout>
    <el-table :data="state.itemList.contents" @row-click="toItemDetail">
      <el-table-column prop="itemId" label="No"></el-table-column>
      <el-table-column prop="itemName" label="제목" style="cursor: pointer"></el-table-column>
      <el-table-column prop="seller" label="작성자"></el-table-column>
      <el-table-column label="작성일시">
        <template v-slot="{ row }">
          {{ formatDateTime(row as Item) }}
        </template>
      </el-table-column>
    </el-table>

    <div class="paging-container">
      <el-pagination
        class="paging-bar"
        :hide-on-single-page="true"
        background
        layout="prev, pager, next"
        v-model:current-page="state.itemList.page"
        :total="state.itemList.totalCount"
        :default-page-size="10"
        @current-change="(page: number) => getItemList(page)"
      />
    </div>
  </CenterLayout>
</template>

<script setup lang="ts">
import Paging from '@/entity/data/Paging'
import Item from '@/entity/item/Item'
import ItemRepository from '@/repository/ItemRepository'
import { container } from 'tsyringe'
import { onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'

const ITEM_REPOSITORY = container.resolve(ItemRepository)
const router = useRouter()
type StateType = {
  itemList: Paging<Item>
}
const state = reactive<StateType>({
  itemList: new Paging<Item>(),
})

onMounted(() => {
  getItemList()
})

const getItemList = (page = 1) => {
  ITEM_REPOSITORY.getItemList(page)
    .then((itemList) => {
      state.itemList = itemList
    })
    .catch((e) => {
      console.error('Failed to get item list.', e)
    })
}

const toItemDetail = (item: Item) => {
  router.push({ name: 'itemDetail', params: { itemId: item.itemId } })
}
const formatDateTime = (item: Item) => {
  return item.getFormattedDateTime()
}
</script>

<style>
.paging-container {
  display: flex;
  justify-content: center;
}

.paging-bar {
  margin-top: 2rem;
}
</style>
