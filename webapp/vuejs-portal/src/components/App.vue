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
      <v-list dense nav>
        <v-list-item v-for="(link, id) in menu.viewScreen[0].children" :key="id" link @click="loadPortalPage({...link.children[0].attributes.parameterMap})">
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
        console.log('MENU_LINK parameterMap : ' , parameterMap)
        this.$store.dispatch('ui/loadPortalPageDetail', {api: this.currentApi, params: parameterMap})
      }
    },
    mounted() {
      console.log(this.$route.path)
      let pathname = window.location.pathname
      let api = pathname.substring(0, pathname.indexOf('/', 1)) + '/control'
      this.$store.dispatch('backOfficeApi/setApi', api)
      // Vue.http.setRequestHeader('Content-Security-Policy', "default-src 'self'")
      this.$store.dispatch('login/check').then(() => {
        this.$router.push({path: this.$route.fullPath})
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
