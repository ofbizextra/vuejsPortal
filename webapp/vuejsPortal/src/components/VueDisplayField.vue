<template>
  <div id="vue-display-field">
    <label v-if="pointer" v-bind="data">{{getPointer}}</label>
    <label v-else v-bind="data">{{data.title ? data.title : data.description}}</label>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "VueDisplayField",
    data() {
      return {}
    },
    computed: {
      data() {
        return {
          ...this.props.attributes,
          pointer: {
            entityName: null,
            id: null,
            attribute: null
          }
        }
      },
      getPointer() {
        return this.getData(this.data.pointer)
      },
      ...mapGetters({
        getData: 'data/entityRowAttribute'
      })
    },
    props: [
      'props'
    ],
    created() {
      if (this.data.data && this.data.data.recordPointer) {
        this.data.pointer.entityName = this.data.data.recordPointer.entity
        this.data.pointer.id = this.data.data.recordPointer.id
        this.data.pointer.attribute = this.data.data.recordPointer.field
      }
    }
  }
</script>

<style scoped>

</style>
