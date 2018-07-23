<template>
  <div id="vue-title">
    <label v-bind="data">{{data.title}}</label>
  </div>
</template>

<script>
  export default {
    name: "VueTitle",
    ddata() {
      return {
      }
    },
    computed: {
      data() {
        return this.parseProps()
      }
    },
    props: [
      'props'
    ]
  }
</script>

<style scoped>

</style>
