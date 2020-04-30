<template>
  <div>
    <h1 :id="id" v-if="isH1" v-bind:is="style">{{text}}</h1>
    <h2 :id="id" v-else-if="isH2" v-bind:is="style">{{text}}</h2>
    <h3 :id="id" v-else-if="isH3" v-bind:is="style">{{text}}</h3>
    <span :id="id" v-else :class="style">{{text}}</span>
  </div>
</template>

<script>
  export default {
    name: "VueLabel",
    props: ['props', 'updateStore'],
    computed: {
      id() {
        return this.props.attributes.hasOwnProperty('id') ? this.props.attributes.id : ''
      },
      isH1() {
        return this.style.toLocaleLowerCase().split(' ').includes('h1')
      },
      isH2() {
        return this.style.toLocaleLowerCase().split(' ').includes('h2')
      },
      isH3() {
        return this.style.toLocaleLowerCase().split(' ').includes('h3')
      },
      style() {
        return this.props.attributes.hasOwnProperty('style') ? this.props.attributes.style : ''
      },
      text() {
        return this.props.attributes.hasOwnProperty('text') ? this.props.attributes.text : ''
      }
    }
  }
</script>

<style scoped>

</style>
