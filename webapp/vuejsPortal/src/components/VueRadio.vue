<template>
  <div id="vue-radio">
    <input v-if="data.conditionGroup" type="hidden" :name="data.name + '_grp'" v-bind:value="data.conditionGroup"/>
    <span v-for="item in props.attributes.items">
      <input type="radio"
             :checked="!!((data.currentValue && data.currentValue === item.key) || (!data.currentValue && data.noCurrentSelectedKey && data.noCurrentSelectedKey === item.key))"
             :tabIndex="data.currentValue && data.tabIndex ? data.tabIndex : false"
             :name="data.name"
             :value="item.key"
             v-model="value"
      />
        {{item.description}}
    </span>
    <vue-error v-if="data.event" component="event"/>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "VueRadio",
    props: ['props', 'updateStore'],
    data() {
      return {}
    },
    computed: {
      data() {
        let data = this.props.attributes
        delete data['currentValue']
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
