<template>
  <div>
    <h1>Inertial Scroll Test</h1>
    <div class="wrapper" ref="wrapperDom" @touchstart="onTouchStart"
         @touchmove="onTouchMove" @touchend="onTouchEnd">
      <ul>
        <li v-for="i in 2000">Item {{ i }}</li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import codeUtils from '@/utils/code'

const wrapperDom = ref()

let movingY

let speedCalcTask

let lastMovingYOfSpeedCalc

let speedArray = []

function onTouchStart(e) {
  let startY = e.changedTouches[0].clientY
  movingY = startY
  lastMovingYOfSpeedCalc = startY
  speedArray = []
  speedCalcTask = setInterval(() => {
    let speed = movingY - lastMovingYOfSpeedCalc
    lastMovingYOfSpeedCalc = movingY
    console.log(speed, 'px/10ms')
    speedArray.push(speed)
  }, 10)
}

function onTouchMove(e) {
  let yDistance = e.changedTouches[0].clientY - movingY
  movingY = e.changedTouches[0].clientY
  wrapperDom.value.scrollBy(0, -yDistance)
}

function onTouchEnd(e) {
  clearInterval(speedCalcTask)
  let lastSpeeds = speedArray.length <= 10 ? speedArray : speedArray.slice(speedArray.length - 10)
  let speedSum = 0
  lastSpeeds.forEach(it => {
    speedSum += it
  })
  //inertialScroll(speedSum / lastSpeeds.length)
  inertialScrollTest()
  //inertialScrollTest2()
  //inertialScrollTest3()
}

async function inertialScroll(speed) {
  //let direct = speed >= 0 ? 1 : -1
  //let unitSpeed = Math.abs(speed)
  //while(unitSpeed >= 0) {
  //  console.log(unitSpeed)
  //  wrapperDom.value.scrollBy(0, unitSpeed * -direct)
  //  unitSpeed -= 10
  //  await codeUtils.sleep(10)
  //}
}

async function inertialScrollTest() {
  console.log(wrapperDom.value.scrollTop)
  let thread = () => {
    let count = 0
    let speed = 100
    let wait = 4
    let scroll = () => {
      console.log(Date.now(), speed)
      //最小单位为0.8，更小的单位不会触发滚动
      wrapperDom.value.scrollBy(0, speed < 0.8 ? 0.8 : speed)
      count += 1
      if(speed > 50) {
        speed -= 0.5
      } else if(speed > 20) {
        speed -= 0.35
      } else if(speed > 10) {
        speed -= 0.2
      } else if(speed > 3) {
        speed -= 0.05
      } else {
        speed -= 0.02
      }
    }
    setTimeout(async () => {
      while(speed > 0) {
        scroll()
        /*
         * 在async函数中使用sleep进行等待时，除了前3次等待的等待时间可以在4ms以下外，其他任何一次等待的等待时间都至少为4ms。
         * 即便传入的参数是4ms以下，也只有前3次调用sleep时，等待时间会与传入的参数保持一致，之后每次调用sleep都会至少等待4ms才会返回。
         *
         * 这与浏览器对嵌套的setTimeout函数的策略有关：https://zhuanlan.zhihu.com/p/385003833
         */
        await codeUtils.sleep(wait)
        if(speed < 0.8 && count % 4 === 0) wait += 1
      }
    })
  }
  for(let i = 0; i < 1; i++) {
    /*
     * 不管启动多少个递归任务，每个递归任务都只能最快每4ms执行一次。
     * 多次启动递归任务仅仅只能使浏览器在每4ms一次的时间点处，同时执行多次同一任务。
     * 比如，仅启动一个递归任务时，浏览器会分别在0、4、8、12ms等时间点处执行一次任务的内容。
     * 而启动两个递归任务时，浏览器会分别在0、4、8、12ms等时间点处同时执行两次任务的内容，不管有多少个递归任务，最小间隔时间都总是4ms。
     */
    thread()
  }
  setTimeout(() => {
    console.log(wrapperDom.value.scrollTop)
  }, 3000)
}

async function inertialScrollTest2() {
  console.log(wrapperDom.value.scrollTop)
  let thread = () => {
    let scroll = () => setTimeout(() => {
      for(let i = 0; i < 100; i++) {
        wrapperDom.value.scrollBy(0, 1)
      }
    })
    scroll()
  }
  for(let i = 0; i < 10; i++) {
    thread()
  }
  setTimeout(() => {
    console.log(wrapperDom.value.scrollTop)
  }, 2000)
}

async function inertialScrollTest3() {
  //for(let i = 0; i < 1000; i++) {
  //  console.log(Date.now())
  //  await codeUtils.sleep(0)
  //}
  let count = 0
  let a = () => setTimeout(() => {
    console.log(Date.now())
    count += 1
    if(count < 1000) a()
  })
  a()
}
</script>

<style scoped lang="scss">
.wrapper {
  height: 80vh;
  border: 1px solid black;
  //overflow-y: scroll;
  overflow-y: hidden;
}
</style>