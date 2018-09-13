<template>
  <div id="vue-field-title">
    <label v-bind="data">{{data.title ? data.title : data.description}}</label>
  </div>
</template>

<script>
  export default {
    name: "VueFieldTitle",
    data() {
      return {
      }
    },
    computed: {
      data() {
        let data = this.props.attributes
        if (data.className || (data.alert && data.alert === true)) {
          data.class = data.className ? data.className : '' + ' ' + data.alert === true ? 'alert' : ''
        }
        return data
      }
    },
    props: [
      'props'
    ]
  }
</script>

<style scoped>

</style>
