<template>
  <div id="edit-example-layer">
    <h2>// editExampleLayer</h2>
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
    name: "EditExampleLayer",
    data () {
      return {
        data: '',
        parsedData: {},
        id: '',
      }
    },
    computed: {
      updateStore() {
        return !!this.$route.params['id']
      }
    },
    mounted () {
      this.$http.get(constantes.apiUrl + constantes.editExampleLayer.path + (this.$route.params['id'] ? `?exampleId=${this.$route.params['id']}` : '')).then(response => {
        this.data = response.body
        let parsedData = parse(this.data)
        if (this.$route.params['id']) {
          this.id = parsedData[1].children[3].children[0].children[1].children[0].attributes.find(attr => attr.key === 'description').value
          this.$store.dispatch('data/addExample', this.id)
          this.$store.dispatch('data/setCurrentId', this.id)
        } else {
          this.$store.dispatch('data/addExample', 'new')
          this.$store.dispatch('data/setCurrentId', 'new')
        }
        this.parsedData = parsedData
      }, error => {
        console.log(error.body)
      })
    },
    watch: {
      '$route'(to, from) {
        this.$http.get(constantes.apiUrl + constantes.editExampleLayer.path + (this.$route.params['id'] ? `?exampleId=${this.$route.params['id']}` : '')).then(response => {
          this.data = response.body
          let parsedData = parse(this.data)
          if (this.$route.params['id']) {
            this.id = parsedData[2].children[5].children[0].children[1].children[0].attributes.find(attr => attr.key === 'description').value
            console.log('EditExampleLayer -> id : ' + this.id)
            this.$store.dispatch('data/addExample',this.id)
            this.$store.dispatch('data/setCurrentId', this.id)
          } else {
            this.$store.dispatch('data/addExample', 'new')
            this.$store.dispatch('data/setCurrentId', 'new')
          }
          console.log('EditExampleLayer -> fin de store.dispatch')
          this.parsedData = parsedData
          console.log('EditExampleLayer -> this.parsedData : ' + this.parsedData)
        }, error => {
          console.log(error.body)
        })
      }
    }
  }
</script>

<style scoped>

</style>
