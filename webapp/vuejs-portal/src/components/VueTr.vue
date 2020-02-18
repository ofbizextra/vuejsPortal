<template>
  <tr id="vue-tr" v-bind="data">
    <td
      v-for="(component, key) in props.children"
      :key="key"
      v-bind:is="constants.components[component.name]"
      :props="component"
      :updateStore="updateStore">
    </td>
  </tr>
</template>

<script>
  import cst from '../js/constants'

  export default {
    name: "VueTr",
    props: ['props', 'updateStore'],
    data() {
      return {
        constants: cst
      }
    },
    computed: {
      data() {
        let data = this.props.attributes
        delete data['value']
        if (data.className || (data.alert && data.alert === true)) {
          data.class = data.className ? data.className : '' + ' ' + data.alert === true ? 'alert' : ''
        }
        if (data.style) {
          data.class = data.style
        }
        if (data.hasOwnProperty('oddRowStyle')) {
          data.class = data.oddRowStyle
        }
        return data
      }
    }
  }
</script>

<style scoped>

</style>
