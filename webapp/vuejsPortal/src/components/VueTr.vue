<template>
  <tr id="vue-tr" v-bind="data">
    <div
      v-for="component in props.children"
      v-if="component.type === 'element' && (component.tagName.includes('vue-'))"
      v-bind:is="component.tagName"
      :props="component"
      :updateStore="updateStore">
    </div>
  </tr>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "VueTr",
    props: ['props', 'updateStore'],
    data() {
      return {}
    },
    computed: {
      data() {
        let data = this.parseProps()
        delete data['value']
        return data
      }
    }
  }
</script>

<style scoped>

</style>
