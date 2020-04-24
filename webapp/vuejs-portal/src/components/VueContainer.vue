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
        let uri = ''
        if (this.updateParams.hasOwnProperty('targetUrl')) {
          uri =  this.updateParams.targetUrl
        }
        else if (this.autoUpdateTarget) {
          uri = this.currentApi + '/' + this.autoUpdateTarget
        }
        let regex = /{(\w+)}/
        while (regex.exec(uri)) {
          uri = uri.replace(regex, this.params[regex.exec(uri)[1]])
        }
        return uri
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
      watcher: function (fresh, old) {
        if (this.autoUpdate && (old !== fresh)) {
          this.$store.dispatch('ui/setArea', this.setArea)
        }
      }
    }
  }
</script>

<style scoped>

</style>
