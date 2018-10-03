<template>
  <div id="app">
    <!--<v-wait message="Loading data...">-->
    <!--</v-wait>-->

    <BlockUI v-if="$wait.any" message="Fetching datas...">
      <spinner
        id="loader-wrapper"
        :animation-duration="1200"
        :size="55"
        :color="'#28b3ff'"
      />
    </BlockUI>
    <router-view></router-view>
  </div>
</template>

<script>
  import logo from '../assets/logo.png'
  export default {
    name: 'app',
    data() {
      return {
        image: logo
      }
    },
    methods: {
      increment() {
        this.$store.dispatch('data/incrementCpt1')
      },
      changeEX11() {
        this.$store.dispatch('data/setEntityRow', {
          entityName: 'Example',
          primaryKey: 'EX11',
          data: {description: 'blablabla'}
        })
      }
    },
    mounted() {
      console.log(this.$route.path)
      this.$store.dispatch('login/check').then(() => {
        this.$router.push('/')
      }, () => {
        this.$router.push('/login')
      })
    },

  }
</script>

<style>

</style>
