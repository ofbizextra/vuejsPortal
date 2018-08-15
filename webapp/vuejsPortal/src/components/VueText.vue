<template>
  <div id="vue-text">
    <input v-model="value" v-bind="data"/>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "VueText",
    props: ['props', 'updateStore'],
    data() {
      return {}
    },
    computed: {
      data() {
        let data = this.props.attributes
        delete data['value']
        return data
      },
      storeData() {
        return {
          id: this.$store.getters['data/currentId'],
          key: this.props.attributes.id,
          value: this.props.attributes.value ? this.props.attributes.value : ''
        }
      },
      storeForm() {
        return {
          formId: this.props.attributes.formName,
          key: this.props.attributes.id,
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
            key: this.props.attributes.id,
            value: value
          })
        }
      },
      ...mapGetters({
        dataFromExample: 'data/dataFromExample',
        currentId: 'data/currentId',
        getForm: 'form/form',
        getDataFromForm: 'form/fieldInForm'
      })
    },
    watch: {
      data: function (from, to) {
        console.log('vue-text : ', this.storeForm)
        this.$store.dispatch('data/addDataToExample', this.storeData)
        this.$store.dispatch('form/setFieldToForm', this.storeForm)
      }
    },
    created() {
      console.log('vue-text : ', this.storeForm)
      this.$store.dispatch('data/addDataToExample', this.storeData)
      this.$store.dispatch('form/setFieldToForm', this.storeForm)
    }
  }
</script>

<style scoped>

</style>
