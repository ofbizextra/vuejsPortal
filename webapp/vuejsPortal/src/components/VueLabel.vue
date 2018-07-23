<template>
  <div id="vue-label">
    {{data.description}}
  </div>
</template>

<script>

  export default {
    name: "VueLabel",
    props: ['props'],
    data() {
      return {
      }
    },
    computed: {
      data() {
        return this.parseProps()
      },
      storeData() {
        return {
          id: this.$store.getters['data/currentId'],
          key: this.parseProps().idName,
          value: this.parseProps().description ? this.parseProps().description : ''
        }
      }
    },
    watch: {
      data: function(from, to) {
        console.log('vue-label : ', this.storeData)
        this.$store.dispatch('data/addDataToExample', this.storeData)
      }
    },
    created() {
      this.$store.dispatch('data/addDataToExample', this.storeData)
    }
  }
</script>

<style scoped>

</style>
