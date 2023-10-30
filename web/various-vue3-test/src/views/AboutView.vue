<template>
  <div class="about">
    <div style="display: block;">
      <h1>This is an about page</h1>
      <div>
        <el-button @click="test">Test</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import messageUtils from '@/utils/message'

function test() {
  messageUtils.success('Test')
}
</script>

<style>
@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>
