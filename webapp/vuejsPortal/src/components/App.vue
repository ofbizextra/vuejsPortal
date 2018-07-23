<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
  export default {
    name: 'app',
    mounted() {
      console.log(this.$route.path)
      this.$store.dispatch('login/check').then(() => {
        this.$router.push('/')
      }, () => {
        this.$router.push('/login')
      })
    }
  }
</script>

<style>

</style>
