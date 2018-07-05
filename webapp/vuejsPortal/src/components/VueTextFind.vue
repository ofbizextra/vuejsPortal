<template>
  <div id="vue-text-find">
    <select class="selectBox">
      <option value="equals">egal</option>
      <option value="like">debute</option>
      <option value="contain" selected="selected">contient</option>
      <option value="empty">est vide</option>
      <option value="notEqual">Diff.</option>
    </select>
    <input type="text" size="25"/>
    <input type="checkbox" value="Y" checked="checked"/>
    (Maj/min)
  </div>
</template>

<script>
  export default {
    name: "VueTextFind",
    props: [
      'props',
      'name'
    ]
  }
</script>

<style scoped>

</style>
