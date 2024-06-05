import MyItem from '@/views/profile/MyItem.vue'
import MyOrder from '@/views/profile/MyOrder.vue'
import ProfileView from '@/views/profile/ProfileView.vue'

export default [
  {
    path: '/profile/:nickname',
    name: 'profile',
    component: ProfileView,
    props: true,
  },
  {
    path: '/profile/:nickname/items',
    name: 'myItems',
    component: MyItem,
    props: true,
  },
  {
    path: '/profile/:nickname/orders',
    name: 'myOrders',
    component: MyOrder,
    props: true,
  },
]
