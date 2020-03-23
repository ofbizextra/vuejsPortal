<template>
  <span id="vue-tooltip" v-if="tooltip" v-bind:class="tooltipStyle">
    {{tooltip}}
  </span>
</template>

<script>
  export default {
    name: "VueTooltip",
    props: ['props', 'updateStore'],
    computed: {
      tooltip() {
        return this.props.attributes.hasOwnProperty('tooltip') ? this.props.attributes.tooltip : ''
      },
      tooltipStyle() {
        return this.props.attributes.hasOwnProperty('tooltipStyle') ? this.props.attributes.tooltipStyle : ''
      }
    }
  }
</script>

<style scoped>

</style>
