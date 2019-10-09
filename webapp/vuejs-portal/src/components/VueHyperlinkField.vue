<template>
  <div id="vue-hyperlink-field">
    <a
      v-if="haveUpdateAreas"
      :title="title"
      :description="description"
      :class="className"
      v-on:click.prevent="submit"
    >
      <img :src="imgSrc" :title="imgTitle" alt="" v-if="hasImg">
      {{description}}
    </a>
    <a
      v-else
      href="data.linkUrl"
      :title="title"
      :class="className"
    >
      <img :src="imgSrc" :title="imgTitle" alt="" v-if="hasImg">
      {{description}}
    </a>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import constantes from './../js/constantes'
  export default {
    name: "VueHyperlinkField",
    props: ['props'],
    data() {
      return {
        pointer: {
          entityName: this.getNestedObject(this.props, ['stPointer', 'stEntityName']),
          id: this.getNestedObject(this.props, ['stPointer', 'id']),
          attribute: this.getNestedObject(this.props, ['stPointer', 'field'])
        }
      }
    },
    computed: {
      data() {
        let data = this.props.attributes
        if (data.className || (data.alert && data.alert === true)) {
          data.class = data.className ? data.className : '' + ' ' + data.alert === true ? 'alert' : ''
        }
        if (data.hasOwnProperty('style')) {
          data.class = data.style
        }
        return data
      },
      attributes() {
        return this.props.hasOwnProperty('attributes') ? this.props.attributes : {}
      },
      getPointer() {
        return this.getData(this.pointer);
      },
      ...mapGetters({
        getData: 'data/entityRowAttribute',
        getForm: 'form/form',
        currentApi: 'backOfficeApi/currentApi'
      }),
      target() {
        return this.attributes.hasOwnProperty('target') ? this.attributes.target : null
      },
      targetWindow() {
        return this.attributes.hasOwnProperty('targetWindow') ? this.attributes.targetWindow : null
      },
      parameterMap() {
        return this.attributes.hasOwnProperty('parameterMap') ? this.attributes.parameterMap : {}
      },
      description() {
        return this.havePointer ? this.getPointer : this.attributes.hasOwnProperty('description') ? this.attributes.description : ''
      },
      title() {
        return this.havePointer ? this.pointer.attribute : this.attributes.hasOwnProperty('title') ? this.attributes.title : ''
      },
      havePointer() {
        return this.props.hasOwnProperty('stPointer')
      },
      haveUpdateAreas() {
        return this.attributes.hasOwnProperty('updateAreas')
      },
      updateAreas() {
        return this.haveUpdateAreas ? this.attributes.updateAreas : []
      },
      requestConfirmation() {
        return this.attributes.hasOwnProperty('requestConfirmation') ? this.attributes.requestConfirmation : false
      },
      confirmationMessage() {
        return this.attributes.hasOwnProperty('confirmationMessage') ? this.attributes.confirmationMessage : ''
      },
      hasImg() {
        return this.attributes.hasOwnProperty('imgSrc') && this.attributes.imgSrc !== ''
      },
      imgSrc() {
        return this.hasImg ? this.attributes.imgSrc : ''
      },
      imgTitle() {
        return this.attributes.hasOwnProperty('imgTitle') ? this.attributes.imgTitle : ''
      },
      className() {
        let data = this.attributes
        if (data.className || (data.alert && data.alert === true)) {
          return data.className ? data.className : '' + ' ' + data.alert === true ? 'alert' : ''
        } else if (data.hasOwnProperty('style')) {
          return data.style
        } else {
          return ''
        }
      }
    },
    methods: {
      resolveEvent(updateArea) {
        switch (updateArea.eventType) {
          case 'post':
            // do post
            return this.$store.dispatch('backOfficeApi/doPost', {
              uri: `${this.currentApi}/${updateArea.areaTarget}`,
              params: updateArea.hasOwnProperty('parameterMap') ? updateArea.parameterMap : {}
            })
          case 'setArea':
            // do setArea
            return this.$store.dispatch('ui/setArea', {
              areaId: updateArea.areaId,
              targetUrl: `${this.$store.getters['backOfficeApi/currentApi']}/${updateArea.areaTarget}`,
              wait: this.$wait,
              params: updateArea.parameterMap
            })
          case 'setWatcher':
            // do setWatcher
            this.$store.dispatch('data/setWatcher', {
              watcherName: updateArea.areaId,
              params: updateArea.hasOwnProperty('parameterMap') && Object.keys(updateArea.parameterMap).length > 0 ? updateArea.parameterMap : {}
            })
            return new Promise((resolve, reject) => {
              setTimeout(() => {
                resolve()
              },0)
            })
          case 'submit':
            // submit
            let form = this.$el.closest('form')
            form.action = updateArea.areaTarget
            form.submit()
            return new Promise((resolve, reject) => {
              setTimeout(() => {
                resolve()
              },0)
            })
          case 'setFieldInForm':
            // do setFieldInForm
            this.$store.dispatch('form/setFieldToForm', {
              formId: updateArea.areaId,
              key: updateArea.areaTarget,
              value: this.description
            })
            return new Promise((resolve, reject) => {
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
            return new Promise((resolve, reject) => {
              setTimeout(() => {
                resolve()
              },0)
            })
          default:
            // do nothing
            return new Promise((resolve, reject) => {
              setTimeout(() => {
                resolve()
              },0)
            })
        }
      },
      submit() {
        if (!this.requestConfirmation || confirm(this.confirmationMessage)) {
          this.updateAreas.reduce((accumulatorPromise, nextUpdateArea) => {
            return accumulatorPromise.then(() => {
              return this.resolveEvent(nextUpdateArea)
            })
          }, Promise.resolve())
        }

        // if (this.targetWindow) {
        //   this.$store.dispatch('ui/setArea', {areaId: this.targetWindow, targetUrl: '/exampleapi/control/' + this.target, wait: this.$wait, params: this.parameterMap})
        // } else {
        //   if (this.pointer.entityName !== '') {
        //     console.log('setWatcher by pointer')
        //     console.log('getPointer: ' + this.getPointer)
        //     this.$store.dispatch('data/setWatcher', {watcherName: this.target, params: {[this.pointer.attribute]: this.getPointer}})
        //   } else {
        //     console.log('setWatcher by data.value')
        //     console.log('data.value: ' + this.data.value.toString())
        //     this.$store.dispatch('data/setWatcher', {watcherName: this.getNestedObject(this.data, ['updateArea']), params: {exampleId: this.data.value}})
        //   }
        // }
      }
    },
    mounted() {
    },
    watch: {
      props: function () {
        this.pointer = {
          entityName: this.getNestedObject(this.props, ['stPointer', 'stEntityName']),
          id: this.getNestedObject(this.props, ['stPointer', 'id']),
          attribute: this.getNestedObject(this.props, ['stPointer', 'field'])
        }
      }
    }
  }
</script>

<style scoped>

</style>
