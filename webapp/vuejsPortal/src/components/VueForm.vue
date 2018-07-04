<template>
  <div id="vue-form">
    <h2>vue-form</h2>
    <h3>attributes :</h3>
    <ul>
      <li v-for="attribute in Object.keys(props.$)">{{attribute + ' : ' + props.$[attribute]}}</li>
    </ul>
    <h3>Data</h3>
    <ul>
      <li v-for="module in Object.keys(props)" v-if="module !== '$'">
        {{module}}
        <ul>
          <li v-for="subModule in Object.keys(props[module])">
            {{subModule + ' : '}}
            <ul>
              <li v-for="subSubModule in Object.keys(props[module][subModule])">
                {{subSubModule}}
              </li>
            </ul>
          </li>
        </ul>
      </li>
    </ul>
  </div>
</template>

<script>
  export default {
    name: "VueForm",
    props: ['props']
  }
</script>

<style scoped>

</style>
