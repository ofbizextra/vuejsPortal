<template>
  <td id="vue-header-row-form-cell" v-bind="data">
    <div
      v-for="component in props.children"
      v-bind:is="constantes.components[component.name]"
      :props="component"
      :updateStore="updateStore">
    </div>
  </td>
</template>

<script>
  import {mapGetters} from 'vuex'
  import cst from '../js/constantes'

  export default {
    name: "VueHeaderRowFormCell",
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
        return data
      }
    }
  }
</script>

<style scoped>

</style>
