<template>
  <div id="app">
    <h1>{{ msg }}</h1>
    <div v-for="module in all" v-bind:is="module.type">

    </div>
  </div>
</template>

<script>
export default {
  name: 'app',
  data () {
    return {
      msg: 'Welcome to Your Vue.js App',
      all: {
        moduleA: { type: 'search' },
        moduleB: { type: 'list' },
        moduleC: { type: 'search' }
      }
    }
  }
}
</script>

<style>

</style>
