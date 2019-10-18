<template>
  <div id="vue-screenlet">
    <div :id="data.id" class="ma-1">
      <v-toolbar v-if="data.showMore" dense color="primary" dark>
        <v-toolbar-title class="title">{{data.title}}</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-btn-icon v-for="(headerItem, headerItemIndex) in headerChildren"
                    :key="headerItemIndex">
          <div v-bind:is="constantes.components[headerItem.name]"
               :props="headerItem"
               :updateStore="updateStore"></div>
        </v-btn-icon>
        <v-btn icon v-if="collapsible" @click="toggle"><v-icon>{{ collapseIcon }}</v-icon></v-btn>
      </v-toolbar>
      <v-expand-transition>
      <v-card :id="collapsibleAreaId" v-show="!collapsed">
        <v-card-content>
        <div
          v-for="(component, key) in bodyChildren"
          :key="key"
          v-bind:is="constantes.components[component.name]"
          :props="component"
          :updateStore="updateStore">
        </div>
        </v-card-content>
      </v-card>
      </v-expand-transition>
    </div>
  </div>
</template>

<script>
  import constantes from '../js/constantes'
  import {mapGetters} from 'vuex'

  export default {
    name: "VueScreenlet",
    props: ['props', 'updateStore'],
    data() {
      return {
        constantes: constantes
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
