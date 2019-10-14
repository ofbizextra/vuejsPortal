<template>
  <td id="vue-field-row-title-cell" v-bind="data">
    <div
      v-for="(component, key) in props.children"
      :key="key"
      v-bind:is="constantes.components[component.name]"
      :props="component"
      :updateStore="updateStore">
    </div>
  </td>
</template>

<script>
  import cst from '../js/constantes'

  export default {
    name: "VueFieldRowTitleCell",
    props: ['props', 'updateStore'],
    data() {
      return {
        constantes: cst
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
    }
  }
</script>

<style scoped>

</style>
