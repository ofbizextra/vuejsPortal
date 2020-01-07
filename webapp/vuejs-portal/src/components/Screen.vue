<template>
  <div id="screen">
    <div v-for="(component, id) in screen.viewScreen" :key="id" v-bind:is="constantes.components[component.name]" :props="component">
    </div>
  </div>
</template>

<script>
  import constantes from './../js/constantes'
  import {mapGetters} from 'vuex'

  const queryString = require('query-string')

  export default {
    name: "Screen",
    data() {
      return {
        params: {},
        screen: {},
        constantes: constantes
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
        },
        error => {
          if (this.$debug) {
            console.log(error.body)
          }
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
          },
          error => {
            if (this.$debug) {
              console.log(error.body)
            }
          }
        )
      }
    }
  }
</script>

<style scoped>

</style>
