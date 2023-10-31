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
import request from '@/utils/request'

const playerDom = ref()

let player

onMounted(() => {
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