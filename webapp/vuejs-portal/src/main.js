import Vue from 'vue'
import VueResource from 'vue-resource'
import VueRouter from 'vue-router'

import VueWait from 'vue-wait'
import {BreedingRhombusSpinner} from 'epic-spinners'
import BlockUI from 'vue-blockui'
import VueFlashMessage from 'vue-flash-message'
import VModal from 'vue-js-modal'
import VueAutosuggest from 'vue-autosuggest'
import VueFlatPickr from 'vue-flatpickr-component';
import 'flatpickr/dist/flatpickr.css';
import _ from 'lodash'
import VueTheMask from 'vue-the-mask'

import App from './components/App'
import Search from './components/Search'
import Login from './components/Login'
import Portal from './components/Portal'
import VueForm from './components/VueForm'
import VueField from './components/VueField'
import VueDropDownField from './components/VueDropDownField'
import VueHiddenField from './components/VueHiddenField'
import VueHyperlinkTitle from './components/VueHyperlinkTitle'
import VueRadioField from './components/VueRadioField'
import VueSubmitField from './components/VueSubmitField'
import VueTextFindField from './components/VueTextFindField'
import VueLabel from './components/VueLabel'
import VueOption from './components/VueOption'
import VueDisplayField from './components/VueDisplayField'
import VueTextField from './components/VueTextField'
import VueLookupField from './components/VueLookupField'
import VueTextAreaField from './components/VueTextAreaField'
import VueDateTimeField from './components/VueDateTimeField'
import VueError from './components/VueError'
import VueAsterisks from './components/VueAsterisks'
import VueTable from './components/VueTable'
import VueTr from './components/VueTr'
import VueTd from './components/VueTd'
import VueSortField from './components/VueSortField'
import VueColumnPortlet from './components/VueColumnPortlet'
import VuePortlet from './components/VuePortlet'
import VueThead from './components/VueThead'
import VueListWrapper from './components/VueListWrapper'
import VueHeader from './components/VueHeader'
import VueHeaderRow from './components/VueHeaderRow'
import VueHeaderRowCell from './components/VueHeaderRowCell'
import VueHeaderRowFormCell from './components/VueHeaderRowFormCell'
import VueItemRow from './components/VueItemRow'
import VueItemRowCell from './components/VueItemRowCell'
import VueItemRowFormCell from './components/VueItemRowFormCell'
import VueSingleWrapper from './components/VueSingleWrapper'
import VueFieldRow from './components/VueFieldRow'
import VueFieldRowTitleCell from './components/VueFieldRowTitleCell'
import VueFieldRowWidgetCell from './components/VueFieldRowWidgetCell'
import VueFieldGroup from './components/VueFieldGroup'
import VueFieldTitle from './components/VueFieldTitle'
import VueHyperlinkField from './components/VueHyperlinkField'
import VueNextPrev from './components/VueNextPrev'
import VueScreenlet from './components/VueScreenlet'
import VueScreenletSubWidget from './components/VueScreenletSubWidget'
import VueMenu from './components/VueMenu'
import VueMenuItem from './components/VueMenuItem'
import VueLink from './components/VueLink'
import VueContainer from './components/VueContainer'
import VueMessageList from './components/VueMessageList'
import VueLogin from './components/VueLogin'
import VueFormatEmptySpace from './components/VueFormatEmptySpace'
import VuePlatformSpecific from './components/VuePlatformSpecific'
import VueNavMenu from './components/VueNavMenu'
import VueNavMenuItem from './components/VueNavMenuItem'
import Screen from './components/Screen'
import VuePasswordField from './components/VuePasswordField'
import VueNavMenuItemInline from './components/VueNavMenuItemInline'
import VueHorizontalSeparator from './components/VueHorizontalSeparator'
import AppMenu from './components/AppMenu'
import AppWait from './components/AppWait'
import AppCpt from './components/AppCpt'

// Platform Specific

import ContactMech from './components/platformSpecific/ContactMech'
import store from './store'
import vuetify from '@/plugins/vuetify';

Vue.use(VueResource)
Vue.use(VueRouter)

Vue.use(VueWait)
Vue.use(BlockUI)
Vue.use(VueFlashMessage, {
  messageOptions: {
    timeout: 8000,
    pauseOnInteract: true
  }
})
require('vue-flash-message/dist/vue-flash-message.min.css')
Vue.use(VModal)
Vue.use(VueAutosuggest)
Vue.use(VueFlatPickr)
Vue.use(VueTheMask)

Vue.component('login', Login)
Vue.component('portal', Portal)
Vue.component('screen', Screen)
Vue.component('search', Search)
Vue.component('vue-form', VueForm)
Vue.component('vue-field', VueField)
Vue.component('vue-drop-down-field', VueDropDownField)
Vue.component('vue-hidden-field', VueHiddenField)
Vue.component('vue-hyperlink-title', VueHyperlinkTitle)
Vue.component('vue-hyperlink-field', VueHyperlinkField)
Vue.component('vue-radio-field', VueRadioField)
Vue.component('vue-submit-field', VueSubmitField)
Vue.component('vue-text-find-field', VueTextFindField)
Vue.component('vue-label', VueLabel)
Vue.component('vue-option', VueOption)
Vue.component('vue-display-field', VueDisplayField)
Vue.component('vue-text-field', VueTextField)
Vue.component('vue-lookup-field', VueLookupField)
Vue.component('vue-text-area-field', VueTextAreaField)
Vue.component('vue-date-time-field', VueDateTimeField)
Vue.component('vue-field-title', VueFieldTitle)
Vue.component('vue-error', VueError)
Vue.component('vue-asterisks', VueAsterisks)
Vue.component('vue-table', VueTable)
Vue.component('vue-tr', VueTr)
Vue.component('vue-td', VueTd)
Vue.component('vue-sort-field', VueSortField)
Vue.component('vue-column-portlet', VueColumnPortlet)
Vue.component('vue-portlet', VuePortlet)
Vue.component('vue-thead', VueThead)
Vue.component('vue-list-wrapper', VueListWrapper)
Vue.component('vue-header', VueHeader)
Vue.component('vue-header-row', VueHeaderRow)
Vue.component('vue-header-row-cell', VueHeaderRowCell)
Vue.component('vue-header-row-form-cell', VueHeaderRowFormCell)
Vue.component('vue-item-row', VueItemRow)
Vue.component('vue-item-row-cell', VueItemRowCell)
Vue.component('vue-item-row-form-cell', VueItemRowFormCell)
Vue.component('vue-single-wrapper', VueSingleWrapper)
Vue.component('vue-field-row', VueFieldRow)
Vue.component('vue-field-row-title-cell', VueFieldRowTitleCell)
Vue.component('vue-field-row-widget-cell', VueFieldRowWidgetCell)
Vue.component('vue-field-group', VueFieldGroup)
Vue.component('vue-next-prev', VueNextPrev)
Vue.component('spinner', BreedingRhombusSpinner)
Vue.component('vue-screenlet', VueScreenlet)
Vue.component('vue-screenlet-sub-widget', VueScreenletSubWidget)
Vue.component('vue-menu', VueMenu)
Vue.component('vue-menu-item', VueMenuItem)
Vue.component('vue-link', VueLink)
Vue.component('vue-container', VueContainer)
Vue.component('vue-message-list', VueMessageList)
Vue.component( 'vue-login', VueLogin)
Vue.component('vue-format-empty-space', VueFormatEmptySpace)
Vue.component('vue-platform-specific', VuePlatformSpecific)
Vue.component('vue-nav-menu', VueNavMenu)
Vue.component('vue-nav-menu-item', VueNavMenuItem)
Vue.component('vue-password-field', VuePasswordField)
Vue.component('vue-nav-menu-item-inline', VueNavMenuItemInline)
Vue.component('vue-horizontal-separator', VueHorizontalSeparator)
Vue.component('app-menu', AppMenu)
Vue.component('app-wait', AppWait)
Vue.component('app-cpt', AppCpt)

// Platform Specific
Vue.component('ContactMech', ContactMech)

Object.defineProperty(Vue.prototype, '$_', {value: _})

const showDebug = false
Object.defineProperty(Vue.prototype, '$debug', {value: (process.env.NODE_ENV !== 'production') && showDebug})

Vue.mixin({
  methods: {
    parseProps() {
      if (this.$props && this.$props.props) {
        let props = this.$props.props
        let data = {}
        if (props) {
          this.props.attributes.map(attr => {
            if (attr.value === 'false') {
              data[attr.key] = false
            } else if (attr.value === 'true') {
              data[attr.key] = true
            } else {
              data[attr.key] = attr.value
            }
          })
          return data
        }
      }
    },
    getNestedObject(nestedObject, pathArray) {
      return pathArray.reduce((obj, key) =>
        (obj && obj[key] !== 'undefined') ? obj[key] : undefined, nestedObject)
    },
    async asyncForEach(array, callback) {
      for (let index = 0; index < array.length; index++) {
        await callback(array[index], index, array)
      }
    }
  }
})

const router = new VueRouter({
  mode: 'hash',
  routes: [
    {path: '/login', component: Login, beforeEnter: requireAuth},
    {path: '/portalPage/:portalPageId', props: true, component: Portal, beforeEnter: requireAuth},
    {path: '/screen/:screenId', props: true, component: Screen, beforeEnter: requireAuth},
  ]
})

function requireAuth(to, from, next) {
  function proceed() {
    if (store.getters['login/isLoggedIn']) {
      if (to.path === '/login') {
        // console.log("Already logged in -> redirect to /home")
        next('/')
      } else {
        // console.log("Authorized")
        next()
      }
    } else {
      if (to.path === '/login') {
        next()
      } else {
        // console.log("You are not logged in redirect to login")
        next('/login')
      }
    }
  }

  // console.log('Routing ...')
  if (store.getters['login/pending']()) {
    // console.log('Pending ...')
    store.watch(store.getters['login/pending'], () => {
      if (!store.getters['login/pending']()) {
        // console.log('Proceed ...')
        proceed()
      }
    })
  } else {
    // console.log('Proceed ...')
    proceed()
  }
}

new Vue({
  el: '#app',
  props: ['content'],
  store: store,
  router: router,
  wait: new VueWait({
    useVuex: true
  }),
  vuetify,
  css: {
    extract: false,
    loaderOptions: {
      sass: {
        data: `@import "~@./sass/main.scss"`,
      },
      css: {
        extract: false
      }
    },
  },
  render: h => h(App)
})
