<template>
    <v-btn text dark class="no-text-transform">
            <div
                    v-for="(component, index) in props.children"
                    :key="index"
                    v-bind:is="constants.components[component.name]"
                    :props="component"
                    :updateStore="updateStore"
            ></div>
    </v-btn>
</template>

<script>
  import constants from '../js/constants'

  export default {
    name: "VueMenuItem",
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
.no-text-transform {
  text-transform: none;
}
</style>
