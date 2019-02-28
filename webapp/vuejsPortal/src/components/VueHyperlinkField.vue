<template>
  <div id="vue-hyperlink-field">
    <a
      v-if="haveUpdateAreas"
      :title="title"
      v-on:click.prevent="submit"
      v-bind="data">{{description}}</a>
    <a
      v-else
      href="data.linkUrl"
      :title="title"
      v-bind="data">{{description}}</a>
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
      getPointer() {
        return this.getData(this.pointer);
      },
      ...mapGetters({
        getData: 'data/entityRowAttribute'
      }),
      target() {
        return this.props.attributes.hasOwnProperty('target') ? this.props.attributes.target : null
      },
      targetWindow() {
        return this.props.attributes.hasOwnProperty('targetWindow') ? this.props.attributes.targetWindow : null
      },
      parameterMap() {
        return this.props.attributes.hasOwnProperty('parameterMap') ? this.props.attributes.parameterMap : {}
      },
      description() {
        return this.havePointer ? this.getPointer : this.props.attributes.hasOwnProperty('description') ? this.props.attributes.description : ''
      },
      title() {
        return this.havePointer ? this.pointer.attribute : this.props.attributes.hasOwnProperty('title') ? this.props.attributes.title : ''
      },
      havePointer() {
        return this.props.hasOwnProperty('stPointer')
      },
      haveUpdateAreas() {
        return this.props.attributes.hasOwnProperty('updateAreas')
      },
      updateAreas() {
        return this.haveUpdateAreas ? this.props.attributes.updateAreas : []
      }
    },
    methods: {
      submit() {
        let promiseList = []
        for (let updateArea of this.updateAreas) {
          Promise.all(promiseList).then(() => {
            switch (updateArea.eventType) {
              case 'post':
                // do post
                promiseList.push(this.$store.dispatch('backOfficeApi/doPost', {
                  uri: `${constantes.apiUrl}/${updateArea.areaTarget}`,
                  params: updateArea.hasOwnProperty('parameterMap') ? updateArea.parameterMap : {}
                }))
                break
              case 'setArea':
                // do setArea
                promiseList.push(this.$store.dispatch('ui/setArea', {
                  areaId: updateArea.areaId,
                  targetUrl: `/exampleapi/control/${updateArea.areaTarget}`,
                  wait: this.$wait,
                  params: updateArea.parameterMap
                }))
                break
              case 'setWatcher':
                // do setWatcher
                this.$store.dispatch('data/setWatcher', {
                  watcherName: updateArea.areaId,
                  params: updateArea.hasOwnProperty('parameterMap') && Object.keys(updateArea.parameterMap).length > 0 ? updateArea.parameterMap : {}
                })
                break
              case 'redirect':
                // todo: redirect
                break
              case 'submit':
                // submit
                let form = this.$el.closest('form')
                form.action = updateArea.areaTarget
                form.submit()
                break
              default:
                // do nothing
                break
            }
          })
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
