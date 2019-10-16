<template>
  <div id="vue-message-list" style="position: fixed; top: 0; left: 0; right: 0; height: 0; text-align: center">
    <flash-message style="display: inline-block ;text-align: left"></flash-message>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'
  export default {
    name: "VueMessageList",
    computed: {
      ...mapGetters({
        messageList: 'backOfficeApi/messageList'
      })
    },
    methods: {
      dismiss(message) {
        this.$store.dispatch('backOfficeApi/deleteMessage', {message})
      },
      click() {
        console.log('flash message')
        this.flash('hello world !!!', 'success', 2000)
      }
    }
  }
</script>

<style scoped>

</style>
