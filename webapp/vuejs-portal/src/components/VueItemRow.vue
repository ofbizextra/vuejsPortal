<template>
  <tr id="vue-item-row">
    <td
      v-for="(component, key) in children"
      :key="key"
      v-bind:is="constants.components[component.name]"
      :props="component"
      :updateStore="updateStore">
    </td>
  </tr>
</template>

<script>
  import constants from '../js/constants'

  export default {
    name: "VueItemRow",
    props: ['props', 'updateStore'],
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
