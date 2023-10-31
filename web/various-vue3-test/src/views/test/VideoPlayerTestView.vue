<template>
  <div>
    <h1>Video Player Test</h1>
    <div class="player" ref="playerDom"></div>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref, watch } from 'vue'
import Player from 'nplayer'
import Danmaku from '@nplayer/danmaku'
import request from '@/utils/request'

const playerDom = ref()

/**
 * 记录了播放器宽度的响应式变量，用于给watch函数进行监听
 */
const playerWidth = ref(0)

let player

//该页面组件在挂载完成（显示在页面上）时，注册窗口大小变化事件监听器
//resize监听器会在浏览器刷新页面前持续存在，故当RouterView中的该组件即将被切换为其他组件时，必须移除这一监听器
onMounted(() => {
  onWindowResize()
  window.addEventListener('resize', onWindowResize)
  initPlayer()
  loadDanmakuList()
})

function initPlayer() {
  //noinspection JSCheckFunctionSignatures
  player = new Player({
    src: 'http://pc.honoka.de:8080/bilibili/video/stream?bvid=BV14N4y1N7H9',
    controls: [
      [
        'play',
        'volume',
        'time',
        'spacer',
        'danmaku-settings',
        'settings',
        'web-fullscreen',
        'fullscreen'
      ],
      [ 'progress' ]
    ],
    plugins: [
      new Danmaku({
        autoInsert: false,
        unlimited: true,
        area: 1
      })
    ]
  })
  player.mount(playerDom.value)
}

function loadDanmakuList() {
  request({
    url: 'http://pc.honoka.de:8080/bilibili/video/danmaku',
    method: 'get',
    params: {
      bvid: 'BV14N4y1N7H9'
    }
  }).then(res => {
    let list = [ ...res.data ]
    list.forEach(it => {
      it.text = it.content
      //noinspection JSUnresolvedReference
      it.color = it.colorRgb
    })
    player.danmaku.appendItems(list)
  })
}

/**
 * 窗口大小变化时回调，需手动注册
 */
//该函数可能会被频繁触发，函数内需尽可能避免大量操作，故不可在此处直接进行播放器高度的调整
function onWindowResize() {
  //仅修改响应式变量的值，用于触发watch函数定义的回调
  playerWidth.value = Number(window.getComputedStyle(playerDom.value).width.match(/\d+/g)[0])
}

//监听playerWidth这一响应式变量的变化，在其变化时触发回调
/*
 * 有时，playerWidth会被频繁赋值，若在极短的时间内，该变量被多次赋予了一个相同的值，则该方法只会被执行一次
 * 从而防止了在窗口大小未发生变化时，修改播放器高度的函数被多次触发的问题
 */
watch(playerWidth, () => {
  setPlayerHeight()
})

/**
 * 根据playerWidth变量（播放器当前的宽度）的值，计算播放器高度
 */
function setPlayerHeight() {
  let height = playerWidth.value * 9 / 16.0
  playerDom.value.style.height = `${height}px`
  //需保证在播放器容器高度计算完成后，再显示播放器容器，从而避免抖动
  playerDom.value.style.visibility = 'visible'
}

/*
 * 该页面组件被取消挂载时（比如切换了路由，RouterView即将加载另一个页面组件时），需移除窗口大小变化事件监听器
 * 否则，即使当前页面组件已不存在于页面当中，resize监听器仍然会触发，并且会由于该页面组件已不存在，
 * 而无法执行成功（控制台输出错误信息）
 */
onUnmounted(() => {
  window.removeEventListener('resize', onWindowResize)
})
</script>

<style scoped>
.player {
  visibility: hidden;
  width: 800px;
  min-height: 450px;
}

@media(max-width: 800px) {
  .player {
    width: 100%;
    min-height: calc((100 / 16.0) * 9vw);
  }
}
</style>