<template>
  <div id="vue-sort-field">
    <a :class="data.style" :href="linkUrl" v-on:click.prevent="sort" v-bind:title="data.tooltip">{{data.title}}</a>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "VueSortField",
    props: ['props', 'updateStore'],
    data() {
      return {}
    },
    computed: {
      ...mapGetters({
        watcher: 'data/watcher'
      }),
      data() {
        let data = this.props.attributes
        delete data.value
        return data
      },
      paginateTarget() {
        return this.data.hasOwnProperty('paginateTarget') ? this.data.paginateTarget : null
      },
      entityField() {
        return this.data.hasOwnProperty('entityField') ? this.data.entityField : null
      }
    },
    methods: {
      sort() {
        if (this.paginateTarget && this.entityField) {
          let currentWatcher = this.watcher(this.paginateTarget)
          this.$store.dispatch('data/setWatcherAttributes', {
            watcherName: this.paginateTarget,
            params: {
              orderBy: this.entityField,
              sortField: currentWatcher.hasOwnProperty('sortField') && currentWatcher.sortField === this.entityField ? '-' + this.entityField : this.entityField
            }
          })
        }
      }
    }
  }
</script>

<style scoped>

</style>
