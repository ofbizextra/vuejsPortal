<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
  export default {
    name: 'app',
    mounted() {
      this.$store.dispatch('login/check').then(response => {
        this.$router.push('/')
      }, error => {
        this.$router.push('/login')
      })
    }
  }
</script>

<style>

</style>
