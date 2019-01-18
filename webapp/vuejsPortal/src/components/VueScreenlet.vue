<template>
  <div id="vue-screenlet">
    <div :id="data.id" class="screenlet">
      <div class="screenlet-title-bar" v-if="data.showMore">
        <ul>
          <li class="h3" v-if="data.hasOwnProperty('title')">
            {{data.title}}
          </li>
          <li :class="data.collapsed ? 'collapsed' : 'expanded'" v-if="data.collapsible">
            <a
              v-bind:title="toolTip"
            >&nbsp;
            </a>
          </li>
          <li
            v-for="(headerItem, headerItemIndex) in headerChildren" :key="headerItemIndex"
            v-bind:is="constantes.components[headerItem.name]"
            :props="headerItem"
            :updateStore="updateStore"
          >
          </li>
        </ul>
        <br class="clear">
      </div>
      <div id="searchOptions_col" class="screenlet-body">
        <div
          v-for="component in bodyChildren"
          v-bind:is="constantes.components[component.name]"
          :props="component"
          :updateStore="updateStore">
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import constantes from '../js/constantes'

  export default {
    name: "ViewScreenlet",
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
      }
    },
    methods: {}
  }
</script>

<style scoped>

</style>
