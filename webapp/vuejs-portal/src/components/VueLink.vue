<template>
  <div id="vue-link">
    <!--method="post"-->
    <!--:action="actionUrl"-->
    <!--:target="targetWindow"-->
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
    <!--:data-dialog-url="linkUrl"-->
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
<!--      v-bind:href="`${href}#${href}`"-->
      <!--v-on:click="redirect"-->
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
      <!--      v-bind:href="`${href}#${href}`"-->
      <!--v-on:click="redirect"-->
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
      v-else-if="linkType === 'hidden-form' || linkUrl.length > 0"
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
    props: ['props', 'updateStore', 'inline', 'clickDisabled'],
    data() {
      return {
        constantes
      }
    },
    computed: {
      data() {
        let data = this.props.attributes
        delete data['value']
        return data
      },
      ...mapGetters({
        currentApi: 'backOfficeApi/currentApi'
      }),
      linkUrl() {
        return this.data.hasOwnProperty('linkUrl') ? this.data.linkUrl : ''
      },
      parameterList() {
        return this.data.hasOwnProperty('parameterList') ? this.data.parameterList : ''
      },
      parameterMap() {
        return this.data.hasOwnProperty('parameterMap') ? this.data.parameterMap : {}
      },
      targetWindow() {
        return this.data.hasOwnProperty('targetWindow') ? this.data.targetWindow : ''
      },
      uniqueItemName() {
        return this.data.hasOwnProperty('uniqueItemName') ? this.data.uniqueItemName : ''
      },
      actionUrl() {
        return this.data.hasOwnProperty('actionUrl') ? this.data.actionUrl : ''
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
      height() {
        return this.data.hasOwnProperty('height') ? this.data.height : ''
      },
      width() {
        return this.data.hasOwnProperty('width') ? this.data.width : ''
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
        return this.data.img.hasOwnProperty('src') ? this.data.img.src : ''
      },
      imgTitle() {
        return this.data.img.hasOwnProperty('title') ? this.data.img.title : ''
      },
      target() {
        return this.data.hasOwnProperty('target') ? this.data.target : ''
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
      href() {
        if (this.linkType === 'hidden-form') {
          // return `javascript:document.${this.uniqueItemName}.submit()` // todo: no yet supported
          return ''
        }
        let href
        switch (this.urlMode) {
          case 'intra-app':
            href = this.constantes.hostUrl + this.linkUrl.split('?')[0]
            break
          case 'inter-app':
            href = this.constantes.hostUrl + this.linkUrl.split('?')[0]
            break
          case 'plain':
            href = this.linkUrl()
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
      api() {
        return this.target.split('?')[0].substring(0, this.linkUrl.indexOf('/', 1)) + '/control'
      },
      routerLink() {
        let pathname = this.linkUrl.split('?')[0]
        let webapp = pathname.substring(pathname.lastIndexOf('/') + 1, pathname.length)
        let search = this.linkUrl.split('?')[1]
        let params = {}
        if (search) {
          search.split('&amp;').forEach(param => {
            let tmp = param.split('=')
            params[tmp[0]] = tmp[1]
          })
        }
        if (webapp === 'showPortalPage') {
          return `/portalPage/${params.portalPageId}`
        } else {
          return `/screen/${webapp}`
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
