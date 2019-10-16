<template>
  <div id="vue-hyperlink-title">
    <a :href="data.linkUrl" :title="data.title ? data.title : ''">{{data.value ? data.value : ''}}</a>
  </div>
</template>

<script>
  export default {
    name: "VueHyperlinkTitle",
    props: ['props'],
    computed: {
      data() {
        let data = this.props.attributes
        //delete data.value
        if (data.className || (data.alert && data.alert === true)) {
          data.class = data.className ? data.className : '' + ' ' + data.alert === true ? 'alert' : ''
        }
        return data
      }
    },
    mounted() {
      console.log(this.data)
    }
  }
</script>

<style scoped>

</style>
