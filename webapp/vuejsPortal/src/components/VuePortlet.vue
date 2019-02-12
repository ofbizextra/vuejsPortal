<template>
  <div v-bind:id="'vue-portlet_' + portletId">
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
        constantes: cst,
      }
    },
    computed: {
      ...mapGetters({
        getPortlet: 'ui/area',
        getWatcher: 'data/watcher'
      }),
      portlet() {
        return this.getPortlet(this.portletId)
      },
      portletId() {
        return this.props.portalPortletId + '-' + this.props.portletSeqId
      },
      isWatching() {
        return this.props.hasOwnProperty('watcherName') ? this.props.watcherName : null
      },
      getParams() {
        return this.$store.getters['data/watcher'](this.isWatching)
      },
      getPortletTarget() {
        return this.$store.getters['ui/portletTarget'](this.portletId)
      }
    },
    created() {
      // this.$wait.start(this.props.portalPortletId + '-' + this.props.portletSeqId)
      if (this.isWatching) {
        this.$store.dispatch('data/setWatcher', {
          watcherName: this.isWatching,
          params: {}
        })
        this.$nextTick(() => {
          this.$wait.end(this.props.portalPortletId + '-' + this.props.portletSeqId)
        })
      } else {
        console.log(cst.apiUrl + cst.showPortlet.path + ' : => ' + this.props.portalPortletId + '-' + this.props.portletSeqId)
        this.$store.dispatch('ui/setArea', {
          areaId: this.props.portalPortletId + '-' + this.props.portletSeqId,
          targetUrl: cst.showPortlet.path,
          params: {portalPortletId: this.props.portalPortletId}
        }).then(success => {
          // this.$nextTick(() => {
          //   this.$wait.end('update' + this.props.portalPortletId + '-' + this.props.portletSeqId)
          // })
        }, error => {
          console.log(error)
          // this.$nextTick(() => {
          //   this.$wait.end('update' + this.props.portalPortletId + '-' + this.props.portletSeqId)
          // })
        })
      }
    },
    watch: {
      getParams: function (val) {
        console.log('portlet params updated: ', val)
        // this.$wait.start('update' + this.props.portalPortletId + '-' + this.props.portletSeqId)
        console.log(cst.apiUrl + cst.showPortlet.path + ' : => ' + this.props.portalPortletId + '-' + this.props.portletSeqId)
        this.$store.dispatch('ui/setArea', {
          areaId: this.portletId,
          targetUrl: cst.showPortlet.path,
          params: {...val, portalPortletId: this.props.portalPortletId}
        }).then(success => {
            // this.$nextTick(() => {
            //   this.$wait.end('update' + this.props.portalPortletId + '-' + this.props.portletSeqId)
            // })
          }, error => {
          console.log(error)
          // this.$nextTick(() => {
          //   this.$wait.end('update' + this.props.portalPortletId + '-' + this.props.portletSeqId)
          // })
        })
      },
      // getPortletTarget: function (newPortletTarget) {
      //   if (newPortletTarget) {
      //     console.log('portlet params updated: ', newPortletTarget)
      //     this.$wait.start('update' + this.props.portalPortletId + '-' + this.props.portletSeqId)
      //     this.$store.dispatch('ui/setPortlet', {
      //       portalPortletId: newPortletTarget.split('-')[0],
      //       portletSeqId: newPortletTarget.split('-')[1],
      //       params: this.getParams
      //     }).then(success => {
      //       console.log('Successfully setPortlet => ' + success)
      //       this.portletName = newPortletTarget.split('-')[0]
      //       this.portletSeqId = newPortletTarget.split('-')[1]
      //       this.$nextTick(() => {
      //         let portletGetter = this.$store.getters['ui/portlet']
      //         let portlet = portletGetter(this.portletName + '-' + this.portletSeqId)
      //         if (portlet) {
      //           console.log('this.portlet => ', portlet)
      //           let entities = []
      //           let records = []
      //           Object.keys(portlet.viewEntities).forEach((key) => {
      //             console.log('setEntity => ' + key)
      //             entities.push(this.$store.dispatch('data/setEntity', {
      //               entityName: key,
      //               primaryKey: portlet.viewEntities[key].primaryKeys.join('-')
      //             }))
      //           })
      //           Promise.all(entities).then(all => {
      //             console.log('all entity have been created: ' + all)
      //             all.forEach((entity => {
      //               console.log('creating record for entity: ' + entity.entityName)
      //               portlet.viewEntities[entity.entityName].list.forEach((record, index) => {
      //                 console.log(entity.entityName + ': creating record number ' + index + ': ', record)
      //                 if (record.stId !== "null") {
      //                   let data = {
      //                     entityName: entity.entityName,
      //                     primaryKey: record.stId,
      //                     data: record
      //                   }
      //                   records.push(this.$store.dispatch('data/setEntityRow', data))
      //                 }
      //               })
      //             }))
      //           })
      //           Promise.all(records).then(
      //             this.$nextTick(() => {
      //               this.$wait.end('update' + this.props.portalPortletId + '-' + this.props.portletSeqId)
      //             })
      //           )
      //         } else {
      //           console.log('CREATED_HOOK: this.portlet doesn\'t exist yet: ' + this.portletName + '-' + this.portletSeqId)
      //           this.$nextTick(() => {
      //             this.$wait.end('update' + this.props.portalPortletId + '-' + this.props.portletSeqId)
      //           })
      //         }
      //       })
      //     }, error => {
      //       console.log(error)
      //       this.$nextTick(() => {
      //         this.$wait.end('update' + this.props.portalPortletId + '-' + this.props.portletSeqId)
      //       })
      //     })
      //   }
      // }
    }
  }
</script>

<style scoped>

</style>
