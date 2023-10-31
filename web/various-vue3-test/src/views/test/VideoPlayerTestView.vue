<template>
  <div>
    <h1>Video Player Test</h1>
    <p>
      <span>横屏：BV14N4y1N7H9</span>
      <span style="margin-left: 2em;">竖屏：BV15W4y1n7VK</span>
    </p>
    <p>
      BVID：<el-input v-model="bvid" style="width: 15em;" />
      <el-button style="margin-left: 1em;" @click="playVideo">播放</el-button>
    </p>
    <div class="player" ref="playerDom"></div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import Player, { EVENT } from 'nplayer'
import Danmaku from '@nplayer/danmaku'
import request from '@/utils/request'

const playerDom = ref()

let player

let videoDom

const bvid = ref('BV14N4y1N7H9')

//该页面组件在挂载完成（显示在页面上）时，注册窗口大小变化事件监听器
//resize监听器会在浏览器刷新页面前持续存在，故当RouterView中的该组件即将被切换为其他组件时，必须移除这一监听器
onMounted(() => {
  initPlayer()
  playVideo()
})

function initPlayer() {
  videoDom = document.createElement('video')
  player = new Player({
    video: videoDom,
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
    volumeVertical: true,
    plugins: [
      new Danmaku({
        autoInsert: false,
        unlimited: true,
        area: 1,
        fontsizeScale: 0.85
      })
    ]
  })
  player.on(EVENT.LOADED_METADATA, onVideoMetaDataLoaded)
  player.mount(playerDom.value)
  playerDom.value.style.backgroundColor = 'white'
}

function playVideo() {
  let url = `http://pc.honoka.de:8080/bilibili/video/stream?bvid=${bvid.value}`
  if(videoDom.src === url) return
  videoDom.src = url
  loadDanmakuList()
}

function loadDanmakuList() {
  request({
    url: 'http://pc.honoka.de:8080/bilibili/video/danmaku',
    method: 'get',
    params: {
      bvid: bvid.value
    }
  }).then(res => {
    let list = [ ...res.data ]
    list.forEach(it => {
      it.text = it.content
      //noinspection JSUnresolvedReference
      it.color = it.colorRgb
    })
    player.danmaku.resetItems(list)
  })
}

/**
 * 视频的元数据加载完成时触发，元数据包括：时长、尺寸以及文本轨道
 */
function onVideoMetaDataLoaded() {
  adjustPlayerHeight()
}

/**
 * 若视频高度大于容器最小高度，则将容器高度调整为视频高度，
 * 若视频高度小于容器最小高度，则使播放器高度充满容器最小高度
 */
function adjustPlayerHeight() {
  //设置了height后，若视频高度大于容器的height或min-height，则播放器高度将不会自适应为视频的高度，需事先移除height
  playerDom.value.style.removeProperty('height')
  let style = window.getComputedStyle(playerDom.value)
  let minHeight = Number(style.minHeight.match(/\d+/g)[0])
  let height = Number(style.height.match(/\d+/g)[0])
  if(height <= minHeight) {
    /*
     * 若不明确设置height，会导致在播放器挂载到容器之后，容器的min-height不会对播放器生效
     * （播放器高度不会充满容器的min-height）
     */
    playerDom.value.style.height = '0'
  }
}
</script>

<style scoped lang="scss">
.player {
  width: 800px;
  min-height: 450px;
  background-color: black;
}

@media(max-width: 800px) {
  .player {
    width: 100%;
    min-height: calc((9 / 16.0) * 100vw);
  }
}
</style>