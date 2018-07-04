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
        <div v-for="iteration in Object.keys(props[module])" :key="iteration" v-bind:is="'vue-' + module" v-bind:props="props[module][iteration]"></div>
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
