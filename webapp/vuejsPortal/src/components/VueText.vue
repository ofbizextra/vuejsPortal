<template>
  <div id="vue-text">
    <input v-bind="data"/>
  </div>
</template>

<script>
  export default {
    name: "VueText",
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
          key: this.parseProps().id,
          value: this.parseProps().value ? this.parseProps().value : ''
        }
      }
    },
    watch: {
      data: function(from, to) {
        console.log('vue-text : ', this.storeData)
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
