import Vuex from 'vuex'
import Vue from 'vue'
import constantes from '../../js/constantes'
import queryString from 'query-string'

Vue.use(Vuex)

const state = {
  messageList: [],
  currentApi: '',
  postId: 0
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
  },
  INCREMENT_POST_ID(state) {
    state.postId++
  }
}

const getters = {
  messageList: state => state.messageList,
  currentApi: state => state.currentApi,
  apiUrl: state => constantes.hostUrl + state.currentApi,
  postId: state => state.postId
}

const actions = {
  doPost({commit}, {uri, params}) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        // let postId = getters['postId']
        // commit('INCREMENT_POST_ID')
        // dispatch('wait/start', `post-${postId}`, {root: true})
        Vue.http.post(uri,
          queryString.stringify({
            ...params
          }),
          {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).then(response => {
          console.log({...response.body})
          console.log('TypeOf response.body : ' + typeof response.body)
          if (typeof response.body === 'string' && response.body.includes('login failed')) {
            // todo: handle login popUp
            this._vm.$modal.show('login')
            // setTimeout(() => {
            //   dispatch('wait/end', `post-${postId}`, {root: true})
            // }, 1000)
            reject(response)
          }
          if (response.body.hasOwnProperty('_ERROR_MESSAGE_')) {
            commit('ADD_MESSAGE', {messageContent: response.body['_ERROR_MESSAGE_'], messageType: 'error'})
            this._vm.flash(response.body['_ERROR_MESSAGE_'], 'error', {timeout: 0})
            // setTimeout(() => {
            //   dispatch('wait/end', `post-${postId}`, {root: true})
            // }, 1000)
            reject(response)
          }
          if (response.body.hasOwnProperty('_ERROR_MESSAGE_LIST_')) {
            for (let errorMessage of response.body['_ERROR_MESSAGE_LIST_']) {
              commit('ADD_MESSAGE', {messageContent: errorMessage, messageType: 'error'})
              this._vm.flash(errorMessage, 'error', {timeout: 0})
            }
            // setTimeout(() => {
            //   dispatch('wait/end', `post-${postId}`, {root: true})
            // }, 1000)
            reject(response)
          }
          if (response.body.hasOwnProperty('_EVENT_MESSAGE_')) {
            commit('ADD_MESSAGE', {messageContent: response.body['_EVENT_MESSAGE_'], messageType: 'event'})
            this._vm.flash(response.body['_EVENT_MESSAGE_'], 'success', 5000)
          }
          if (response.body.hasOwnProperty('_EVENT_MESSAGE_LIST_')) {
            for (let eventMessage of response.body['_EVENT_MESSAGE_LIST_']) {
              commit('ADD_MESSAGE', {messageContent: eventMessage, messageType: 'event'})
              this._vm.flash(eventMessage, 'success', 5000)
            }
          }
          // setTimeout(() => {
          //     dispatch('wait/end', `post-${postId}`, {root: true})
          //   }, 1000)
          resolve(response)
        }, error => {
          console.log(error)
          reject(error)
        })
      }, 0)
    })
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
