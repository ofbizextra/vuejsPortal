<template>
  <div id="vue-text-find">
    <select class="selectBox" v-if="data.opEquals" v-model="valueOp">
      <option value="equals">egal</option>
      <option value="beginWith">debute</option>
      <option value="contains">contient</option>
      <option value="isEmpty">est vide</option>
      <option value="notEqual">Diff.</option>
    </select>
    <input
      type="text"
      v-bind:name="data.name"
      v-bind:size="data.size"
      v-model="value"
      v-bind:maxlength="data.maxlength"
      v-bind:autocomplete="data.autocomplete"
      v-bind:tabindex="data.tabindex"
    />
    <span v-bind:class="data.titleStyle ? data.titleStyle : ''">
      <input v-if="data.hideIgnoreCase" type="hidden" :name="data.name + '_ic'" :value="data.ignCase ? 'Y' : ''"/>
      <input v-else type="checkbox" :name="data.name + '_ic'" v-model="valueIc" checked="data.ignCase"/>
    </span>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "VueTextFind",
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
          key: this.parseProps().name,
          value: this.parseProps().value ? this.parseProps().value : ''
        }
      },
      storeDataOp() {
        return {
          id: this.$store.getters['data/currentId'],
          key: this.parseProps().name + '_op',
          value: this.parseProps().defaultOption ? this.parseProps().defaultOption : ''
        }
      },
      storeDataIc() {
        return {
          id: this.$store.getters['data/currentId'],
          key: this.parseProps().name + '_ic',
          value: this.parseProps().ignCase ? this.parseProps().ignCase : ''
        }
      },
      storeForm() {
        return {
          formId: this.parseProps().formName,
          key: this.parseProps().name,
          value: this.parseProps().value ? this.parseProps().value : ''
        }
      },
      storeFormOp() {
        return {
          formId: this.parseProps().formName,
          key: this.parseProps().name + '_op',
          value: this.parseProps().defaultOption ? this.parseProps().defaultOption : ''
        }
      },
      storeFormIc() {
        return {
          formId: this.parseProps().formName,
          key: this.parseProps().name + '_ic',
          value: this.parseProps().ignCase ? this.parseProps().ignCase : ''
        }
      },
      value: {
        get() {
          return this.getDataFromForm(this.storeForm)
        },
        set(value) {
          this.$store.dispatch('form/setFieldToForm', {
            formId: this.parseProps().formName,
            key: this.parseProps().name,
            value: value
          })
        }
      },
      valueOp: {
        get() {
          return this.getDataFromForm(this.storeFormOp)
        },
        set(value) {
          this.$store.dispatch('form/setFieldToForm', {
            formId: this.parseProps().formName,
            key: this.parseProps().name + '_op',
            value: value
          })
        }
      },
      valueIc: {
        get() {
          return this.getDataFromForm(this.storeFormIc)
        },
        set(value) {
          this.$store.dispatch('form/setFieldToForm', {
            formId: this.parseProps().formName,
            key: this.parseProps().name + '_ic',
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
        console.log('vue-text : ', this.storeData)
        this.$store.dispatch('data/addDataToExample', this.storeData)
        this.$store.dispatch('data/addDataToExample', this.storeDataOp)
        this.$store.dispatch('data/addDataToExample', this.storeDataIc)
        this.$store.dispatch('form/setFieldToForm', this.storeForm)
        this.$store.dispatch('form/setFieldToForm', this.storeFormOp)
        this.$store.dispatch('form/setFieldToForm', this.storeFormIc)
      }
    },
    created() {
      console.log('vue-text : ', this.storeData)
      this.$store.dispatch('data/addDataToExample', this.storeData)
      this.$store.dispatch('data/addDataToExample', this.storeDataOp)
      this.$store.dispatch('data/addDataToExample', this.storeDataIc)
      this.$store.dispatch('form/setFieldToForm', this.storeForm)
      this.$store.dispatch('form/setFieldToForm', this.storeFormOp)
      this.$store.dispatch('form/setFieldToForm', this.storeFormIc)
    }
  }
</script>

<style scoped>

</style>
