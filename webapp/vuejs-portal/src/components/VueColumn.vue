<template>
  <v-col id="vue-column">
    <div v-for="(component, id) in children" :key="id" v-bind:is="constants.components[component.name]" :props="component">
    </div>
  </v-col>
</template>

<script>
  export default {
    name: "VueColumn",
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
