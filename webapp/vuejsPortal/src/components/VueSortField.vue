<template>
  <div id="vue-sort-field">
    <a :class="data.style" href="linkUrl" v-on:click.prevent="sort" v-bind:title="data.tooltip">{{data.title}}</a>
  </div>
</template>

<script>
  export default {
    name: "VueSortField",
    props: ['props', 'updateStore'],
    data() {
      return {
      }
    },
    computed: {
      data() {
        let data = this.props.attributes
        delete data.value
        return data
      }
    },
    methods: {
      sort() {
        if (this.data.hasOwnProperty('onPaginateUpdateAreas')) {
          this.data.onPaginateUpdateAreas.forEach((area) => {
            this.$store.dispatch('data/setWatcherAttributes', {
              watcherName: area.areaId,
              params: {
                orderBy: this.data.entityField,
                sortField: this.data.entityField
              }
            })
          })
        }
      }
    }
  }
</script>

<style scoped>

</style>
