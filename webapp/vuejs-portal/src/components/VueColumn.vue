<template>
  <v-col v-if="colSimple" id="vue-column" class="ma-0 pa-0">
    <div v-for="(component, id) in children" :key="id" v-bind:is="constants.components[component.name]" :props="component">
    </div>
  </v-col>
  <v-col v-else id="vue-column" class="ma-0 pa-0" :cols="grid.cols" :sm="grid.sm" :md="grid.md" :lg="grid.lg" :xl="grid.lg">
    <div v-for="(component, id) in children" :key="id" v-bind:is="constants.components[component.name]" :props="component">
    </div>
  </v-col>
</template>

<script>
  import constants from '../js/constants'

  export default {
    name: "VueColumn",
    props: ['props'],
    data() {
      return {
        constants: constants
      }
    },
    computed: {
      children() {
        return this.props.hasOwnProperty('children') ? this.props.children : []
      },
      style() {
        return this.props.attributes.hasOwnProperty('style') ? this.props.attributes.style : ''
      },
      grid() {
        let gridVal = {cols:'',
                         sm:'',
                         md:'',
                         lg:'',
                         xl:''}
        if (this.style)  {
          this.style.split(' ').forEach(val => {
            let tmp = val.split('-')
            gridVal[tmp[0]] = tmp[1]
          })
        }
        if (gridVal.cols || gridVal.sm || gridVal.md || gridVal.lg || gridVal.xl) {
            if (! gridVal.cols) gridVal.cols = '12'
            if (! gridVal.sm) gridVal.sm = gridVal.cols
            if (! gridVal.md) gridVal.md = gridVal.sm
            if (! gridVal.lg) gridVal.lg = gridVal.md
            if (! gridVal.xl) gridVal.xl = gridVal.lg
        }
        return gridVal
      },
      colSimple() {
          return ! this.grid.sm
      }
    }
  }
</script>

<style scoped>

</style>
