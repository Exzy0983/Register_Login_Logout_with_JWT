import { createRouter, createWebHistory } from "vue-router"
import authRoutes from './auth.js'
import Home from '@/pages/Home.vue'

const routes = [
  {
    path : '/',
    name : 'Home',
    component : Home,
    meta : { requiresAuth : false }
  },
]

const router = createRouter({
  history : createWebHistory(),
  routes
})

export default router