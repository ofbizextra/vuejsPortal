<template>
  <div id="vue-drop-down">
    <select>
    </select>
  </div>
</template>

<script>
  import constantes from './../js/constantes'
  import queryString from 'query-string'

  export default {
    name: "VueDropDown",
    data () {
      return {
        options: []
      }
    },
    props: [
      'props',
      'name'
    ],
    mounted () {
      console.log(decodeURIComponent(document.cookie))
      this.$http.post(
        constantes.ApiUrl + constantes.listForDropDown.path,
        queryString.stringify({
          entityName: this.props['entity-options'][0].$['entity-name'],
          keyFieldName: this.props['entity-options'][0].$['key-field-name'],
          orderByFieldName: this.props['entity-options'][0]['entity-order-by'] ? this.props['entity-options'][0]['entity-order-by'][0].$['field-name'] : ''
        }),
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
      ).then(response => {
        console.log(response)
        this.option = response.body.dropDownList
      }, error => {
        console.log(error)
      })
    }
  }
</script>

<style scoped>

</style>
