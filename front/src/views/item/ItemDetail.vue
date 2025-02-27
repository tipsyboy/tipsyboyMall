<template>
  <CenterLayout>
    <div class="item-images">
      <el-empty
        :image-size="150"
        v-if="!state.item.itemImages || state.item.itemImages.length === 0"
      />
      <el-carousel v-else indicator-position="outside" :pause-on-hover="true" height="430px">
        <el-carousel-item v-for="(itemImage, index) in state.item.itemImages" :key="index">
          <img class="item-image" :src="getImageByFileName(itemImage.storedName)" />
        </el-carousel-item>
      </el-carousel>
    </div>

    <div class="item-header">
      <h1 class="item-name">{{ state.item.itemName }}</h1>

      <div class="item-btn" v-if="getAuthor() === state.item.seller">
        <el-button type="primary" @click="toEditItem()" :icon="Edit" circle />
        <el-button type="danger" @click="deleteItem()" :icon="Delete" circle />
      </div>
    </div>

    <el-divider />

    <div class="item-info-container">
      <div class="item-info-left">
        <el-descriptions :column="1" size="large">
          <el-descriptions-item label="판매자">{{ state.item.seller }}</el-descriptions-item>
          <el-descriptions-item label="가격">
            {{ formatCurrency(state.item.price) }} 원
          </el-descriptions-item>
          <el-descriptions-item label="재고">{{ state.item.stock }}</el-descriptions-item>
          <el-descriptions-item>{{ state.item.getFormattedDateTime() }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <div class="item-info-right">
        <el-descriptions :column="1">
          <el-descriptions-item label="상태">{{ state.item.status }}</el-descriptions-item>
          <el-descriptions-item label="수량">
            <el-input-number v-model="state.cartItemCreateForm.count" :min="1" />
          </el-descriptions-item>
          <el-descriptions-item>
            <el-button type="info" @click="addToCart(state.item.itemId)" :icon="ShoppingCart" plain>
              카트에 담기
            </el-button>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </div>

    <el-divider />

    <div class="item-description">
      <div>{{ state.item.description }}</div>
    </div>

    <el-divider />

    <CommentVue :itemId="props.itemId" />
  </CenterLayout>
</template>

<script setup lang="ts">
import { markRaw, onMounted, reactive } from 'vue'
import { container } from 'tsyringe'
import ItemRepository from '@/repository/ItemRepository'
import { Delete, Edit, Check, ShoppingCart } from '@element-plus/icons-vue'
import CommentVue from '../comment/CommentVue.vue'
import Item from '@/entity/item/Item'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import CartItemCreateForm from '@/entity/cart/CartItemCreateForm'
import CartRepository from '@/repository/CartRepository'
import { formatCurrency } from '@/utils/utils'

const ITEM_REPOSITORY = container.resolve(ItemRepository)
const CART_REPOSITORY = container.resolve(CartRepository)
const router = useRouter()
const props = defineProps<{
  itemId: number
}>()

type StateType = {
  item: Item
  cartItemCreateForm: CartItemCreateForm
}

const state = reactive<StateType>({
  item: new Item(),
  cartItemCreateForm: new CartItemCreateForm(),
})

onMounted(() => {
  getItem()
})

const getItem = () => {
  ITEM_REPOSITORY.getItem(props.itemId)
    .then((item: Item) => {
      state.item = item
      console.log(state.item)
    })
    .catch((e) => {
      console.error(e)
    })
}

const toEditItem = () => {
  router.push({ name: 'itemEdit' })
}

const deleteItem = () => {
  ElMessageBox.confirm('정말로 삭제하시겠습니까?', '삭제', {
    type: 'warning',
    icon: markRaw(Delete),
  })
    .then(() => {
      ITEM_REPOSITORY.deleteItem(props.itemId)
        .then(() => {
          ElMessage.success('삭제되었습니다.')
          router.push('/items')
        })
        .catch((e) => {
          console.error(e)
          ElMessage.error('삭제할 수 없습니다.')
        })
    })
    .catch(() => {
      console.log('삭제 - 취소함')
    })
}

const addToCart = (itemId: number) => {
  ElMessageBox.confirm('상품을 장바구니에 추가하시겠습니까?', '상품 추가', {
    type: 'success',
    icon: markRaw(Check),
  })
    .then((cartItemId) => {
      state.cartItemCreateForm.setItemId(itemId)

      CART_REPOSITORY.addToCart(state.cartItemCreateForm)
        .then((cartItemId) => {
          ElMessageBox.confirm(
            '장바구니에 상품이 추가되었습니다. 장바구니로 이동하시겠습니까?',
            '장바구니',
            {
              type: 'success',
              icon: markRaw(Check),
            },
          )
            .then(() => {
              router.push('/cart')
            })
            .catch(() => {})
        })
        .catch((e) => {
          console.error('>>>> 에러임.', e)
        })
    })
    .catch(() => {
      console.log('장바구니 등록 취소.')
    })
}

const getAuthor = () => {
  const profileData = localStorage.getItem('profile')
  if (profileData !== null) {
    const profile = JSON.parse(profileData)
    return profile.nickname
  }
  return null
}

const getImageByFileName = (storedName: string) => {
  return `/api/images/${storedName}`
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
  white-space: pre-line;
}
.el-carousel__item .item-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
}
</style>
