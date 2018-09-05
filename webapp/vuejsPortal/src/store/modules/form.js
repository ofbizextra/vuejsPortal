import Vuex from 'vuex'
import Vue from 'vue'
import queryString from 'query-string'
import constantes from './../../js/constantes'

Vue.use(Vuex)

const state = {
  forms: {},
}

const mutations = {
  ADD_FORM: (state, id) => {
    state.forms[id] = {}
  },
  SET_FIELD_TO_FORM: (state, data) => {
    state.forms[data.formId][data.key] = data.value
  }
}

const getters = {
  forms: state => state.forms,
  form: state => {
    return function (formId) {
      return state.forms[formId]
    }
  },
  fieldInForm: state => (data) => {
    return state.forms[data.formId][data.key]
  }
}

const actions = {
  addForm({commit}, formId) {
    commit('ADD_FORM', formId)
  },
  setFieldToForm({commit, state}, data) {
    if (!state.forms[data.formId]) {
      commit('ADD_FORM', data.formId)
    }
    commit('SET_FIELD_TO_FORM', data)
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
