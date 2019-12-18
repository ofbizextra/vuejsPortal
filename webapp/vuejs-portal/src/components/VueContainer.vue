<template>
  <div :id="areaId">
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
  import {mapGetters} from 'vuex'
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
      ...mapGetters({
        getWatcher: 'data/watcher'
      }),
      area() {
        return this.$store.getters['ui/area'](this.areaId)
      },
      areaId() {
        return this.props.attributes.hasOwnProperty('id') ? this.props.attributes.id : null
      },
      autoUpdatetarget() {
        return this.props.attributes.hasOwnProperty('autoUpdateTarget') ? this.props.attributes.autoUpdateTarget : ''
      },
      setArea() {
        return {
          areaId: this.areaId,
          targetUrl: this.autoUpdatetarget,
          wait: this.$wait,
          params: this.watcher
        }
      },
      watcher() {
        return this.$store.getters['data/watcher'](this.areaId)
        //return this.getWatcher(this.areaId) // waiting question to julien
      }
    },
    methods: {},
    created() {
      if (this.props.attributes.autoUpdateTarget) {
        this.$store.dispatch('data/setWatcher', {watcherName: this.props.attributes.id, params: {}})
      }
      if (this.areaId.includes('_modalContent')) {return}
      this.$store.dispatch('ui/deleteArea', {areaId: this.props.attributes.id})
    },
    beforeDestroy() {
      this.$store.dispatch('ui/deleteArea', {areaId: this.props.attributes.id})
    },
    watch: {
      props: function (val) {
        if (this.areaId.includes('_modalContent')) {return}
        this.$store.dispatch('ui/deleteArea', {areaId: val.attributes.id})
      },
      watcher: function () {
        if (this.autoUpdatetarget) {
          this.$store.dispatch('ui/setArea', this.setArea)
        }
      }
    }
  }
</script>

<style scoped>

</style>
