<template>
  <div id="vue-menu" v-bind:class="style + ' text-center'">
    <v-row v-if="props.children.length < 2">
      <div v-for="(component, index) in props.children"
           :key="index"
           v-bind:is="constantes.components[component.name]"
           :props="component"
           :updateStore="updateStore"></div>
    </v-row>
    <v-menu bottom left v-else transition="scale-transition" origin="center center">
      <template v-slot:activator="{ on }">
        <v-btn icon v-on="on">
          <v-icon>mdi-dots-vertical</v-icon>
        </v-btn>
      </template>

      <v-list dense>
        <div v-for="(component, index) in props.children"
             :key="index"
             v-bind:is="constantes.components[component.name]"
             :props="component"
             :updateStore="updateStore"></div>
      </v-list>
    </v-menu>
  </div>
</template>

<script>
  import constantes from '../js/constantes'

  export default {
    name: "VueMenu",
    props: ['props', 'updateStore'],
    data() {
      return {
        constantes: constantes,
        on: false
      }
    },
    computed: {
      data() {
        let data = this.props.attributes
        delete data['value']
        return data
      },
      boundaryComment() {
        return this.data.hasOwnProperty('boundaryComment') ? this.data.boundaryComment : ''
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
    },
    methods: {}
  }
</script>

<style scoped>

</style>
