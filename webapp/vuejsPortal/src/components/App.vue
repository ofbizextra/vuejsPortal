<template>
  <div id="app">
    <input type="hidden" id="updateCpt" :value="updateCpt">
    <BlockUI v-if="$wait.any" message="Fetching datas...">
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
  </div>
</template>

<script>
  import logo from '../assets/logo.png'
  import {mapGetters} from 'vuex'
  import Vue from 'vue'
  export default {
    name: 'app',
    data() {
      return {
        image: logo
      }
    },
    computed: {
      ...mapGetters({
        updateCpt: 'ui/updateCpt'
      })
    },
    methods: {
      increment() {
        this.$store.dispatch('data/incrementCpt1')
      },
      changeEX11() {
        this.$store.dispatch('data/setEntityRow', {
          entityName: 'Example',
          primaryKey: 'EX11',
          data: {description: 'blablabla'}
        })
      }
    },
    mounted() {
      console.log(this.$route.path)
      // Vue.http.setRequestHeader('Content-Security-Policy', "default-src 'self'")
      this.$store.dispatch('login/check').then(() => {
        if (this.$route.fullPath.includes('/exampleapi/control')) {
          let path = this.$route.fullPath.substring(this.$route.fullPath.indexOf('/exampleapi/control'))
          this.$router.push({path: path})
        } else {
          this.$router.push({path: this.$route.fullPath})
        }
      }, () => {
        this.$router.push('/login')
      })
    },

  }
</script>

<style>

</style>
