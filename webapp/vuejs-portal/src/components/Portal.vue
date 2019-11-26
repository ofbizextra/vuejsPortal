<template>
  <div id="portal">
    <table v-if="portalPageDetail" class="d-block">
      <tr class="d-block">
        <vue-column-portlet v-for="column in portalPageDetail.listColumnPortlet" :key="column.portalPageId + '-' + column.columnSeqId"
                            :props="{portalPageId: portalPage, columnSeqId: column.columnSeqId}">
        </vue-column-portlet>
      </tr>
    </table>
  </div>
</template>

<script>
  import constantes from './../js/constantes'
  import {mapGetters} from 'vuex'
  import Vue from 'vue'

  const queryString = require('query-string')

  export default {
    name: "Portal",
    data() {
      return {
        params: {}
      }
    },
    computed: {
      ...mapGetters({
        portalPage: 'ui/currentPortalPage',
        portalPageDetail: 'ui/currentPortalPageDetail',
        currentApi: 'backOfficeApi/currentApi',
        currentPortalPageParams: 'ui/currentPortalPageParams'
      })
    },
    methods: {
      oldMounted() {
        console.log('----- Portal mounted -----', this.$root.$route.fullPath, this.$route.params)
        let fullPath = window.location.href
        console.log('FULLPATH ===> ' + fullPath)
        let pathName = window.location.pathname
        console.log('PATHNAME ===> ' + pathName)
        let api = pathName.toString().substring(0, pathName.toString().indexOf('/', 1)) + '/control'
        console.log('API ===> ' + api)
        this.$store.dispatch('backOfficeApi/setApi', api)
        if (this.$root.$route.fullPath.includes('?')) {
          console.log('Root FullPath: ', this.$root.$route.fullPath)
          let paramsString = unescape(this.$root.$route.fullPath.split("?")[1])
          console.log('splited FullPath: ', paramsString)
          let paramsArray = paramsString.split("&amp;")
          console.log('string params: ', paramsArray)
          for (let element of paramsArray) {
            Vue.set(this.params, element.split('=')[0], element.split('=')[1])
          }
          console.log('final params: ', this.params)
          console.log('setting up watcherName')
          // this.$store.dispatch('data/setWatcher', {watcherName: 'showExample', params: this.params})
          this.$http.post(constantes.apiUrl + constantes.portalPageDetail.path,
            queryString.stringify(this.params),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded', 'locale': 'en_US'}}
          ).then(
            response => {
              let portalPage = response.body
              // PORTAL_PAGE_ID is defined out of the application scope
              // eslint-disable-next-line
              this.$store.dispatch('ui/setPortalPage', {portalPageId: PORTAL_PAGE_ID, portalPage})
            },
            error => console.log(error.body)
          )
        } else {
          this.$http.post(constantes.apiUrl + constantes.portalPageDetail.path,
            queryString.stringify({
              // PORTAL_PAGE_ID is defined out of the application scope
              // eslint-disable-next-line
              portalPageId: PORTAL_PAGE_ID
            }),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded', 'locale': 'en_US'}}
          ).then(
            response => {
              let portalPage = response.body
              // PORTAL_PAGE_ID is defined out of the application scope
              // eslint-disable-next-line
              this.$store.dispatch('ui/setPortalPage', {portalPageId: PORTAL_PAGE_ID, portalPage})
            },
            error => console.log(error.body)
          )
        }
      }
    },
    created() {
      let search = window.location.search
      let params = this.$route.query
      search.substr(1).split('&').forEach(param => {
        let tmp = param.split('=')
        params[tmp[0]] = tmp[1]
      })
      params.portalPageId = this.$route.params.portalPageId
      this.$store.dispatch('ui/loadPortalPageDetail', {api: this.currentApi, params})
    },
    watch: {
      '$route': function () {
        let search = window.location.search
        let params = this.$route.query
        search.substr(1).split('&').forEach(param => {
          let tmp = param.split('=')
          params[tmp[0]] = tmp[1]
        })
        params.portalPageId = this.$route.params.portalPageId
        this.$store.dispatch('ui/loadPortalPageDetail', {api: this.currentApi, params})
      }
    }
  }
</script>

<style scoped>

</style>
