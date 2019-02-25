<template>
  <div id="vue-hyperlink-field">
    <a v-if="pointer.entityName !== ''" href="data.linkUrl" :title="pointer.attribute" v-on:click.prevent="submit" v-bind="data">{{getPointer}}</a>
    <a v-else href="data.linkUrl" :title="data.title ? data.title : ''" v-on:click.prevent="submit" v-bind="data">{{data.value ? data.value : ''}}</a>
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
        let data = this.props.attributes
        if (data.className || (data.alert && data.alert === true)) {
          data.class = data.className ? data.className : '' + ' ' + data.alert === true ? 'alert' : ''
        }
        if (data.hasOwnProperty('style')) {
          data.class = data.style
        }
        return data
      },
      getPointer() {
        return this.getData(this.pointer);
      },
      ...mapGetters({
        getData: 'data/entityRowAttribute'
      }),
      target() {
        return this.props.attributes.hasOwnProperty('target') ? this.props.attributes.target : null
      },
      targetWindow() {
        return this.props.attributes.hasOwnProperty('targetWindow') ? this.props.attributes.targetWindow : null
      },
      parameterMap() {
        return this.props.attributes.hasOwnProperty('parameterMap') ? this.props.attributes.parameterMap : {}
      }
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
        if (this.targetWindow) {
          this.$store.dispatch('ui/setArea', {areaId: this.targetWindow, targetUrl: '/exampleapi/control/' + this.target, wait: this.$wait, params: this.parameterMap})
        } else {
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
