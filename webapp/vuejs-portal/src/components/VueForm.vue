<template>
  <v-form :ref="data.name" v-bind:id="'vue-form_' + data.name" v-bind="data" :autocomplete="autocomplete" v-model="valid" lazy-validation>
    <div
      v-for="(component, key) in props.children"
      :key="key"
      v-bind:is="constantes.components[component.name]"
      :props="component"
      :updateStore="updateStore">
    </div>
  </v-form>
</template>

<script>
  import cst from '../js/constantes'

  export default {
    name: "VueForm",
    props: ['props', 'updateStore'],
    data() {
      return {
        constantes: cst,
        valid: false
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
      storeData() {
        return {
          id: this.$store.getters['data/currentId'],
          key: this.props.attributes.id,
          value: this.props.attributes.value ? this.props.attributes.value : ''
        }
      },
      autocomplete() {
        return this.data.hasOwnProperty('autocomplete') && this.data.autocomplete !== '' ? 'on' : 'off'
      }
    },
    watch: {
      data: function (from, to) {
        this.$store.dispatch('form/addForm', to.name)
        this.$store.dispatch('form/setFieldToForm', {
          formId: this.props.attributes.name,
          key: 'linkUrl',
          value: this.props.attributes.linkUrl
        })
        this.$store.dispatch('form/setFieldToForm', {
          formId: this.props.attributes.name,
          key: 'viewIndex',
          value: this.props.attributes.viewIndex
        })
        this.$store.dispatch('form/setFieldToForm', {
          formId: this.props.attributes.name,
          key: 'viewIndexField',
          value: this.props.attributes.viewIndexField
        })
        this.$store.dispatch('form/setFieldToForm', {
          formId: this.props.attributes.name,
          key: 'viewSize',
          value: this.props.attributes.viewSize
        })
        this.$store.dispatch('form/setFieldToForm', {
          formId: this.props.attributes.name,
          key: 'viewSizeField',
          value: this.props.attributes.viewSizeField
        })

      }
    },
    created() {
      console.log('vue-form : ', this.data.name)
      this.$store.dispatch('form/addForm', this.data.name)
      this.$store.dispatch('form/setFieldToForm', {
        formId: this.props.attributes.name,
        key: 'linkUrl',
        value: this.props.attributes.linkUrl
      })
      this.$store.dispatch('form/setFieldToForm', {
        formId: this.props.attributes.name,
        key: 'viewIndex',
        value: this.props.attributes.viewIndex
      })
      this.$store.dispatch('form/setFieldToForm', {
        formId: this.props.attributes.name,
        key: 'viewIndexField',
        value: this.props.attributes.viewIndexField
      })
      this.$store.dispatch('form/setFieldToForm', {
        formId: this.props.attributes.name,
        key: 'viewSize',
        value: this.props.attributes.viewSize
      })
      this.$store.dispatch('form/setFieldToForm', {
        formId: this.props.attributes.name,
        key: 'viewSizeField',
        value: this.props.attributes.viewSizeField
      })
      if (this.props.attributes.defaultEntityName) {
        this.$store.dispatch('data/setEntity', {
          entityName: this.props.attributes.defaultEntityName,
          list: {},
          primaryKey: this.props.attributes.primaryKey ? this.props.attributes.primaryKey : ''
        })
      }
    },
    mounted() {
      this.$store.dispatch('form/addFormValidate', {formName: this.data.name, validate: this.$refs[this.data.name].validate})
    }
  }
</script>

<style scoped>

</style>
