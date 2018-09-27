import Vuex from 'vuex'
import Vue from 'vue'
import {enhancedGetters} from 'vuex-strong-cache'
import queryString from 'query-string'
import constantes from './../../js/constantes'

Vue.use(Vuex)

const state = {
  example: {},
  currentId: '',
  cpt: 0,
  entities: {},
  isUpdating: false,
  updatingCpt: 0,
  watchers: {},
  watchersCpt: 0
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
      list: {},
      primaryKey: data.primaryKey
    }
  },
  SET_ENTITY_ROW: (state, data) => {
    if (state.entities[data.entityName] === undefined) {
      return 'Entities doesn\'t exist'
    }
    if (!state.entities[data.entityName].list[data.primaryKey]) {
      state.entities[data.entityName].list[data.primaryKey] = {};
    }
    Object.keys(data.data).forEach(key => {
      state.entities[data.entityName].list[data.primaryKey][key] = data.data[key]
    })
  },
  START_UPDATE: (state) => {
    state.updatingCpt++
  },
  STOP_UPDATE: (state) => {
    state.updatingCpt--
    if (state.updatingCpt === 0) {
      state.cpt++
    }
  },
  SET_WATCHER: (state, {watcherName, params}) => {
    state.watchers[watcherName] = params
    state.watchersCpt++
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
  entityRow: state => ({entityName, id}) => {
    return state.entities[entityName].list.find(row => row.stId === id)
  },
  entityRowAttribute(state) {
    let temp = state.cpt
    return function ({entityName, id, attribute}) {
      return state.entities[entityName].list[id][attribute]
    }
  },
  watcher(state) {
    let temp = state.watchersCpt
    return function (watcherName) {
      return state.watchers[watcherName]
    }
  },
  isUpdating: state => state.updatingCpt > 0,
  updatingCpt: state => state.updatingCpt
}

const actions = {
  setEntity({commit}, data) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        if (!state.entities[data.entityName]) {
          commit('SET_ENTITY', data);
        }
        resolve(data)
      }, 0)
    })
  },
  setEntityRow({commit}, data) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        commit('SET_ENTITY_ROW', data)
        resolve(data)
      }, 0)
    })
  },
  setWatcher({commit}, data) {
    console.log('data/setWatcher : ', data)
    commit('SET_WATCHER', data)
  },
  startUpdate({commit}) {
    commit('START_UPDATE')
  },
  stopUpdate({commit}) {
    commit('STOP_UPDATE')
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
