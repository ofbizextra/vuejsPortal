<template>
  <div id="vue-submit-field">
    <button :value="data.title" v-bind="data" v-on:click.prevent="resolveEvents">{{data.title}}</button>
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
        let data =  this.props.attributes
        if (data.className || (data.alert && data.alert === true)) {
          data.class = data.className ? data.className : '' + ' ' + data.alert === true ? 'alert' : ''
        }
        return data
      },
      ...mapGetters({
        getForm: 'form/form',
        getDataFromForm: 'form/fieldInForm',
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
      },
      updateStore() {
        let data = {watcherName: this.getNestedObject(this.data, ['updateArea', 0, 'areaId']), params: this.getForm(this.props.attributes.formName)}
        console.log('data to setWatcher : ', data)
        this.$store.dispatch('data/setWatcher', data)
      },
      resolveEvents() {
        if (this.data['updateArea']) {
          console.log('trigger updateStore from submit button ...')
          this.updateStore()
          console.log('updateStore ended.')
        } else {
          console.log('trigger post from submit button ...')
          this.post()
          console.log('post ended.')
        }
      }
    }
  }
</script>

<style scoped>

</style>
