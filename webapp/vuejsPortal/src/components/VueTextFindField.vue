<template>
  <div id="vue-text-find-field">
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
    name: "VueTextFindField",
    props: ['props', 'updateStore'],
    data() {
      return {}
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
      storeForm() {
        return {
          formId: this.props.attributes.formName,
          key: this.props.attributes.name,
          value: this.props.attributes.value ? this.props.attributes.value : ''
        }
      },
      storeFormOp() {
        return {
          formId: this.props.attributes.formName,
          key: this.props.attributes.name + '_op',
          value: this.props.attributes.defaultOption ? this.props.attributes.defaultOption : ''
        }
      },
      storeFormIc() {
        return {
          formId: this.props.attributes.formName,
          key: this.props.attributes.name + '_ic',
          value: this.props.attributes.ignCase ? this.props.attributes.ignCase : ''
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
      valueOp: {
        get() {
          return this.getDataFromForm(this.storeFormOp)
        },
        set(value) {
          this.$store.dispatch('form/setFieldToForm', {
            formId: this.props.attributes.formName,
            key: this.props.attributes.name + '_op',
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
            formId: this.props.attributes.formName,
            key: this.props.attributes.name + '_ic',
            value: value
          })
        }
      },
      ...mapGetters({
        getForm: 'form/form',
        getDataFromForm: 'form/fieldInForm'
      })
    },
    watch: {
      data: function (from, to) {
        this.$store.dispatch('form/setFieldToForm', this.storeForm)
        this.$store.dispatch('form/setFieldToForm', this.storeFormOp)
        this.$store.dispatch('form/setFieldToForm', this.storeFormIc)
      }
    },
    created() {
      this.$store.dispatch('form/setFieldToForm', this.storeForm)
      this.$store.dispatch('form/setFieldToForm', this.storeFormOp)
      this.$store.dispatch('form/setFieldToForm', this.storeFormIc)
    }
  }
</script>

<style scoped>

</style>
