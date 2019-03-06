<template>
  <div id="portal">
    <table v-if="portalPageDetail">
      <tr>
        <vue-column-portlet v-for="column in portalPageDetail.listColumnPortlet" :key="column.columnSeqId"
                            :props="{portalPageId: portalPage, columnSeqId: column.columnSeqId}">
        </vue-column-portlet>
        <!--<find-example-layer />-->
        <!--<list-example-layer />-->
        <!--<edit-example-layer />-->
      </tr>
    </table>
  </div>
</template>

<script>
  import constantes from './../js/constantes'
  import queryString from 'query-string'
  import {mapGetters} from 'vuex'
  import Vue from 'vue'

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
        portalPageDetail: 'ui/currentPortalPageDetail'
      })
    },
    mounted() {
      console.log('----- Portal mounted -----', this.$root.$route.fullPath, this.$route.params)
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
            this.$store.dispatch('ui/setPortalPage', {portalPageId: PORTAL_PAGE_ID, portalPage})
          },
          error => console.log(error.body)
        )
      } else {
        this.$http.post(constantes.apiUrl + constantes.portalPageDetail.path,
          queryString.stringify({
            portalPageId: PORTAL_PAGE_ID
          }),
          {headers: {'Content-Type': 'application/x-www-form-urlencoded', 'locale': 'en_US'}}
        ).then(
          response => {
            let portalPage = response.body
            this.$store.dispatch('ui/setPortalPage', {portalPageId: PORTAL_PAGE_ID, portalPage})
          },
          error => console.log(error.body)
        )
      }
    }
  }
</script>

<style scoped>

</style>
