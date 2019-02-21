<template>
  <div id="vue-error-message-list">
    <table>
      <tr v-for="(errorMessage, errorMessageId) of errorMessageList" :key="errorMessageId">
        <td>
          <h3>{{errorMessage}}</h3>
        </td>
        <td>
          <input type="button" value="Dismiss" @click.prevent="dismiss(errorMessage)">
        </td>
      </tr>
    </table>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "VueErrorMessageList",
    computed: {
      ...mapGetters({
        errorMessageList: 'ui/errorMessageList'
      })
    },
    methods: {
      dismiss(errorMessage) {
        this.$store.dispatch('ui/deleteErrorMessage', {errorMessage})
      }
    }
  }
</script>

<style scoped>

</style>
