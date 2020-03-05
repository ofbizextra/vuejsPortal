<template>
    <label class="font-weight-medium ma-2" v-if="!single">{{data.title ? data.title : data.description}}</label>
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
      },
      single() {
          return this.data.hasOwnProperty('single')
      }
    },
    props: [
      'props'
    ]
  }
</script>

<style scoped>

</style>
