<template>
  <div class="about">
    <div style="display: block">
      <h1>This is an about page</h1>
      <p>
        counter: {{ counter.count }}<br />
        double counter: {{ counter.doubleCount }}
        <el-button @click="incr()">incr</el-button>
      </p>
      <p>
        Test Android JavaScript Interface:
        <el-button @click="jsInterfaceTest">test</el-button>
      </p>
    </div>
  </div>
</template>

<script setup>
import { useCounterStore } from '@/stores/counter'

const counter = useCounterStore()

function incr() {
  counter.increment()
}

function jsInterfaceTest() {
  android.test()
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
