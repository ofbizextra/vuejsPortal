<template>
  <div id="vue-sort-field">
    <a :class="data.style" href="linkUrl" v-bind:title="data.tooltip">{{data.title}}</a>
  </div>
</template>

<script>
  export default {
    name: "VueSortField",
    data() {
      return {
      }
    },
    computed: {
      data() {
        let data = this.props.attributes
        delete data.value
        return data
      },
      linkUrl() {
        return this.data.ajaxEnabled ? "javascript:ajaxUpdateAreas('" + this.data.link + "')" : this.data.linkUrl
      }
    }
  }
</script>

<style scoped>

</style>
