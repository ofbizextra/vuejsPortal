import Vuex from 'vuex'
import Vue from 'vue'
import queryString from 'query-string'
import constantes from './../../js/constantes'

Vue.use(Vuex)

const state = {
  Example: {},
  CurrentId: ''
}

const mutations = {
  ADD_EXAMPLE: (state, id) => {
    state.Example[id] = {}
  },
  ADD_DATA_TO_EXAMPLE: (state, id, data) => {
    state.Example[id].push(data)
  },
  SET_CURRENT_ID: (state, id) => {
    state.CurrentId = id
  }
}

const getters = {
  currentId: state => state.currentId
}

const actions = {
  addExample({commit}, id) {
    commit('ADD_EXAMPLE', id)
  },
  addDataToExample({commit}, id) {
    commit('ADD_DATA_TO_EXAMPLE', id, data)
  },
  setCurrentId({commit}, id) {
    commit('SET_CURRENT_ID', id)
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
