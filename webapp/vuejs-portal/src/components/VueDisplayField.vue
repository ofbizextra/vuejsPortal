// todo compare size and description.size and tronque
<template>
  <div id="vue-display-field">
    <label v-if="pointer.entityName" v-bind="data" class="ma-1">{{getPointer}}</label>
    <label v-else v-bind="data" class="ma-1">{{data.title ? data.title : data.description}}</label>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "VueDisplayField",
    data() {
      return {
        pointer: {
          entityName: this.getNestedObject(this.props, ['stPointer', 'stEntityName']),
          id: this.getNestedObject(this.props, ['stPointer', 'id']),
          attribute: this.getNestedObject(this.props, ['stPointer', 'field'])
        }
      }
    },
    computed: {
      data() {
        let data = this.props.attributes
        if (data.className || (data.alert && data.alert === true)) {
          data.class = data.className ? data.className : '' + ' ' + data.alert === true ? 'alert' : ''
        }
        return data
      },
      getPointer() {
        return this.$store.getters['data/entityRowAttribute'](this.pointer);
      },
      storeForm() {
        return {
          formId: this.props.attributes.formName,
          key: this.props.attributes.name,
          value: this.pointer.entityName ? this.getPointer : this.data.description
        }
      },
      ...mapGetters({
        getData: 'data/entityRowAttribute',
        getDataFromForm: 'form/fieldInForm'
      })
    },
    props: [
      'props'
    ],
    created() {
      // this.$store.dispatch('form/setFieldToForm', this.storeForm)
    },
    watch: {
      props: function () {
        this.pointer = {
          entityName: this.getNestedObject(this.props, ['stPointer', 'stEntityName']),
          id: this.getNestedObject(this.props, ['stPointer', 'id']),
          attribute: this.getNestedObject(this.props, ['stPointer', 'field'])
        }
        // this.$store.dispatch('form/setFieldToForm', this.storeForm)
      }
    }
  }
</script>

<style scoped>

</style>
