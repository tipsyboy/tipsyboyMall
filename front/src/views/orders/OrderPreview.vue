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
import { onMounted, reactive } from 'vue'
import CartItem from '@/entity/cart/CartItem'

const CART_REPOSITORY = container.resolve(CartRepository)

type StateType = {
  selectedItem: CartItem[]
}

const state = reactive<StateType>({
  selectedItem: [] as CartItem[],
})

onMounted(() => {
  getCartItem(history.state.cartItemIds)
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
