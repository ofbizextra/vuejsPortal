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
        isWatching: '',
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
      switch (this.props.portalPortletId) {
        case 'listExample':
          this.params = {
            exampleId: 'EX',
            exampleId_op: 'contains'
          }
          this.isWatching = 'watchListExample'
          this.updateWatcher = 'watchEditExample'
          break
        case 'editExample':
          this.params = {
            exampleId: 'EX11'
          }
          this.isWatching = 'watchEditExample'
          this.updateWatcher = ''
          break
        case 'findExample':
          this.params = {}
          this.isWatching = null
          this.updateWatcher = 'watchListExample'
          break
      }
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
            Object.keys(this.portlet.viewEntities[key].list).forEach((recordPk) => {
              let data = {
                entityName: key,
                primaryKey: recordPk,
                data: this.portlet.viewEntities[key].list[recordPk]
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
              Object.keys(this.portlet.viewEntities[key].list).forEach((recordPk) => {
                let data = {
                  entityName: key,
                  primaryKey: recordPk,
                  data: this.portlet.viewEntities[key].list[recordPk]
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
