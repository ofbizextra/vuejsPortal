<template>
  <v-expansion-panels :id="id" class="ma-1" v-model="panels">
    <v-expansion-panel :disabled="!collapsible">
      <v-expansion-panel-header v-if="title !== ''">
            {{title}}
      </v-expansion-panel-header>
      <v-expansion-panel-content :id="collapsibleAreaId">
        <div
            v-for="(component, key) in props.children"
            :key="key"
            v-bind:is="constantes.components[component.name]"
            :props="component"
            :updateStore="updateStore">
        </div>
      </v-expansion-panel-content>
    </v-expansion-panel>
  </v-expansion-panels>
</template>

<script>
  import constantes from '../js/constantes'

  export default {
    name: "VueFieldGroup",
    props: ['props', 'updateStore'],
    data() {
      return {
        constantes: constantes,
        panels: []
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
      collapseToolTip() {
        return this.data.hasOwnProperty('collapseToolTip') ? this.data.collapseToolTip : ''
      },
      collapsed() {
        return this.data.hasOwnProperty('collapsed') ? this.data.collapsed : false
      },
      expanded() {
        return !this.collapsed
      },
      collapsible() {
        return this.data.hasOwnProperty('collapsible') ? this.data.collapsible : false
      },
      collapsibleAreaId() {
        return this.data.hasOwnProperty('collapsibleAreaId') ? this.data.collapsibleAreaId : ''
      },
      expandToolTip() {
        return this.data.hasOwnProperty('expandToolTip') ? this.data.expandToolTip : ''
      },
      id() {
        return this.data.hasOwnProperty('id') ? this.data.id : ''
      },
      style() {
        return this.data.hasOwnProperty('style') ? this.data.style : ''
      },
      title() {
        return this.data.hasOwnProperty('title') ? this.data.title : ''
      },
      iconClass() {
        return this.collapsed ? 'collapsed' : 'expanded'
      },
      tooltip() {
        return this.collapsed ? this.expandToolTip : this.collapseToolTip
      }
    },
    methods: {
      toggleCollapse() {
        this.data.collapsed = !this.data.collapsed
      }
    },
    created() {
      if (!this.data.collapsed) {
        this.panels = 0
      }
    }
  }
</script>

<style scoped>

</style>
