// todo compare size and description.size and tronque
<template>
  <div id="vue-display-field">
    <label v-if="pointer.entityName !== ''" v-bind="data">{{getPointer}}</label>
    <label v-else v-bind="data">{{data.title ? data.title : data.description}}</label>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "VueDisplayField",
    data() {
      return {
        pointer: {
          entityName: this.getNestedObject(this.props, ['attributes', 'data', 'recordPointer', 'entity']),
          id: this.getNestedObject(this.props, ['attributes', 'data', 'recordPointer', 'id']),
          attribute: this.getNestedObject(this.props, ['attributes', 'data', 'recordPointer', 'field'])
        }
      }
    },
    computed: {
      data() {
        return {
          ...this.props.attributes,
        }
      },
      getPointer() {
        return this.getData(this.pointer);
      },
      ...mapGetters({
        getData: 'data/entityRowAttribute'
      })
    },
    props: [
      'props'
    ],
    created() {
      // this.pointer = {
      //   entityName: this.getNestedObject(this.props, ['attributes', 'data', 'recordPointer', 'entity']),
      //   id: this.getNestedObject(this.props, ['attributes', 'data', 'recordPointer', 'id']),
      //   attribute: this.getNestedObject(this.props, ['attributes', 'data', 'recordPointer', 'field']),
      // }
    }
  }
</script>

<style scoped>

</style>
