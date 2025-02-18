<template>
  <CenterLayout>
    <div class="order-page">
      <div class="left">
        <DeliveryInput />
        <ItemInfo :selectedItems="state.selectedItem" />
      </div>
      <div class="right">
        <PaymentSummary :selectedItems="state.selectedItem" />
      </div>
    </div>
  </CenterLayout>
</template>

<script setup lang="ts">
import DeliveryInput from './components/DeliveryInput.vue'
import ItemInfo from './components/ItemInfo.vue'
import PaymentSummary from './components/PaymentSummary.vue'
import { container } from 'tsyringe'
import CartRepository from '@/repository/CartRepository'
import { onMounted, reactive, provide } from 'vue'
import CartItem from '@/entity/cart/CartItem'
import OrderCreateForm from '@/entity/order/OrderCreateForm'

const CART_REPOSITORY = container.resolve(CartRepository)

type StateType = {
  orderCreateForm: OrderCreateForm
  selectedItem: CartItem[]
}

const state = reactive<StateType>({
  selectedItem: [] as CartItem[],
  orderCreateForm: new OrderCreateForm(),
})

onMounted(() => {
  getCartItem(history.state.cartItemIds)
  const profile = JSON.parse(localStorage.getItem('profile') || '{}')
  state.orderCreateForm.orderer = profile.nickname || ''
  state.orderCreateForm.email = profile.email || ''
})

const getCartItem = (cartItemIds) => {
  CART_REPOSITORY.getCartItem(cartItemIds)
    .then((cartItems) => {
      state.selectedItem = cartItems
    })
    .catch((e) => {
      console.error(e)
    })
}

provide('orderCreateForm', state.orderCreateForm) // Provide를 사용해서 하위 컴포넌트에서 접근 가능하게 만듦
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
