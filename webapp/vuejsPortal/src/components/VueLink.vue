<template>
  <a id="vue-link" :href="data.linkUrl" v-on:click.prevent="redirect">{{data.text}}</a>
</template>

<script>
  export default {
    name: "VueLink",
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
      }
    },
    methods: {
      redirect() {
        console.log('click on link : ', this.data.text)
      }
    }
  }
</script>

<style scoped>

</style>
