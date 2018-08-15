<template>
  <div id="vue-drop-down">
    <select v-model="value" v-bind="data">
      <option value="" v-if="data.allowEmpty || (data.allowEmpty && props.children.length === 0)"></option>
      <option v-if="data.firstInList && value && !data.multiple" selected="selected" :value="value">
        {{data.explicitDescription}}
      </option>
      <vue-option
        v-for="option in data.options"
        :key="option.key"
        :props="option"
        :selected="option.key === value">
      </vue-option>
    </select>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "VueDropDown",
    props: ['props', 'updateStore'],
    data() {
      return {}
    },
    computed: {
      data() {
        let data = this.props.attributes
        delete data['currentValue']
        Object.keys(data).map(it => {
          if (data[it] === ''){
            delete data[it]
          }
        })
        return data
      },
      storeData() {
        return {
          id: this.$store.getters['data/currentId'],
          key: this.props.attributes.id,
          value: this.props.attributes.currentValue ? this.props.attributes.currentValue : this.props.attributes.multiple ? [''] : ''
        }
      },
      storeForm() {
        return {
          formId: this.props.attributes.formName,
          key: this.props.attributes.id,
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
        console.log('vue-dropdown', this.storeData)
        this.$store.dispatch('data/addDataToExample', this.storeData)
        this.$store.dispatch('form/setFieldToForm', this.storeForm)
      }
    },
    created() {
      console.log('vue-dropdown : ', this.storeData)
      this.$store.dispatch('data/addDataToExample', this.storeData)
      this.$store.dispatch('form/setFieldToForm', this.storeForm)
    }
  }
</script>

<style scoped>

</style>
