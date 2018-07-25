<template>
  <div id="vue-text-find">
    <select class="selectBox" v-if="data.opEquals">
      <option value="equals">egal</option>
      <option value="like">debute</option>
      <option value="contain" selected="selected">contient</option>
      <option value="empty">est vide</option>
      <option value="notEqual">Diff.</option>
    </select>
    <input
      type="text"
      v-bind:name="data.name"
      v-bind:size="data.size"
      v-bind:value="getValue"
      v-bind:maxlength="data.maxlength"
      v-bind:autocomplete="data.autocomplete"
      v-bind:tabindex="data.tabindex"
    />
    <span v-bind:class="data.titleStyle ? data.titleStyle : ''">
      <input v-if="data.hideIgnoreCase" type="hidden" :name="data.name + '_ic'" :value="data.ignCase ? 'Y' : ''"/>
      <input v-else type="checkbox" :name="data.name + '_ic'" value="Y" checked="data.ignCase"/>
    </span>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'

  export default {
    name: "VueTextFind",
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
          console.log('vue-text : ', this.storeData)
          this.$store.dispatch('data/addDataToExample', this.storeData)
        }
      }
    },
    created() {
      if (this.updateStore) {
        console.log('vue-text : ', this.storeData)
        this.$store.dispatch('data/addDataToExample', this.storeData)
      }
    }
  }
</script>

<style scoped>

</style>
