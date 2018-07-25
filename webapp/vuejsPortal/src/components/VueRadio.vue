<template>
  <div id="vue-radio">
    <input v-if="data.conditionGroup" type="hidden" :name="data.name + '_grp'" v-bind:value="data.conditionGroup"/>
    <span v-for="(item, index) in props.children" v-if="item.type === 'element' && item.tagName === 'vue-radio-item'">
      <input type="radio"
             :checked="!!((data.currentValue && data.currentValue === childrenData(index).key) || (!data.currentValue && data.noCurrentSelectedKey && data.noCurrentSelectedKey === childrenData(index).key))"
             :tabIndex="data.currentValue && data.tabIndex ? data.tabIndex : false"
             :name="data.name"
             :value="childrenData(index).key"
      />
        {{childrenData(index).description}}
    </span>
    <vue-error v-if="data.event" component="event"/>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "VueRadio",
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
      childrenData() {
        return (index) => {
          let data = {}
          this.props.children[index].attributes.map(attr => {
            if (attr.value === 'false') {
              data[attr.key] = false
            } else if (attr.value === 'true') {
              data[attr.key] = true
            } else {
              data[attr.key] = attr.value
            }
          })
          return data
        }
      },
      ...mapGetters({
        dataFromExample: 'data/dataFromExample',
        currentId: 'data/currentId'
      })
    },
    watch: {
      data: function (from, to) {
        if (this.updateStore) {
          console.log('vue-radio : ', this.storeData)
          this.$store.dispatch('data/addDataToExample', this.storeData)
        }
      }
    },
    created() {
      if (this.updateStore) {
        console.log('vue-radio : ', this.storeData)
        this.$store.dispatch('data/addDataToExample', this.storeData)
      }
    }
  }
</script>

<style scoped>

</style>
