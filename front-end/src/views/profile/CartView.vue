<template>
  <CenterLayout>
    <el-table :data="state.cartItemList.contents" @selection-change="handleSelectionChange">
      <el-table-column type="selection"></el-table-column>
      <el-table-column prop="itemId" label="No" width="80"></el-table-column>

      <el-table-column>
        <template v-slot="{ row }">
          <el-image style="width: 80px; height: 80px" :src="getImageUrl(row.itemThumbnailImage)" fit="cover" />
        </template>
      </el-table-column>

      <el-table-column prop="itemName" label="상품명"></el-table-column>
      <el-table-column prop="count" label="수량" header-align="center">
        <template v-slot="{ row }">
          <div class="quantity-container">
            <el-input-number
              v-model="row.count"
              :min="1"
              :max="row.stock"
              controls-position="right"
              size="small"
              class="quantity-input"
            />
            <el-button class="cartItem-update" @click="editCartItemCount(row.cartItemId, row.count)" size="small">
              변경
            </el-button>
          </div>
        </template>
      </el-table-column>

      <el-table-column prop="price" label="가격"></el-table-column>

      <el-table-column prop="total" label="합계" header-align="center">
        <template v-slot="{ row }">
          {{ row.price * row.count }}
        </template>
      </el-table-column>

      <el-table-column>
        <template v-slot="{ row }">
          <el-button type="danger" @click="deleteCartItem(row.cartItemId)">삭제</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="cartView-footer">
      <div>선택한 상품 가격: {{ calSelectedCartItemsPrice() }}</div>
      <el-button @click="goToOrderPreview" type="primary"> 선택한 상품 주문 </el-button>
    </div>
  </CenterLayout>
</template>

<script setup lang="ts">
import CartItem from '@/entity/cart/CartItem'
import CartItemEditForm from '@/entity/cart/CartItemEditForm'
import Paging from '@/entity/data/Paging'
import CartRepository from '@/repository/CartRepository'
import { ElMessage, ElMessageBox } from 'element-plus'
import { container } from 'tsyringe'
import { markRaw, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { Delete } from '@element-plus/icons-vue'

const CART_REPOSITORY = container.resolve(CartRepository)
const router = useRouter()

type StateType = {
  cartItemList: Paging<CartItem>
  cartItemEditForm: CartItemEditForm
  selectedCartItem: CartItem[]
}

const state = reactive<StateType>({
  cartItemList: new Paging<CartItem>(),
  cartItemEditForm: new CartItemEditForm(),
  selectedCartItem: [],
})

onMounted(() => {
  getCartItemList()
})

const getCartItemList = (page = 1) => {
  CART_REPOSITORY.getCartItemList(page)
    .then((cartItemList) => {
      state.cartItemList = cartItemList
    })
    .catch((e) => {
      console.error('>>>>', e)
    })
}

const editCartItemCount = (cartItemId: number, count: number) => {
  state.cartItemEditForm.setCartItemId(cartItemId)
  state.cartItemEditForm.setCount(count)
  CART_REPOSITORY.editCartItemCount(state.cartItemEditForm, cartItemId)
    .then(() => {
      ElMessage.success('수량이 변경되었습니다.')
      router.go(0)
    })
    .catch((e) => {
      ElMessage.error('에러')
      console.error('>>>>', e)
    })
}

const deleteCartItem = (cartItemId: number) => {
  ElMessageBox.confirm('정말로 삭제하시겠습니까?', '삭제', {
    type: 'warning',
    icon: markRaw(Delete),
  })
    .then(() => {
      CART_REPOSITORY.deleteCartItem(cartItemId)
        .then(() => {
          ElMessage.success('성공적으로 삭제되었습니다.')
          router.go(0)
        })
        .catch((e) => {
          console.error(e)
          ElMessage.error('삭제할 수 없습니다.')
        })
    })
    .catch(() => {
      console.log('삭제 취소')
    })
}

const goToOrderPreview = () => {
  const selectedCartItemIds = state.selectedCartItem.map((cartItem) => cartItem.cartItemId)
  router.push({
    name: 'orderPreview',
    state: {
      cartItemIds: selectedCartItemIds,
    },
  })
}

const handleSelectionChange = (selectedCartItem: CartItem[]) => {
  state.selectedCartItem = selectedCartItem
}

const calSelectedCartItemsPrice = () => {
  return state.selectedCartItem.reduce((total, cartItem) => {
    return total + cartItem.price * cartItem.count
  }, 0)
}

const getImageUrl = (storedName: string) => {
  return storedName ? `/api/images/${storedName}` : '@element-plus/theme-chalk/el-icon-picture'
}
</script>

<style scoped>
.quantity-container {
  display: flex;
  align-items: center;
  flex-direction: column;
}

.quantity-input {
  min-width: 100px;
  width: 70%;
}
.cartItem-update {
  margin-top: 5px;
}

.cartView-footer {
  margin-top: 2rem;
  display: flex;
  justify-content: space-between;
  /* align-items: center; */
}
</style>
