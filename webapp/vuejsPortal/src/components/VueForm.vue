<template>
  <form v-bind:id="'vue-form_' + data.name" v-bind="data">
    <div
      v-for="(component, key) in props.children"
      :key="key"
      v-bind:is="constantes.components[component.name]"
      :props="component"
      :updateStore="updateStore">
    </div>
  </form>
</template>

<script>
  import {mapGetters} from 'vuex'
  import cst from '../js/constantes'

  export default {
    name: "VueForm",
    props: ['props', 'updateStore'],
    data() {
      return {
        constantes: cst
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
          list: {
            data1: 'blablabla',
            data2: 'blablablabla',
            data3: 'blablablablabla',
          },
          primaryKey: this.props.attributes.primaryKey ? this.props.attributes.primaryKey : ''
        })
      }
    }
  }
</script>

<style scoped>

</style>
