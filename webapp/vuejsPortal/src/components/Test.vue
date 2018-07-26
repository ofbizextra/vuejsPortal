<template>
  <div id="test">
    <table>
      <tr>
        <td><label>URI : </label></td>
        <td><input type="text" v-model="uri"/></td>
      </tr>
      <tr>
        <td><label>key 1 : </label></td>
        <td><input type="text" v-model="key1"/></td>
        <td><label>value 1 : </label></td>
        <td><input type="text" v-model="value1"/></td>
      </tr>
      <tr>
        <td><label>key 2 : </label></td>
        <td><input type="text" v-model="key2"/></td>
        <td><label>value 2 : </label></td>
        <td><input type="text" v-model="value2"/></td>
      </tr>
      <tr>
        <td><label>key 3 : </label></td>
        <td><input type="text" v-model="key3"/></td>
        <td><label>value 3 : </label></td>
        <td><input type="text" v-model="value3"/></td>
      </tr>
      <tr>
        <td><label>key 4 : </label></td>
        <td><input type="text" v-model="key4"/></td>
        <td><label>value 4 : </label></td>
        <td><input type="text" v-model="value4"/></td>
      </tr>
      <tr>
        <td>
          <button v-on:click="post">Post</button>
        </td>
      </tr>
    </table>
    <p style="color: green">{{response}}</p>
    <p style="color: red">{{error}}</p>
  </div>
</template>

<script>
  import constantes from '../js/constantes'
  import queryString from 'query-string'

  export default {
    name: "Test",
    data() {
      return {
        uri: '',
        key1: '',
        value1: '',
        key2: '',
        value2: '',
        key3: '',
        value3: '',
        key4: '',
        value4: '',
        response: '',
        error: ''
      }
    },
    computed: {
      postData() {
        let data = {}
        data[this.key1] = this.value1
        data[this.key2] = this.value2
        data[this.key3] = this.value3
        data[this.key4] = this.value4
        return data
      }
    },
    methods: {
      post() {
        this.$http.post(constantes.apiUrl + '/' + this.uri,
        queryString.stringify(this.postData),
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).then(response => {
          console.log(response)
          this.response = response
          this.error = ''
        }, error => {
          console.log(error)
          this.error = error
          this.response = ''
        })
      }
    }
  }
</script>

<style scoped>

</style>
