<template>
  <v-col id="vue-column" class="ma-0 pa-0">
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
      },
      style() {
        return this.props.attributes.hasOwnProperty('style') ? this.props.attributes.style : ''
      }
    }
  }
</script>

<style scoped>

</style>
