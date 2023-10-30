import { createRouter, createWebHistory } from 'vue-router'
import MainView from '@/views/MainView.vue'
import Test1View from '@/views/test/Test1View.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: MainView
    },
    {
      path: '/test/test1',
      component: Test1View
    }
  ]
})

export default router
