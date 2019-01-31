import Vuex from 'vuex'
import Vue from 'vue'
import queryString from 'query-string'
import constantes from './../../js/constantes'

Vue.use(Vuex)

const state = {
  currentPortalPage: '',
  portalPages: {},
  portlets: {},
  portletTarget: {},
  containers: {},
  containerWatcher: {},
  areas: {},
  watchers: {},
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
  },
  SET_CONTAINER: (state, {containerName, content}) => {
    Vue.set(state.containers, containerName, content)
  },
  SET_CONTAINER_WATCHER: (state, {watcheName, watcherTarget, params}) => {
    Vue.set(state.containerWatcher, watcheName, watcherTarget.isEmpty() ? null : {portalPageId: watcherTarget, params})
  },
  SET_AREA: (state, {areaId, areaContent}) => {
    Vue.set(state.areas, areaId, areaContent)
  },
  DELETE_AREA: (state, {areaId}) => {
    Vue.delete(state.areas, areaId)
  },
  SET_WATCHER: (state, {watcherName, watcherContent}) => {
    Vue.set(state.watchers, watcherName, watcherContent)
  },
  ADD_TO_WATCHER: (state, {watcherName, watcherContent}) => {
    if (state.watchers.hasOwnProperty(watcherName)) {
      Vue.set(state.watchers, watcherName, {...state.watchers[watcherName], ...watcherContent})
    } else {
      Vue.set(state.watchers, watcherName, watcherContent)
    }
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
  },
  container(state) {
    return function (containerName) {
      return state.containers.hasOwnProperty(containerName) ? state.containers[containerName] : null
    }
  },
  containerTarget(state) {
    return function (watcherName) {
      return state.containerWatcher.hasOwnProperty(watcherName) ? state.containerWatcher[watcherName] : null
    }
  },
  area(state) {
    return function (areaId) {
      return state.areas.hasOwnProperty(areaId) ? state.areas[areaId] : null
    }
  },
  watcher(state) {
    return function (watcherName) {
      return state.watchers.hasOwnProperty(watcherName) ? state.watchers[watcherName] : null
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
  },
  setContainer({commit}, {containerName, containerTarget, params = {}}) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        Vue.http.post(constantes.apiUrl + constantes.showPortlet.path,
          queryString.stringify({
            portalPortletId: containerTarget,
            ...params
          }),
          {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).then(response => {
          commit('SET_CONTAINER', {containerName, content: response.body})
          resolve(containerName)
        }, error => {
          console.log(error)
          reject()
        })
      }, 1000)
    })
  },
  setContainerWatcher({commit}, {watcherName, watcherTarget, params}) {
    commit('SET_CONTAINER_WATCHER', {watcherName, watcherTarget, params})
  },
  setArea({commit, dispatch}, {areaId, targetUrl, params = {}}) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        Vue.http.post(constantes.hostUrl + targetUrl.replace('amp;',''),
          queryString.stringify({...params}),
          {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).then(response => {
          console.log({...response.body})
          commit('SET_AREA', {areaId: areaId, areaContent: response.body})
          if (response.body.hasOwnProperty('viewEntities')) {
            let entities = []
            let records = []
            Object.keys(response.body.viewEntities).forEach((key) => {
              console.log('setEntity => ' + key)
              entities.push(dispatch('data/setEntity', {
                entityName: key,
                primaryKey: response.body.viewEntities[key].primaryKeys.join('-')
              },{
                root: true
              }))
            })
            Promise.all(entities).then(all => {
              console.log('all entity have been created')
              all.forEach((entity => {
                console.log('creating record for entity: ' + entity.entityName)
                response.body.viewEntities[entity.entityName].list.forEach((record, index) => {
                  console.log(entity.entityName + ': creating record number ' + index + ': ', record)
                  if (record.stId !== null) {
                    let data = {
                      entityName: entity.entityName,
                      primaryKey: record.stId,
                      data: record
                    }
                    records.push(dispatch('data/setEntityRow', data, {root: true}))
                  }
                })
              }))
            })
            Promise.all(records).then(() => {
                resolve(areaId)
            })
          } else {
            resolve(areaId)
          }
        }, error => {
          console.log(error)
          reject(error)
        })
      }, 1000)
    })
  },
  deleteArea({commit}, {areaId}) {
    commit('DELETE_AREA', {areaId})
  },
  setWatcher({commit}, {watcherName, watcherContent}) {
    commit('SET_WATCHER', {watcherName, watcherContent})
  },
  addToWatcher({commit}, {watcherName, watcherContent}) {
    commit('ADD_TO_WATCHER', {watcherName, watcherContent})
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
