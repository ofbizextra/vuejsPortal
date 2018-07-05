<template>
  <div id="vue-label">
    <label>{{label}}</label>
  </div>
</template>

<script>
  import uiLabel from './../../../../config/VuejsPortalUiLabels.xml'

  export default {
    name: "VueLabel",
    props: ['props'],
    data () {
      return {
      }
    },
    computed: {
      label() {
        if (!this.key) return ''
        let label
        uiLabel.resource.property.map(property => {
          if (property.$.key === this.key) {
            property.value.map(lang => {
              if (lang.$['xml:lang'] === 'en') {
                label = lang._
              }
            })
          }
        })
        return label
      },
      key() {
        return this.props ? this.props.substring(this.props.indexOf('.') + 1, this.props.length - 1) : null
      }
    },
    mounted () {
      if(!this.props) {console.log('no props');return}
    }
  }
</script>

<style scoped>

</style>
