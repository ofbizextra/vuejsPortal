<template>
  <div id="vue-text-area">
    <textarea v-bind="data"></textarea>
  </div>
</template>

<script>
  export default {
    name: "VueTextArea",
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
        console.log('vue-text-area : ', this.storeData)
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
