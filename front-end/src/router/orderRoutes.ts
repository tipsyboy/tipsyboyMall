import OrderDetail from '@/views/orders/OrderDetail.vue'
import OrderPreview from '@/views/orders/OrderPreview.vue'

export default [
  {
    path: '/order',
    name: 'orderPreview',
    component: OrderPreview,
  },
  {
    path: '/order/:orderId',
    name: 'orderDetail',
    component: OrderDetail,
    props: true,
  },
]
