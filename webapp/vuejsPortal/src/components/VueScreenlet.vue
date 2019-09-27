<template>
  <div id="vue-screenlet">
    <v-card :id="data.id">
      <v-toolbar v-if="data.showMore">
        <v-toolbar-title color="indigo" dark>{{data.title}}</v-toolbar-title>
        <div class="flex-grow-1"></div>
        <v-btn-icon v-for="(headerItem, headerItemIndex) in headerChildren"
                    :key="headerItemIndex">
          <div v-bind:is="constantes.components[headerItem.name]"
               :props="headerItem"
               :updateStore="updateStore"></div>
        </v-btn-icon>
      </v-toolbar>
      <v-card-content :id="collapsibleAreaId">
        <div
          v-for="(component, key) in bodyChildren"
          :key="key"
          v-bind:is="constantes.components[component.name]"
          :props="component"
          :updateStore="updateStore">
        </div>
      </v-card-content>
    </v-card>
  </div>
</template>

<script>
  import constantes from '../js/constantes'

  export default {
    name: "VueScreenlet",
    props: ['props', 'updateStore'],
    data() {
      return {
        constantes: constantes
      }
    },
    computed: {
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
          if (this.data.collapsed) {
            return {display: 'none'}
          } else {
            return {}
          }
        } else {
          return {}
        }
      }
    },
    methods: {
      toggle() {
        this.data.collapsed = !this.data.collapsed
      }
    }
  }
</script>

<style scoped>

</style>
