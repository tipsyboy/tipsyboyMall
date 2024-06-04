<template>
  <CenterLayout>
    <div class="order-page">
      <div class="left">
        <DeliveryInfo />
        <!-- :orderData="orderData" -->
        <ItemInfo :orderData="dummyOrderData" />
      </div>
      <div class="right">
        <PaymentSummary :orderData="dummyOrderData" />
      </div>
    </div>
  </CenterLayout>
</template>

<script setup lang="ts">
import DeliveryInfo from './components/DeliveryInfo.vue'
import ItemInfo from './components/ItemInfo.vue'
import PaymentSummary from './components/PaymentSummary.vue'

import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { componentToSlot } from 'element-plus/es/components/table-v2/src/utils.mjs'

const orderData = ref<any>(null)
const requestUrl = 'http://localhost:8080/api/cartItem/byIds'

onMounted(() => {
  temp()
  // const cartItemIds = history.state.selectedItemIds
  // fetchData(cartItemIds)
})

// const { tempData } = history.state
const temp = () => {
  console.log(history.state)
  // console.log(selectedItems)
}
const fetchData = async (cartItemIds: any) => {
  await axios
    .get(requestUrl, { params: { ids: cartItemIds }, withCredentials: true })
    .then((response) => {
      console.log(response)
    })
    .catch((error) => {
      console.log('실패임')
    })
}

const dummyOrderData = [
  {
    cartItemId: 1,
    itemId: 101,
    itemName: '상품 1',
    price: 10000,
    count: 2,
  },
  {
    cartItemId: 2,
    itemId: 102,
    itemName: '상품 2',
    price: 20000,
    count: 1,
  },
]
</script>

<style scoped>
.order-page {
  display: flex;
}

.left {
  flex: 1;
  max-width: 68%;
  margin-right: 30px;
}

.right {
  flex: 1;
  max-width: 28%;
}
</style>
