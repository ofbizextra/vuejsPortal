<template>
  <div id="vue-drop-down">
    <select v-bind:currentValue="getValue" v-bind="data">
      <option value="" v-if="data.allowEmpty || (data.allowEmpty && props.children.length === 0)"></option>
      <option v-if="data.firstInList && getValue && !data.multiple" selected="selected" :value="getValue" >{{data.explicitDescription}}</option>
      <vue-option
        v-for="option in props.children"
        :key="option.attributes[0].value"
        v-if="option.type === 'element' &&
        option.tagName === 'vue-option'"
        :props="option"
        :selected="option.attributes[0].value === getValue">
      </vue-option>
    </select>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'

  export default {
    name: "VueDropDown",
    props: ['props', 'updateStore'],
    data() {
      return {
      }
    },
    computed: {
      data () {
        let data = this.parseProps()
        delete data['currentValue']
        return data
      },
      storeData () {
        return {
          id: this.$store.getters['data/currentId'],
          key: this.parseProps().id,
          value: this.parseProps().currentValue ? this.parseProps().currentValue : ''
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
