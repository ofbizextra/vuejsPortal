<template>
  <div>
    <div
        v-for="(component, index) in props.children"
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
      data() {
        let data = this.props.attributes
        delete data['value']
        return data
      },
      style() {
        return this.data.hasOwnProperty('style') ? this.data.style : ''
      },
      toolTip() {
        return this.data.hasOwnProperty('toolTip') ? this.data.toolTip : ''
      },
      linkStr() {
        return this.data.hasOwnProperty('linkStr') ? this.data.linkStr : ''
      },
      containsNestedMenus() {
        return this.data.hasOwnProperty('containsNestedMenus') ? this.data.containsNestedMenus : ''
      }
    },
    methods: {}
  }
</script>

<style scoped>

</style>
