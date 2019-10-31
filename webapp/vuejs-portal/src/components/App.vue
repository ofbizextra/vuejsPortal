<template>
  <v-app id="app">
    <v-navigation-drawer mini-variant expand-on-hover app>
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title class="title">Ofbiz</v-list-item-title>
          <v-list-item-subtitle>application</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-divider></v-divider>
      <v-list dense nav id="app-navigation">
        <v-list-item v-for="(link, id) in menu.viewScreen[0].children" :key="id" link
                     :to="generateRouterLink(link.children[0].attributes.linkUrl)">
          <v-list-item-content>
            <v-list-item-title>
              {{link.children[0].attributes.text}}
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
    <v-content>
      <input type="hidden" id="updateCpt" :value="updateCpt">
      <input type="hidden" id="loader-wrapper" v-if="waitAny && !blockUi"/>
      <BlockUI v-if="waitAny && blockUi" message="Fetching datas...">
        <spinner
            id="loader-wrapper"
            :animation-duration="1200"
            :size="55"
            :color="'#28b3ff'"
        />
      </BlockUI>
      <vue-message-list></vue-message-list>
      <vue-login></vue-login>
      <router-view></router-view>
    </v-content>
  </v-app>
</template>

<script>
  import {mapGetters} from 'vuex'
  import constantes from '../js/constantes'

  export default {
    name: 'app',
    data() {
      return {
        menu: {}
      }
    },
    computed: {
      ...mapGetters({
        updateCpt: 'ui/updateCpt',
        currentApi: 'backOfficeApi/currentApi'
      }),
      blockUi() {
        return constantes.blockUi
      },
      waitAny() {
        return this.$wait.any
      }
    },
    methods: {
      increment() {
        this.$store.dispatch('data/incrementCpt1')
      },
      loadPortalPage(parameterMap) {
        console.log('MENU_LINK parameterMap : ', parameterMap)
        this.$store.dispatch('ui/loadPortalPageDetail', {api: this.currentApi, params: parameterMap})
      },
      generateRouterLink(url) {
        let pathname = url.split('?')[0]
        let webapp = pathname.substring(pathname.lastIndexOf('/') + 1, pathname.length)
        let search = url.split('?')[1]
        let params = {}
        if (search) {
          search.split('&amp;').forEach(param => {
            let tmp = param.split('=')
            params[tmp[0]] = tmp[1]
          })
        }
        if (webapp === 'showPortalPage') {
          console.log('Generating MenuLink => url : ', url, 'Final router-link : ', `/portalPage/${params.portalPageId}`)
          return `/portalPage/${params.portalPageId}`
        } else {
          console.log('Generating MenuLink => url : ', url, 'Final router-link : ', `/screen/${webapp}`)
          return `/screen/${webapp}`
        }
      }
    },
    mounted() {
      console.log(this.$route.path)
      let pathname = window.location.pathname
      let api = pathname.substring(0, pathname.indexOf('/', 1)) + '/control'
      let webapp = pathname.substring(pathname.lastIndexOf('/') + 1, pathname.length)
      console.log('WebApp : ', webapp)
      this.$store.dispatch('backOfficeApi/setApi', api)
      // Vue.http.setRequestHeader('Content-Security-Policy', "default-src 'self'")
      this.$store.dispatch('login/check').then(() => {
        if (webapp === 'main') {
          this.$router.push(`/screen/main`)
        }
        if (webapp === 'showPortalPage') {
          let search = window.location.search.split('?').join('')
          let parametersList = search.split('&amp;')
          for (let parameter of parametersList) {
            if (parameter.split('=')[0] === 'portalPageId') {
              webapp = parameter.split('=')[1]
            }
          }
          this.$router.push(`/portalPage/${webapp}`)
        }
      }, () => {
        this.$router.push('/login')
      })
      this.$store.dispatch('backOfficeApi/doPost', {uri: 'applicationMenu', params: {}}).then(result => {
        this.menu = result.body
      }, error => {
        console.log(error)
      })
    },

  }
</script>

<style>
  @import url('https://fonts.googleapis.com/css?family=Roboto:100,300,400,500,700,900');
</style>
