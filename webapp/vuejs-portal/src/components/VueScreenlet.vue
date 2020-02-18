<template>
  <div id="vue-screenlet">
    <div :id="data.id + '-' + data.name" class="ma-1">
      <v-toolbar v-if="data.showMore" dense color="primary" dark class="screenlet-title-bar">
        <v-toolbar-title class="title">{{data.title}}</v-toolbar-title>
        <v-spacer></v-spacer>
        <vue-nav-menu v-if="navMenu" :props="navMenu" :updateStore="updateStore"></vue-nav-menu>
        <v-btn id="toggleCollapse" icon v-if="collapsible" @click="toggle"><v-icon>{{ collapseIcon }}</v-icon></v-btn>
      </v-toolbar>
      <v-expand-transition>
      <v-card :id="collapsibleAreaId" v-show="!collapsed">
        <v-card-text class="pa-1">
        <div
          v-for="(component, key) in props.children"
          :key="key"
          v-bind:is="constants.components[component.name]"
          :props="component"
          :updateStore="updateStore"
          class="mt-2">
        </div>
        </v-card-text>
      </v-card>
      </v-expand-transition>
    </div>
  </div>
</template>

<script>
  import constants from '../js/constants'
  import {mapGetters} from 'vuex'

  export default {
    name: "VueScreenlet",
    props: ['props', 'updateStore'],
    data() {
      return {
        constants: constants
      }
    },
    computed: {
      ...mapGetters({
        collapsibleStatus: 'ui/collapsibleStatus'
      }),
      collapsed() {
        return this.collapsibleStatus(this.data.name)
      },
      data() {
        let data = this.props.attributes
        delete data['value']
        if (data.className || (data.alert && data.alert === true)) {
          data.class = data.className ? data.className : '' + ' ' + data.alert === true ? 'alert' : ''
        }
        return data
      },
      headerChildren() {
        return this.props.children.filter(component => ['Menu'].includes(component.name)) // 'ScreenletSubWidget'
      },
      bodyChildren() {
        return this.props.children.filter(component => !['Menu', 'ScreenletSubWidget'].includes(component.name)) // 'ScreenletSubWidget'
      },
      toolTip() {
        if (this.data.hasOwnProperty('collapsed') && this.data.collapsed == true) {
          return this.data.hasOwnProperty('expandToolTip') ? this.data.expandToolTip : ''
        } else {
          return this.data.hasOwnProperty('collapseToolTip') ? this.data.collapseToolTip : ''
        }
      },
      collapsibleAreaId() {
        return this.data.hasOwnProperty('collapsibleAreaId') ? this.data.collapsibleAreaId : ''
      },
      style() {
        if (this.data.collapsible) {
          if (this.collapsed) {
            return {display: 'none'}
          } else {
            return {}
          }
        } else {
          return {}
        }
      },
      id() {
        return `${this.data.id}-${this.data.name}`
      },
      collapseIcon() {
        return this.collapsed ? 'mdi-arrow-expand-down' :  'mdi-arrow-collapse-up'
      },
      collapsible() {
        return this.data.hasOwnProperty('collapsible') && this.data.collapsible
      },
      tabMenu() {
        return this.data.hasOwnProperty('tabMenu') ? this.data.tabMenu : null
      },
      navMenu() {
        return this.data.hasOwnProperty('navMenu') ? this.data.navMenu : null
      }
    },
    methods: {
      toggle() {
        this.$store.dispatch('ui/setCollapsibleStatus', {areaId: this.data.name, areaTarget: !this.collapsed})
      }
    },
    created() {
      if (this.data.collapsible) {
        this.$store.dispatch('ui/setCollapsibleStatus', {areaId: this.props.attributes.name, areaTarget: this.props.attributes.collapsed})
      }
    }
  }
</script>

<style scoped>

</style>
