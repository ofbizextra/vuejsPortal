<template>
  <div id="screen">
    <div v-for="(component, id) in screen.viewScreen" :key="id" v-bind:is="constants.components[component.name]" :props="component">
    </div>
  </div>
</template>

<script>
  import constants from './../js/constants'
  import {mapGetters} from 'vuex'

  const queryString = require('query-string')

  export default {
    name: "Screen",
    data() {
      return {
        params: {},
        screen: {},
        constants: constants
      }
    },
    computed: {
      ...mapGetters({
        currentApi: 'backOfficeApi/currentApi'
      })
    },
    mounted() {
      let screenId = this.$route.params.screenId
      if (screenId === 'main') {
        screenId = 'mainfjs'
      }
      this.$http.post(this.currentApi + '/' + screenId,
        queryString.stringify(this.$route.query),
        {headers: {'Content-Type': 'application/x-www-form-urlencoded', 'locale': 'en_US'}}
      ).then(
        response => {
          this.screen = response.body
        }
      )
    },
    watch: {
      '$route': function () {
        let screenId = this.$route.params.screenId
        if (screenId === 'main') {
          screenId = 'mainfjs'
        }
        this.$http.post(this.currentApi + '/' + screenId,
          queryString.stringify(this.$route.query),
          {headers: {'Content-Type': 'application/x-www-form-urlencoded', 'locale': 'en_US'}}
        ).then(
          response => {
            this.screen = response.body
          }
        )
      }
    }
  }
</script>

<style scoped>

</style>
