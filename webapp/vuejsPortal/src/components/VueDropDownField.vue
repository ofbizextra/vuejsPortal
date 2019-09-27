<template>
  <div id="vue-drop-down-field">
    <v-select :items="data.options" item-value="key" item-text="description" v-model="value">

    </v-select>
  </div>
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
