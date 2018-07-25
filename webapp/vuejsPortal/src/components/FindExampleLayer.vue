<template>
  <div id="find-example-layer">
    <h2>// findExampleLayer</h2>
    <div v-for="form in parsedData" v-if="form.type === 'element' && form.tagName === 'vue-form'" is="form">
      <div v-for="table in form.children" v-if="table.type === 'element' && table.tagName === 'vue-table'" is="table">
        <div v-for="row in table.children" v-if="row.type === 'element' && row.tagName === 'vue-tr'" is="tr">
          <div v-for="cell in row.children" v-if="cell.type === 'element' && cell.tagName === 'vue-td'" is="td">
            <div v-for="component in cell.children" v-if="component.type === 'element' && (component.tagName.includes('vue-'))" v-bind:is="component.tagName" :props="component" :updateStore="updateStore">

            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import constantes from './../js/constantes'
  import {parse} from 'himalaya'

  export default {
    name: "FindExampleLayer",
    data () {
      return {
        data: '',
        parsedData: {}
      }
    },
    computed: {
      updateStore() {
        return !!this.$route.params['id']
      }
    },
    mounted () {
      console.log('findExampleLayer : Mounted')
      this.$http.get(constantes.apiUrl + constantes.findExampleLayer.path).then(response => {
        this.data = response.body
        this.parsedData = parse(this.data)
      }, error => {
        console.log(error.body)
      })
    }
  }
</script>

<style scoped>

</style>
