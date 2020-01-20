<template>
  <div component="vue-label">
    <div v-if="hasStyle">
      <h1 :id="id" v-if="style === 'h1'">{{text}}</h1>
      <h2 :id="id" v-else-if="style === 'h2'">{{text}}</h2>
      <h3 :id="id" v-else-if="style === 'h3'">{{text}}</h3>
      <h4 :id="id" v-else-if="style === 'h4'">{{text}}</h4>
      <h5 :id="id" v-else-if="style === 'h5'">{{text}}</h5>
      <h6 :id="id" v-else-if="style === 'h6'">{{text}}</h6>
      <span :id="id" :class="style" v-else>{{text}}</span>
    </div>
    <span :id="id" v-else>{{text}}</span>
  </div>
</template>

<script>
  export default {
    name: "VueLabel",
    props: ['props', 'updateStore'],
    data() {
      return {}
    },
    computed: {
      attributes() {
        return this.props.hasOwnProperty('attributes') ? this.props.attributes : {}
      },
      text() {
        return this.attributes.hasOwnProperty('text') ? this.attributes.text : ''
      },
      style() {
        return this.hasStyle ? this.attributes.style : ''
      },
      id() {
        return this.hasId ? this.attributes.id : ''
      },
      hasStyle() {
        return this.attributes.hasOwnProperty('style')
      },
      hasId() {
        return this.attributes.hasOwnProperty('id')
      }
    }
  }
</script>

<style scoped>

</style>
