<template>
  <div id="vue-hyperlink-field">
    <a href="data.linkUrl" :title="data.title ? data.title : ''" v-on:click.prevent.stop="submit">{{data.value ? data.value : ''}}</a>
  </div>
</template>

<script>
  export default {
    name: "VueHyperlinkField",
    props: ['props'],
    computed: {
      data() {
        let data = this.props.attributes
        //delete data.value
        return data
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
        this.$store.dispatch('data/setWatcher', {watcherName: 'watchEditExample', params: {exampleId: this.data.value}})
      }
    },
    mounted() {
    }
  }
</script>

<style scoped>

</style>
