<template>
  <CenterLayout>
    <el-table :data="cartItems" @selection-change="handleSelectionChange" style="width: 100%">
      <el-table-column type="selection"></el-table-column>

      <el-table-column align="center">
        <template v-slot="{ row }">
          <el-image
            style="width: 80px; height: 80px"
            :src="getImageUrl(row.itemThumnailImage)"
            fit="cover"
          />
        </template>
      </el-table-column>

      <el-table-column
        prop="itemName"
        label="상품 이름"
        align="center"
        header-align="center"
      ></el-table-column>

      <el-table-column prop="count" label="수량" align="center" header-align="center">
        <template v-slot="{ row }">
          <div class="quantity-container">
            <el-input-number
              v-model="row.count"
              :min="1"
              :max="row.stock"
              controls-position="right"
              size="small"
              class="quantity-input"
            ></el-input-number>
            <el-button
              class="cartItem-update"
              @click="updateCartItem(row.cartItemId, row.count)"
              size="small"
              >변경</el-button
            >
          </div>
        </template>
      </el-table-column>

      <el-table-column
        prop="price"
        label="가격"
        align="center"
        header-align="center"
      ></el-table-column>

      <el-table-column prop="total" label="합계" align="center" header-align="center">
        <template v-slot="{ row }">
          {{ row.price * row.count }}
        </template></el-table-column
      >

      <el-table-column>
        <template v-slot="{ row }">
          <el-button type="danger" @click="deleteCartItem(row.cartItemId)">삭제</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div>총 합계 가격: {{ getTotalPrice() }}</div>

    <el-button class="cart-order-btn" @click="cartToOrder()" type="primary">
      선택한 상품 주문
    </el-button>
  </CenterLayout>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { markRaw } from 'vue'
import { Delete } from '@element-plus/icons-vue'

const router = useRouter()
const cartItems = ref([])
const selectedItems = ref<any[]>([])
const requestUrl = 'http://localhost:8080/api/cartItem'

onMounted(async () => {
  fetchData()
})

const fetchData = async () => {
  await axios
    .get(requestUrl, { withCredentials: true })
    .then((response) => {
      cartItems.value = response.data
    })
    .catch((error) => {
      console.log(error)
    })
}

const updateCartItem = async (cartItemId: number, newCount: number) => {
  await axios
    .patch(
      `${requestUrl}/api/cartItem/${cartItemId}`,
      { cartItemId, count: newCount },
      { withCredentials: true }
    )
    .then(() => {
      console.log('수정이 완료됨.')
    })
    .catch((error) => {
      console.error(error)
    })
}

const deleteCartItem = async (cartItemId: number) => {
  ElMessageBox.confirm('정말로 삭제하시겠습니까?', '삭제', {
    type: 'warning',
    confirmButtonText: '삭제',
    cancelButtonText: '취소',
    icon: markRaw(Delete)
  })
    .then(async () => {
      await axios
        .delete(`${requestUrl}/api/cartItem/${cartItemId}`, { withCredentials: true })
        .then(() => {
          ElMessage({
            message: '삭제되었습니다.',
            type: 'success'
          })
          router.go(0)
          // window.location.reload()
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

const cartToOrder = async () => {
  // if (selectedItems.value.length === 0) {
  //   ElMessage({
  //     message: '선택된 상품이 없습니다.',
  //     type: 'warning'
  //   })
  //   return
  // }

  ElMessageBox.confirm('선택한 상품들을 주문합니다. 결제창으로 이동합니다.', '주문', {
    type: 'success',
    confirmButtonText: '확인',
    cancelButtonText: '취소'
  }).then(() => {
    const serializedItems = selectedItems.value.map((item) => ({
      cartItemId: item.cartItemId,
      itemName: item.itemName,
      itemThumnailImage: item.itemThumnailImage,
      price: item.price,
      count: item.count,
      stock: item.stock
    }))
    router.push({
      name: 'order',
      state: {
        tempData: { a: 1, b: 'string', c: true },
        selectedItems: serializedItems
      }
    })
  })
}

const getImageUrl = (storedName: string) => {
  return storedName
    ? `http://localhost:8080/images/${storedName}`
    : '@element-plus/theme-chalk/el-icon-picture'
}

const handleSelectionChange = (selectedRows: any) => {
  selectedItems.value = selectedRows
}

const getTotalPrice = () => {
  let totalPrice = 0
  for (const item of selectedItems.value) {
    totalPrice += item.price * item.count
  }
  return totalPrice
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

.cart-order-btn {
  margin-top: 10px;
}
</style>
