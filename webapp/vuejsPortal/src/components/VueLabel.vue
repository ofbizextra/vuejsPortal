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
        return this.props.attributes
      },
      getValue () {
        return this.updateStore ? this.dataFromExample(this.storeData) : ''
      }
    }
  }
</script>

<style scoped>

</style>
