import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import SignupView from '@/views/auth/SignupView.vue'
import LoginView from '@/views/auth/LoginView.vue'

import OrderView from '@/views/orders/OrderView.vue'

import ProfileView from '@/views/personal/ProfileView.vue'
import MyItem from '@/views/personal/MyItem.vue'
import MyOrder from '@/views/personal/MyOrder.vue'

import ItemCreate from '@/views/item/ItemCreate.vue'
import ItemList from '@/views/item/ItemList.vue'
import ItemDetail from '@/views/item/ItemDetail.vue'



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
    // personal
    {
      path: "/profile/:nickname",
      name: "profile",
      component: ProfileView
    },
    {
      path: "/profile/:nickname/my-item",
      name: "my-item",
      component: MyItem
    },
    {
      path: "/profile/:nickname/my-order",
      name: "my-order",
      component: MyOrder
    },
    // items
    {
      path: '/item/save',
      name: 'itemCreate',
      component: ItemCreate
    },
    {
      path: '/item/list',
      name: 'itemList',
      component: ItemList
    },
    {
      path: '/item/:itemId',
      name: 'itemDetail',
      component: ItemDetail
    },
    // order
    {
      path: '/order',
      name: 'order',
      component: OrderView
    },
  ]
})

export default router
