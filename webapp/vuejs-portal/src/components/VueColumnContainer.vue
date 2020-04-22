<template>
  <v-row id="vue-column-container">
    <div v-for="(component, id) in children" :key="id" v-bind:is="constants.components[component.name]" :props="component">
    </div>
  </v-row>
</template>

<script>
  export default {
    name: "VueColumnContainer",
    props: ['props'],
    computed: {
      children() {
        return this.props.hasOwnProperty('children') ? this.props.children : []
      }
    }
  }
</script>

<style scoped>

</style>
