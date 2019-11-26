<template>
  <v-radio-group id="vue-radio-field" v-model="value" row :hide-details="noRules" :rules="rules">
    <input v-if="data.conditionGroup" type="hidden" :name="data.name + '_grp'" v-bind:value="data.conditionGroup"/>
    <v-radio v-for="item in props.attributes.items" :key="item.key" :label="item.description" :value="item.key">
    </v-radio>
    <v-tooltip bottom v-if="value">
      <template v-slot:activator="{ on }">
        <v-btn icon small v-on="on" v-on:click="clear"><v-icon>mdi-close</v-icon></v-btn>
      </template>
      <span>Clear</span>
    </v-tooltip>
    <vue-error v-if="data.event" component="event"/>
  </v-radio-group>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "VueRadioField",
    props: ['props', 'updateStore'],
    data() {
      return {}
    },
    computed: {
      ...mapGetters({
        getDataFromForm: 'form/fieldInForm',
        getForm: 'form/form'
      }),
      data() {
        let data = this.props.attributes
        //delete data['currentValue']
        if (data.className || (data.alert && data.alert === true)) {
          data.class = data.className ? data.className : '' + ' ' + data.alert === true ? 'alert' : ''
        }
        return data
      },
      storeForm() {
        return {
          formId: this.props.attributes.formName,
          key: this.props.attributes.name,
          value: this.props.attributes.currentValue ? this.props.attributes.currentValue : ''
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
          required: this.data.hasOwnProperty('requiredField') &&  this.data.requiredField === true
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
        return rules
      }
    },
    methods: {
      clear() {
        this.value = ''
      }
    },
    watch: {
      data: function () {
        console.log('vue-radio : ', this.storeForm)
        this.$store.dispatch('form/setFieldToForm', this.storeForm)
      }
    },
    created() {
      console.log('vue-radio : ', this.storeForm)
      this.$store.dispatch('form/setFieldToForm', this.storeForm)
    }
  }
</script>

<style scoped>

</style>
