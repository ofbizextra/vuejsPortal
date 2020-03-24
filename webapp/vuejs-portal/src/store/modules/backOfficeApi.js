import Vuex from 'vuex'
import Vue from 'vue'
import constants from '../../js/constants'
import queryString from 'query-string'

Vue.use(Vuex)

const state = {
  messageList: [],
  currentApi: ''
}

const mutations = {
  ADD_MESSAGE(state, {messageContent, messageType}) {
    Vue.set(state, 'messageList', [...state.messageList, {messageContent, messageType}])
  },
  DELETE_MESSAGE(state, {message}) {
    state.messageList.splice(state.messageList.indexOf(message), 1)
  },
  SET_CURRENT_API(state, api) {
    Vue.set(state, 'currentApi', api)
  }
}

const getters = {
  messageList: state => state.messageList,
  currentApi: state => state.currentApi,
  apiUrl: state => constants.hostUrl + state.currentApi
}

const actions = {
  doPost({commit, dispatch}, {uri, params}) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        Vue.http.post(uri,
          queryString.stringify({
            ...params
          }),
          {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).then(response => {
          if (typeof response.body === 'string' && response.body.includes('login failed')) {
            dispatch('ui/setDialogStatus', {
              dialogId: 'loginDialog',
              dialogStatus: true
            }, {root: true})
            this._vm.$modal.show('login')
            reject(response)
          }
          if (response.body.hasOwnProperty('_ERROR_MESSAGE_')) {
            commit('ADD_MESSAGE', {messageContent: response.body['_ERROR_MESSAGE_'], messageType: 'error'})
            reject(response)
          }
          if (response.body.hasOwnProperty('_ERROR_MESSAGE_LIST_')) {
            for (let errorMessage of response.body['_ERROR_MESSAGE_LIST_']) {
              commit('ADD_MESSAGE', {messageContent: errorMessage, messageType: 'error'})
            }
            reject(response)
          }
          if (response.body.hasOwnProperty('_EVENT_MESSAGE_')) {
            commit('ADD_MESSAGE', {messageContent: response.body['_EVENT_MESSAGE_'], messageType: 'event'})
          }
          if (response.body.hasOwnProperty('_EVENT_MESSAGE_LIST_')) {
            for (let eventMessage of response.body['_EVENT_MESSAGE_LIST_']) {
              commit('ADD_MESSAGE', {messageContent: eventMessage, messageType: 'event'})
            }
          }
          resolve(response)
        }, error => {
          reject(error)
        })
      }, 0)
    })
  },
  addMessage({commit}, {messageContent, messageType}) {
    commit('ADD_MESSAGE', {messageContent, messageType})
  },
  deleteMessage({commit}, {message}) {
    commit('DELETE_MESSAGE', {message})
  },
  setApi({commit}, api) {
    commit('SET_CURRENT_API', api)
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
