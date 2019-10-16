<template>
  <li id="vue-screenlet-sub-widget" class="expanded">
    <a v-on:click.prevent="toggleCollapse" title="expand">&nbsp;</a>
  </li>
</template>

<script>
  export default {
    name: "VueScreenletSubWidget",
    props: ['props', 'updateStore'],
    data() {
      return {
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
      toggleCollapse() {
        console.log('toggle collapse')
      }
    }
  }
</script>

<style scoped>

</style>
