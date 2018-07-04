import Vue from 'vue'
import VueResource from 'vue-resource'

import App from './components/App.vue'
import Search from './components/Search.vue'
import List from './components/List.vue'
import VueForm from './components/VueForm'


Vue.use(VueResource)

Vue.component('search', Search)
Vue.component('list', List)
Vue.component('vue-form', VueForm)

new Vue({
  el: '#app',
  props: ['content'],
  render: h => h(App)
})
