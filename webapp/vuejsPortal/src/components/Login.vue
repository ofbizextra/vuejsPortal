<template>
  <div id="login">
    <div class="row errors" v-if="false">
      <div class="col-md-offset-2 col-md-8">{{'serverError'}}</div>
    </div>
    <div class="row">
      <div class="offset-md-4 col-md-4">
        <div class="card card-primary">
          <div class="card-header bg-primary text-white">
            <h3 class="card-title">Connexion</h3>
          </div>
          <div class="card-body">
            <form class="form-horizontal">
              <div class="form-group row">
                <label for="inputEmail" class="control-label col-md-4">Email</label>
                <div class="col-md-8">
                  <input type="email"
                  class="form-control"
                  id="inputEmail"
                  placeholder="Email"
                  v-model="credentials.username">
                </div>
              </div>
              <div class="form-group row">
                <label for="inputPassword" class="control-label col-md-4">Mot de passe</label>
                <div class="col-md-8">
                  <input type="password"
                  class="form-control"
                  id="inputPassword"
                  placeholder="mot de passe"
                  v-model="credentials.password"
                  v-on:keyup.enter="submit()">
                </div>
              </div>
              <div class="form-group">
                <div class="offset-md-4 col-md-8">
                  <div class="checkbox">
                    <label> <input type="checkbox" v-model="credentials.rememberMe"/>Se souvenir de moi
                    </label>
                  </div>
                </div>
              </div>
              <div class="form-group">
                <div class="offset-md-4 col-md-8">
                  <a href="#/passwordLost"> Mot de passe perdu ? </a>
                </div>
              </div>
              <div class="form-group">
                <div class="offset-md-4 col-md-8">
                  <button type="button" class="btn btn-primary" v-on:click="submit()">Connexion</button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
    <button v-on:click="add()">
      Add Component
    </button>
  </div>
</template>

<script>
  import { mapActions, mapGetters } from 'vuex'

  export default {
    name: 'login',
    data () {
      return {
        credentials: {
          username: "",
          password: "",
          rememberMe: false
        },
        dataTest: {
          attributes: [{key: 'id', value: 'someValue'}],
          children: [],
          tagName: 'vue-drop-down',
          type: 'element'}
      }
    },
    computed: mapGetters('login', { token: 'token' } ),
    methods: {
      submit() {
        console.log("Connecting ...",this.credentials)
        this.$store.dispatch('login/login', this.credentials).then(response => {
          this.$router.push('/')
          console.log('recherche des offres ...')
          this.$store.dispatch('offers/setOffers')
          this.$store.dispatch('offers/setCategories')
        }, error => {
          console.log('Echec de la connexion')
        })
      },
      add() {
        console.log(this.dataTest.toString())
        this.$store.dispatch(
          'ui/addComponent',
          this.dataTest)
      }
    }
  }
</script>

<style>

</style>
