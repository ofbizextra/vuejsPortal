<template>
  <div id="vue-screenlet">
    <div :id="data.id" class="screenlet">
      <div class="screenlet-title-bar" v-if="data.showMore">
        <ul>
          <li class="h3" v-if="data.hasOwnProperty('title')">
            {{data.title}}
          </li>
          <li :class="collapsed ? 'collapsed' : 'expanded'" v-if="data.collapsible">
            <a
              v-bind:title="toolTip"
              v-on:click.prevent="toggle"
            >&nbsp;
            </a>
          </li>
          <li
            v-for="(headerItem, headerItemIndex) in headerChildren"
            :key="headerItemIndex"
            v-bind:is="constantes.components[headerItem.name]"
            :props="headerItem"
            :updateStore="updateStore"
          >
          </li>
        </ul>
        <br class="clear">
      </div>
      <div :id="collapsibleAreaId" class="screenlet-body" v-bind:style="style">
        <div
          v-for="(component, key) in bodyChildren"
          :key="key"
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
        return this.collapsibleStatus(this.data.id)
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
      }
    },
    methods: {
      toggle() {
        this.$store.dispatch('ui/setCollapsibleStatus', {areaId: this.data.id, areaTarget: !this.collapsed})
      }
    },
    created() {
      if (this.data.collapsible) {
        this.$store.dispatch('ui/setCollapsibleStatus', {areaId: this.props.attributes.id, areaTarget: this.props.attributes.collapsed})
      }
    }
  }
</script>

<style scoped>

</style>
