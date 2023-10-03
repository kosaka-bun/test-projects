<template>
  <div>
    <h2>HTTP API Test</h2>
    <p>URL: https://api.bilibili.com/x/web-interface/wbi/index/top/feed/rcmd</p>
    <div class="content">
      <pre>{{ content }}</pre>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { android } from '@/utils/android'

const content = ref('{"a": "123"}')

onMounted(() => {
  content.value = android.test.loadApi()
})
</script>

<style scoped>
.content {
  border: 1px solid black;
  padding: 0.5em;
  margin-top: 0.5em;
  margin-bottom: 1em;
}

pre {
  font-size: 1em;
  font-family: Consolas, serif;
  white-space: pre-wrap;
  word-wrap: break-word;
}
</style>