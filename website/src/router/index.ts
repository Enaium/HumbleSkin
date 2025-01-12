import HomeLayout from '@/views/HomeLayout'
import About from '@/views/home/About'
import Account from '@/views/home/Account'
import Home from '@/views/home/Home'
import Login from '@/views/home/Login'
import Register from '@/views/home/Register'
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: HomeLayout,
      redirect: { name: 'home' },
      children: [
        {
          path: '',
          name: 'home',
          component: Home,
        },
        {
          path: 'about',
          name: 'about',
          component: About,
        },
        {
          path: 'register',
          name: 'register',
          component: Register,
        },
        {
          path: 'login',
          name: 'login',
          component: Login,
        },
        {
          path: 'account',
          name: 'account',
          component: Account,
        },
      ],
    },
  ],
})

export default router
