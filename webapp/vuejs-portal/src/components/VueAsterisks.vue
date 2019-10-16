<template>
  <span id="vue-asterisks" v-if="data.requiredField && !data.requiredStyle">*</span>
</template>

<script>
  export default {
    name: "VueAsterisks",
    props: ['props', 'updateStore'],
    data() {
      return {
      }
    },
    computed: {
      data() {
        let data = this.props.attributes
        delete data['value']
        if (data.className || (data.alert && data.alert === true)) {
          data.class = data.className ? data.className : '' + ' ' + data.alert === true ? 'alert' : ''
        }
        return data
      }
    }
  }
</script>

<style scoped>

</style>
