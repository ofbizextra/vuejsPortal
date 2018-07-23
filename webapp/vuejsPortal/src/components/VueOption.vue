<template>
  <option id="vue-option" v-bind="data" :selected="selected">
    {{data.description}}
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
    created() {
      this.props.attributes.map(attr => {
        this.data[attr.key] = attr.value
      })
    }
  }
</script>

<style scoped>

</style>
