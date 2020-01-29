<template>
  <div id="vue-link">
    <!--method="post"-->
    <form
        v-if="linkType === 'hidden-form'"
        v-on:click.prevent=""
        :name="uniqueItemName"
    >
      <input
          v-for="(parameter, id) in parameterMap"
          :key="id"
          :name="parameter.name"
          :value="parameter.value"
          type="hidden"
      />
    </form>
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
          <v-icon v-if="haveIcon" v-on="on">{{src}}</v-icon>
        </template>
        <span>{{imgTitle}}</span>
      </v-tooltip>
      <img :src="src" :title="imgTitle" alt="" v-if="haveImage"/>
      <span class="font-weight-regular">
        {{text}}
      </span>
    </router-link>
    <a
        v-else-if="linkType === 'auto'"
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
          <v-icon v-if="haveIcon" v-on="on">{{src}}</v-icon>
        </template>
        <span>{{imgTitle}}</span>
      </v-tooltip>
      <img :src="src" :title="imgTitle" alt="" v-if="haveImage"/>
      <span class="font-weight-regular">
        {{text}}
      </span>
    </a>
    <v-btn
        v-else-if="linkType === 'anchor' && inline"
        v-bind:id="id + '_link'"
        :data-dialog-params="params"
        :data-dialog-width="width"
        :data-dialog-height="height"
        :data-dialog-title="text"
        :class="style"
        v-on:click.prevent="redirect"
        icon
    >
      <v-tooltip bottom>
        <template v-slot:activator="{ on }">
          <v-icon v-if="haveIcon" v-on="on">{{src}}</v-icon>
        </template>
        <span>{{imgTitle}}</span>
      </v-tooltip>
      <img :src="src" :title="imgTitle" alt="" v-if="haveImage"/>
      <span class="font-weight-regular secondary--text">
            {{text}}
        </span>
    </v-btn>
    <a
        v-else-if="linkType === 'anchor'"
        v-bind:id="id + '_link'"
        :data-dialog-params="params"
        :data-dialog-width="width"
        :data-dialog-height="height"
        :data-dialog-title="text"
        :class="style"
        v-on:click.prevent="clickDisabled ? null : redirect"
    >
      <v-tooltip bottom>
        <template v-slot:activator="{ on }">
          <v-icon v-if="haveIcon" v-on="on">{{src}}</v-icon>
        </template>
        <span>{{imgTitle}}</span>
      </v-tooltip>
      <img :src="src" :title="imgTitle" alt="" v-if="haveImage"/>
      <span class="font-weight-regular secondary--text">
            {{text}}
        </span>
    </a>
    <a
        v-else-if="linkType === 'hidden-form' || target.length > 0"
        v-bind:id="id"
        v-bind:class="style"
        v-bind:name="name"
        v-bind:target="targetWindow"
        v-bind:href="`${href}#${href}`"
        v-on:click.prevent="clickDisabled ? null : redirect"
    >
      <v-tooltip bottom>
        <template v-slot:activator="{ on }">
          <v-icon v-if="haveIcon" v-on="on">{{src}}</v-icon>
        </template>
        <span>{{imgTitle}}</span>
      </v-tooltip>
      <img :src="src" :title="imgTitle" alt="" v-if="haveImage">
      {{' ' + text}}
    </a>
  </div>
</template>

<script>
  import constantes from '../js/constantes'
  import {mapGetters} from 'vuex'

  export default {
    name: "VueLink",
    props: ['props', 'updateStore', 'inline', 'clickDisabled'],  // <== TODO-TRAING OH what is the rule of inline ?
    data() {
      return {
        constantes
      }
    },
    computed: {
      data() {
        let data = this.props.attributes
        delete data['value']  // <== why, no 'value' exist as attribute ?
        return data
      },
      ...mapGetters({
        currentApi: 'backOfficeApi/currentApi'
      }),
      parameterMap() {
        return this.data.hasOwnProperty('parameterMap') ? this.data.parameterMap : {}
      },
      targetWindow() {
        return this.data.hasOwnProperty('targetWindow') ? this.data.targetWindow : ''
      },
      uniqueItemName() {
        return this.data.uniqueItemName
      },
      linkType() {
        return this.data.hasOwnProperty('linkType') ? this.data.linkType : ''
      },
      id() {
        return this.data.hasOwnProperty('id') ? this.data.id : ''
      },
      style() {
        return this.data.hasOwnProperty('style') ? this.data.style : ''
      },
      name() {
        return this.data.hasOwnProperty('name') ? this.data.name : ''
      },
      height() { // what is usage except for modal, there is always a value the default theme value or specifics to this link
        return this.data.height 
      },
      width() { // what is usage except for modal, there is always a value the default theme value or specifics to this link
        return this.data.width
      },
      text() {
        return this.data.hasOwnProperty('text') ? this.data.text : ''
      },
      haveImage() {
        return this.data.hasOwnProperty('img') && this.data.img.hasOwnProperty('src') && !this.data.img.src.startsWith('mdi-')
      },
      haveIcon() {
        return this.data.hasOwnProperty('img') && this.data.img.hasOwnProperty('src') && this.data.img.src.startsWith('mdi-')
      },
      src() {
        return this.data.hasOwnProperty('img') && this.data.img.hasOwnProperty('src') ? this.data.img.src : ''
      },
      imgTitle() {
        return this.data.hasOwnProperty('img') && this.data.img.hasOwnProperty('title') ? this.data.img.title : ''
      },
      target() {
        return this.data.hasOwnProperty('target') ? this.data.target : ''
      },
      params() {  // TODO-TRAINING OH why using a string for Map, or why not using parameterMap
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
      href() {
        if (this.linkType === 'hidden-form') {
          // return `javascript:document.${this.uniqueItemName}.submit()` // todo: no yet supported
          return ''
        }
        let href
        switch (this.urlMode) {
          case 'intra-app':
            href = this.constantes.hostUrl + this.target
            break
          case 'inter-app':
            href = this.constantes.hostUrl + this.target // it should be necessary to add externalLoginKey as parameters
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
      urlMode() {
        if (this.data.hasOwnProperty('urlMode')) {
          return this.data.urlMode
        } else {
          return 'intra-app'
        }
      },
      routerLink() {
        if (this.target === 'showPortalPage') {
          return `/portalPage/${this.parameterMap.portalPageId}`
        } else {
          return `/screen/${this.target}`
        }
      }
    },
    methods: {
      redirect() {
        console.log('click on link : ', this.data.text)
        if (this.data.hasOwnProperty('targetWindow') && this.data.hasOwnProperty('target')) {
          this.$store.dispatch('ui/setArea', {
            areaId: this.data.targetWindow,
            targetUrl: `${this.$store.getters['backOfficeApi/currentApi']}/${this.data.target}`,
            wait: this.$wait,
            params: this.parameterMap
          })
        }
      },
      loadPortalPage() {
        this.$store.dispatch('ui/loadPortalPageDetail', {api: this.currentApi, params: this.parameterMap})
      }
    }
  }
</script>

<style scoped>

</style>
