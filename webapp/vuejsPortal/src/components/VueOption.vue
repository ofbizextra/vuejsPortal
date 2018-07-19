<template>
  <option id="vue-option" :value="data.value">
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
    props: ['props'],
    created() {
      this.props.attributes.map(attr => {
        this.data[attr.key] = attr.value
      })
      console.log(this.data)
    }
  }
</script>

<style scoped>

</style>
