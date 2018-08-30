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
        constantes: cst
      }
    },
    computed: {
      ...mapGetters({
        portlets: 'ui/portlets',
        getPortlet: 'ui/portlet'
      }),
      portlet() {
        return this.getPortlet(this.portletId)
      },
      portletId() {
        return this.portletName + '-' + this.portletSeqId
      }
    },
    created() {
      this.$store.dispatch('ui/setPortlet', {
        ...this.props,
        params: {exampleId: 'EX', exampleId_op: 'contains'}
      }).then(success => {
        console.log(success)
        this.portletName = this.props.portalPortletId
        this.portletSeqId = this.props.portletSeqId
        if (this.portlet) {
          console.log('data store initialisation: ' + this.portletName, Object.keys(this.portlet.viewEntities))
          Object.keys(this.portlet.viewEntities).forEach(key => {
            console.log('data store initialisation Entity: ' + key)
            this.$store.dispatch('data/setEntity', {entityName: key, primaryKey: this.portlet.viewEntities[key].primaryKey}).then(() => {
              Object.keys(this.portlet.viewEntities[key].list).forEach(recordPk => {
                console.log('data store initialisation EntityRow: ', recordPk)
                this.$store.dispatch('data/setEntityRow', {
                  entityName: key,
                  primaryKey: recordPk,
                  data: this.portlet.viewEntities[key].list[recordPk]
                }).then(succes => console.log(succes), error => console.log(error))
              })
            }, error => {
              console.log(error)
            })
          })
        } else {
          console.log('this.portlet doesn\'t exist yet: ' + this.portletName)
        }
      }, error => {
        console.log(error)
      })
    },
    mounted() {

    }
  }
</script>

<style scoped>

</style>
