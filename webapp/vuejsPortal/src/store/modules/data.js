import Vuex from 'vuex'
import Vue from 'vue'
import queryString from 'query-string'
import constantes from './../../js/constantes'

Vue.use(Vuex)

const state = {
  example: {},
  currentId: '',
  entities: {}
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
  },
  SET_ENTITY: (state, data) => {
    state.entities[data.entityName] = {
      list: data.list,
      primaryKey: data.primaryKey
    }
  },
  SET_ENTITY_ROW: (state, data) => {
    state.entities[data.entityName].list[data.content[state.entities[data.entityName].primaryKey]] = data.content
  }
}

const getters = {
  currentId: state => state.currentId,
  dataFromExample: state => (data) => {
    return state.example[data.id][data.key]
  },
  entity: state => entityName => {
    return state.entities[entityName]
  },
  entityRow: state => (entityName, id) => {
    return state.entities[entityName].list.find(row => row[state.entities[entityName]].primaryKey === id)
  }
}

const actions = {
  addExample({commit}, id) {
    commit('ADD_EXAMPLE', id)
  },
  addDataToExample({commit, state}, data) {
    if (!state.example[data.id]) {
      commit('ADD_EXAMPLE', data.id)
    }
    commit('ADD_DATA_TO_EXAMPLE', data)
  },
  setCurrentId({commit}, id) {
    commit('SET_CURRENT_ID', id)
  },
  setEntity({commit}, data) {
    console.log('setEntity : ' + data)
    commit('SET_ENTITY', data)
  },
  setEntityRow({commit}, data) {
    console.log('setEntityRow : ' + data)
    commit('SET_ENTITY_ROW', data)
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
