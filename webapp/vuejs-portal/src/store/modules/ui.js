import Vuex from 'vuex'
import Vue from 'vue'
import queryString from 'query-string'
import constantes from './../../js/constantes'

Vue.use(Vuex)

const state = {
  currentPortalPage: '',
  currentPortalPageParams: {},
  currentPortalPageDetail: {},
  portalPages: {},
  portlets: {},
  portletTarget: {},
  containers: {},
  containerWatcher: {},
  areas: {},
  watchers: {},
  updateCpt: 0,
  errorMessageList: [],
  collapsibleStatus: {},
  dialogStatus: {}
}

const mutations = {
  SET_CURRENT_PORTAL_PAGE: (state, portalPageId) => {
    Vue.set(state, 'currentPortalPage', portalPageId)
  },
  SET_PORTAL_PAGE: (state, {portalPageId, portalPage}) => {
    Vue.set(state.portalPages, portalPageId, portalPage)
    Vue.set(state, 'currentPortalPageDetail', portalPage)
  },
  REMOVE_PORTAL_PAGE: (state, id) => {
    state.portalPages.slice(id)
  },
  SET_CURRENT_PORTAL_PAGE_PARAMS: (state, params) => {
    Vue.set(state, 'currentPortalPageParams', params)
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
  },
  INCREMENT_UPDATE_CPT: (state) => {
    Vue.set(state, 'updateCpt', state.updateCpt + 1)
  },
  ADD_ERROR_MESSAGE: (state, {errorMessage}) => {
    Vue.set(state, 'errorMessageList', [...state.errorMessageList, errorMessage])
  },
  DELETE_ERROR_MESSAGE: (state, {errorMessage}) => {
    state.errorMessageList.splice(state.errorMessageList.indexOf(errorMessage), 1)
  },
  SET_COLLAPSIBLE_STATUS: (state, {areaId, areaTarget}) => {
    Vue.set(state.collapsibleStatus, areaId, areaTarget)
  },
  SET_DIALOG_STATUS: (state, {dialogId, dialogStatus}) => {
    Vue.set(state.dialogStatus, dialogId, dialogStatus)
  }
}

const getters = {
  currentPortalPage: state => state.currentPortalPage,
  currentPortalPageDetail: state => state.currentPortalPageDetail,
  currentPortalPageParams: state => state.currentPortalPageParams,
  portalPage: state => (id) => {
    return state.portalPages[id]
  },
  portalPages: state => state.portalPages,
  column: state => ({portalPageId, columnSeqId}) => {
    return state.portalPages[portalPageId].listColumnPortlet.find(col => col.columnSeqId === columnSeqId)
  },
  portlet(state) {
    return (id) => {
      return state.portlets[id]
    }
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
  },
  updateCpt: state => state.updateCpt,
  errorMessageList: state => state.errorMessageList,
  collapsibleStatus(state) {
    return function (areaId) {
      return state.collapsibleStatus.hasOwnProperty(areaId) ? state.collapsibleStatus[areaId] : false
    }
  },
  dialogStatus(state) {
    return function (dialogId) {
      return state.dialogStatus.hasOwnProperty(dialogId) ? state.dialogStatus[dialogId] : false
    }
  },
  dialogs: state => state.dialogStatus
}

const actions = {
  setPortalPage({commit}, {portalPageId, portalPage}) {
    commit('SET_PORTAL_PAGE', {portalPageId, portalPage})
    commit('SET_CURRENT_PORTAL_PAGE', portalPageId)
  },
  setPortlet({commit, getters}, {portalPortletId, portletSeqId, params = {}}) {
    // this.$wait.start(portalPortletId + '-' + portletSeqId)
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        // this._vm.$wait.start(portalPortletId + '-' + portletSeqId)
        Vue.http.post(getters['backOfficeApi/apiUrl'] + getters['backOfficeApi/currentApi'] + constantes.showPortlet.path,
          queryString.stringify({
            portalPortletId: portalPortletId,
            ...params
          }),
          {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).then(response => {
          commit('SET_PORTLET', {portletId: portalPortletId + '-' + portletSeqId, data: response.body})
          // this._vm.$wait.end(portalPortletId + '-' + portletSeqId)
          // this.$wait.end(portalPortletId + '-' + portletSeqId)
          resolve(portalPortletId)
        }, error => {
          console.log(error)
          // this._vm.$wait.end(portalPortletId + '-' + portletSeqId)
          // this.$wait.end(portalPortletId + '-' + portletSeqId)
          reject()
        })
      }, 0)
    })
  },
  setPortletTarget({commit}, {portletId, target}) {
    commit('SET_PORTLET_TARGET', {portletId, target})
  },
  setContainer({commit, getters}, {containerName, containerTarget, params = {}}) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        Vue.http.post(getters['backOfficeApi/apiUrl'] + getters['backOfficeApi/currentApi'] + constantes.showPortlet.path,
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
      }, 0)
    })
  },
  setContainerWatcher({commit}, {watcherName, watcherTarget, params}) {
    commit('SET_CONTAINER_WATCHER', {watcherName, watcherTarget, params})
  },
  setArea({commit, dispatch}, {areaId, targetUrl, wait, params = {}}) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        wait.start(areaId)
        dispatch('incrementUpdateCpt')
        dispatch('backOfficeApi/doPost', {uri: constantes.hostUrl + targetUrl.replace('amp;', ''), params: params}, {root: true}
        ).then(response => {
          console.log({...response.body})
          if (response.body.hasOwnProperty('_ERROR_MESSAGE_')) {
            dispatch('addErrorMessage', {errorMessage: response.body['_ERROR_MESSAGE_']})
            reject()
          }
          if (response.body.hasOwnProperty('_ERROR_MESSAGE_LIST_')) {
            for (let errorMessage of response.body['_ERROR_MESSAGE_LIST_']) {
              dispatch('addErrorMessage', {errorMessage})
            }
            reject()
          }
          commit('SET_AREA', {areaId: areaId, areaContent: response.body})
          if (response.body.hasOwnProperty('viewEntities')) {
            let entities = []
            let records = []
            Object.keys(response.body.viewEntities).forEach((key) => {
              console.log('setEntity => ' + key)
              entities.push(dispatch('data/setEntity', {
                entityName: key,
                primaryKey: response.body.viewEntities[key].primaryKeys.join('-')
              }, {
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
              setTimeout(() => {
                wait.end(areaId)
              }, 0)
              resolve(areaId)
            })
          } else {
            setTimeout(() => {
              wait.end(areaId)
            }, 0)
            resolve(areaId)
          }
        }, error => {
          console.log(error)
          setTimeout(() => {
            wait.end(areaId)
          }, 0)
          reject(error)
        })
      }, 0)
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
  },
  incrementUpdateCpt({commit}) {
    commit('INCREMENT_UPDATE_CPT')
  },
  addErrorMessage({commit}, {errorMessage}) {
    commit('ADD_ERROR_MESSAGE', {errorMessage})
  },
  deleteErrorMessage({commit}, {errorMessage}) {
    commit('DELETE_ERROR_MESSAGE', {errorMessage})
  },
  initialize({dispatch}, location) {
    console.log('location : ', location)
    let origin = location.origin
    let pathname = location.pathname
    let path = origin + pathname
    let search = location.search
    console.log('path : ', path)
    console.log('Params : ', search)
    // PORTAL_PAGE_ID not defined in the scope but in the main application
    // eslint-disable-next-line
    let params = {portalPageId: this._vm.$route.params.portalPageId}
    search.substr(1).split('&amp;').forEach(param => {
      let tmp = param.split('=')
      params[tmp[0]] = tmp[1]
    })
    console.log('params : ', params)
    let api = pathname.substring(0, pathname.indexOf('/', 1)) + '/control'
    console.log('API ===> ' + api)
    dispatch('loadPortalPageDetail', {api: api, params: params})
  },
  loadPortalPageDetail({commit, dispatch}, {api, params}) {
    dispatch('backOfficeApi/setApi', api, {root: true})
    dispatch('backOfficeApi/doPost', {uri: constantes.hostUrl + api + constantes.portalPageDetail.path, params}, {root: true}).then(response => {
      let portalPage = response.body
      console.log('PortalPage : ', portalPage)
      commit('SET_CURRENT_PORTAL_PAGE_PARAMS', params)
      commit('SET_PORTAL_PAGE', {portalPageId: params.portalPageId, portalPage})
      commit('SET_CURRENT_PORTAL_PAGE', params.portalPageId)
    }, error => {
      console.log('Error during portalPage acquisition : ', error)
    })
  },
  setCollapsibleStatus({commit}, {areaId, areaTarget}) {
    commit('SET_COLLAPSIBLE_STATUS', {areaId, areaTarget})
  },
  setDialogStatus({commit}, {dialogId, dialogStatus}) {
    commit('SET_DIALOG_STATUS', {dialogId, dialogStatus})
  },
  closeAllDialogs({commit, getters}) {
    for (let dialogId of Object.keys(getters.dialogs)) {
      commit('SET_DIALOG_STATUS', {dialogId, dialogStatus: false})
    }
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
