<template>
  <td id="vue-column-portlet" :style="'width: ' + width + ';vertical-align: top'">
    <div v-for="(portlet, id) in components" :key="id" v-bind:is="portlet">

    </div>
  </td>
</template>

<script>
  import {mapGetters} from 'vuex'
  export default {
    name: "VueColumnPortlet",
    props: ['props'],
    data() {
      return {
        column: {},
      }
    },
    computed: {
      components() {
        return this.column.listPorlet ? this.column.listPorlet.map(portlet => {return portlet.replace( /([a-z])([A-Z])/g, '$1-$2' ).toLowerCase() + '-layer'}) : []
      },
      width() {
        return this.column.columnWidthPercentage ? this.column.columnWidthPercentage + '%' : this.column.columnWidthPixels ? this.column.columnWidthPixels : ''
      },
      ...mapGetters({
        getColumn: 'ui/column',
        portalPages: 'ui/portalPages'
      })
    },
    mounted() {
      console.log(this.data)
      this.column = this.getColumn({portalPageId: this.props.portalPageId, columnSeqId: this.props.columnSeqId})
    },
    watch: {
      portalPages() {
        this.column = this.getColumn({portalPageId: this.props.portalPageId, columnSeqId: this.props.columnSeqId})
      }
    }
  }
</script>

<style scoped>

</style>
