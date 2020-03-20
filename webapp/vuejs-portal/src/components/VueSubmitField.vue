<template>
  <v-flex text-center vue-component="vue-submit-field" class="ma-2">
    <v-btn
        raised
        dark
        color="primary"
        type="submit"
        :label="title"
        v-on:click.prevent="resolveEvents">{{title}}
    </v-btn>
  </v-flex>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "VueSubmitField",
    props: ['props'],
    computed: {
      ...mapGetters({
        getForm: 'form/form',
        getDataFromForm: 'form/fieldInForm',
        formValidate: 'form/formValidate'
      }),
      haveUpdateAreas() {
        return this.props.attributes.hasOwnProperty('updateAreas') && !this.props.attributes.updateAreas.empty
      },
      form() {
        return this.getForm(this.formName)
      },
      formName() {
        return this.props.attributes.hasOwnProperty('formName') ? this.props.attributes.formName : ''
      },
      title() {
        return this.props.attributes.hasOwnProperty('title') ? this.props.attributes.title : ''
      },
      updateAreas() {
        return this.haveUpdateAreas ? this.props.attributes.updateAreas : []
      }
    },
    methods: {
      resolveEvent(updateArea) {
        switch (updateArea.eventType) {
          case 'closeModal':
            if (updateArea.hasOwnProperty('areaId') && updateArea.areaId !== '') {
              this.$store.dispatch('ui/setDialogStatus', {
                dialogId: updateArea.areaId,
                dialogStatus: false
              })
            } else {
              this.$store.dispatch('ui/closeAllDialogs')
            }
            return new Promise((resolve) => {
              setTimeout(() => {
                resolve()
              }, 0)
            })
          case 'collapse':
            switch (updateArea.areaTarget) {
              case 'collapse':
                this.$store.dispatch('ui/setCollapsibleStatus', {
                  areaId: updateArea.areaId,
                  areaTarget: true
                })
                break
              case 'expand':
                this.$store.dispatch('ui/setCollapsibleStatus', {
                  areaId: updateArea.areaId,
                  areaTarget: false
                })
                break
              case 'toggle':
                this.$store.dispatch('ui/setCollapsibleStatus', {
                  areaId: updateArea.areaId,
                  areaTarget: !this.$store.getters['ui/collapsibleStatus'](updateArea.areaId)
                })
                break
              default:
                this.$store.dispatch('ui/setCollapsibleStatus', {
                  areaId: updateArea.areaId,
                  areaTarget: !this.$store.getters['ui/collapsibleStatus'](updateArea.areaId)
                })
                break
            }
            return new Promise((resolve) => {
              setTimeout(() => {
                resolve()
              }, 0)
            })
          case 'setArea':
            return this.setArea(updateArea)
          case 'setWatcher':
            this.setWatcher(updateArea)
            return new Promise((resolve) => {
              setTimeout(() => {
                resolve()
              }, 0)
            })
          case 'submit':
            return this.submit()
          default:
            return new Promise((resolve) => {
              setTimeout(() => {
                resolve()
              }, 0)
            })
        }
      },
      resolveEvents() {
        if (this.formValidate(this.props.attributes.formName)()) {
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
      },
      setArea(updateArea) {
        return this.$store.dispatch('ui/setArea', {
          areaId: updateArea.areaId,
          targetUrl: `${this.$store.getters['backOfficeApi/currentApi']}/${updateArea.areaTarget}`,
          wait: this.$wait,
          params: (updateArea.hasOwnProperty('parameterMap') && Object.keys(updateArea.parameterMap).length > 0) ? updateArea.parameterMap : this.form
        })
      },
      setWatcher(updateArea) {
        this.$store.dispatch('data/setWatcher', {
          watcherName: updateArea.areaId,
          params: (updateArea.hasOwnProperty('parameterMap') && Object.keys(updateArea.parameterMap).length > 0) ? updateArea.parameterMap : this.form
        })
      },
      submit() {
        return this.$store.dispatch('backOfficeApi/doPost', {
          uri: this.getDataFromForm({formId: this.props.attributes.formName, key: 'linkUrl'}),
          params: this.form
        })
      }
    }
  }
</script>

<style scoped>

</style>
