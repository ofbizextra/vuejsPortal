<template>
  <div id="vue-portlet">
    <div v-bind:id="'vue-portlet_' + portletId">
      <div v-if="portlet">
        <div
          v-for="(component, key) in portlet.viewScreen"
          :key="key"
          v-bind:is="constantes.components[component.name]"
          :props="component">
        </div>
      </div>
      <div v-else-if="isPosted">
        <div
          v-for="(component, key) in children"
          :key="key"
          v-bind:is="constantes.components[component.name]"
          :props="component">
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import cst from '../js/constantes'

  export default {
    name: "VuePortlet",
    props: ['props'],
    data() {
      return {
        constantes: cst,
      }
    },
    computed: {
      ...mapGetters({
        getPortlet: 'ui/area',
        getWatcher: 'data/watcher',
        portalPageId: 'ui/currentPortalPage'
      }),
      portalPortletId() {
        return this.props.hasOwnProperty('portalPortletId') ? this.props.portalPortletId : this.props.attributes.portalPortletId
      },
      portletSeqId() {
        return this.props.hasOwnProperty('portletSeqId') ? this.props.portletSeqId : this.props.attributes.portletSeqId
      },
      watcherName() {
        return this.props.hasOwnProperty('watcherName') ? this.props.watcherName : this.props.attributes.watcherName
      },
      portlet() {
        return this.getPortlet(this.portletId)
      },
      portletId() {
        return this.portalPortletId + '-' + this.portletSeqId
      },
      isWatching() {
        return this.watcherName ? this.watcherName : null
      },
      getParams() {
        return this.$store.getters['data/watcher'](this.isWatching)
      },
      children() {
        return this.props.hasOwnProperty('children') ? this.props.children : []
      },
      isPosted() {
        return this.props.hasOwnProperty('attributes')
      }
    },
    created() {
      this.$store.dispatch('ui/deleteArea', {areaId: this.portletId})
      if (this.isWatching) {
        this.$store.dispatch('data/setWatcherAttributes', {
          watcherName: this.isWatching,
          params: {}
        })
      } else {
        if (this.isPosted) {
          return
        }
        console.log(cst.apiUrl + cst.showPortlet.path + ' : => ' + this.portalPortletId + '-' + this.portletSeqId)
        this.$store.dispatch('ui/setArea', {
          areaId: this.portalPortletId + '-' + this.portletSeqId,
          targetUrl: cst.showPortlet.path,
          wait: this.$wait,
          params: {portalPortletId: this.portalPortletId, portalPageId: this.portalPageId, portletSeqId: this.portletSeqId}
        })
      }
    },
    beforeDestroy() {
      this.$store.dispatch('ui/deleteArea', {areaId: this.portletId})
    },
    watch: {
      getParams: function (val) {
        console.log('portlet params updated: ', val)
        console.log(cst.apiUrl + cst.showPortlet.path + ' : => ' + this.portalPortletId + '-' + this.portletSeqId)
        this.$store.dispatch('ui/setArea', {
          areaId: this.portletId,
          targetUrl: cst.showPortlet.path,
          wait: this.$wait,
          params: {...val, portalPortletId: this.portalPortletId, portalPageId: this.portalPageId, portletSeqId: this.portletSeqId}
        })
      },
      props: function () {
        this.$store.dispatch('ui/deleteArea', {areaId: this.portletId})
      }
    }
  }
</script>

<style scoped>

</style>
