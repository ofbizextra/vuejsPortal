<template>
  <div id="list-example-layer">
    <h2>// listExampleLayer</h2>
    <div v-for="table in parsedData" v-if="table.type === 'element' && table.tagName === 'table'" is="table">
      <div v-for="thead in table.children" v-if="thead.type === 'element' && thead.tagName === 'thead'" is="thead">
        <div v-for="row in thead.children" v-if="row.type === 'element' && row.tagName === 'tr'" is="tr">
          <div v-for="cell in row.children" v-if="cell.type === 'element' && cell.tagName === 'td'" is="td">
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
  import queryString from 'query-string'

  export default {
    name: "ListExampleLayer",
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
      this.$http.post(constantes.apiUrl + constantes.listExampleLayer.path,
        queryString.stringify({
          listOtherElements: 'items',
          searchButton: 'Search'
        }),
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).then(response => {
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
