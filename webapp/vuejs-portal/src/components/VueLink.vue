<template>
  <div id="vue-link">
    <router-link
        v-if="linkType === 'auto' && urlMode === 'intra-app'"
        v-bind:id="id + '_link'"
        :data-dialog-params="params"
        :data-dialog-width="width"
        :data-dialog-height="height"
        :data-dialog-title="text"
        :class="style"
        :to="{path: routerLink, query: parameterMap}"
    >
      <v-tooltip bottom>
        <template v-slot:activator="{ on }">
          <v-icon v-if="haveIcon" v-on="on" :id="src">{{getIcon(src)}}</v-icon>
        </template>
        <span>{{imgTitle}}</span>
      </v-tooltip>
      <img :src="src" :title="imgTitle" alt="" v-if="haveImage"/>
      <span class="font-weight-regular">
        {{text}}
      </span>
    </router-link>
    <a
        v-else-if="linkType === 'auto' && urlmode === 'inter-app'"
        v-bind:id="id + '_link'"
        :data-dialog-params="params"
        :data-dialog-width="width"
        :data-dialog-height="height"
        :data-dialog-title="text"
        :class="style"
        :href="href"
    >
      <v-tooltip bottom>
        <template v-slot:activator="{ on }">
          <v-icon v-if="haveIcon" v-on="on" :id="src">{{getIcon(src)}}</v-icon>
        </template>
        <span>{{imgTitle}}</span>
      </v-tooltip>
      <img :src="src" :title="imgTitle" alt="" v-if="haveImage"/>
      <span class="font-weight-regular">
        {{text}}
      </span>
    </a>
    <span v-else :icon="haveIcon" @click="handleUpdate()">
      <v-tooltip bottom>
        <template v-slot:activator="{ on }">
          <v-icon v-if="haveIcon" v-on="on" :id="src">{{getIcon(src)}}</v-icon>
        </template>
        <span>{{imgTitle}}</span>
      </v-tooltip>
      <img :src="src" :title="imgTitle" alt="" v-if="haveImage"/>
      <span class="font-weight-regular">
        {{text}}
      </span>
    </span>
  </div>
</template>

<script>
  import constants from '../js/constants'
  import {mapGetters} from 'vuex'
  import icons from '../js/icons'

  export default {
    name: "VueLink",
    props: ['props', 'updateStore', 'inline', 'clickDisabled'],  // <== TODO-TRAING OH what is the rule of inline ?
    data() {
      return {
        constants: constants
      }
    },
    computed: {
      ...mapGetters({
        currentApi: 'backOfficeApi/currentApi'
      }),
      id() {
        return this.props.attributes.hasOwnProperty('id') ? this.props.attributes.id : ''
      },
      haveIcon() {
        return this.props.attributes.hasOwnProperty('img') && this.props.attributes.img.hasOwnProperty('src') && this.props.attributes.img.src.startsWith('mdi-')
      },
      haveImage() {
        return this.props.attributes.hasOwnProperty('img') && this.props.attributes.img.hasOwnProperty('src') && !this.props.attributes.img.src.startsWith('mdi-')
      },
      height() {
        return this.props.attributes.hasOwnProperty('height') ? this.props.attributes.height : ''
      },
      href() {
        if (this.linkType === 'hidden-form') {
          return ''
        }
        let href
        switch (this.urlMode) {
          case 'intra-app':
            href = this.constants.hostUrl + this.target
            break
          case 'inter-app':
            href = this.constants.hostUrl + this.target
            break
          case 'plain':
            href = this.target
            break
          default:
            href = ''
            break
        }
        return href
      },
      imgTitle() {
        return this.props.attributes.hasOwnProperty('img') && this.props.attributes.img.hasOwnProperty('title') ? this.props.attributes.img.title : ''
      },
      linkType() {
        return this.props.attributes.hasOwnProperty('linkType') ? this.props.attributes.linkType : ''
      },
      name() {
        return this.props.attributes.hasOwnProperty('name') ? this.props.attributes.name : ''
      },
      parameterMap() {
        return this.props.attributes.hasOwnProperty('parameterMap') ? this.props.attributes.parameterMap : {}
      },
      params() {
        if (this.uniqueItemName.length > 0) {
          return ""
        }
        let params = "{&quot;presentation&quot;:&quot;layer&quot; "
        for (let key in Object.keys(this.parameterMap)) {
          params += `,&quot;${key}&quot;: &quot;${this.parameterMap[key]}&quot;`
        }
        params += "}"
        return params
      },
      routerLink() {
        if (this.target === 'showPortalPage') {
          return `/portalPage/${this.parameterMap.portalPageId}`
        } else {
          return `/screen/${this.target}`
        }
      },
      src() {
        return this.props.attributes.hasOwnProperty('img') && this.props.attributes.img.hasOwnProperty('src') ? this.props.attributes.img.src : ''
      },
      style() {
        return this.props.attributes.hasOwnProperty('style') ? this.props.attributes.style : ''
      },
      text() {
        return this.props.attributes.hasOwnProperty('text') ? this.props.attributes.text : ''
      },
      targetWindow() {
        return this.props.attributes.hasOwnProperty('targetWindow') ? this.props.attributes.targetWindow : ''
      },
      target() {
        return this.props.attributes.hasOwnProperty('target') ? this.props.attributes.target : ''
      },
      uniqueItemName() {
        return this.props.attributes.hasOwnProperty('uniqueItemName') ? this.props.attributes.uniqueItemName : ''
      },
      urlMode() {
        if (this.props.attributes.hasOwnProperty('urlMode')) {
          return this.props.attributes.urlMode
        } else {
          return 'intra-app'
        }
      },
      width() {
        return this.props.attributes.hasOwnProperty('width') ? this.props.attributes.width : ''
      }
    },
    methods: {
      getIcon(icon) {
        return icons.hasOwnProperty(icon) ? icons[icon] : null
      },
      loadPortalPage() {
        this.$store.dispatch('ui/loadPortalPageDetail', {api: this.currentApi, params: this.parameterMap})
      },
      redirect() {
        if (this.props.attributes.hasOwnProperty('targetWindow') && this.props.attributes.hasOwnProperty('target')) {
          this.$store.dispatch('ui/setArea', {
            areaId: this.props.attributes.targetWindow,
            targetUrl: `${this.$store.getters['backOfficeApi/currentApi']}/${this.props.attributes.target}`,
            wait: this.$wait,
            params: this.parameterMap
          })
        }
      },
      handleUpdate() {
        switch (this.linkType) {
          case 'set-watcher':
            this.setWatcher()
            break
          case 'set-area':
            this.setArea()
            break
          default:
            this.$store.dispatch('backOfficeApi/addMessage', {messageContent: this.linkType + ' linkType is not yet supported.', messageType: 'error'})
            break
        }
      },
      setWatcher() {
        this.$store.dispatch('data/setWatcher', {watcherName: this.targetWindow, data: this.parameterMap})
      },
      setArea() {
        this.$store.dispatch('ui/setArea', {areaId: this.targetWindow, targetUrl: this.$store.getters['backOfficeApi/currentApi'] + '/' + this.target, wait: this.$wait})
      }
    }
  }
</script>

<style scoped>

</style>
