<template>
  <div id="vue-title">
    <label v-bind="data">{{data.title}}</label>
  </div>
</template>

<script>
  export default {
    name: "VueTitle",
    data() {
      return {
      }
    },
    computed: {
      data() {
        return this.props.attributes
      }
    },
    props: [
      'props'
    ]
  }
</script>

<style scoped>

</style>
