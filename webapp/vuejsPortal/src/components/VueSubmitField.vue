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
      ...mapGetters({
        getForm: 'form/form',
        getDataFromForm: 'form/fieldInForm',
      }),
      data() {
        let data = this.props.attributes
        if (data.className || (data.alert && data.alert === true)) {
          data.class = data.className ? data.className : '' + ' ' + data.alert === true ? 'alert' : ''
        }
        return data
      },
      haveUpdateAreas() {
        return this.data.hasOwnProperty('updateAreas') && !this.data.updateAreas.empty
      },
      updateAreas() {
        return this.haveUpdateAreas ? this.data.updateAreas : []
      },
      form() {
        return this.getForm(this.formName)
      },
      formName() {
        return this.data.formName
      }
    },
    methods: {
      submit() {
        let linkUrl = this.getDataFromForm({formId: this.props.attributes.formName, key: 'linkUrl'})
        let url = linkUrl
        return this.$store.dispatch('backOfficeApi/doPost', {
          uri: url,
          params: this.form
        })
      },
      setWatcher(updateArea) {
        this.$store.dispatch('data/setWatcher', {
          watcherName: updateArea.areaId,
          params: (updateArea.hasOwnProperty('parameterMap') && Object.keys(updateArea.parameterMap).length > 0) ? updateArea.parameterMap : this.form
        })
      },
      setArea(updateArea) {
        return this.$store.dispatch('ui/setArea', {
          areaId: updateArea.areaId,
          targetUrl: `${this.$store.getters['backOfficeApi/currentApi']}/${updateArea.areaTarget}`,
          wait: this.$wait,
          params: updateArea.hasOwnProperty('parameterMap') ? updateArea.parameterMap : {}
        })
      },
      resolveEvents() {
        if (this.haveUpdateAreas) {
          let promiseList = []
          for (let updateArea of this.updateAreas) {
            Promise.all(promiseList).then(() => {
              switch (updateArea.eventType) {
                case 'submit':
                  // do post
                  promiseList.push(this.submit())
                  break
                case 'setWatcher':
                  // do update store
                  this.setWatcher(updateArea)
                  break
                case 'setArea':
                  // do post then set area
                  promiseList.push(this.setArea(updateArea))
                  break
                default:
                  // do nothing
                  break
              }
            })
          }
        } else {
          this.submit()
        }
      }
    }
  }
</script>

<style scoped>

</style>
