<template>
  <div id="vue-hyperlink-field">
    <v-btn outlined
           :x-small="!haveIcon"
           :small="haveIcon"
           color="primary"
           v-if="haveUpdateAreas"
           :class="className"
           v-on:click.prevent="submit"
           :icon="haveIcon"
    >
      <v-tooltip bottom>
        <template v-slot:activator="{ on }">
          <v-icon :id="src" v-if="haveIcon" v-on="on">{{getIcon(src)}}</v-icon>
        </template>
        <span>{{imgTitle}}</span>
      </v-tooltip>
      <img :src="src" :title="imgTitle" alt="" v-if="haveImage"/>
      {{description}}
    </v-btn>
    <router-link
        v-else-if="linkType === 'auto' && urlMode === 'intra-app'"
        v-bind:id="description + '_link'"
        :class="className"
        :to="{path: routerLink, query: parameterMap}"
    >
      <v-tooltip bottom>
        <template v-slot:activator="{ on }">
          <v-icon :id="src" v-if="haveIcon" v-on="on">{{getIcon(src)}}</v-icon>
        </template>
        <span>{{imgTitle}}</span>
      </v-tooltip>
      <img :src="src" :title="imgTitle" alt="" v-if="haveImage"/>
      <span class="font-weight-regular">
            {{description}}
        </span>
    </router-link>
    <v-btn outlined
           :x-small="!haveIcon"
           :small="haveIcon"
           color="primary"
           v-else
           :href="target"
           :class="className"
           :icon="haveIcon"
    >
      <v-tooltip bottom>
        <template v-slot:activator="{ on }">
          <v-icon :id="src" v-if="haveIcon" v-on="on">{{getIcon(src)}}</v-icon>
        </template>
        <span>{{imgTitle}}</span>
      </v-tooltip>
      <img :src="src" :title="imgTitle" alt="" v-if="haveImage">
      {{description}}
    </v-btn>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  import icons from '../js/icons'

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
      ...mapGetters({
        getData: 'data/entityRowAttribute',
        getForm: 'form/form',
        currentApi: 'backOfficeApi/currentApi'
      }),
      className() {
        let data = this.props.attributes
        if (data.className || (data.alert && data.alert === true)) {
          return data.className ? data.className : '' + ' ' + data.alert === true ? 'alert' : ''
        } else if (data.hasOwnProperty('style')) {
          return data.style
        } else {
          return ''
        }
      },
      confirmationMessage() {
        return this.props.attributes.hasOwnProperty('confirmationMessage') ? this.props.attributes.confirmationMessage : ''
      },
      description() {
        return this.havePointer ? this.getPointer : this.props.attributes.hasOwnProperty('description') ? this.props.attributes.description : ''
      },
      getPointer() {
        return this.getData(this.pointer)
      },
      haveIcon() {
        return this.props.attributes.hasOwnProperty('imgSrc') && this.props.attributes.imgSrc.startsWith('mdi-')
      },
      haveImage() {
        return this.props.attributes.hasOwnProperty('imgSrc') && !this.props.attributes.imgSrc.startsWith('mdi-')
      },
      havePointer() {
        return this.props.hasOwnProperty('stPointer')
      },
      haveUpdateAreas() {
        return this.props.attributes.hasOwnProperty('updateAreas')
      },
      imgTitle() {
        return this.props.attributes.hasOwnProperty('imgTitle') ? this.props.attributes.imgTitle : ''
      },
      linkType() {
        return this.props.attributes.hasOwnProperty('linkType') ? this.props.attributes.linkType : ''
      },
      parameterMap() {
        return this.props.attributes.hasOwnProperty('parameterMap') ? this.props.attributes.parameterMap : {}
      },
      requestConfirmation() {
        return this.props.attributes.hasOwnProperty('requestConfirmation') ? this.props.attributes.requestConfirmation : false
      },
      routerLink() {
        if (this.target === 'showPortalPage') {
          return `/portalPage/${this.parameterMap.portalPageId}`
        } else {
          return `/screen/${this.target}`
        }
      },
      src() {
        return this.props.attributes.hasOwnProperty('imgSrc') ? this.props.attributes.imgSrc : ''
      },
      target() {
        return this.props.attributes.hasOwnProperty('target') ? this.parseUrl(this.props.attributes.target, this.parameterMap) : null
      },
      targetWindow() {
        return this.props.attributes.hasOwnProperty('targetWindow') ? this.props.attributes.targetWindow : null
      },
      title() {
        return this.havePointer ? this.pointer.attribute : this.props.attributes.hasOwnProperty('title') ? this.props.attributes.title : ''
      },
      updateAreas() {
        return this.haveUpdateAreas ? this.props.attributes.updateAreas : []
      },
      urlMode() {
        return this.props.attributes.hasOwnProperty('urlMode') ? this.props.attributes.urlMode : 'intra-app'
      }
    },
    methods: {
      getIcon(icon) {
        return icons.hasOwnProperty(icon) ? icons[icon] : null
      },
      resolveEvent(updateArea) {
        switch (updateArea.eventType) {
          case 'post':
            return this.$store.dispatch('backOfficeApi/doRequest', {
              uri: `${this.currentApi}/${this.parseUrl(updateArea.areaTarget, updateArea.parameterMap)}`,
              mode: 'post',
              params: updateArea.hasOwnProperty('parameterMap') ? updateArea.parameterMap : {}
            })
          case 'setArea':
            return this.$store.dispatch('ui/setArea', {
              areaId: updateArea.areaId,
              targetUrl: `${this.currentApi}/${this.parseUrl(updateArea.areaTarget, updateArea.parameterMap)}`,
              params: updateArea.parameterMap,
              mode: Object.keys(updateArea.parameterMap).length > 0 ? 'post' : 'get'
            })
          case 'setWatcher':
            this.$store.dispatch('data/setWatcher', {
              watcherName: updateArea.areaId,
              params: updateArea.hasOwnProperty('parameterMap') && Object.keys(updateArea.parameterMap).length > 0 ? updateArea.parameterMap : {}
            })
            return new Promise((resolve) => {
              setTimeout(() => {
                resolve()
              }, 0)
            })
          case 'submit':
            this.$el.closest('form').action = updateArea.areaTarget
            this.$el.closest('form').submit()
            return new Promise((resolve) => {
              setTimeout(() => {
                resolve()
              }, 0)
            })
          case 'setFieldInForm':
            this.$store.dispatch('form/setFieldToForm', {
              formId: updateArea.areaId,
              key: updateArea.areaTarget,
              value: this.description
            })
            return new Promise((resolve) => {
              setTimeout(() => {
                resolve()
              }, 0)
            })
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
          default:
            return new Promise((resolve) => {
              setTimeout(() => {
                resolve()
              }, 0)
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
      }
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
