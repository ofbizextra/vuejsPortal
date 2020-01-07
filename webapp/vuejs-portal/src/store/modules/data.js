import Vuex from 'vuex'
import Vue from 'vue'

Vue.use(Vuex)

const state = {
  entities: {},
  watchers: {}
}

const mutations = {
  SET_ENTITY: (state, data) => {
    Vue.set(state.entities, data.entityName, {
      list: {},
      primaryKey: data.primaryKey
    })
  },
  SET_ENTITY_ROW: (state, data) => {
    if (state.entities[data.entityName] === undefined) {
      return 'Entities doesn\'t exist'
    }
    if (!state.entities[data.entityName].list[data.primaryKey]) {
      Vue.set(state.entities[data.entityName].list, data.primaryKey, {})
    }
    Object.keys(data.data).forEach(key => {
      Vue.set(state.entities[data.entityName].list[data.primaryKey], key, data.data[key])
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
  },
  SET_WATCHER_ATTRIBUTES: (state, {watcherName, params}) => {
    Vue.set(state.watchers, watcherName, {...state.watchers[watcherName], ...params})
  }
}

const getters = {
  entities: state => state.entities,
  entity: state => entityName => {
    return state.entities[entityName]
  },
  entityRow: state => ({entityName, id}) => {
    return state.entities[entityName].list.find(row => row.stId === id)
  },
  entityRowAttribute(state) {
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
    return function (watcherName) {
      if (watcherName.includes(',')) {
        watcherName = watcherName.replace(/\s+/g, '')
        let watchers = watcherName.split(',')
        let response = {}
        watchers.forEach(watcher => response = {...state.watchers[watcher], ...response})
        return response
      } else {
        return state.watchers[watcherName]
      }
    }
  }
}

const actions = {
  setEntity({commit}, data) {
    return new Promise((resolve) => {
      setTimeout(() => {
        if (!state.entities[data.entityName]) {
          commit('SET_ENTITY', data);
        }
        resolve(data)
      }, 0)
    })
  },
  setEntityRow({commit}, data) {
    return new Promise((resolve) => {
      setTimeout(() => {
        commit('SET_ENTITY_ROW', data)
        resolve(data)
      }, 0)
    })
  },
  setWatcher({commit}, data) {
    if (this.$debug) {
      console.log('data/setWatcher : ', data)
    }
    commit('SET_WATCHER', data)
  },
  setWatcherAttributes({commit}, data) {
    if (this.$debug) {
      console.log('data/setWatcherAttributes : ', data)
    }
    commit('SET_WATCHER_ATTRIBUTES', data)
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
