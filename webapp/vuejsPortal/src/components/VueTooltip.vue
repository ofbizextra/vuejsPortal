<template>
  <span id="vue-tooltip" v-if="data.tooltip" v-bind:class="data.tooltipStyle ? data.tooltipStyle : ''">
    {{data.tooltip}}
  </span>
</template>

<script>
  import { mapGetters } from 'vuex'

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
      }
    }
  }
</script>

<style scoped>

</style>
