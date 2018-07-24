import Vuex from 'vuex'
import Vue from 'vue'
import queryString from 'query-string'
import constantes from './../../js/constantes'

Vue.use(Vuex)

const state = {
  example: {},
  currentId: ''
}

const mutations = {
  ADD_EXAMPLE: (state, id) => {
    state.example[id] = {}
  },
  ADD_DATA_TO_EXAMPLE: (state, data) => {
    state.example[data.id][data.key] = data.value
  },
  SET_CURRENT_ID: (state, id) => {
    state.currentId = id
  }
}

const getters = {
  currentId: state => state.currentId,
  dataFromExample: state => (data) => {
    return state.example[data.id][data.key]
  }
}

const actions = {
  addExample({commit}, id) {
    commit('ADD_EXAMPLE', id)
  },
  addDataToExample({commit}, data) {
    commit('ADD_DATA_TO_EXAMPLE', data)
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
