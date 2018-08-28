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
      this.$store.dispatch('ui/setPortlet', {...this.props, params: {exampleId: 'EX', exampleId_op: 'contains'}}).then(success => {
        console.log(success)
        this.portletName = this.props.portalPortletId
        this.portletSeqId = this.props.portletSeqId
      }, error => {
        console.log(error)
      })
    }
  }
</script>

<style scoped>

</style>
