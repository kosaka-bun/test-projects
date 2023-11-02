import { createRouter, createWebHistory } from 'vue-router'
import MainView from '@/views/MainView.vue'
import ScrollBarTestView from '@/views/test/ScrollBarTestView.vue'
import VideoPlayerTestView from '@/views/test/VideoPlayerTestView.vue'

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
      path: '/test/videoTest',
      component: VideoPlayerTestView
    }
  ]
})

export default router
