<template>
  <form id="vue-form">
    <table>
      <tbody>
        <vue-field v-for="field in Object.keys(props.field)" :key="field" v-bind:props="props.field[field]"/>
      </tbody>
    </table>
  </form>
</template>

<script>
  export default {
    name: "VueForm",
    props: ['props']
  }
</script>

<style scoped>

</style>
