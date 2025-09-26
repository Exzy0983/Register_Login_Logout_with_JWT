import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'

import '@/styles/variables.css'
import '@/styles/global.css'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
const { useAuthStore } = await import ('@/stores/auth')
const authStore = useAuthStore()
authStore.initializeAuth()

console.log('main.js 인증 초기화 완료:', {
  isAuthenticated: authStore.isAuthenticated,
  isAdmin: authStore.isAdmin
})

app.use(router)
app.mount('#app')