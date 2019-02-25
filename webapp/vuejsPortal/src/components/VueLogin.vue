<template>
  <div id="vue-login">
    <input type="button" value="show modal" @click.prevent="show"/>
      <modal name="login">
        blablabla
      </modal>
  </div>
</template>

<script>
  import VueJsModal from 'vue-js-modal/src/Modal'
  export default {
    name: "VueLogin",
    components: {VueJsModal},
    methods: {
      show() {
        this.$modal.show('login')
      }
    }
  }
</script>

<style scoped>

</style>
