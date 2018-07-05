import Vue from 'vue'
import VueResource from 'vue-resource'

import App from './components/App.vue'
import Search from './components/Search.vue'
import List from './components/List.vue'
import VueForm from './components/VueForm'
import VueField from './components/VueField'
import VueDropDown from './components/VueDropDown'
import VueHidden from './components/VueHidden'
import VueHyperlink from './components/VueHyperlink'
import VueRadio from './components/VueRadio'
import VueSubmit from './components/VueSubmit'
import VueTextFind from './components/VueTextFind'
import VueLabel from './components/VueLabel'

Vue.use(VueResource)

Vue.component('search', Search)
Vue.component('list', List)
Vue.component('vue-form', VueForm)
Vue.component('vue-field', VueField)
Vue.component('vue-drop-down', VueDropDown)
Vue.component('vue-hidden', VueHidden)
Vue.component('vue-hyperlink', VueHyperlink)
Vue.component('vue-radio', VueRadio)
Vue.component('vue-submit', VueSubmit)
Vue.component('vue-text-find', VueTextFind)
Vue.component('vue-label', VueLabel)

new Vue({
  el: '#app',
  props: ['content'],
  render: h => h(App)
})
