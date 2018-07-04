<template>
  <div id="vue-field">
    <h2>vue-field</h2>
    <h3>attributes :</h3>
    <ul>
      <li v-for="attribute in Object.keys(props.$)" :key="attribute">{{attribute + ' : ' + props.$[attribute]}}</li>
    </ul>
    <h3>content</h3>
    <ul>
      <li v-for="module in Object.keys(props)" :key="module" v-if="module !== '$'">
        {{module}}
      </li>
    </ul>
  </div>
</template>

<script>
  export default {
    name: "VueField",
    props: ['props']
  }
</script>

<style scoped>

</style>
