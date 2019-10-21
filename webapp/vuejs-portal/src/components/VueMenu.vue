<template>
  <v-toolbar id="vue-menu" color="secondary" dark class="ma-0 pa-0" height="35px">
    <v-toolbar-items class="ma-0 pa-0">
      <div v-for="(component, index) in props.children"
           :key="index"
           v-bind:is="constantes.components[component.name]"
           :props="component"
           :updateStore="updateStore">
      </div>
    </v-toolbar-items>
  </v-toolbar>
<!--  <v-row v-if="props.children.length < 2">-->
<!--    <div v-for="(component, index) in props.children"-->
<!--         :key="index"-->
<!--         v-bind:is="constantes.components[component.name]"-->
<!--         :props="component"-->
<!--         :updateStore="updateStore"></div>-->
<!--  </v-row>-->
<!--  <v-menu bottom left v-else transition="scale-transition" origin="center center">-->
<!--    <template v-slot:activator="{ on }">-->
<!--      <v-btn icon v-on="on">-->
<!--        <v-icon>mdi-dots-vertical</v-icon>-->
<!--      </v-btn>-->
<!--    </template>-->

<!--    <v-list dense>-->
<!--      <div v-for="(component, index) in props.children"-->
<!--           :key="index"-->
<!--           v-bind:is="constantes.components[component.name]"-->
<!--           :props="component"-->
<!--           :updateStore="updateStore"></div>-->
<!--    </v-list>-->
<!--  </v-menu>-->
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
