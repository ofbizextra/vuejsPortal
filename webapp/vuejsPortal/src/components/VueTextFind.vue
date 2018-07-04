<template>
  <div id="vue-text-find">
    <h2>vue-text-find</h2>
    <h3>attributes :</h3>
    <ul>
      <li v-for="attribute in Object.keys(props.$)" :key="attribute">{{attribute + ' : ' + props.$[attribute]}}</li>
    </ul>
  </div>
</template>

<script>
  export default {
    name: "VueTextFind",
    props: ['props']
  }
</script>

<style scoped>

</style>
