<template>
  <div id="vue-drop-down">
    <select v-model="value" v-bind="data">
      <option value="" v-if="data.allowEmpty || (data.allowEmpty && props.children.length === 0)"></option>
      <option v-if="data.firstInList && value && !data.multiple" selected="selected" :value="value">
        {{data.explicitDescription}}
      </option>
      <vue-option
        v-for="option in props.children"
        :key="option.attributes[0].value"
        v-if="option.type === 'element' &&
        option.tagName === 'vue-option'"
        :props="option"
        :selected="option.attributes[0].value === value">
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
        let data = this.parseProps()
        delete data['currentValue']
        return data
      },
      storeData() {
        return {
          id: this.$store.getters['data/currentId'],
          key: this.parseProps().id,
          value: this.parseProps().currentValue ? this.parseProps().currentValue : ''
        }
      },
      storeForm() {
        return {
          formId: this.parseProps().formName,
          key: this.parseProps().id,
          value: this.parseProps().currentValue ? this.parseProps().currentValue : ''
        }
      },
      value: {
        get() {
          return this.getDataFromForm(this.storeForm)
        },
        set(value) {
          this.$store.dispatch('form/setFieldToForm', {
            formId: this.parseProps().formName,
            key: this.parseProps().id,
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
