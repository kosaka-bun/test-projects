<template>
  <div>
    <h1>Scroll Bar Test</h1>
    <scroll-block class="container">
      <div v-for="item in 20" :key="item" class="scrollbar-demo-item">{{ item }}</div>
    </scroll-block>
    <p v-for="item in 20" :key="item" class="scrollbar-demo-item">{{ item }}</p>
  </div>
</template>

<script setup>
import ScrollBlock from '@/components/ScrollBlock.vue'
</script>

<style scoped lang="scss">
.container {
  width: 800px;
  height: 450px;
  background-color: lightgreen;
}

.scrollbar-demo-item {
  height: 50px;
  text-align: center;
  border-radius: 4px;
  background: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
}
</style>