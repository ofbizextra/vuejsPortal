<template>
    <v-row v-if="props.children.length < 2">
      <vue-nav-menu-item v-for="(component, index) in props.children"
           :key="index"
           :props="component"
           :updateStore="updateStore"
           class="text-right">

      </vue-nav-menu-item>
    </v-row>
    <v-menu bottom left v-else transition="scale-transition" origin="center center">
      <template v-slot:activator="{ on }">
        <v-btn icon v-on="on">
          <v-icon>mdi-dots-vertical</v-icon>
        </v-btn>
      </template>

      <v-list dense>
        <vue-nav-menu-item v-for="(component, index) in props.children"
             :key="index"
             :props="component"
             :updateStore="updateStore"></vue-nav-menu-item>
      </v-list>
    </v-menu>
</template>

<script>
  import constantes from '../js/constantes'

  export default {
    name: "VueNavMenu",
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
