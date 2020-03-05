<template>
  <v-row class="ma-0 pa-0">
    <v-select class="col-4 mt-0 mb-0" :items="items" v-if="data.opEquals" v-model="valueOp" hide-details dense>
    </v-select>
    <v-tooltip top>
      <template v-slot:activator="{ on }">
        <v-text-field class="col-6 mt-0 mb-0"
                      :label="fieldTitle"
                      v-bind:name="data.name"
                      v-bind:size="data.size"
                      v-model="value"
                      v-bind:maxlength="data.maxlength"
                      v-bind:autocomplete="data.autocomplete"
                      v-bind:tabindex="data.tabindex"
                      hide-details
                      dense
                      v-on="fieldHelpText ? on : null"
        />
      </template>
      <span>{{fieldHelpText}}</span>
    </v-tooltip>
      <input v-if="data.hideIgnoreCase" type="hidden" :name="data.name + '_ic'" :value="data.ignCase ? 'Y' : ''"/>
      <v-checkbox class="col-2 mt-0 mb-0" v-else type="checkbox" label="ignore case" :name="data.name + '_ic'" v-model="valueIc" hide-details dense/>
  </v-row>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "VueTextFindField",
    props: ['props', 'updateStore'],
    data() {
      return {
        items: [
          {text: 'egal', value: 'equals'},
          {text: 'débute', value: 'like'},
          {text: 'contient', value: 'contains'},
          {text: 'est vide', value: 'empty'},
          {text: 'Diff', value: 'notEqual'}
          ]
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
      },
      storeForm() {
        return {
          formId: this.props.attributes.formName,
          key: this.props.attributes.name,
          value: this.props.attributes.value ? this.props.attributes.value : ''
        }
      },
      storeFormOp() {
        return {
          formId: this.props.attributes.formName,
          key: this.props.attributes.name + '_op',
          value: this.props.attributes.defaultOption ? this.props.attributes.defaultOption : ''
        }
      },
      storeFormIc() {
        return {
          formId: this.props.attributes.formName,
          key: this.props.attributes.name + '_ic',
          value: this.props.attributes.ignCase ? 'Y' : 'N'
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
      valueOp: {
        get() {
          return this.getDataFromForm(this.storeFormOp)
        },
        set(value) {
          this.$store.dispatch('form/setFieldToForm', {
            formId: this.props.attributes.formName,
            key: this.props.attributes.name + '_op',
            value: value
          })
        }
      },
      valueIc: {
        get() {
          return this.getDataFromForm(this.storeFormIc) === 'Y'
        },
        set(value) {
          this.$store.dispatch('form/setFieldToForm', {
            formId: this.props.attributes.formName,
            key: this.props.attributes.name + '_ic',
            value: value ? 'Y' : 'N'
          })
        }
      },
      opEquals() {
        return this.data.hasOwnProperty('opEquals') ? this.data.opEquals : false
      },
      titleStyle() {
        return this.data.hasOwnProperty('titleStyle') ? this.data.titleStyle : ''
      },
      hideIgnoreCase() {
        return this.data.hasOwnProperty('hideIgnoreCase') ? this.data.hideIgnoreCase : false
      },
      ...mapGetters({
        getForm: 'form/form',
        getDataFromForm: 'form/fieldInForm'
      }),
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
        this.$store.dispatch('form/setFieldToForm', this.storeFormOp)
        this.$store.dispatch('form/setFieldToForm', this.storeFormIc)
      }
    },
    created() {
      this.$store.dispatch('form/setFieldToForm', this.storeForm)
      this.$store.dispatch('form/setFieldToForm', this.storeFormOp)
      this.$store.dispatch('form/setFieldToForm', this.storeFormIc)
    }
  }
</script>

<style scoped>

</style>
