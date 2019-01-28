<template>
  <a id="vue-link" :href="data.linkUrl" v-on:click.prevent="redirect">{{data.text}}</a>
</template>

<script>
  import constantes from '../js/constantes'
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
        if (this.data.hasOwnProperty('targetWindow') && this.data.hasOwnProperty('targetPortlet')) {
          this.$store.dispatch('ui/setPortletTarget', {
            portletId: this.data.targetWindow,
            target: this.data.targetPortlet
          })
        }
      }
    }
  }
</script>

<style scoped>

</style>
