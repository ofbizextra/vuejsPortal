<template>
  <div :id="areaId">
    <div v-if="area">
      <div
        v-for="(component, key) in area.viewScreen"
        :key="key"
        v-bind:is="constants.components[component.name]"
        :props="component"
        :updateStore="updateStore">
      </div>
    </div>
    <div v-else>
      <div
        v-for="(component, key) in props.children"
        :key="key"
        v-bind:is="constants.components[component.name]"
        :props="component"
        :updateStore="updateStore">
      </div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import constants from '../js/constants'

  export default {
    name: "VueContainer",
    props: ['props', 'updateStore', 'autoUpdateParams'],
    data() {
      return {
        constants: constants
      }
    },
    computed: {
      ...mapGetters({
        getWatcher: 'data/watcher',
        currentApi: 'backOfficeApi/currentApi'
      }),
      area() {
        return this.$store.getters['ui/area'](this.areaId)
      },
      areaId() {
        return this.props.attributes.hasOwnProperty('id') ? this.props.attributes.id : null
      },
      updateParams() {
        return this.autoUpdateParams ? this.autoUpdateParams : false
      },
      autoUpdate() {
        return this.autoUpdateParams || this.autoUpdateTarget
      },
      targetUrl() {
        if (this.updateParams.hasOwnProperty('targetUrl')) {
          return this.updateParams.targetUrl
        }
        if (this.autoUpdateTarget) {
          let uri = this.currentApi + '/' + this.autoUpdateTarget
          if (uri.includes('{')) {
            let regexKey = /{(\w+)}/
            let regexReplace = /{\w+}/
            uri = uri.replace(regexReplace, this.params[regexKey.exec(uri)[1]])
          }
          return uri
        }
        return ''
      },
      params() {
        return this.updateParams.hasOwnProperty('params') ? {...this.updateParams.params, ...this.watcher} : this.watcher
      },
      autoUpdateTarget() {
        return this.props.attributes.hasOwnProperty('autoUpdateTarget') ? this.props.attributes.autoUpdateTarget : ''
      },
      setArea() {
        return {
          areaId: this.areaId,
          targetUrl: this.targetUrl,
          wait: this.$wait,
          params: this.params
        }
      },
      watcher() {
        return this.$store.getters['data/watcher'](this.watcherName)
      },
      watcherName() {
        return this.props.attributes.hasOwnProperty('watcherName') ? this.props.attributes.watcherName : this.areaId
      }
    },
    methods: {},
    created() {
      if (this.updateParams || this.autoUpdateTarget) {
        this.$store.dispatch('data/setWatcher', {watcherName: this.watcherName, params: {}})
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
        if (this.autoUpdate) {
          this.$store.dispatch('ui/setArea', this.setArea)
        }
      }
    }
  }
</script>

<style scoped>

</style>
