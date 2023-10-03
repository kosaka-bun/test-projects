<template>
  <div>
    <h2>This is an about page</h2>
    <p>
      counter: {{ counter.count }}<br />
      double counter: {{ counter.doubleCount }}
      <el-button @click="incr()">incr</el-button>
    </p>
    <p>
      Test Android JavaScript Interface:
      <el-button @click="jsInterfaceTest()">test</el-button>
    </p>
    <p>
      <router-link to="/httpApiTest">HTTP API Test</router-link>
    </p>
  </div>
</template>

<script setup>
import { useCounterStore } from '@/stores/counter'
import { android } from '@/utils/android'

const counter = useCounterStore()

function incr() {
  counter.increment()
}

function jsInterfaceTest() {
  console.log(android)
  android.test.test1()
}
</script>

<style scoped>

</style>