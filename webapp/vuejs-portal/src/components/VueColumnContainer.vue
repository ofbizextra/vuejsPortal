<template>
  <v-container fluid>
    <v-row id="vue-column-container">
      <div v-for="(component, id) in children" :key="id" v-bind:is="constants.components[component.name]" :props="component">
      </div>
    </v-row>
  </v-container>
</template>

<script>
  import constants from '../js/constants'
  export default {
    name: "VueColumnContainer",
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
