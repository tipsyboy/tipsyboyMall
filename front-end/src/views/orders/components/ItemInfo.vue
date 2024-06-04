<template>
  <div>
    <h2>주문 상품 {{ orderData.length }}종 {{ getTotalItemsCount() }}개</h2>
    <el-table :data="orderData" style="width: 100%">
      <el-table-column align="center">
        <template v-slot="{ row }">
          <el-image style="width: 80px; height: 80px" :src="getImageUrl(row.itemThumnailImage)" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column label="상품 정보" align="center">
        <template v-slot="{ row }">
          {{ row.itemName }}
        </template>
      </el-table-column>
      <el-table-column label="판매가" align="center">
        <template v-slot="{ row }">
          <div>
            {{ row.price }}원 | 수량 {{ row.count }}개<br />
            {{ row.price * row.count }} 원
          </div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
const props = defineProps<{
  orderData: any
}>()

const getImageUrl = (storedName: string) => {
  return storedName ? `http://localhost:8080/images/${storedName}` : '@element-plus/theme-chalk/el-icon-picture'
}

const getTotalItemsCount = () => {
  let totalCount = 0
  for (const item of props.orderData) {
    totalCount += item.count // 예시: 각 항목의 count 속성을 누적
  }
  return totalCount
}
</script>
