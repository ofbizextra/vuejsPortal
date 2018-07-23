<template>
  <div id="vue-label">
    {{data.description}}
  </div>
</template>

<script>

  export default {
    name: "VueLabel",
    props: ['props'],
    data () {
      return {
        data: {}
      }
    },
    mounted() {
      console.log(this.data)
      this.$store.dispatch('data/addDataToExample', this.data.idName, this.data)
    }
  }
</script>

<style scoped>

</style>
