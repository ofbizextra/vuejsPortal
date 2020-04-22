<template>
  <v-col id="vue-column">
    <div v-for="(component, id) in children" :key="id" v-bind:is="constants.components[component.name]" :props="component">
    </div>
  </v-col>
</template>

<script>
  import constants from '../js/constants'

  export default {
    name: "VueColumn",
    props: ['props'],
    data() {
      return {
        constants: constants
      }
    },
    computed: {
      children() {
        return this.props.hasOwnProperty('children') ? this.props.children : []
      }
    }
  }
</script>

<style scoped>

</style>
