<template>
  <div>
    <h1>Various Vue 3 Test</h1>
    <div class="links">
      <RouterLink to="/test/test1">Test 1</RouterLink>
    </div>
  </div>
</template>

<script setup>

</script>

<style scoped>
.links {
  display: flex;
  width: 35%;
  flex-wrap: wrap;
}

::v-deep(.links a) {
  margin-right: 1em;
}
</style>