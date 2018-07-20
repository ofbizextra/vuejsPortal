<template>
  <div id="vue-drop-down">
    <select v-bind="data">
      <option value="" v-if="data.allowEmpty || (data.allowEmpty && props.children.length === 0)"></option>
      <option v-if="data.firstInList && data.currentValue && !data.multiple" selected="selected" :value="data.currentValue" >{{data.explicitDescription}}</option>
      <vue-option
        v-for="option in props.children"
        :key="option.attributes[0].value"
        v-if="option.type === 'element' &&
        option.tagName === 'vue-option'"
        :props="option"
        :selected="option.attributes[0].value === data.currentValue">
      </vue-option>
    </select>
  </div>
</template>

<script>
  export default {
    name: "VueDropDown",
    data () {
      return {
        data: {}
      }
    },
    props: [
      'props'
    ]
  }
</script>

<style scoped>

</style>
