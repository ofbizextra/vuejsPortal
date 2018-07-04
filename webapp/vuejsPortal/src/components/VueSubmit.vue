<template>
  <div id="vue-submit">
    <h2>vue-submit</h2>
    <h3>attributes :</h3>
    <ul>
      <li v-for="attribute in Object.keys(props.$)" :key="attribute">{{attribute + ' : ' + props.$[attribute]}}</li>
    </ul>
  </div>
</template>

<script>
  export default {
    name: "VueSubmit",
    props: ['props']
  }
</script>

<style scoped>

</style>
