<template>
  <v-list-item :id="props.children[0].attributes.text.split(' ').join('_')" link @click="propagate">
    <v-list-item-content>
    <div
        :ref="props.children[0].attributes.text.split(' ').join('_') + '_link'"
        v-for="(component, index) in props.children"
        :key="index"
        v-bind:is="constants.components[component.name]"
        :props="component"
        :updateStore="updateStore"
        :inline="false"
        :clickDisabled="true"
    ></div>
    </v-list-item-content>
  </v-list-item>
</template>

<script>
  import constants from '../js/constants'

  export default {
    name: "VueNavMenuItem",
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
    methods: {
      propagate() {
        console.log(this.$refs[this.props.children[0].attributes.text.split(' ').join('_') + '_link'])
        this.$refs[this.props.children[0].attributes.text.split(' ').join('_') + '_link'][0].redirect()
      }
    }
  }
</script>

<style scoped>

</style>
