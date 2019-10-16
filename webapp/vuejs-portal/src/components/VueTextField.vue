<template>
  <div id="vue-text-field">
    <input v-model="value" v-bind="data"/>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "VueTextField",
    props: ['props', 'updateStore'],
    data() {
      return {}
    },
    computed: {
      data() {
        let data = this.props.attributes
        //delete data['value']
        if (data.className || (data.alert && data.alert === true)) {
          data.class = data.className ? data.className : '' + ' ' + data.alert === true ? 'alert' : ''
        }
        return data;
      },
      storeForm() {
        return {
          formId: this.props.attributes.formName,
          key: this.props.attributes.name,
          value: this.props.attributes.value ? this.props.attributes.value : ''
        }
      },
      value: {
        get() {
          return this.getDataFromForm(this.storeForm)
        },
        set(value) {
          this.$store.dispatch('form/setFieldToForm', {
            formId: this.props.attributes.formName,
            key: this.props.attributes.name,
            value: value
          })
        }
      },
      ...mapGetters({
        getForm: 'form/form',
        getDataFromForm: 'form/fieldInForm'
      })
    },
    watch: {
      data: function () {
        this.$store.dispatch('form/setFieldToForm', this.storeForm)
      }
    },
    created() {
      this.$store.dispatch('form/setFieldToForm', this.storeForm)
    }
  }
</script>

<style scoped>

</style>
