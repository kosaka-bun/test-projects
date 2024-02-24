import { createRouter, createWebHistory } from 'vue-router'
import MainView from '@/views/MainView.vue'
import ScrollBarTestView from '@/views/test/ScrollBarTestView.vue'
import LenisTestView from '@/views/test/LenisTestView.vue'
import InertialScrollTestView from '@/views/test/InertialScrollTestView.vue'

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
    },
    {
      path: '/test/lenisTest',
      component: LenisTestView
    },
    {
      path: '/test/inertialScrollTest',
      component: InertialScrollTestView
    }
  ]
})

export default router
