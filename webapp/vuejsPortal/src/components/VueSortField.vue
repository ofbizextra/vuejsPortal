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
        if (data.className || (data.alert && data.alert === true)) {
          data.class = data.className ? data.className : '' + ' ' + data.alert === true ? 'alert' : ''
        }
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
