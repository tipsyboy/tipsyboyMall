<template>
  <CenterLayout>
    <h1>주문이 완료되었습니다!</h1>
    <el-divider />

    <!-- 상품 정보 -->
    <ItemInfo :selectedItems="state.order.orderItemList" />

    <!-- 주문 정보 -->
    <el-descriptions :column="2" :border="true" style="margin: 30px 0">
      <template #title>
        <h2>주문/배송 정보</h2>
      </template>
      <el-descriptions-item label="주문번호" width="25%">Y23712093</el-descriptions-item>
      <el-descriptions-item label="주문일자">{{ state.order.getFormattedDateTime() }}</el-descriptions-item>
      <el-descriptions-item label="주문하신 분">주문자</el-descriptions-item>
      <el-descriptions-item label="받으시는 분">받는사람</el-descriptions-item>
      <el-descriptions-item label="전화번호" :span="2">010-1234-5678</el-descriptions-item>
      <el-descriptions-item label="배송지 주소">
        <span> <el-tag size="small">도로명</el-tag>(13529) 경기 성남시 분당구 판교역로 166 (카카오 판교아지트) </span>
        <br />
        <span> <el-tag size="small">지번</el-tag>경기 성남시 분당구 백현동 532 </span>
      </el-descriptions-item>
    </el-descriptions>

    <!-- 결제 정보-->
    <el-descriptions :column="2" :border="true" style="margin: 30px 0">
      <template #title>
        <h2>결제 정보</h2>
      </template>
      <el-descriptions-item label="총 주문금액" :span="2">55,800원</el-descriptions-item>
      <el-descriptions-item label="총 할인 금액" width="25%">0원</el-descriptions-item>
      <el-descriptions-item label="총 포인트 사용액" width="25%">0원</el-descriptions-item>
      <el-descriptions-item label="실 결제금액">55,800원</el-descriptions-item>
      <el-descriptions-item label="총 적립액">0원</el-descriptions-item>
      <el-descriptions-item label="결제하신 금액" :span="2">55,800원</el-descriptions-item>
      <el-descriptions-item label="결제수단" :span="2">55,800원</el-descriptions-item>
    </el-descriptions>

    <div class="home-btn-container" style="margin: 30px 0">
      <el-button type="primary" size="large" @click="toItemList()">돌아가기</el-button>
    </div>
  </CenterLayout>
</template>

<script setup lang="ts">
import Order from '@/entity/order/Order'
import DeliveryInfo from './components/DeliveryInfo.vue'
import ItemInfo from './components/ItemInfo.vue'
import PaymentDetail from './components/PaymentDetail.vue'
import { onMounted, reactive } from 'vue'
import { container } from 'tsyringe'
import OrderRepository from '@/repository/OrderRepository'
import { useRouter } from 'vue-router'

const ORDER_REPOSITORY = container.resolve(OrderRepository)
const router = useRouter()

const props = defineProps<{
  orderId: number
}>()

type StateType = {
  order: Order
}

const state = reactive<StateType>({
  order: new Order(),
})

onMounted(() => {
  getOrder()
})

const getOrder = () => {
  ORDER_REPOSITORY.getOrder(props.orderId).then((order: Order) => {
    console.log(order)
    state.order = order
    console.log(state.order)
  })
}

const toItemList = () => {
  location.href = '/'
}
</script>

<style scoped>
.home-btn-container {
  display: flex;
  justify-content: center;
}
</style>
