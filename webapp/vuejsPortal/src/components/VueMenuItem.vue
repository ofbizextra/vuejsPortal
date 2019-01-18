<template>
  <li id="vue-menu-item">
    <div
      v-for="(component, index) in props.children"
      :key="index"
      v-bind:is="constantes.components[component.name]"
      :props="component"
      :updateStore="updateStore"
    >
    </div>
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
        if (data.className || (data.alert && data.alert === true)) {
          data.class = data.className ? data.className : '' + ' ' + data.alert === true ? 'alert' : ''
        }
        return data
      }
    },
    methods: {
    }
  }
</script>

<style scoped>

</style>
