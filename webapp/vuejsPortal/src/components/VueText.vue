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
      },
      storeForm() {
        return {
          formId: document.getElementById(this.data.id).form.name,
          key: this.parseProps().id,
          value: this.parseProps().value ? this.parseProps().value : ''
        }
      },
      value: {
        get () {
          if (document.getElementById(this.data.id) && this.getForm(this.storeForm.formId)) {
            return this.dataFromExample(this.storeData)
          } else {
            return ''
          }
        },
        set (value) {
          this.$store.dispatch('form/setFieldToForm', {
            formId: document.getElementById(this.data.id).form.name,
            key: this.parseProps().id,
            value: value
          })
        }
      },
      ...mapGetters({
        dataFromExample: 'data/dataFromExample',
        currentId: 'data/currentId'
      })
    },
    watch: {
      data: function (from, to) {
        console.log('vue-text : ', this.storeData)
        this.$store.dispatch('data/addDataToExample', this.storeData)
        this.$store.dispatch('form/setFieldToForm', this.storeForm)
      }
    },
    mounted() {
      console.log('vue-text : ', this.storeForm)
      this.$store.dispatch('data/addDataToExample', this.storeData)
      this.$store.dispatch('form/setFieldToForm', this.storeForm)
    }
  }
</script>

<style scoped>

</style>
