<template>
  <div id="vue-submit-field">
    <input type="submit" :value="data.title" v-bind="data" v-on:click.prevent="resolveEvents"/>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

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
        return this.$store.dispatch('backOfficeApi/doPost', {
          uri: this.getDataFromForm({formId: this.props.attributes.formName, key: 'linkUrl'}),
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
      resolveEvent(updateArea) {
        switch (updateArea.eventType) {
          case 'submit':
            // do post
            return this.submit()
          case 'setWatcher':
            // do update store
            this.setWatcher(updateArea)
            return new Promise((resolve) => {
              setTimeout(() => {
                resolve()
              },0)
            })
          case 'setArea':
            // do post then set area
            return this.setArea(updateArea)
          case 'closeModal':
            // do closeModal
            this.$modal.hide(updateArea.areaId + '_modal')
            return new Promise((resolve) => {
              setTimeout(() => {
                resolve()
              },0)
            })
          case 'collapse':
            switch (updateArea.areaTarget) {
              case 'collapse':
                // collapse
                this.$store.dispatch('ui/setCollapsibleStatus', {
                  areaId: updateArea.areaId,
                  areaTarget: true
                })
                break
              case 'expand':
                // expand
                this.$store.dispatch('ui/setCollapsibleStatus', {
                  areaId: updateArea.areaId,
                  areaTarget: false
                })
                break
              case 'toggle':
                // toggle
                this.$store.dispatch('ui/setCollapsibleStatus', {
                  areaId: updateArea.areaId,
                  areaTarget: !this.$store.getters['ui/collapsibleStatus'](updateArea.areaId)
                })
                break
              default:
                // toggle
                this.$store.dispatch('ui/setCollapsibleStatus', {
                  areaId: updateArea.areaId,
                  areaTarget: !this.$store.getters['ui/collapsibleStatus'](updateArea.areaId)
                })
                break
            }
            return new Promise((resolve) => {
              setTimeout(() => {
                resolve()
              },0)
            })
          default:
            // do nothing
            return new Promise((resolve) => {
              setTimeout(() => {
                resolve()
              },0)
            })
        }
      },
      resolveEvents() {
        if (this.haveUpdateAreas) {
          this.updateAreas.reduce((accumulatorPromise, nextUpdateArea) => {
            return accumulatorPromise.then(() => {
              return this.resolveEvent(nextUpdateArea)
            })
          }, Promise.resolve())
        } else {
          this.submit()
        }
      }
    }
  }
</script>

<style scoped>

</style>
