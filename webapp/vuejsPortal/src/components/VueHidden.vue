<template>
  <div id="vue-hidden">
    <h3>vue-hidden</h3>
    <h3>attributes :</h3>
    <ul>
      <li v-for="attribute in Object.keys(props.$)" :key="attribute">{{attribute + ' : ' + props.$[attribute]}}</li>
    </ul>
  </div>
</template>

<script>
  export default {
    name: "VueHidden",
    props: ['props']
  }
</script>

<style scoped>

</style>
