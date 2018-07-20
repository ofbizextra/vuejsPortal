import Vuex from 'vuex'
import Vue from 'vue'
import queryString from 'query-string'
import constantes from './../../js/constantes'

Vue.use(Vuex)

const state = {
  components: {}
}

const mutations = {
  ADD_COMPONENT: (state, data) => {
    console.log('data : ' + data)
    let id = data.attributes.find(attr => attr.key === 'id')
    state.components[id.value] = data
  },
  REMOVE_COMPONENT: (state, id) => {
    state.components.slice(id)
  }
}

const getters = {
  component: state => (id) => { return state.components[id] },
  components: state => state.components
}

const actions = {
  addComponent({commit}, data) {
    console.log('data: ' + data)
    commit('ADD_COMPONENT', data)
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
