<template>
  <div id="vue-text">
    <input v-bind="data"/>
  </div>
</template>

<script>
  export default {
    name: "VueText",
    props: ['props'],
    data() {
      return {
        data: {}
      }
    }
  }
</script>

<style scoped>

</style>
