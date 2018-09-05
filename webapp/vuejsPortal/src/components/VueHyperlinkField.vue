<template>
  <div id="vue-hyperlink-field">
    <a v-if="pointer.entityName !== ''" href="data.linkUrl" :title="pointer.attribute" v-on:click.prevent="submit">{{getPointer}}</a>
    <a v-else href="data.linkUrl" :title="data.title ? data.title : ''" v-on:click.prevent="submit">{{data.value ? data.value : ''}}</a>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  export default {
    name: "VueHyperlinkField",
    props: ['props'],
    data() {
      return {
        pointer: {
          entityName: this.getNestedObject(this.props, ['stPointer', 'stEntityName']),
          id: this.getNestedObject(this.props, ['stPointer', 'id']),
          attribute: this.getNestedObject(this.props, ['stPointer', 'field'])
        }
      }
    },
    computed: {
      data() {
        return this.props.attributes
      },
      getPointer() {
        return this.getData(this.pointer);
      },
      ...mapGetters({
        getData: 'data/entityRowAttribute'
      })
    },
    params() {
      let param = {};
      if (this.data.uniqueItemName) {
        param.presentation = 'layer'
        if (this.data.targetParameters) {

        }
      }
    },
    methods: {
      submit() {
        if (this.pointer.entityName !== '') {
          console.log('setWatcher by pointer')
          console.log('getPointer: ' + this.getPointer)
          this.$store.dispatch('data/setWatcher', {watcherName: this.getNestedObject(this.data, ['updateArea']), params: {[this.pointer.attribute]: this.getPointer}})
        } else {
          console.log('setWatcher by data.value')
          console.log('data.value: ' + this.data.value.toString())
          this.$store.dispatch('data/setWatcher', {watcherName: this.getNestedObject(this.data, ['updateArea']), params: {exampleId: this.data.value}})
        }
      }
    },
    mounted() {
    },
    watch: {
      props: function () {
        this.pointer = {
          entityName: this.getNestedObject(this.props, ['stPointer', 'stEntityName']),
          id: this.getNestedObject(this.props, ['stPointer', 'id']),
          attribute: this.getNestedObject(this.props, ['stPointer', 'field'])
        }
      }
    }
  }
</script>

<style scoped>

</style>
