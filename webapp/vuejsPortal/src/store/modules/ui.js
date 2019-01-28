import Vuex from 'vuex'
import Vue from 'vue'
import queryString from 'query-string'
import constantes from './../../js/constantes'

Vue.use(Vuex)

const state = {
  currentPortalPage: '',
  portalPages: {},
  portlets: {},
  portletTarget: {}
}

const mutations = {
  SET_CURRENT_PORTAL_PAGE: (state, portalPageId) => {
    state.currentPortalPage = portalPageId
  },
  SET_PORTAL_PAGE: (state, {portalPageId, portalPage}) => {
    Vue.set(state.portalPages, portalPageId, portalPage)
  },
  REMOVE_PORTAL_PAGE: (state, id) => {
    state.portalPages.slice(id)
  },
  SET_PORTLET: (state, {portletId, data}) => {
    Vue.set(state.portlets, portletId, data)
  },
  SET_PORTLET_TARGET: (state, {portletId, target}) => {
    Vue.set(state.portletTarget, portletId, target)
  }
}

const getters = {
  currentPortalPage: state => state.currentPortalPage,
  currentPortalPageDetail: state => state.portalPages[state.currentPortalPage],
  portalPage: state => (id) => { return state.portalPages[id] },
  portalPages: state => state.portalPages,
  column: state => ({portalPageId, columnSeqId}) => { return state.portalPages[portalPageId].listColumnPortlet.find(col => col.columnSeqId === columnSeqId)},
  portlet(state) {
    return (id) => { return state.portlets[id] }
  },
  portlets: state => state.portlets,
  portletTarget(state) {
    return function (id) {
      return state.portletTarget.hasOwnProperty(id) ? state.portletTarget[id] : null
    }
  }
}

const actions = {
  setPortalPage({commit}, {portalPageId, portalPage}) {
    commit('SET_PORTAL_PAGE', {portalPageId, portalPage})
    commit('SET_CURRENT_PORTAL_PAGE', portalPageId)
  },
  setPortlet({commit}, {portalPortletId, portletSeqId, params = {}}) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        Vue.http.post(constantes.apiUrl + constantes.showPortlet.path,
          queryString.stringify({
            portalPortletId: portalPortletId,
            ...params
          }),
          {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).then(response => {
          commit('SET_PORTLET', {portletId: portalPortletId + '-' + portletSeqId, data: response.body})
          resolve(portalPortletId)
        }, error => {
          console.log(error)
          reject()
        })
      }, 1000)
    })
  },
  setPortletTarget({commit}, {portletId, target}) {
    commit('SET_PORTLET_TARGET', {portletId , target})
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
