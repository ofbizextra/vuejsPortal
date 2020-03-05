<template>
  <v-tooltip top>
    <template v-slot:activator="{ on }">
      <div :id="id" :name="name" v-on="fieldHelpText ? on : null">
        <input type="hidden" :name="data.name" :value="value" :formname="data.formName"/>
        <v-select :label="fieldTitle" :items="data.options" item-value="key" item-text="description" v-model="value"
                  :hide-details="noRules" dense clearable :rules="rules">
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
    data() {
      return {}
    },
    computed: {
      data() {
        let data = this.props.attributes
        //delete data['currentValue']
        Object.keys(data).map(it => {
          if (data[it] === ''){
            delete data[it]
          }
        })
        return data
      },
      storeForm() {
        return {
          formId: this.props.attributes.formName,
          key: this.props.attributes.name,
          value: this.props.attributes.currentValue ? this.props.attributes.currentValue : this.props.attributes.multiple ? [''] : ''
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
      }),
      id() {
        return this.data.hasOwnProperty('id') ? this.data.id : ''
      },
      name() {
        return this.data.hasOwnProperty('name') ? this.data.name : ''
      },
      controls() {
        return {
          required: this.data.hasOwnProperty('requiredField') && this.data.requiredField === true
        }
      },
      noRules() {
        return this.controls.required === false
      },
      rules() {
        let rules = []
        if (this.controls.required) {
          rules.push((v) => !!v || 'This field is required')
        }
        return rules
      },
      fieldTitle() {
        return this.data.hasOwnProperty('fieldTitle') ? this.data.fieldTitle : ''
      },
      fieldHelpText() {
        return this.data.hasOwnProperty('fieldHelpText') ? this.data.fieldHelpText : ''
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
