<template>
  <td id="vue-td" v-bind="data">
    <div
      v-for="component in props.children"
      v-if="component.type === 'element' && (component.tagName.includes('vue-'))"
      v-bind:is="component.tagName"
      :props="component"
      :updateStore="updateStore">
    </div>
  </td>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "VueTd",
    props: ['props', 'updateStore'],
    data() {
      return {}
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
      getValue() {
        return this.updateStore ? this.dataFromExample(this.storeData) : ''
      },
      ...mapGetters({
        dataFromExample: 'data/dataFromExample',
        currentId: 'data/currentId'
      })
    },
    watch: {
      data: function (from, to) {
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
