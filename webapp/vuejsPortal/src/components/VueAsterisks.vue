<template>
  <span id="vue-asterisks" v-if="data.requiredField && !data.requiredStyle">*</span>
</template>

<script>
  import { mapGetters } from 'vuex'

  export default {
    name: "VueAsterisks",
    props: ['props', 'updateStore'],
    data() {
      return {
      }
    },
    computed: {
      data() {
        let data = this.parseProps()
        delete data['value']
        return data
      }
    }
  }
</script>

<style scoped>

</style>
