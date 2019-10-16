<template>
  <div id="vue-login">
    <modal name="login" :reset="true" height="auto">
      <div class="screenlet login-screenlet">
        <div class="screenlet-title-bar">
          <h3>Registered User</h3>
        </div>
        <div class="screenlet-body">
          <form name="loginform">
            <table class="basic-table">
              <tbody>
              <tr>
                <td class="label">User Name</td>
                <td><input name="USERNAME" v-model="username" size="20" type="text"></td>
              </tr>
              <tr>
                <td class="label">Password</td>
                <td><input name="PASSWORD" autocomplete="off" v-model="password" size="20" type="password"></td>
              </tr>
              <tr>
                <td colspan="2">
                  <input value="Login" type="button" @click.prevent="singIn">
                </td>
              </tr>
              </tbody>
            </table>
            <!--<input name="JavaScriptEnabled" value="Y" type="hidden">-->
            <br>
            <a href="https://localhost:8443/exampleapi/control/forgotPassword_step1">Forgot Your Password?</a>
          </form>
        </div>
      </div>
    </modal>
  </div>
</template>

<script>
  import VueJsModal from 'vue-js-modal/src/Modal'

  export default {
    name: "VueLogin",
    data() {
      return {
        username: 'admin',
        password: '',
      }
    },
    computed: {
      credential() {
        return {username: this.username, password: this.password}
      }
    },
    methods: {
      show() {
        this.$modal.show('login')
      },
      singIn() {
        this.$store.dispatch('login/login', this.credential)
          .then(() => {
            console.log('login successfull from modal', 'hidding modal')
            this.$modal.hide('login')
          }, (error) => {
            console.log('Error during login from modal', error, 'keep modal open')
          })
      }
    }
  }
</script>

<style scoped>

</style>
