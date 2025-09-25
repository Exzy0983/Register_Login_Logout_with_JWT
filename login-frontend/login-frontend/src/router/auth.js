import Login from '@/pages/Login.vue'
import Register from '@/pages/Register.vue'

export default [
  {
    path : '/login',
    name : 'Login',
    component : Login,
    meta : {requiresAuth : false}
  },
  {
    path : '/register',
    name : 'Register',
    component : Register,
    meta : {requiresAuth : false}
  },
]

