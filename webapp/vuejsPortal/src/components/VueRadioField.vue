<template>
  <v-radio-group id="vue-radio-field" v-model="value" row>
    <input v-if="data.conditionGroup" type="hidden" :name="data.name + '_grp'" v-bind:value="data.conditionGroup"/>
    <v-radio v-for="item in props.attributes.items" :label="item.description" :value="item.key">
    </v-radio>
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
      ...mapGetters({
        getDataFromForm: 'form/fieldInForm',
        getForm: 'form/form'
      })
    },
    watch: {
      data: function (from, to) {
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
