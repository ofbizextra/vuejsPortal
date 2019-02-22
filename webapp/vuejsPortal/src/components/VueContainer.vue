<template>
  <div id="vue-container">
    <div v-if="area">
      <div
        v-for="(component, key) in area.viewScreen"
        :key="key"
        v-bind:is="constantes.components[component.name]"
        :props="component"
        :updateStore="updateStore">
      </div>
    </div>
    <div v-else>
      <div
        v-for="(component, key) in props.children"
        :key="key"
        v-bind:is="constantes.components[component.name]"
        :props="component"
        :updateStore="updateStore">
      </div>
    </div>
  </div>
</template>

<script>
  import constantes from '../js/constantes'

  export default {
    name: "VueContainer",
    props: ['props', 'updateStore'],
    data() {
      return {
        constantes: constantes
      }
    },
    computed: {
      area() {
        return this.$store.getters['ui/area'](this.areaId)
      },
      areaId() {
        return this.props.attributes.hasOwnProperty('id') ? this.props.attributes.id : null
      }
    },
    methods: {},
    created() {
      this.$store.dispatch('ui/deleteArea', {areaId: this.props.attributes.id})
    },
    beforeDestroy() {
      this.$store.dispatch('ui/deleteArea', {areaId: this.props.attributes.id})
    },
    watch: {
      props: function (val) {
        this.$store.dispatch('ui/deleteArea', {areaId: val.attributes.id})
      }
    }
  }
</script>

<style scoped>

</style>
