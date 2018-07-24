<template>
  <div id="vue-text-area">
    <textarea v-bind:value="getValue" v-bind="data"></textarea>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'

  export default {
    name: "VueTextArea",
    props: ['props', 'updateStore'],
    data() {
      return {
      }
    },
    computed: {
      data () {
        let data = this.parseProps()
        delete data['value']
        return data
      },
      storeData () {
        return {
          id: this.$store.getters['data/currentId'],
          key: this.parseProps().id,
          value: this.parseProps().value ? this.parseProps().value : ''
        }
      },
      getValue () {
        return this.updateStore ? this.dataFromExample(this.storeData) : ''
      },
      ...mapGetters({
        dataFromExample: 'data/dataFromExample',
        currentId: 'data/currentId'
      })
    },
    watch: {
      data: function(from, to) {
        if (this.updateStore) {
          console.log('vue-text-area : ', this.storeData)
          this.$store.dispatch('data/addDataToExample', this.storeData)
        }
      }
    },
    created() {
      if (this.updateStore) {
        console.log('vue-text-area : ', this.storeData)
        this.$store.dispatch('data/addDataToExample', this.storeData)
      }
    }
  }
</script>

<style scoped>

</style>
