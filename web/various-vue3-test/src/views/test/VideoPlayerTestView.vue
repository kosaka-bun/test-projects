<template>
  <div>
    <h1>Video Player Test</h1>
    <div class="player" ref="playerDom"></div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import Player from 'nplayer'
import Danmaku from '@nplayer/danmaku'

const playerDom = ref()
let player

onMounted(() => {
  initPlayer()
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
    plugins: [ new Danmaku(danmakuOptions) ]
  })
  player.mount(playerDom.value)
}

const danmakuOptions = {
  items: [
    { time: 1, text: '弹幕～' }
  ],
  autoInsert: false
}
</script>

<style scoped>
.player {
  width: 800px;
  height: 500px;
}
</style>