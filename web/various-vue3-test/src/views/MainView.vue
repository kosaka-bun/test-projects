<template>
  <div>
    <h1>Various Vue 3 Test</h1>
    <div class="links">
      <RouterLink to="/test/scrollBarTest">Scroll Bar Test</RouterLink>
      <RouterLink to="/test/lenisTest">Lenis Test</RouterLink>
      <RouterLink to="/test/inertialScrollTest">Inertial Scroll Test</RouterLink>
    </div>
  </div>
</template>

<script setup>

</script>

<style scoped lang="scss">
.links {
  display: flex;
  width: 35%;
  flex-wrap: wrap;
}

::v-deep(.links a) {
  margin-right: 1em;
}
</style>