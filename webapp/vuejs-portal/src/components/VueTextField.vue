<template>
  <div id="vue-text-field">
    <v-textField v-if="mask" v-model="value" :rules="rules" v-bind="data" v-mask="parsedMask" error-count="3" validate-on-blur/>
    <v-textField v-else v-model="value" :rules="rules" v-bind="data" error-count="3" validate-on-blur/>
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
      ...mapGetters({
        getForm: 'form/form',
        getDataFromForm: 'form/fieldInForm',
        formControlField: 'form/formControlField'
      }),
      data() {
        let data = this.props.attributes
        //delete data['value']
        if (data.className || (data.alert && data.alert === true)) {
          data.class = data.className ? data.className : '' + ' ' + data.alert === true ? 'alert' : ''
        }
        return data;
      },
      name() {
        return this.data.hasOwnProperty('name') ? this.data.name : ''
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
      mask() {
        return this.data.hasOwnProperty('mask') ? this.data.mask : null
      },
      parsedMask() {
        return this.data.hasOwnProperty('mask') ? this.data.mask.replace(/\*/gi, 'X').replace(/9/gi, '#').replace(/a/gi, 'S') : []
      },
      controls() {
        return {
          required: this.data.hasOwnProperty('required') && this.data.required.hasOwnProperty('requiredField') && this.data.required.requiredField === "true",
          maxLength: this.data.hasOwnProperty('maxLength') ? this.data.maxLength : null,
          mask: this.data.hasOwnProperty('mask') ? this.data.mask : null
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
        if (this.controls.mask !== null) {
          rules.push((v) => v.length === this.mask.length || `mask : ${this.mask}`)
        }
        return rules
      }
    },
    watch: {
      data: function () {
        this.$store.dispatch('form/setFieldToForm', this.storeForm)
      }
    },
    mounted() {
      this.$store.dispatch('form/setFieldToForm', this.storeForm)
    }
  }
</script>

<style scoped>

</style>
