<template>
  <option id="vue-option" v-bind:value="props.key" :selected="selected">
    {{props.description}}
  </option>
</template>

<script>
  export default {
    name: "VueOption",
    data() {
      return {
        data: {}
      }
    },
    props: ['props','selected'],
  }
</script>

<style scoped>

</style>
