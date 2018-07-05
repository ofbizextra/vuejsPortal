<template>
  <div id="vue-label">
    <label>{{label}}</label>
  </div>
</template>

<script>
  import uiLabel from './../../../../config/VuejsPortalUiLabels.xml'
  import commonUiLabel from './../../../../config/CommonUiLabels.xml'

  export default {
    name: "VueLabel",
    props: ['props'],
    data () {
      return {
        language: navigator.language
      }
    },
    computed: {
      label() {
        if (!this.key) return ''
        let label = ''
        uiLabel.resource.property.map(property => {
          if (property.$.key === this.key) {
            property.value.map(lang => {
              if (lang.$['xml:lang'] === this.language) {
                label = lang._
              }
            })
          }
        })
        commonUiLabel.resource.property.map(property => {
          if (property.$.key === this.key) {
            property.value.map(lang => {
              if (lang.$['xml:lang'] === this.language) {
                label = lang._
              }
            })
          }
        })
        return label !== '' ? label : this.key
      },
      key() {
        return this.props ? this.props.substring(this.props.indexOf('.') + 1, this.props.length - 1) : null
      }
    }
  }
</script>

<style scoped>

</style>
