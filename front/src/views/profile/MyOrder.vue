<template>
  <CenterLayout>
    <ProfileMenu :nickname="props.nickname" />

    <el-table :data="state.orderList.contents" @row-click="toOrderDetail">
      <el-table-column prop="id" label="주문번호" width="80"></el-table-column>

      <el-table-column label="주문 내역">
        <template v-slot="{ row }">
          {{ getFirstItemDescription(row as Order) }}
        </template>
      </el-table-column>
      <el-table-column label="주문 날짜">
        <template v-slot="{ row }">
          {{ formatDateTime(row as Order) }}
        </template>
      </el-table-column>
      <el-table-column>
        <template v-slot="{ row }">
          <el-button type="danger" @click.stop="cancelOrder(row.id)">취소</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="paging-container">
      <el-pagination
        class="paging-bar"
        :hide-on-single-page="true"
        background
        layout="prev, pager, next"
        v-model:current-page="state.orderList.page"
        :total="state.orderList.totalCount"
        :default-page-size="5"
        @current-change="(page: number) => getMyOrderList(page)"
      />
    </div>
  </CenterLayout>
</template>

<script setup lang="ts">
import { markRaw, onMounted, reactive } from 'vue'
import ProfileMenu from './ProfileMenu.vue'
import { container } from 'tsyringe'
import OrderRepository from '@/repository/OrderRepository'
import Paging from '@/entity/data/Paging'
import Order from '@/entity/order/Order'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete } from '@element-plus/icons-vue'

const ORDER_REPOSITORY = container.resolve(OrderRepository)
const router = useRouter()

const props = defineProps<{
  nickname: string
}>()

type StateType = {
  orderList: Paging<Order>
}

const state = reactive<StateType>({
  orderList: new Paging<Order>(),
})

onMounted(() => {
  getMyOrderList()
})

const getMyOrderList = (page = 1) => {
  ORDER_REPOSITORY.getMyOrderList(page)
    .then((orderList) => {
      state.orderList = orderList
    })
    .catch((e) => {
      console.error(e)
    })
}

const cancelOrder = (orderId: number) => {
  ElMessageBox.confirm('주문을 취소하시겠습니까?', '취소', {
    type: 'warning',
    icon: markRaw(Delete),
  })
    .then(() => {
      ORDER_REPOSITORY.cancelORder(orderId)
        .then(() => {
          ElMessage.success('성공적으로 취소되었습니다.')
          router.go(0)
        })
        .catch((e) => {
          console.error(e)
          ElMessage.error('취소할 수 없습니다.')
        })
    })
    .catch(() => {
      console.log('취소')
    })
}

const toOrderDetail = (order: Order) => {
  router.push({ name: 'orderDetail', params: { orderId: order.id } })
}

const formatDateTime = (order: Order) => {
  return order.getFormattedDateTime()
}

const getFirstItemDescription = (order: Order) => {
  if (order.orderItemList.length > 1) {
    return `${order.orderItemList[0].itemName} 외 ${order.orderItemList.length - 1}종`
  }
  return `${order.orderItemList[0].itemName}`
}
</script>

<style scoped></style>
