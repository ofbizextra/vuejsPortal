<template>
  <div id="vue-date-time">
    <input v-bind="data"/>
  </div>
</template>

<script>
  export default {
    name: "VueDateTime",
    data() {
      return {
        data: {}
      }
    },
    props: ['props']
  }
</script>

<style scoped>

</style>
