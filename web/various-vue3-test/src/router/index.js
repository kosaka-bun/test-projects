import { createRouter, createWebHistory } from 'vue-router'
import MainView from '@/views/MainView.vue'
import ScrollBarTestView from '@/views/test/ScrollBarTestView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: MainView
    },
    {
      path: '/test/scrollBarTest',
      component: ScrollBarTestView
    }
  ]
})

export default router
