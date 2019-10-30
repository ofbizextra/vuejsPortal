<template>
  <table dense id="vue-list-wrapper" v-bind="data" v-if="show">
    <div
        v-for="(component, key) in props.children"
        :key="key"
        v-bind:is="constantes.components[component.name]"
        :props="component"
        :updateStore="updateStore">
    </div>
  </table>
</template>

<script>
  import cst from '../js/constantes'

  export default {
    name: "VueListWrapper",
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
        if (data.style || (data.alert && data.alert === true)) {
          data.class = data.style ? data.style : '' + ' ' + data.alert === true ? 'alert' : ''
        }
        return data
      },
      listSize() {
        return this.data.hasOwnProperty("listSize") ? this.data.listSize : 0
      },
      show() {
        return this.listSize > 0
      }
    },
    created() {
      if (this.props.attributes.hasOwnProperty('errorMessage')) {
        console.log(this.props.attributes.errorMessage)
        this._vm.flash(this.props.attributes.errorMessage, 'error', 10000)
      }
    }
  }
</script>

<style scoped>

</style>
