<template>
  <tr id="vue-header-row" v-bind="data">
    <div
      v-for="(component, key) in props.children"
      :key="key"
      v-bind:is="constantes.components[component.name]"
      :props="component"
      :updateStore="updateStore">
    </div>
  </tr>
</template>

<script>
  import {mapGetters} from 'vuex'
  import cst from '../js/constantes'

  export default {
    name: "VueHeaderRow",
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
        if (data.hasOwnProperty('style')) {
          data.class = data.style
        }
        return data
      }
    }
  }
</script>

<style scoped>

</style>
