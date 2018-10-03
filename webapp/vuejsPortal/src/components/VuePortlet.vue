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
        return this.$store.getters['data/watcher'](this.isWatching)
      }
    },
    created() {
      this.$wait.start(this.props.portalPortletId + '-' + this.props.portletSeqId)
      if (this.isWatching) {
        this.$store.dispatch('data/setWatcher', {
          watcherName: this.isWatching,
          params: this.params
        })
        this.$nextTick(() => {
          this.$wait.end(this.props.portalPortletId + '-' + this.props.portletSeqId)
        })
      } else {
        this.$store.dispatch('ui/setPortlet', {
          ...this.props,
          params: this.params
        }).then(success => {
          console.log('Successfully setPortlet => ' + success)
          this.portletName = this.props.portalPortletId
          this.portletSeqId = this.props.portletSeqId
          this.$nextTick(() => {
            let portletGetter = this.$store.getters['ui/portlet']
            let portlet = portletGetter(this.portletName + '-' + this.portletSeqId)
            if (portlet) {
              console.log('this.portlet => ', portlet)
              let entities = []
              let records = []
              Object.keys(portlet.viewEntities).forEach((key) => {
                console.log('setEntity => ' + key)
                entities.push(this.$store.dispatch('data/setEntity', {
                  entityName: key,
                  primaryKey: portlet.viewEntities[key].primaryKeys.join('-')
                }))
              })
              Promise.all(entities).then(all => {
                console.log('all entity have been created')
                all.forEach((entity => {
                  console.log('creating record for entity: ' + entity.entityName)
                  portlet.viewEntities[entity.entityName].list.forEach((record, index) => {
                    console.log(entity.entityName + ': creating record number ' + index + ': ', record)
                    if (record.stId !== null) {
                      let data = {
                        entityName: entity.entityName,
                        primaryKey: record.stId,
                        data: record
                      }
                      records.push(this.$store.dispatch('data/setEntityRow', data))
                    }
                  })
                }))
              })
              Promise.all(records).then(
                this.$nextTick(() => {
                  this.$store.dispatch('data/stopUpdate')
                  this.$wait.end(this.props.portalPortletId + '-' + this.props.portletSeqId)
                })
              )
            } else {
              console.log('CREATED_HOOK: this.portlet doesn\'t exist yet: ' + this.portletName + '-' + this.portletSeqId)
              this.$nextTick(() => {
                this.$wait.end(this.props.portalPortletId + '-' + this.props.portletSeqId)
              })
            }
          })
        }, error => {
          console.log(error)
          this.$nextTick(() => {
            this.$wait.end(this.props.portalPortletId + '-' + this.props.portletSeqId)
          })
        })
      }
    },
    watch: {
      getParams: function (val) {
        console.log('portlet params updated: ' + val)
        this.$wait.start('update' + this.props.portalPortletId + '-' + this.props.portletSeqId)
        this.$store.dispatch('ui/setPortlet', {
          ...this.props,
          params: val
        }).then(success => {
          console.log('Successfully setPortlet => ' + success)
          this.portletName = this.props.portalPortletId
          this.portletSeqId = this.props.portletSeqId
          this.$nextTick(() => {
            let portletGetter = this.$store.getters['ui/portlet']
            let portlet = portletGetter(this.portletName + '-' + this.portletSeqId)
            if (portlet) {
              console.log('this.portlet => ', portlet)
              let entities = []
              let records = []
              Object.keys(portlet.viewEntities).forEach((key) => {
                console.log('setEntity => ' + key)
                entities.push(this.$store.dispatch('data/setEntity', {
                  entityName: key,
                  primaryKey: portlet.viewEntities[key].primaryKeys.join('-')
                }))
              })
              Promise.all(entities).then(all => {
                console.log('all entity have been created: ' + all)
                all.forEach((entity => {
                  console.log('creating record for entity: ' + entity.entityName)
                  portlet.viewEntities[entity.entityName].list.forEach((record, index) => {
                    console.log(entity.entityName + ': creating record number ' + index + ': ', record)
                    if (record.stId !== "null") {
                      let data = {
                        entityName: entity.entityName,
                        primaryKey: record.stId,
                        data: record
                      }
                      records.push(this.$store.dispatch('data/setEntityRow', data))
                    }
                  })
                }))
              })
              Promise.all(records).then(
                this.$nextTick(() => {
                  this.$store.dispatch('data/stopUpdate')
                  this.$wait.end('update' + this.props.portalPortletId + '-' + this.props.portletSeqId)
                })
              )
            } else {
              console.log('CREATED_HOOK: this.portlet doesn\'t exist yet: ' + this.portletName + '-' + this.portletSeqId)
              this.$nextTick(() => {
                this.$wait.end('update' + this.props.portalPortletId + '-' + this.props.portletSeqId)
              })
            }
          })
        }, error => {
          console.log(error)
          this.$nextTick(() => {
            this.$wait.end('update' + this.props.portalPortletId + '-' + this.props.portletSeqId)
          })
        })
      }
    }
  }
</script>

<style scoped>

</style>
