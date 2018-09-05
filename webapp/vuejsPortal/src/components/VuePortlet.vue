<template>
  <div id="vue-portlet">
    <div v-if="portlet">
      <div
        v-for="component in portlet.viewScreen"
        v-bind:is="constantes.components[component.name]"
        :props="component">
      </div>
    </div>
    <div v-else>
      <p>no portlet data</p>
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
        portletName: '',
        portletSeqId: '',
        constantes: cst,
        params: {},
        isWatching: this.getNestedObject(this.props, ['watcherName']),
        updateWatcher: ''
      }
    },
    computed: {
      ...mapGetters({
        portlets: 'ui/portlets',
        getPortlet: 'ui/portlet',
        getWatcher: 'data/watcher'
      }),
      portlet() {
        return this.getPortlet(this.portletId)
      },
      portletId() {
        return this.portletName + '-' + this.portletSeqId
      },
      getParams() {
        return this.getWatcher(this.isWatching)
      }
    },
    created() {
      if (this.isWatching) {
        this.$store.dispatch('data/setWatcher', {
          watcherName: this.isWatching,
          params: this.params
        })
      }
      this.$store.dispatch('ui/setPortlet', {
        ...this.props,
        params: this.params
      }).then(success => {
        console.log(success)
        this.portletName = this.props.portalPortletId
        this.portletSeqId = this.props.portletSeqId
        if (this.portlet) {
          this.$store.dispatch('data/startUpdate')
          Object.keys(this.portlet.viewEntities).forEach((key) => {
            this.$store.dispatch('data/setEntity', {
              entityName: key,
              primaryKey: this.portlet.viewEntities[key].primaryKey
            })
            this.portlet.viewEntities[key].list.forEach((record) => {
              let data = {
                entityName: key,
                primaryKey: record.stId,
                data: record
              }
              this.$store.dispatch('data/setEntityRow', data).then(response => {
                return response
              })
            })
          })
          this.$store.dispatch('data/stopUpdate')
        } else {
          console.log('this.portlet doesn\'t exist yet: ' + this.portletName)
        }
      }, error => {
        console.log(error)
      })
    },
    mounted() {

    },
    watch: {
      getParams: function (val) {
        this.$store.dispatch('ui/setPortlet', {
          ...this.props,
          params: val
        }).then(success => {
          console.log(success)
          this.portletName = this.props.portalPortletId
          this.portletSeqId = this.props.portletSeqId
          if (this.portlet) {
            this.$store.dispatch('data/startUpdate')
            Object.keys(this.portlet.viewEntities).forEach((key) => {
              this.$store.dispatch('data/setEntity', {
                entityName: key,
                primaryKey: this.portlet.viewEntities[key].primaryKey
              })
              this.portlet.viewEntities[key].list.forEach((record) => {
                let data = {
                  entityName: key,
                  primaryKey: record.stId,
                  data: record
                }
                this.$store.dispatch('data/setEntityRow', data).then(response => {
                  return response
                })
              })
            })
            this.$store.dispatch('data/stopUpdate')
          } else {
            console.log('this.portlet doesn\'t exist yet: ' + this.portletName)
          }
        }, error => {
          console.log(error)
        })
      }
    }
  }
</script>

<style scoped>

</style>
