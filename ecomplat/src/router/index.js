import { createRouter, createWebHistory } from 'vue-router'
import EcommerceHome from '@/views/EcommerceHome.vue'
import MerchantHome from '@/views/MerchantHome.vue'
import UserHome from '@/views/UserHome.vue'
import UserProfile from '@/views/UserProfile.vue' 
import Cart from '@/views/Cart.vue'

const routes = [
  {
    path: '/',
    name: 'EcommerceHome',
    component: EcommerceHome
  },
  {
    path: '/merchant',
    name: 'MerchantHome',
    component: MerchantHome
  },
  {
    path: '/user',
    name: 'UserHome',
    component: UserHome
  },
  {
    path: '/merchant/add-goods',
    name: 'AddGoods',
    component: () => import('@/views/AddGoods.vue')
  },
  {
    path: '/product/:id(\\d+)',   // 正则强制字符串
    name: 'ProductDetail',
    component: () => import('@/views/ProductDetail.vue')
  },
  {
    path: '/user_profile',
    name: 'UserProfile',
    component: UserProfile,
    meta: { title: '个人中心' }
  },
  {
    path: '/cart', // 购物车路由
    name: 'Cart',
    component: Cart
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router