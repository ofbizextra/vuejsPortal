<template>
  <div id="find-example-layer">
    <h2>// findExampleLayer</h2>
    <div
      v-for="component in parsedData"
      v-if="component.type === 'element' && (component.tagName.includes('vue-'))"
      v-bind:is="component.tagName"
      :props="component"
      :updateStore="updateStore">
    </div>
  </div>
</template>

<script>
  import constantes from './../js/constantes'
  import {parse} from 'himalaya'

  export default {
    name: "FindExampleLayer",
    data () {
      return {
        data: '',
        parsedData: {}
      }
    },
    computed: {
      updateStore() {
        return !!this.$route.params['id']
      }
    },
    mounted () {
      console.log('findExampleLayer : Mounted')
      this.$http.get(constantes.apiUrl + constantes.findExampleLayer.path).then(response => {
        this.data = response.body
        this.parsedData = parse(this.data)
      }, error => {
        console.log(error.body)
      })
    }
  }
</script>

<style scoped>

</style>
