<template>
  <div class="search-bar">
    <el-select v-model="state.itemSearchForm.searchType" style="width: 120px">
      <el-option label="제목" value="title"></el-option>
      <el-option label="작성자" value="seller"></el-option>
    </el-select>
    <el-input
      v-model="state.itemSearchForm.query"
      placeholder="검색어를 입력해주세요."
      @keyup.enter="onSearch()"
      clearable
    >
      <template #append>
        <el-button :icon="Search" @click="onSearch()"> Search </el-button>
      </template>
    </el-input>
  </div>
</template>

<script setup lang="ts">
import Paging from '@/entity/data/Paging'
import Item from '@/entity/item/Item'
import ItemSearchForm from '@/entity/item/ItemSearchForm'
import ItemRepository from '@/repository/ItemRepository'
import { Search } from '@element-plus/icons-vue'
import { container } from 'tsyringe'
import { reactive } from 'vue'

const emit = defineEmits(['search'])
const ITEM_REPOSITORY = container.resolve(ItemRepository)

const state = reactive({
  itemSearchForm: new ItemSearchForm(),
  searchedItemList: new Paging<Item>(),
})

const onSearch = (page = 1) => {
  ITEM_REPOSITORY.searchItem(page, state.itemSearchForm)
    .then((searchedItemList) => {
      state.searchedItemList = searchedItemList
      emit('search', state.searchedItemList)
    })
    .catch((e) => {
      console.error(e)
    })
}
</script>

<style scoped>
.search-bar {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 20px;
}

.el-input {
  width: 400px;
}
</style>
