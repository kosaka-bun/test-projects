<template>
  <div>
    <h1>Scroll Bar Test</h1>
    <div class="container">
      <div v-for="i in 100">abc {{ i }}<br /><br /></div>
    </div>
  </div>
</template>

<script setup>

</script>

<style scoped lang="scss">
.container {
  width: 800px;
  height: 450px;
  background-color: lightblue;
  overflow-y: scroll;
}

.container::-webkit-scrollbar {
  width: 10px;
}

.container::-webkit-scrollbar-track {
  background-color: transparent;
}

.container::-webkit-scrollbar-thumb {
  background-color: gray;
}
</style>