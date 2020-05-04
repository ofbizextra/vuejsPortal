<template>
  <v-tooltip top>
    <template v-slot:activator="{ on }">
      <div :id="id" :name="name" class="mt-4" v-on="fieldHelpText ? on : null">
        <input type="hidden" :name="name" :value="value" :formname="formName"/>
        <v-select :label="label" :items="options" item-value="key" item-text="description" v-model="value"
                  :hide-details="!required" dense clearable :rules="rules">
          <template slot="item" slot-scope="data">
        <span :id="data.item.key">
          {{data.item.description}}
        </span>
          </template>
        </v-select>
      </div>
    </template>
    <span>{{fieldHelpText}}</span>
  </v-tooltip>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "VueDropDownField",
    props: ['props', 'updateStore'],
    computed: {
      ...mapGetters({
        getForm: 'form/form',
        getDataFromForm: 'form/fieldInForm'
      }),
      controls() {
        return {
          required: this.props.attributes.hasOwnProperty('requiredField') && this.props.attributes.requiredField === true
        }
      },
      id() {
        return this.props.attributes.hasOwnProperty('id') ? this.props.attributes.id : ''
      },
      fieldTitle() {
        return this.props.attributes.hasOwnProperty('fieldTitle') ? this.props.attributes.fieldTitle : ''
      },
      fieldHelpText() {
        return this.props.attributes.hasOwnProperty('fieldHelpText') ? this.props.attributes.fieldHelpText : ''
      },
      formName() {
        return this.props.attributes.hasOwnProperty('formName') ? this.props.attributes.formName : ''
      },
      label() {
        return this.required ? this.fieldTitle + ' *' : this.fieldTitle
      },
      name() {
        return this.props.attributes.hasOwnProperty('name') ? this.props.attributes.name : ''
      },
      options() {
        return this.props.attributes.hasOwnProperty('options') ? this.props.attributes.options : ''
      },
      required() {
        return this.controls.required
      },
      rules() {
        let rules = []
        if (this.controls.required) {
          rules.push((v) => !!v || 'This field is required')
        }
        return rules
      },
      storeForm() {
        return {
          formId: this.props.attributes.formName,
          key: this.props.attributes.name,
          value: this.props.attributes.hasOwnProperty('currentValue') ? this.props.attributes.currentValue : this.props.attributes.multiple ? [''] : ''
        }
      },
      value: {
        get() {
          return this.getDataFromForm(this.storeForm)
        },
        set(value) {
          this.$store.dispatch('form/setFieldToForm', {
            formId: this.formName,
            key: this.name,
            value: value
          })
        }
      }
    },
    created() {
      this.$store.dispatch('form/setFieldToForm', this.storeForm)
    },
    watch: {
      props: function () {
        this.$store.dispatch('form/setFieldToForm', this.storeForm)
      }
    }
  }
</script>

<style scoped>

</style>
