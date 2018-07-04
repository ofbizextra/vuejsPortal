<template>
  <div id="search">
    <h2>Search</h2>
    <form class="form-control">
      <span><label>ID: </label></span>
      <input type="text" name="id" v-model="search.id"/>
      <span><label>Name: </label></span>
      <input type="text" name="name" v-model="search.name"/>
      <span><label>Description: </label></span>
      <input type="text" name="description" v-model="search.description" />
      <button v-on:click.prevent="log">Search</button>
    </form>
  </div>
</template>

<script>
export default {
  name: 'search',
  data () {
    return {
      search: {
        id: null,
        name: '',
        description: ''
      }
    }
  },
  methods: {
    log: () => {
      console.log('submit')
    }
  }
}
</script>

<style>

</style>
