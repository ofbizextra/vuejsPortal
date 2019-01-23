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

  export default {
    name: "Portal",
    mounted() {
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
    },
    computed: {
      ...mapGetters({
        portalPage: 'ui/currentPortalPage',
        portalPageDetail: 'ui/currentPortalPageDetail'
      })
    }
  }
</script>

<style scoped>

</style>
