<template>
  <span id="vue-tooltip" v-if="data.tooltip" v-bind:class="tooltipStyle">
    {{data.tooltip}}
  </span>
</template>

<script>

  export default {
    name: "VueTooltip",
    props: ['props', 'updateStore'],
    data() {
      return {
      }
    },
    computed: {
      data() {
        let data = this.props.attributes
        //delete data['value']
        if (data.className || (data.alert && data.alert === true)) {
          data.class = data.className ? data.className : '' + ' ' + data.alert === true ? 'alert' : ''
        }
        return data
      },
      tooltipStyle() {
        return this.data.hasOwnProperty('tooltipStyle') ? this.data.tooltipStyle : ''
      }
    }
  }
</script>

<style scoped>

</style>
