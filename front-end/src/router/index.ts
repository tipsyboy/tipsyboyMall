import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import SignupView from '@/views/auth/SignupView.vue'
import LoginView from '@/views/auth/LoginView.vue'

import OrderView from '@/views/orders/OrderView.vue'
import OrderDetail from '@/views/orders/OrderDetail.vue'

import ProfileView from '@/views/personal/ProfileView.vue'
import MyItem from '@/views/personal/MyItem.vue'
import MyOrder from '@/views/personal/MyOrder.vue'
import CartView from '@/views/personal/CartView.vue'

import ItemCreate from '@/views/item/ItemCreate.vue'
import ItemList from '@/views/item/ItemList.vue'
import ItemDetail from '@/views/item/ItemDetail.vue'
import ItemEdit from '@/views/item/ItemEdit.vue'

import TempView from '@/components/TempView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    // auth
    {
      path: '/signup',
      name: 'signup',
      component: SignupView
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    
    // items
    {
      path: '/item/save',
      name: 'itemCreate',
      component: ItemCreate
    },
    // {
    //   path: '/item/list',
    //   name: 'itemList',
    //   component: ItemList
    // },
    {
      path: '/item/:itemId',
      name: 'itemDetail',
      component: ItemDetail,
      props: true
    },
    // {
    //   path: '/item/edit/:itemId',
    //   name: 'itemEdit',
    //   component: ItemEdit
    // },

    // // personal
    // {
    //   path: "/profile/:nickname",
    //   name: "profile",
    //   component: ProfileView
    // },
    // {
    //   path: "/profile/:nickname/my-item",
    //   name: "my-item",
    //   component: MyItem
    // },
    // {
    //   path: "/profile/:nickname/my-order",
    //   name: "my-order",
    //   component: MyOrder
    // },
    // {
    //   path: '/cart',
    //   name: 'cart',
    //   component: CartView
    // },

    // // order
    // {
    //   path: '/order',
    //   name: 'order',
    //   component: OrderView
    // },
    // {
    //   path: '/order/:orderId',
    //   name: 'orderDetail',
    //   component: OrderDetail
    // },

    // // temp
    // {
    //   path: '/temp',
    //   name: 'temp',
    //   component: TempView
    // },
  ]
})

export default router
