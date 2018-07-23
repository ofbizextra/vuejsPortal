import Vuex from 'vuex'
import Vue from 'vue'
import queryString from 'query-string'
import constantes from './../../js/constantes'

Vue.use(Vuex)

const state = {
  credentials: {
    username: '',
    token: '',
    rememberMe: false,
    partyId: ''
  },
  isLoggedIn: false,
  pending: true
}

const mutations = {
  SET_USERNAME: (state, username) => {
    state.credentials.username = username
  },
  SET_TOKEN: (state, token) => {
    state.credentials.token = token
  },
  SET_PARTY_ID: (state, partyId) => {
    state.credentials.partyId = partyId
  },
  LOGIN: (state) => {
    state.pending = true
  },
  LOGIN_SUCCESS: (state, params) => {
    state.isLoggedIn = true
    state.credentials.username = params.username
    state.pending = false
  },
  LOGIN_FAILURE: (state, error) => {
    state.isLoggedIn = false
    state.pending = false
  },
  LOGOUT: (state) => {
    state.credentials.token = ''
    state.credentials.username = ''
    state.credentials.partyId = ''
    state.isLoggedIn = false
    state.pending = false
  },
  CHECK: (state) => {
    state.pending = true
  },
  CHECK_SUCCESS: (state, params) => {
    state.isLoggedIn = true
    state.pending = false
  },
  CHECK_FAILURE: (state) => {
    state.isLoggedIn = false
    state.pending = false
  }
}

const getters = {
  username: state => state.credentials.username,
  token: state => state.credentials.token,
  isLoggedIn: state => state.isLoggedIn,
  pending: (state) => () => {
    return state.pending
  }
}

const actions = {
  login({commit}, credentials) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        commit('LOGIN')
        Vue.http.post(
          constantes.apiUrl + constantes.login.path,
          queryString.stringify({
            JavaScriptEnabled: 'Y',
            USERNAME: credentials.username,
            PASSWORD: credentials.password
          }),
          {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).then(response => {
          console.log(response)
          if (response.body.includes('PONG')
            && !response.body._ERROR_MESSAGE_
            && !response.body._ERROR_MESSAGES_LIST_) {
            console.log('login success')
            commit('LOGIN_SUCCESS', credentials)
            resolve()
          } else {
            if (response.body._ERROR_MESSAGE_) {
              console.log(response.body._ERROR_MESSAGE_)
            }
            if (response.body._ERROR_MESSAGES_LIST_) {
              console.log(response.body._ERROR_MESSAGES_LIST_)
            }
            commit('LOGIN_FAILURE')
            reject()
          }
        }, error => {
          if (error.body._ERROR_MESSAGE_) {
            console.log(error.body._ERROR_MESSAGE_)
          }
          if (error.body._ERROR_MESSAGES_LIST_) {
            console.log(error.body._ERROR_MESSAGES_LIST_)
          }
          commit('LOGIN_FAILURE')
          reject()
        })
      }, 1000)
    })
  },
  logout({commit}) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        commit('LOGIN')
        localStorage.removeItem('token')
        commit('LOGOUT')
        resolve()
      }, 1000)
    })
  },
  check({commit}) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        Vue.http.post(
          constantes.apiUrl + constantes.ajaxCheckLogin.path,
          queryString.stringify({
          }),
          {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).then(response => {
          if (response.body.includes('PONG')
            && !response.body._ERROR_MESSAGE_
            && !response.body._ERROR_MESSAGES_LIST_) {
            console.log('login success')
            commit('CHECK_SUCCESS')
            resolve()
          } else {
            console.log(response.body._ERROR_MESSAGE_)
            commit('CHECK_FAILURE')
            reject()
          }
        }, error => {
          console.log('error : ', error)
          commit('CHECK_FAILURE')
          reject()
        })
      }, 1000)
    })
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
