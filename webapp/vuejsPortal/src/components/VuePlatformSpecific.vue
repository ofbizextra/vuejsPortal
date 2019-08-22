<template>
  <div id="vue-platform-specific">
    <div v-bind:is="componentName"></div>
  </div>
</template>

<script>
  export default {
    name: "VuePlatformSpecific",
    props: ['props', 'updateStore'],
    data() {
      return {
      }
    },
    computed: {
      componentName() {
        return this.props.attributes.hasOwnProperty('componentName') ? this.props.attributes.componentName : ''
      }
    }
  }
</script>

<style scoped>

</style>
