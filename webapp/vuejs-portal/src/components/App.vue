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
      <v-list dense nav id="app-navigation" height="auto">
        <v-list-item v-for="(link, id) in menu.viewScreen[0].children" :key="id" link
                     :to="generateRouterLink(link.children[0].attributes.linkUrl)">
          <v-list-item-content>
            <v-list-item-title>
              {{link.children[0].attributes.text}}
            </v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
      <template v-slot:append>
        <v-list>
          <v-list-item>
            <v-list-item-icon>
              <v-icon>mdi-theme-light-dark</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>
                Dark Mode :
              </v-list-item-title>
            </v-list-item-content>
            <v-list-item-action>
              <v-switch v-model="darkMode"></v-switch>
            </v-list-item-action>
          </v-list-item>
          <v-list-group dense prepend-icon="mdi-palette">
            <template v-slot:activator>
              <v-list-item-title>Colors</v-list-item-title>
            </template>
            <v-list-item dense>
              <v-list-item-title>Primary</v-list-item-title>
              <v-list-item-action>
                <v-dialog v-model="primaryDialog" width="300">
                  <template v-slot:activator="{ on }">
                    <v-btn small color="primary" dark v-on="on" class="ms-2">
                      Change
                    </v-btn>
                  </template>
                  <v-color-picker v-model="primaryColor" width="300" mode="hexa" show-swatches></v-color-picker>
                </v-dialog>
              </v-list-item-action>
            </v-list-item>
            <v-list-item dense>
              <v-list-item-title>Secondary</v-list-item-title>
              <v-list-item-action>
                <v-dialog v-model="secondaryDialog" width="300">
                  <template v-slot:activator="{ on }">
                    <v-btn small color="secondary" dark v-on="on" class="ms-2">
                      Change
                    </v-btn>
                  </template>
                  <v-color-picker v-model="secondaryColor" width="300" mode="hexa" show-swatches></v-color-picker>
                </v-dialog>
              </v-list-item-action>
            </v-list-item>
            <v-list-item dense>
              <v-list-item-title>Accent</v-list-item-title>
              <v-list-item-action>
                <v-dialog v-model="accentDialog" width="300">
                  <template v-slot:activator="{ on }">
                    <v-btn small color="accent" dark v-on="on" class="ms-2">
                      Change
                    </v-btn>
                  </template>
                  <v-color-picker v-model="accentColor" width="300" mode="hexa" show-swatches></v-color-picker>
                </v-dialog>
              </v-list-item-action>
            </v-list-item>
            <v-list-item dense>
              <v-list-item-title>Success</v-list-item-title>
              <v-list-item-action>
                <v-dialog v-model="successDialog" width="300">
                  <template v-slot:activator="{ on }">
                    <v-btn small color="success" dark v-on="on" class="ms-2">
                      Change
                    </v-btn>
                  </template>
                  <v-color-picker v-model="successColor" width="300" mode="hexa" show-swatches></v-color-picker>
                </v-dialog>
              </v-list-item-action>
            </v-list-item>
            <v-list-item dense>
              <v-list-item-title>Error</v-list-item-title>
              <v-list-item-action>
                <v-dialog v-model="errorDialog" width="300">
                  <template v-slot:activator="{ on }">
                    <v-btn small color="error" dark v-on="on" class="ms-2">
                      Change
                    </v-btn>
                  </template>
                  <v-color-picker v-model="errorColor" width="300" mode="hexa" show-swatches></v-color-picker>
                </v-dialog>
              </v-list-item-action>
            </v-list-item>
            <v-list-item dense>
              <v-list-item-title>Warning</v-list-item-title>
              <v-list-item-action>
                <v-dialog v-model="warningDialog" width="300">
                  <template v-slot:activator="{ on }">
                    <v-btn small color="warning" dark v-on="on" class="ms-2">
                      Change
                    </v-btn>
                  </template>
                  <v-color-picker v-model="warningColor" width="300" mode="hexa" show-swatches></v-color-picker>
                </v-dialog>
              </v-list-item-action>
            </v-list-item>
          </v-list-group>
        </v-list>
      </template>
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
        menu: {},
        primaryDialog: false,
        secondaryDialog: false,
        accentDialog: false,
        successDialog: false,
        errorDialog: false,
        warningDialog: false,
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
      },
      darkMode: {
        get() {
          return this.$vuetify.theme.dark
        },
        set(value) {
          this.$vuetify.theme.dark = value
          this.$store.dispatch('backOfficeApi/doPost', {
            uri: constantes.hostUrl + this.currentApi + '/ajaxSetUserPreference',
            params : {
              userPrefTypeId: 'DARK_MODE',
              userPrefValue: value,
              userPrefGroupTypeId: 'FRONTJS_PREFERENCES'
            }})
        }
      },
      primaryColor: {
        get() {
          return this.$vuetify.theme.themes.dark.primary
        },
        set(value) {
          this.$vuetify.theme.themes.dark.primary = value
          this.$vuetify.theme.themes.light.primary = value
          this.$store.dispatch('backOfficeApi/doPost', {
            uri: constantes.hostUrl + this.currentApi + '/ajaxSetUserPreference',
            params : {
              userPrefTypeId: 'PRIMARY_COLOR',
              userPrefValue: value,
              userPrefGroupTypeId: 'FRONTJS_PREFERENCES'
            }})
        }
      },
      secondaryColor: {
        get() {
          return this.$vuetify.theme.themes.dark.secondary
        },
        set(value) {
          this.$vuetify.theme.themes.dark.secondary = value
          this.$vuetify.theme.themes.light.secondary = value
          this.$store.dispatch('backOfficeApi/doPost', {
            uri: constantes.hostUrl + this.currentApi + '/ajaxSetUserPreference',
            params : {
              userPrefTypeId: 'SECONDARY_COLOR',
              userPrefValue: value,
              userPrefGroupTypeId: 'FRONTJS_PREFERENCES'
            }})
        }
      },
      accentColor: {
        get() {
          return this.$vuetify.theme.themes.dark.accent
        },
        set(value) {
          this.$vuetify.theme.themes.dark.accent = value
          this.$vuetify.theme.themes.light.accent = value
          this.$store.dispatch('backOfficeApi/doPost', {
            uri: constantes.hostUrl + this.currentApi + '/ajaxSetUserPreference',
            params : {
              userPrefTypeId: 'ACCENT_COLOR',
              userPrefValue: value,
              userPrefGroupTypeId: 'FRONTJS_PREFERENCES'
            }})
        }
      },
      successColor: {
        get() {
          return this.$vuetify.theme.themes.dark.success
        },
        set(value) {
          this.$vuetify.theme.themes.dark.success = value
          this.$vuetify.theme.themes.light.success = value
          this.$store.dispatch('backOfficeApi/doPost', {
            uri: constantes.hostUrl + this.currentApi + '/ajaxSetUserPreference',
            params : {
              userPrefTypeId: 'SUCCESS_COLOR',
              userPrefValue: value,
              userPrefGroupTypeId: 'FRONTJS_PREFERENCES'
            }})
        }
      },
      errorColor: {
        get() {
          return this.$vuetify.theme.themes.dark.error
        },
        set(value) {
          this.$vuetify.theme.themes.dark.error = value
          this.$vuetify.theme.themes.light.error = value
          this.$store.dispatch('backOfficeApi/doPost', {
            uri: constantes.hostUrl + this.currentApi + '/ajaxSetUserPreference',
            params : {
              userPrefTypeId: 'ERROR_COLOR',
              userPrefValue: value,
              userPrefGroupTypeId: 'FRONTJS_PREFERENCES'
            }})
        }
      },
      warningColor: {
        get() {
          return this.$vuetify.theme.themes.dark.warning
        },
        set(value) {
          this.$vuetify.theme.themes.dark.warning = value
          this.$vuetify.theme.themes.light.warning = value
          this.$store.dispatch('backOfficeApi/doPost', {
            uri: constantes.hostUrl + this.currentApi + '/ajaxSetUserPreference',
            params : {
              userPrefTypeId: 'WARNING_COLOR',
              userPrefValue: value,
              userPrefGroupTypeId: 'FRONTJS_PREFERENCES'
            }})
        }
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
      this.$store.dispatch('backOfficeApi/doPost', {uri: 'getUserPreference', params: {userPrefTypeId: 'DARK_MODE', userPrefGroupTypeId: 'FRONTJS_PREFERENCES'}}).then(result => {
        if (result.body.hasOwnProperty('userPrefValue')) {
          this.$vuetify.theme.dark = result.body.userPrefValue === "true"
        }
      })
      this.$store.dispatch('backOfficeApi/doPost', {uri: 'getUserPreference', params: {userPrefTypeId: 'PRIMARY_COLOR', userPrefGroupTypeId: 'FRONTJS_PREFERENCES'}}).then(result => {
        if (result.body.hasOwnProperty('userPrefValue')) {
          this.$vuetify.theme.themes.dark.primary = result.body.userPrefValue
          this.$vuetify.theme.themes.light.primary = result.body.userPrefValue
        }
      })
      this.$store.dispatch('backOfficeApi/doPost', {uri: 'getUserPreference', params: {userPrefTypeId: 'SECONDARY_COLOR', userPrefGroupTypeId: 'FRONTJS_PREFERENCES'}}).then(result => {
        if (result.body.hasOwnProperty('userPrefValue')) {
          this.$vuetify.theme.themes.dark.secondary = result.body.userPrefValue
          this.$vuetify.theme.themes.light.secondary = result.body.userPrefValue
        }
      })
      this.$store.dispatch('backOfficeApi/doPost', {uri: 'getUserPreference', params: {userPrefTypeId: 'ACCENT_COLOR', userPrefGroupTypeId: 'FRONTJS_PREFERENCES'}}).then(result => {
        if (result.body.hasOwnProperty('userPrefValue')) {
          this.$vuetify.theme.themes.dark.accent = result.body.userPrefValue
          this.$vuetify.theme.themes.light.accent = result.body.userPrefValue
        }
      })
      this.$store.dispatch('backOfficeApi/doPost', {uri: 'getUserPreference', params: {userPrefTypeId: 'SUCCESS_COLOR', userPrefGroupTypeId: 'FRONTJS_PREFERENCES'}}).then(result => {
        if (result.body.hasOwnProperty('userPrefValue')) {
          this.$vuetify.theme.themes.dark.success = result.body.userPrefValue
          this.$vuetify.theme.themes.light.success = result.body.userPrefValue
        }
      })
      this.$store.dispatch('backOfficeApi/doPost', {uri: 'getUserPreference', params: {userPrefTypeId: 'ERROR_COLOR', userPrefGroupTypeId: 'FRONTJS_PREFERENCES'}}).then(result => {
        if (result.body.hasOwnProperty('userPrefValue')) {
          this.$vuetify.theme.themes.dark.error = result.body.userPrefValue
          this.$vuetify.theme.themes.light.error = result.body.userPrefValue
        }
      })
      this.$store.dispatch('backOfficeApi/doPost', {uri: 'getUserPreference', params: {userPrefTypeId: 'WARNING_COLOR', userPrefGroupTypeId: 'FRONTJS_PREFERENCES'}}).then(result => {
        if (result.body.hasOwnProperty('userPrefValue')) {
          this.$vuetify.theme.themes.dark.warning = result.body.userPrefValue
          this.$vuetify.theme.themes.light.warning = result.body.userPrefValue
        }
      })
    },

  }
</script>

<style>
  @import url('https://fonts.googleapis.com/css?family=Roboto:100,300,400,500,700,900');
</style>
