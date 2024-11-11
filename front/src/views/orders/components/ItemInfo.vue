<template>
  <div>
    <h2>주문 상품 {{ selectedItems.length }}종 {{ getTotalItemsCount() }}개</h2>
    <el-table :data="selectedItems" style="width: 100%">
      <el-table-column>
        <template v-slot="{ row }">
          <el-image style="width: 80px; height: 80px" :src="getImageUrl(row.itemThumbnailImage)" fit="cover" />
        </template>
      </el-table-column>

      <el-table-column label="상품명">
        <template v-slot="{ row }">
          {{ row.itemName }}
        </template>
      </el-table-column>

      <el-table-column label="판매가">
        <template v-slot="{ row }">
          <div>
            <span v-if="row.orderPrice !== undefined">{{ row.orderPrice }}원</span>
            <span v-else>{{ row.price }}원</span>
            | 수량 {{ row.count }}개<br />
            <span v-if="row.orderPrice !== undefined">{{ row.orderPrice * row.count }} 원</span>
            <span v-else>{{ row.price * row.count }} 원</span>
          </div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import type CartItem from '@/entity/cart/CartItem'
import type OrderItem from '@/entity/order/OrderItem'

const props = defineProps<{
  selectedItems: CartItem[] | OrderItem[]
}>()

const getImageUrl = (storedName: string) => {
  return storedName ? `/api/images/${storedName}` : '@element-plus/theme-chalk/el-icon-picture'
}

const getTotalItemsCount = () => {
  let totalCount = 0
  for (const item of props.selectedItems) {
    totalCount += item.count
  }
  return totalCount
}
</script>
