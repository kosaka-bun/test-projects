<template>
  <div>
    <h1>Dash Test</h1>
    <div class="player" ref="playerDom"></div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { Player } from 'nplayer'
import { MediaPlayer } from 'dashjs'
import request from '@/utils/request'

const playerDom = ref()

let player

let dashPlayer

let videoDom

onMounted(() => {
  initPlayer()
  playVideo()
  configurePlayer()
})

function initPlayer() {
  videoDom = document.createElement('video')
  player = new Player({
    video: videoDom,
    controls: [
      [
        'play',
        'time',
        'spacer',
        'danmaku-settings',
        'settings',
        'fullscreen'
      ],
      [ 'progress' ]
    ],
    bpControls: {
      500: [
        [ 'play', 'progress', 'time', 'danmaku-settings', 'fullscreen' ],
        [],
        [ 'spacer' ]
      ]
    },
    volumeVertical: true,
    plugins: []
  })
  player.mount(playerDom.value)
  playerDom.value.style.backgroundColor = 'white'
  playerDom.value.querySelector('.nplayer_poster').remove()
}

async function playVideo() {
  //let url = 'https://dash.akamaized.net/digitalprimates/fraunhofer/480p_video/heaac_2_0_with_video/Sintel/sintel_480p_heaac2_0.mpd'
  //dashPlayer = MediaPlayer().create()
  //dashPlayer.initialize(videoDom, url, true)
  let videoId = '?????'
  let episodeId = (await request({
    url: 'http://localhost:8080/video/episode/list',
    method: 'get',
    params: {
      videoId
    }
  })).data[0].id
  //noinspection JSUnresolvedReference
  let manifest = (await request({
    url: 'http://localhost:8080/video/stream/urlList',
    method: 'get',
    params: {
      videoId,
      episodeId
    }
  })).data[0].dashManifest
  let url = URL.createObjectURL(new Blob([ manifest ], { type: 'application/dash+xml' }))
  dashPlayer = MediaPlayer().create()
  dashPlayer.initialize(videoDom, url, true)
}

function configurePlayer() {
  //禁用单击播放暂停
  videoDom.removeEventListener('click', player.toggle)
  //禁用双击全屏
  videoDom.removeEventListener('dblclick', player.fullscreen.toggle)
  //禁用默认触摸事件（移动端）
  //noinspection JSUnresolvedReference
  videoDom.removeEventListener('touchstart', player.touch.onTouchStart)
}
</script>

<style scoped lang="scss">
.player {
  width: 800px;
  min-height: 450px;
}

@media(max-width: 800px) {
  .player {
    width: 100%;
    min-height: unset;
    height: calc((9 / 16.0) * 100vw);
  }
}
</style>