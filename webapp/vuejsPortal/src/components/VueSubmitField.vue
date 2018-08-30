<template>
  <div id="vue-submit-field">
    <button :value="data.title" v-bind="data" v-on:click.prevent="post">{{data.title}}</button>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import constantes from '../js/constantes'
  import queryString from 'query-string'

  export default {
    name: "VueSubmitField",
    props: ['props'],
    data() {
      return {}
    },
    computed: {
      data() {
        return this.props.attributes
      },
      ...mapGetters({
        getForm: 'form/form',
        getDataFromForm: 'form/fieldInForm'
      })
    },
    methods: {
      post() {
        console.log('post ...')
        let linkUrl = this.getDataFromForm({formId: this.props.attributes.formName, key: 'linkUrl'})
        let url = constantes.hostUrl + linkUrl
        this.$http.post(
          url,
          queryString.stringify(this.getForm(this.props.attributes.formName)),
          {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).then(
          response => {
            console.log(response)

          }, error => {
            console.log(error)
          }
        )
      }
    }
  }
</script>

<style scoped>

</style>
