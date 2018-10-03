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
  cpt1: 0,
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
    Vue.set(state.entities, data.entityName, {
      list: {},
      primaryKey: data.primaryKey
    })
    // state.entities[data.entityName] = {
    //   list: {},
    //   primaryKey: data.primaryKey
    // }
  },
  SET_ENTITY_ROW: (state, data) => {
    if (state.entities[data.entityName] === undefined) {
      return 'Entities doesn\'t exist'
    }
    if (!state.entities[data.entityName].list[data.primaryKey]) {
      Vue.set(state.entities[data.entityName].list, data.primaryKey, {})
      // state.entities[data.entityName].list[data.primaryKey] = {};
    }
    Object.keys(data.data).forEach(key => {
      Vue.set(state.entities[data.entityName].list[data.primaryKey], key, data.data[key])
      // state.entities[data.entityName].list[data.primaryKey][key] = data.data[key]
    })
  },
  START_UPDATE: (state) => {
    state.updatingCpt++
  },
  STOP_UPDATE: (state) => {
      state.cpt++
  },
  INCREMENT_CPT1: (state) => {
    state.cpt1++
  },
  SET_WATCHER: (state, {watcherName, params}) => {
    Vue.set(state.watchers, watcherName, {...params})
    // state.watchers[watcherName] = {...params}
    // state.watchersCpt++
  }
}

const getters = {
  currentId: state => state.currentId,
  dataFromExample: state => (data) => {
    return state.example[data.id][data.key]
  },
  entities: state => state.entities,
  entity: state => entityName => {
    return state.entities[entityName]
  },
  entityRow: state => ({entityName, id}) => {
    return state.entities[entityName].list.find(row => row.stId === id)
  },
  entityRowAttribute(state) {
    // let temp = state.cpt
    return function ({entityName, id, attribute}) {
      if (state.entities[entityName] === undefined ||
        state.entities[entityName].list[id] === undefined ||
        state.entities[entityName].list[id][attribute] === undefined
      ) {
        return ''
      } else {
        return state.entities[entityName].list[id][attribute]
      }
    }
  },
  watcher(state) {
    let temp = state.watchersCpt
    return function (watcherName) {
      return state.watchers[watcherName]
    }
  },
  isUpdating: state => state.updatingCpt > 0,
  updatingCpt: state => state.updatingCpt,
  cpt: state => state.cpt,
  cpt1: state => state.cpt1,
  cptWithParams(state) {
    return function (params) {
      if (state.entities[params.entityName] === undefined) {
        return state.entities[params.entityName]
      } else {
        return state.entities[params.entityName].list[params.key][params.attribute]
      }
    }
  }
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
  },
  incrementCpt1({commit}) {
    commit('INCREMENT_CPT1')
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
