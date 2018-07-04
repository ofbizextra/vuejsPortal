import Vue from 'vue'
import VueResource from 'vue-resource'

import App from './components/App.vue'
import Search from './components/Search.vue'
import List from './components/List.vue'
import VueForm from './components/VueForm'
import VueField from './components/VueField'


Vue.use(VueResource)

Vue.component('search', Search)
Vue.component('list', List)
Vue.component('vue-form', VueForm)
Vue.component('vue-field', VueField)

new Vue({
  el: '#app',
  props: ['content'],
  render: h => h(App)
})
