import Vuex from 'vuex'
import Vue from 'vue'
import queryString from 'query-string'
import constants from './../../js/constants'

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
  LOGIN_FAILURE: (state) => {
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
  CHECK_SUCCESS: (state) => {
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
  login({commit, rootGetters}, credentials) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        commit('LOGIN')
        Vue.http.post(
          constants.hostUrl + rootGetters['backOfficeApi/currentApi'] + constants.login.path,
          queryString.stringify({
            JavaScriptEnabled: 'Y',
            USERNAME: credentials.username,
            PASSWORD: credentials.password
          }),
          {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).then(response => {
          if (this.$debug) {
            console.log(response)
          }
          if ((typeof response.body === 'string' && response.body.includes('login successful')
            && !response.body._ERROR_MESSAGE_
            && !response.body._ERROR_MESSAGES_LIST_) || typeof response.body === 'object') {
            if (this.$debug) {
              console.log('login success')
            }
            commit('LOGIN_SUCCESS', credentials)
            resolve()
          } else {
            if (response.body._ERROR_MESSAGE_) {
              if (this.$debug) {
                console.log(response.body._ERROR_MESSAGE_)
              }
            }
            if (response.body._ERROR_MESSAGES_LIST_) {
              if (this.$debug) {
                console.log(response.body._ERROR_MESSAGES_LIST_)
              }
            }
            commit('LOGIN_FAILURE')
            reject(response)
          }
        }, error => {
          if (error.body._ERROR_MESSAGE_) {
            if (this.$debug) {
              console.log(error.body._ERROR_MESSAGE_)
            }
          }
          if (error.body._ERROR_MESSAGES_LIST_) {
            if (this.$debug) {
              console.log(error.body._ERROR_MESSAGES_LIST_)
            }
          }
          commit('LOGIN_FAILURE')
          reject(error)
        })
      }, 0)
    })
  },
  logout({commit}) {
    return new Promise((resolve) => {
      setTimeout(() => {
        commit('LOGIN')
        localStorage.removeItem('token')
        commit('LOGOUT')
        resolve()
      }, 0)
    })
  },
  check({commit, rootGetters}) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        Vue.http.post(
          constants.hostUrl + rootGetters['backOfficeApi/currentApi'] + constants.ajaxCheckLogin.path,
          queryString.stringify({
          }),
          {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).then(response => {
          if (response.body.includes('login successful')
            && !response.body._ERROR_MESSAGE_
            && !response.body._ERROR_MESSAGES_LIST_) {
            if (this.$debug) {
              console.log('login success')
            }
            commit('CHECK_SUCCESS')
            resolve()
          } else {
            if (this.$debug) {
              console.log(response.body._ERROR_MESSAGE_)
            }
            commit('CHECK_FAILURE')
            reject()
          }
        }, error => {
          if (this.$debug) {
            console.log('error : ', error)
          }
          commit('CHECK_FAILURE')
          reject()
        })
      }, 0)
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
