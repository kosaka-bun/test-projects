<template>
  <div>
    <h1>Video Player Test</h1>
    <div class="player" ref="playerDom"></div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { Player } from 'nplayer'

const playerDom = ref()
let player

onMounted(() => {
  initPlayer()
})

function initPlayer() {
  player = new Player({
    src: 'http://pc.honoka.de:8080/bilibili/video/stream?bvid=BV14N4y1N7H9'
  })
  player.mount(playerDom.value)
}
</script>

<style scoped>
.player {
  width: 800px;
  height: 500px;
}
</style>