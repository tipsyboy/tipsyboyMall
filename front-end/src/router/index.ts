import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import authRoutes from './authRoutes'
import itemRoutes from './itemRoutes'
import profileRoutes from './profileRoutes'
import utilRoutes from './utilRoutes'
import ErrorPage from '@/components/ErrorPage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },

    ...authRoutes,
    ...itemRoutes,
    ...profileRoutes,
    ...utilRoutes,
  ],
})

export default router
