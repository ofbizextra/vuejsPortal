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
        let data = this.props.attributes
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
        let linkUrl = this.getDataFromForm({formId: this.props.attributes.formName, key: 'linkUrl'})
        let url = linkUrl
        return this.$store.dispatch('backOfficeApi/doPost', {
          uri: url,
          params: this.getForm(this.props.attributes.formName)
        })
      },
      updateStore(updateArea) {
        let params = {}
        if (updateArea.parameterMap.length > 0) {
          params = updateArea.parameterMap
        } else {
          params = this.getForm(this.props.attributes.formName)
        }
        let data = {watcherName: this.getNestedObject(updateArea, ['areaId']), params: params}
        this.$store.dispatch('data/setWatcher', data)
      },
      setArea(updateArea) {
        this.$store.dispatch('ui/setArea', {areaId: updateArea.areaId, targetUrl: `/exampleapi/control/${updateArea.areaTarget}`, wait: this.$wait, params: updateArea.parameterMap})
      },
      resolveEvents() {
        if (this.data.hasOwnProperty('updateArea')) {
          for (let updateArea of this.data.updateArea) {
            switch (updateArea.eventType) {
              case 'submit':
                // do post
                this.post()
                break
              case 'updateStore':
                // do update store
                this.updateStore(updateArea)
                break
              case 'post-updateStore':
                // do post then update store
                this.post().then(() => {
                  this.updateStore(updateArea)
                }, (error) => {
                  console.log('Error during VueSubmitField.post() : ', error)
                })
                break
              case 'post-setArea':
                // do post then set area
                this.post().then(() => {
                  this.setArea(updateArea)
                }, (error) => {
                  console.log('Error during VueSubmitField.post() : ', error)
                })
                break
              default:
                // do nothing
                break
            }
          }
        } else {
          this.post()
        }
      }
    }
  }
</script>

<style scoped>

</style>
