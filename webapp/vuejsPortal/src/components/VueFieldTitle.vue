<template>
  <div id="vue-field-title">
    <label v-bind="data">{{data.title ? data.title : data.description}}</label>
  </div>
</template>

<script>
  export default {
    name: "VueFieldTitle",
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
