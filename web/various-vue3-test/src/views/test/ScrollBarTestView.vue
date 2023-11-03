<template>
  <div>
    <h1>Scroll Bar Test</h1>
    <div class="container">
      <el-scrollbar :always="true">
        <div v-for="item in 20" :key="item" class="scrollbar-demo-item">{{ item }}</div>
      </el-scrollbar>
    </div>
    <p v-for="item in 20" :key="item" class="scrollbar-demo-item">{{ item }}</p>
  </div>
</template>

<script setup>

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