<template>
  <div class="main-container">
    <!-- main page header -->
    <div class="main-header">
      <h2 class="header__shop-name" style="color: white">책책책 책을 읽읍시다</h2>
      <div class="header__search-container">
        <el-input
          v-model="state.itemSearchForm.query"
          class="search__input"
          size="large"
          placeholder="검색어를 입력하세요."
          :prefix-icon="Search"
          @keyup.enter="searchItem()"
        />
      </div>
    </div>

    <!-- main page content -->
    <div class="main-rep-items">
      <el-carousel
        :interval="5000"
        type="card"
        height="380px"
        indicator-position="outside"
        :pause-on-hover="true"
      >
        <el-carousel-item
          v-for="(item, index) in state.representativeItemList.contents"
          :key="index"
        >
          <div class="rep-items__content">
            <div class="rep-item__image-container" @click="toItemDetail(item.itemId)">
              <el-image
                class="rep-item__image"
                :src="getImageByFileName(item.itemImages[0]?.storedName)"
                alt="Product Image"
              />
            </div>
            <div class="rep-item__info">
              <h3 class="rep-item__name" @click="toItemDetail(item.itemId)">{{ item.itemName }}</h3>
              <div class="rep-item__price" @click="toItemDetail(item.itemId)">
                ₩ {{ formatCurrency(item.price) }}
              </div>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>
  </div>
</template>

<script setup lang="ts">
import Paging from '@/entity/data/Paging'
import Item from '@/entity/item/Item'
import ItemSearchForm from '@/entity/item/ItemSearchForm'
import ItemRepository from '@/repository/ItemRepository'
import { Search } from '@element-plus/icons-vue'
import { container } from 'tsyringe'
import { onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { formatCurrency } from '@/utils/utils'

const ITEM_REPOSITORY = container.resolve(ItemRepository)
const router = useRouter()

const state = reactive({
  itemSearchForm: new ItemSearchForm(),
  representativeItemList: new Paging<Item>(),
})

onMounted((page = 1) => {
  ITEM_REPOSITORY.getItemList(page).then(
    (representativeItemList) => (state.representativeItemList = representativeItemList),
  )
})

const searchItem = (page = 1) => {
  // ITEM_REPOSITORY.searchItem(page, state.itemSearchForm)
  //   .then((searchedItemList) => {
  //     console.log(searchedItemList)
  //   })
  //   .catch((e) => console.error(e))
  router.push({
    path: '/items',
    query: { searchType: state.itemSearchForm.searchType, query: state.itemSearchForm.query },
  })
}

const toItemDetail = (itemId: number) => {
  router.push({ name: 'itemDetail', params: { itemId: itemId } })
}

const getImageByFileName = (storedName: string) => {
  return storedName ? `/api/images/${storedName}` : '@element-plus/theme-chalk/el-icon-picture'
}
</script>

<style scoped>
/* main page header block*/
.main-header {
  background-color: black;
  height: 300px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.header__shop-name {
  display: flex;
  justify-content: center;
}

.header__search-container {
  width: 40vw;
  margin: 0 auto;
  display: flex;
  justify-content: center;
  --el-border-radius-base: 25px;
}

.search__input {
  height: 50px;
  min-width: 100px;
}

/* main page rep-items block*/
.main-rep-items {
  margin-top: 50px;
}

.rep-items__content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 90%;
}

.rep-item__image-container {
  max-width: 200%;
  max-height: 100%;
  overflow: hidden;
}

.rep-item__image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.rep-item__name {
  margin-bottom: 5px;
}
</style>
