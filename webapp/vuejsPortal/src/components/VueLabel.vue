<template>
  <div id="vue-label">
    {{getValue}}
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
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
      storeData() {
        return {
          id: this.$store.getters['data/currentId'],
          key: this.props.attributes.idName,
          value: this.props.attributes.description ? this.props.attributes.description : ''
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
          console.log('vue-label : ', this.storeData)
          this.$store.dispatch('data/addDataToExample', this.storeData)
        }
      }
    },
    created() {
      if (this.updateStore) {
        console.log('vue-label : ', this.storeData)
        this.$store.dispatch('data/addDataToExample', this.storeData)
      }
    }
  }
</script>

<style scoped>

</style>
