import Vue from 'vue'
import VueResource from 'vue-resource'
import App from './components/App.vue'
import Search from './components/Search.vue'
import List from './components/List.vue'


Vue.use(VueResource)

Vue.component('search', Search)
Vue.component('list', List)

new Vue({
  el: '#app',
  props: ['content'],
  render: h => h(App)
})
