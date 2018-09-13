<template>
  <div id="vue-label">
    {{getValue}}
  </div>
</template>

<script>
  export default {
    name: "VueLabel",
    props: ['props', 'updateStore'],
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
      getValue () {
        return this.updateStore ? this.dataFromExample(this.storeData) : ''
      }
    }
  }
</script>

<style scoped>

</style>
