import Vue from 'vue'
import Vuex from 'vuex'

import login from './modules/login'
import ui from './modules/ui'
import data from './modules/data'
import form from './modules/form'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    login,
    ui,
    data,
    form
  },
  strict: process.env.NODE_ENV !== 'production'
})
