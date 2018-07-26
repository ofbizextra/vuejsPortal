<template>
  <div id="vue-radio">
    <input v-if="data.conditionGroup" type="hidden" :name="data.name + '_grp'" v-bind:value="data.conditionGroup"/>
    <span v-for="(item, index) in props.children" v-if="item.type === 'element' && item.tagName === 'vue-radio-item'">
      <input type="radio"
             :checked="!!((data.currentValue && data.currentValue === childrenData(index).key) || (!data.currentValue && data.noCurrentSelectedKey && data.noCurrentSelectedKey === childrenData(index).key))"
             :tabIndex="data.currentValue && data.tabIndex ? data.tabIndex : false"
             :name="data.name"
             :value="childrenData(index).key"
             v-model="value"
      />
        {{childrenData(index).description}}
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
        let data = this.parseProps()
        delete data['currentValue']
        return data
      },
      storeForm() {
        return {
          formId: document.getElementsByName(this.data.name)[0].form.name,
          key: this.parseProps().name,
          value: this.parseProps().currentValue ? this.parseProps().currentValue : ''
        }
      },
      value: {
        get () {
          if (document.getElementsByName(this.data.name)[0] && this.getForm(this.storeForm.formId)) {
            return this.getDataFromForm(this.storeForm)
          } else {
            return ''
          }
        },
        set (value) {
          this.$store.dispatch('form/setFieldToForm', {
            formId: document.getElementsByName(this.data.name)[0].form.name,
            key: this.parseProps().name,
            value: value
          })
        }
      },
      childrenData() {
        return (index) => {
          let data = {}
          this.props.children[index].attributes.map(attr => {
            if (attr.value === 'false') {
              data[attr.key] = false
            } else if (attr.value === 'true') {
              data[attr.key] = true
            } else {
              data[attr.key] = attr.value
            }
          })
          return data
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
    mounted() {
      console.log('vue-radio : ', document.getElementsByName(this.data.name))
      this.$store.dispatch('form/setFieldToForm', this.storeForm)
    }
  }
</script>

<style scoped>

</style>
