<template>
  <form id="vue-form" v-bind="data">
    <div
      v-for="component in props.children"
      v-if="component.type === 'element' && (component.tagName.includes('vue-'))"
      v-bind:is="component.tagName"
      :props="component"
      :updateStore="updateStore">
    </div>
  </form>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "VueForm",
    props: ['props', 'updateStore'],
    data() {
      return {}
    },
    computed: {
      data() {
        let data = this.parseProps()
        delete data['value']
        return data
      },
      storeData() {
        return {
          id: this.$store.getters['data/currentId'],
          key: this.parseProps().id,
          value: this.parseProps().value ? this.parseProps().value : ''
        }
      }
    },
    watch: {
      data: function (from, to) {
        this.$store.dispatch('form/addForm', to.name)
        this.$store.dispatch('form/setFieldToForm', {
          formId: this.parseProps().name,
          key: 'linkUrl',
          value: this.parseProps().linkUrl
        })
        this.$store.dispatch('form/setFieldToForm', {
          formId: this.parseProps().name,
          key: 'viewIndex',
          value: this.parseProps().viewIndex
        })
        this.$store.dispatch('form/setFieldToForm', {
          formId: this.parseProps().name,
          key: 'viewIndexField',
          value: this.parseProps().viewIndexField
        })
        this.$store.dispatch('form/setFieldToForm', {
          formId: this.parseProps().name,
          key: 'viewSize',
          value: this.parseProps().viewSize
        })
        this.$store.dispatch('form/setFieldToForm', {
          formId: this.parseProps().name,
          key: 'viewSizeField',
          value: this.parseProps().viewSizeField
        })

      }
    },
    created() {
      console.log('vue-form : ', this.data.name)
      this.$store.dispatch('form/addForm', this.data.name)
      this.$store.dispatch('form/setFieldToForm', {
        formId: this.parseProps().name,
        key: 'linkUrl',
        value: this.parseProps().linkUrl
      })
      this.$store.dispatch('form/setFieldToForm', {
        formId: this.parseProps().name,
        key: 'viewIndex',
        value: this.parseProps().viewIndex
      })
      this.$store.dispatch('form/setFieldToForm', {
        formId: this.parseProps().name,
        key: 'viewIndexField',
        value: this.parseProps().viewIndexField
      })
      this.$store.dispatch('form/setFieldToForm', {
        formId: this.parseProps().name,
        key: 'viewSize',
        value: this.parseProps().viewSize
      })
      this.$store.dispatch('form/setFieldToForm', {
        formId: this.parseProps().name,
        key: 'viewSizeField',
        value: this.parseProps().viewSizeField
      })

    }
  }
</script>

<style scoped>

</style>
