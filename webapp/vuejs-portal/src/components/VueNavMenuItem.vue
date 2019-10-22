<template>
    <v-list-item
    >
        <v-list-item-content class="text-right">
            <div
                    v-for="(component, index) in props.children"
                    :key="index"
                    v-bind:is="constantes.components[component.name]"
                    :props="component"
                    :updateStore="updateStore"
            ></div>
        </v-list-item-content>
    </v-list-item>
</template>

<script>
  import constantes from '../js/constantes'

  export default {
    name: "VueNavMenuItem",
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
        return data
      },
      style() {
        return this.data.hasOwnProperty('style') ? this.data.style : ''
      },
      toolTip() {
        return this.data.hasOwnProperty('toolTip') ? this.data.toolTip : ''
      },
      linkStr() {
        return this.data.hasOwnProperty('linkStr') ? this.data.linkStr : ''
      },
      containsNestedMenus() {
        return this.data.hasOwnProperty('containsNestedMenus') ? this.data.containsNestedMenus : ''
      }
    },
    methods: {}
  }
</script>

<style scoped>

</style>
