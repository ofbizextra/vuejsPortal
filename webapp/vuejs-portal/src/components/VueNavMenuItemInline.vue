<template>
  <div>
    <div
        v-for="(component, index) in children"
        :key="index"
        v-bind:is="constants.components[component.name]"
        :props="component"
        :updateStore="updateStore"
        :inline="true"
    ></div>
  </div>
</template>

<script>
  import constants from '../js/constants'

  export default {
    name: "VueNavMenuItemInline",
    props: ['props', 'updateStore'],
    data() {
      return {
        constants: constants
      }
    },
    computed: {
      children() {
        return this.props.hasOwnProperty('children') ? this.props.children : []
      },
      containsNestedMenus() {
        return this.props.attributes.hasOwnProperty('containsNestedMenus') ? this.props.attributes.containsNestedMenus : ''
      },
      linkStr() {
        return this.props.attributes.hasOwnProperty('linkStr') ? this.props.attributes.linkStr : ''
      },
      style() {
        return this.props.attributes.hasOwnProperty('style') ? this.props.attributes.style : ''
      },
      toolTip() {
        return this.props.attributes.hasOwnProperty('toolTip') ? this.props.attributes.toolTip : ''
      }
    },
  }
</script>

<style scoped>

</style>
