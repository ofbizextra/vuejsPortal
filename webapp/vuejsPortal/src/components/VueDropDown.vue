<template>
  <div id="vue-drop-down">
    <select v-bind="data">
      <option value=""></option>
      <vue-option v-for="option in props.children" :key="option.attributes[0].value" v-if="option.type === 'element' && option.tagName === 'vue-option'" :props="option">
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
