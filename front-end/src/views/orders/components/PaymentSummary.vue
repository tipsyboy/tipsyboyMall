<template>
  <el-card shadow="hover">
    <div class="total-price-container">
      <ul>
        <li>
          <span class="price-span-label">상품 금액</span>
          <span class="total-price-span">{{ priceFormatter(getTotalPrice()) }}</span>
        </li>
        <li>
          <span class="price-span-label">배송비</span>
          <span class="total-price-span">{{ priceFormatter(getDeliveryPrice()) }}</span>
        </li>
        <el-divider />
        <li>
          <strong class="price-span-label">최종 결제 금액</strong>
          <strong class="total-price-span" style="color: red; font-size: 20px">
            {{ priceFormatter(getFinalPrice()) }}
          </strong>
        </li>
      </ul>
    </div>

    <el-divider />
    <div class="payment-btn-container">
      <el-button type="primary" class="payment-btn" @click="handlePayment()">결제하기</el-button>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { ElMessage, ElMessageBox } from 'element-plus'

const props = defineProps<{
  orderData: any
}>()

const getTotalPrice = () => {
  let totalPrice = 0
  for (const item of props.orderData) {
    totalPrice += item.price * item.count
  }
  return totalPrice
}

const getDeliveryPrice = () => {
  const totalPrice = getTotalPrice()

  if (totalPrice >= 50000) {
    return 0
  }
  return 3000
}

const getDiscount = () => {
  return 0
}

const getFinalPrice = () => {
  return getTotalPrice() + getDeliveryPrice() - getDiscount()
}

const priceFormatter = (price: Number) => {
  return price.toLocaleString() + '원'
}

const handlePayment = () => {
  ElMessageBox.confirm(
    `최종 결제 금액 ${priceFormatter(getFinalPrice())}을 결제합니다.`,
    '결제 확인',
    {
      type: 'warning'
    }
  )
    .then(() => {
      ElMessage.success('호구 당첨')
    })
    .catch(() => {
      console.log('실패')
    })
}
</script>

<style scoped>
.total-price-container ul {
  padding-left: 0;
  list-style: none;
}

.total-price-container li {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}

.payment-btn-container {
  display: flex;
  justify-content: center;
}
.payment-btn {
  font-size: 18px;
  padding: 20px 40px;
}
</style>
