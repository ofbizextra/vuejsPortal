<template>
  <div id="vue-radio">
    <h2>vue-radio</h2>
    <h3>attributes :</h3>
    <ul>
      <li v-for="attribute in Object.keys(props.$)" :key="attribute">{{attribute + ' : ' + props.$[attribute]}}</li>
    </ul>
  </div>
</template>

<script>
  export default {
    name: "VueRadio",
    props: ['props']
  }
</script>

<style scoped>

</style>
