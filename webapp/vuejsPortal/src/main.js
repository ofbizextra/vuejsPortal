import Vue from 'vue'
import VueResource from 'vue-resource'
import VueRouter from 'vue-router'

import App from './components/App'
import Search from './components/Search'
import List from './components/List'
import Login from './components/Login'
import Portal from './components/Portal'
import EditExampleLayer from './components/EditExampleLayer'
import FindExampleLayer from './components/FindExampleLayer'
import ListExampleLayer from './components/ListExampleLayer'
import VueForm from './components/VueForm'
import VueField from './components/VueField'
import VueDropDown from './components/VueDropDown'
import VueHidden from './components/VueHidden'
import VueHyperlink from './components/VueHyperlink'
import VueRadio from './components/VueRadio'
import VueSubmit from './components/VueSubmit'
import VueTextFind from './components/VueTextFind'
import VueLabel from './components/VueLabel'
import VueOption from './components/VueOption'
import VueTitle from './components/VueTitle'
import VueText from './components/VueText'
import VueTextArea from './components/VueTextArea'
import VueDateTime from './components/VueDateTime'
import VueError from './components/VueError'
import VueTooltip from './components/VueTooltip'
import VueAsterisks from './components/VueAsterisks'
import VueTable from './components/VueTable'
import VueTr from './components/VueTr'
import VueTd from './components/VueTd'
import Test from './components/Test'
import VueSortField from './components/VueSortField'
import VueColumnPortlet from './components/VueColumnPortlet'
import VuePortlet from './components/VuePortlet'

import store from './store'

Vue.use(VueResource)
Vue.use(VueRouter)

Vue.component('login', Login)
Vue.component('portal', Portal)
Vue.component('edit-example-layer', EditExampleLayer)
Vue.component('find-example-layer', FindExampleLayer)
Vue.component('list-example-layer', ListExampleLayer)
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
Vue.component('vue-option', VueOption)
Vue.component('vue-title', VueTitle)
Vue.component('vue-text', VueText)
Vue.component('vue-text-area', VueTextArea)
Vue.component('vue-date-time', VueDateTime)
Vue.component('vue-error', VueError)
Vue.component('vue-tooltip', VueTooltip)
Vue.component('vue-asterisks', VueAsterisks)
Vue.component('vue-table', VueTable)
Vue.component('vue-tr', VueTr)
Vue.component('vue-td', VueTd)
Vue.component('test', Test)
Vue.component('vue-sort-field', VueSortField)
Vue.component('vue-column-portlet', VueColumnPortlet)
Vue.component('vue-portlet', VuePortlet)

Vue.mixin({
  methods: {
    parseProps() {
      if (this.$props && this.$props.props) {
        let props = this.$props.props;
        let data = {}
        if (props) {
          this.props.attributes.map(attr => {
            if (attr.value === 'false') {
              data[attr.key] = false
            }else if (attr.value === 'true') {
              data[attr.key] = true
            }else {
              data[attr.key] = attr.value
            }
          })
          return data
        }
      }
    }
  }
})

const router = new VueRouter({
  mode: 'hash',
routes: [
    { path: '/', component: Portal, beforeEnter: requireAuth },
    { path: '/findExampleLayer', component: FindExampleLayer, beforeEnter: requireAuth },
    { path: '/editExampleLayer', component: EditExampleLayer, beforeEnter: requireAuth },
    { path: '/editExampleLayer/:id', component: EditExampleLayer, beforeEnter: requireAuth },
    { path: '/listExampleLayer', component: ListExampleLayer, beforeEnter: requireAuth},
    { path: '/test', component: Test, beforeEnter: requireAuth},
    { path: '/login', component: Login, beforeEnter: requireAuth }
  ]
})

function requireAuth (to, from, next) {
  function proceed () {
    if (store.getters['login/isLoggedIn']) {
      if (to.path === '/login') {
        console.log("Already logged in -> redirect to /home")
        next('/')
      } else {
        console.log("Authorized")
        next()
      }
    } else {
      if (to.path === '/login') {
        next()
      } else {
        console.log("You are not logged in redirect to login")
        next('/login')
      }
    }
  }
  console.log('Routing ...')
  if (store.getters['login/pending']()) {
    console.log('Pending ...')
    store.watch(store.getters['login/pending'], () => {
      if (!store.getters['login/pending']()) {
        console.log('Proceed ...')
        proceed()
      }
    })
  } else {
    console.log('Proceed ...')
    proceed()
  }
}

new Vue({
  el: '#app',
  props: ['content'],
  store: store,
  router: router,
  render: h => h(App)
})
