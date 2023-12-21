import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import SignupView from '@/views/auth/SignupView.vue'
import LoginView from '@/views/auth/LoginView.vue'
import ProfileView from '@/views/auth/ProfileView.vue'
import OrderView from '@/views/orders/OrderView.vue'


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
    {
      path: "/profile/:nickname",
      name: "profile",
      component: ProfileView
    },
    //
    {
      path: '/order',
      name: 'order',
      component: OrderView
    },
  ]
})

export default router
