<template>
  <div id="vue-screenlet">
    <div :id="templateId" class="ma-1">
      <v-toolbar v-if="showMore" dense color="primary" dark class="screenlet-title-bar">
        <v-toolbar-title class="title">{{title}}</v-toolbar-title>
        <v-spacer></v-spacer>
        <vue-nav-menu v-if="navMenu" :props="navMenu" :updateStore="updateStore"></vue-nav-menu>
        <v-btn id="toggleCollapse" icon v-if="collapsible" @click="toggle">
          <v-icon>{{ collapseIcon }}</v-icon>
        </v-btn>
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
      bodyChildren() {
        return this.props.children.filter(component => !['Menu', 'ScreenletSubWidget'].includes(component.name)) // 'ScreenletSubWidget'
      },
      collapsed() {
        return this.collapsibleStatus(this.props.attributes.id)
      },
      collapseIcon() {
        return this.collapsed ? 'mdi-arrow-expand-down' : 'mdi-arrow-collapse-up'
      },
      collapseToolTip() {
        return this.props.attributes.hasOwnProperty('collapseToolTip') ? this.props.attributes.collapseToolTip : ''
      },
      collapsible() {
        return this.props.attributes.hasOwnProperty('collapsible') && this.props.attributes.collapsible
      },
      collapsibleAreaId() {
        return this.props.attributes.hasOwnProperty('collapsibleAreaId') ? this.props.attributes.collapsibleAreaId : ''
      },
      expandToolTip() {
        return this.props.attributes.hasOwnProperty('expandToolTip') ? this.props.attributes.expandToolTip : ''
      },
      headerChildren() {
        return this.props.children.filter(component => ['Menu'].includes(component.name))
      },
      id() {
        return this.props.attributes.hasOwnProperty('id') ? this.props.attributes.id : ''
      },
      name() {
        return this.props.attributes.hasOwnProperty('name') ? this.props.attributes.name : ''
      },
      navMenu() {
        return this.props.attributes.hasOwnProperty('navMenu') ? this.props.attributes.navMenu : null
      },
      showMore() {
        return this.props.attributes.hasOwnProperty('showMore') ? this.props.attributes.showMore : ''
      },
      style() {
        if (this.props.attributes.collapsible) {
          if (this.collapsed) {
            return {display: 'none'}
          } else {
            return {}
          }
        } else {
          return {}
        }
      },
      tabMenu() {
        return this.props.attributes.hasOwnProperty('tabMenu') ? this.props.attributes.tabMenu : null
      },
      templateId() {
        return `${this.id}-${this.name}`
      },
      title() {
        return this.props.attributes.hasOwnProperty('title') ? this.props.attributes.title : ''
      },
      toolTip() {
        if (this.collapsed) {
          return this.expandToolTip
        } else {
          return this.collapseToolTip
        }
      }
    },
    methods: {
      toggle() {
        this.$store.dispatch('ui/setCollapsibleStatus', {areaId: this.templateId, areaTarget: !this.collapsed})
      }
    },
    created() {
      if (this.collapsible) {
        this.$store.dispatch('ui/setCollapsibleStatus', {
          areaId: this.templateId,
          areaTarget: this.collapsed
        })
      }
    }
  }
</script>

<style scoped>

</style>
