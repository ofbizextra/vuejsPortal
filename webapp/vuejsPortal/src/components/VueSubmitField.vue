<template>
  <div id="vue-submit-field">
    <input type="submit" :value="data.title" v-bind="data" v-on:click.prevent="resolveEvents"/>
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
        this.$wait.start('updating ' + this.props.attributes.formName)
        let linkUrl = this.getDataFromForm({formId: this.props.attributes.formName, key: 'linkUrl'})
        let url = linkUrl
        this.$http.post(
          url,
          queryString.stringify(this.getForm(this.props.attributes.formName)),
          {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).then(
          response => {
            console.log(response)
            if (this.getNestedObject(response, ['body', '_EVENT_MESSAGE_']) === 'Example Entity updated successfully') {
              // console.log('update successful => trigger store update')
              // this.$store.dispatch('data/setEntityRow', {
              //   entityName: 'Example',
              //   primaryKey: this.getForm(this.props.attributes.formName).exampleId,
              //   data: this.getForm(this.props.attributes.formName)
              // }).then(response => {
              //   console.log(response)
              //   console.log('Store updated successfully')
              //   this.$nextTick(() => {
              //     this.$wait.end('updating ' + this.props.attributes.formName)
              //   })
              // }, error => {
              //   console.log(error)
              //   this.$nextTick(() => {
              //     this.$wait.end('updating ' + this.props.attributes.formName)
              //   })
              // })
              this.$nextTick(() => {
                this.$wait.end('updating ' + this.props.attributes.formName)
              })
            } else {
              console.log('Store don\'t need to be updated')
              this.$nextTick(() => {
                this.$wait.end('updating ' + this.props.attributes.formName)
              })
            }
          }, error => {
            console.log(error)
            this.$nextTick(() => {
              this.$wait.end('updating ' + this.props.attributes.formName)
            })
          }
        )
      },
      updateStore() {
        this.$wait.start('updating ' + this.props.attributes.formName)
        this.$nextTick(() => {
          let areasToUpdate = [ ...this.data.updateArea]
          console.log('areasToUpdate', areasToUpdate)
          areasToUpdate.forEach((areaToUpdate) => {
            console.log('areaToUpdate : ', areaToUpdate)
            let params = {}
            if (areaToUpdate.parameterList.length > 0) {
              let form = this.getForm(this.props.attributes.formName)
              areaToUpdate.parameterList.forEach((parameter) => {
                params[parameter.name] = form[parameter.name]
              })
            } else {
              params = this.getForm(this.props.attributes.formName)
            }
            console.log('params : ' + params)
            let data = {watcherName: this.getNestedObject(areaToUpdate, ['areaId']), params: params}
            console.log('data to setWatcher : ', data)
            this.$store.dispatch('data/setWatcher', data)
          })
          this.$nextTick(() => {
            this.$wait.end('updating ' + this.props.attributes.formName)
          })
        })
      },
      resolveEvents() {
        if (this.data['updateArea']) {
          if (this.getNestedObject(this.data, ['updateArea', 0, 'areaTarget']) === 'p') {
            console.log('trigger post from submit button ...')
            this.post()
            console.log('post ended.')
          }
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
