<template>
  <div id="vue-submit">
    <button :value="data.title" v-bind="data" v-on:click="post">{{data.title}}</button>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import constantes from '../js/constantes'
  import queryString from 'query-string'

  export default {
    name: "VueSubmit",
    props: ['props'],
    data() {
      return {}
    },
    computed: {
      data() {
        return this.parseProps()
      },
      ...mapGetters({
        getForm: 'form/form',
        getDataFromForm: 'form/fieldInForm'
      })
    },
    methods: {
      post() {
        console.log('post ...')
        let url = constantes.hostUrl + this.getDataFromForm({formId: this.parseProps().formName, key: 'linkUrl'})
        this.$http.post(
          url,
          queryString.stringify(this.getForm(this.parseProps().formName)),
          {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).then(
          response => console.log(response),
          error => console.log(error)
        )
      }
    }
  }
</script>

<style scoped>

</style>
