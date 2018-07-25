<template>
  <span id="vue-tooltip" v-if="data.tooltip" v-bind:class="data.tooltipStyle ? data.tooltipStyle : ''">
    {{data.tooltip}}
  </span>
</template>

<script>
  import { mapGetters } from 'vuex'

  export default {
    name: "VueTooltip",
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
      },
      storeData() {
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
          console.log('vue-hidden : ', this.storeData)
          this.$store.dispatch('data/addDataToExample', this.storeData)
        }
      }
    },
    created() {
      if (this.updateStore) {
        console.log('vue-hidden : ', this.storeData)
        this.$store.dispatch('data/addDataToExample', this.storeData)
      }
    }
  }
</script>

<style scoped>

</style>
