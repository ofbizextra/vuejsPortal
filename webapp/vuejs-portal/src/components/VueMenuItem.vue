<template>
  <li id="vue-menu-item" v-bind:class="style" v-bind:title="toolTip">
    <ul v-if="containsNestedMenus">
      <div
        v-for="(component, index) in props.children"
        :key="index"
        v-bind:is="constantes.components[component.name]"
        :props="component"
        :updateStore="updateStore"
      />
    </ul>
    <div
      v-else
      v-for="(component, index) in props.children"
      :key="index"
      v-bind:is="constantes.components[component.name]"
      :props="component"
      :updateStore="updateStore"
    />
  </li>
</template>

<script>
  import constantes from '../js/constantes'
  export default {
    name: "VueMenuItem",
    props: ['props', 'updateStore'],
    data() {
      return {
        constantes: constantes
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
    }
  }
</script>

<style scoped>

</style>
