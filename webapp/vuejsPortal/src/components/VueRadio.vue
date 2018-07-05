<template>
  <div id="vue-radio">
    <span v-for="option in Object.keys(props.option)" :key="option">
      <input :value="props.option[option].$.key" type="radio" :name="name">
      {{props.option[option].$.key}}
    </span>
  </div>
</template>

<script>
  export default {
    name: "VueRadio",
    props: [
      'props',
      'name'
    ]
  }
</script>

<style scoped>

</style>
