<template>
  <div id="vue-password-field">
    <v-textField v-model="value" v-bind="data" dense :hide-details="noRules" :rules="rules" type="password"/>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "VuePasswordField",
    props: ['props', 'updateStore'],
    data() {
      return {}
    },
    computed: {
      ...mapGetters({
        getForm: 'form/form',
        getDataFromForm: 'form/fieldInForm'
      }),
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
      controls() {
        return {
          required: this.data.hasOwnProperty('required') && this.data.required.hasOwnProperty('requiredField') && this.data.required.requiredField === "true",
          maxLength: this.data.hasOwnProperty('maxLength') ? this.data.maxLength : null,
        }
      },
      noRules() {
        return this.controls.required === false && this.controls.maxLength === null && this.controls.mask === null
      },
      rules() {
        let rules = []
        if (this.controls.required) {
          rules.push((v) => !!v || 'This field is required')
        }
        if (this.controls.maxLength !== null) {
          rules.push((v) => v.length > this.controls.maxLength || `This field must be less than ${this.controls.maxLength} characters` )
        }
        return rules
      }
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