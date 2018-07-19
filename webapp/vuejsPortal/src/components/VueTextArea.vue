<template>
  <div id="vue-text-area">
    <textarea v-bind="data"></textarea>
  </div>
</template>

<script>
  export default {
    name: "VueTextArea",
    data() {
      return {
        data: {}
      }
    },
    props: ['props']
  }
</script>

<style scoped>

</style>
