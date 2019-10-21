<template>
  <v-app id="app">
    <v-content>
      <input type="hidden" id="updateCpt" :value="updateCpt">
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
      }
    },
    computed: {
      ...mapGetters({
        updateCpt: 'ui/updateCpt'
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
    },

  }
</script>

<style>
  @import url('https://fonts.googleapis.com/css?family=Roboto:100,300,400,500,700,900');
</style>
