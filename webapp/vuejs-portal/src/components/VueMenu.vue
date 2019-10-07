<template>
  <div id="vue-menu" v-bind:class="style">
    <h2 v-if="title.length > 0">
      {{title}}
    </h2>
    <ul>
      <li>
        <ul>
          <li
            v-for="(component, index) in props.children"
            :key="index"
            v-bind:is="constantes.components[component.name]"
            :props="component"
            :updateStore="updateStore"
          >
          </li>
        </ul>
      </li>
    </ul>
  </div>
</template>

<script>
  import constantes from '../js/constantes'

  export default {
    name: "VueMenu",
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
