<template>
  <div id="vue-hyperlink-title">
    <a :href="linkUrl" :title="title">{{value}}</a>
  </div>
</template>

<script>
  export default {
    name: "VueHyperlinkTitle",
    props: ['props'],
    computed: {
      linkUrl() {
        return this.props.attributes.hasOwnProperty('linkUrl') ? this.props.attributes.linkUrl : ''
      },
      title() {
        return this.props.attributes.hasOwnProperty('title') ? this.props.attributes.title : ''
      },
      value() {
        return this.props.attributes.hasOwnProperty('value') ? this.props.attributes.value : ''
      }
    },
  }
</script>

<style scoped>

</style>
