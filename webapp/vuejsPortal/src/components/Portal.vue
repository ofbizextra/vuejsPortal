<template>
  <div id="portal">
    <div v-for="form in parsedData" v-if="form.type === 'element' && form.tagName === 'vue-form'" is="form">
      <div v-for="table in form.children" v-if="table.type === 'element' && table.tagName === 'vue-table'" is="table">
        <div v-for="row in table.children" v-if="row.type === 'element' && row.tagName === 'vue-tr'" is="tr">
          <div v-for="cell in row.children" v-if="cell.type === 'element' && cell.tagName === 'vue-td'" is="td">
            <div v-for="component in cell.children" v-if="component.type === 'element' && (component.tagName.includes('vue-'))" v-bind:is="component.tagName" :props="component">

            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import constantes from './../js/constantes'
  import {parse} from 'himalaya'

  export default {
    name: "Portal",
    data () {
      return {
        data: '',
        parsedData: {},
        id: ''
      }
    },
    mounted () {
      this.$http.get(constantes.apiUrl + constantes.editExampleLayer.path + (this.$route.params['id'] ? `?exampleId=${this.$route.params['id']}` : '')).then(response => {
        console.log(response.body)
        this.data = response.body
        this.parsedData = parse(this.data)
        this.id = this.parsedData[2].children[5].children[0].children[1].children[0].attributes.find(attr => attr.key === 'description').value
        this.$store.dispatch('data/addExample', this.id)
        this.$store.dispatch('data/setCurrentId', this.id)
      }, error => {
        console.log(error.body)
      })
    },
    watch: {
      '$route'(to, from) {
        this.$http.get(constantes.apiUrl + constantes.editExampleLayer.path + (this.$route.params['id'] ? `?exampleId=${this.$route.params['id']}` : '')).then(response => {
          console.log(response.body)
          this.data = response.body
          this.parsedData = parse(this.data)
          this.id = this.parsedData[2].children[5].children[0].children[1].children[0].attributes.find(attr => attr.key === 'description').value
          this.$store.dispatch('data/addExample',this.id)
          this.$store.dispatch('data/setCurrentId', this.id)
        }, error => {
          console.log(error.body)
        })
      }
    }
  }
</script>

<style scoped>

</style>
