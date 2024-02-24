<template>
  <div class="lenis-test-view">
    <h1>Lenis Test</h1>
    <el-button @click="onBtn1">Btn1</el-button>
    <div class="wrapper" ref="wrapperDom" @touchstart="onTouchStart" @touchmove="onTouchMove">
      <div ref="contentDom">
        <ul>
          <li v-for="i in 100">Item {{ i }}</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref } from 'vue'
import Lenis from '@studio-freight/lenis'
import viewUtils from '@/utils/view'

const wrapperDom = ref()

const contentDom = ref()

//noinspection JSCheckFunctionSignatures
let lenis

let movingY

onMounted(() => {
  viewUtils.disableMargins()
  lenis = new Lenis({
    wrapper: wrapperDom.value,
    content: contentDom.value,
    eventsTarget: wrapperDom.value
  })
  window.lenis = lenis
  lenis.on('scroll', e => {
    console.log('animated:', e.animatedScroll)
  })
  requestAnimationFrame(raf)
})

onUnmounted(() => {
  lenis.destroy()
})

function raf(time) {
  lenis.raf(time)
  requestAnimationFrame(raf)
}

function onBtn1() {
  console.log(wrapperDom.value.scrollTop)
  lenis.scrollTo(600)
}

function onTouchStart(e) {
  movingY = e.changedTouches[0].clientY
}

function onTouchMove(e) {
  let yDistance = e.changedTouches[0].clientY - movingY
  movingY = e.changedTouches[0].clientY
  let absDistance = Math.abs(yDistance)
  let pos = wrapperDom.value.scrollTop + absDistance
  console.log(pos)
  lenis.scrollTo(pos + 50)
}
</script>

<style scoped lang="scss">
h1 {
  margin: 0;
}

.wrapper {
  height: 80vh;
  //overflow-y: scroll;
  overflow-y: hidden;
}
</style>