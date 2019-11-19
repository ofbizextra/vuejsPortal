// todo compare size and description.size and tronque
<template>
  <div id="vue-display-field">
    <div v-if="editable">
      <div v-if="editing">
        <v-text-field class="d-inline-flex ma-1" v-model="editValue" @keyup.enter="save"></v-text-field><span>
        <v-btn class="d-inline-flex ma-1 primary dark" @click="save">Save</v-btn>
        <v-btn class="d-inline-flex ma-1" @click="toggleEdit">Cancel</v-btn>
      </span>
      </div>
      <div v-else>
        <v-hover v-slot:default="{ hover }">
          <label :class="hover ? 'font-weight-bold' : ''" v-bind="data" class="ma-1" @click="toggleEdit">{{value}}</label>
        </v-hover>
      </div>
    </div>
    <div v-else>
        <label v-bind="data" class="ma-1">{{value}}</label>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: "VueDisplayField",
    props: [
      'props'
    ],
    data() {
      return {
        pointer: {
          entityName: this.getNestedObject(this.props, ['stPointer', 'stEntityName']),
          id: this.getNestedObject(this.props, ['stPointer', 'id']),
          attribute: this.getNestedObject(this.props, ['stPointer', 'field'])
        },
        editing: false,
        editValue: '',
        edited: false,
        newValue: ''
      }
    },
    computed: {
      ...mapGetters({
        getData: 'data/entityRowAttribute',
        getDataFromForm: 'form/fieldInForm'
      }),
      data() {
        let data = this.props.attributes
        if (data.className || (data.alert && data.alert === true)) {
          data.class = data.className ? data.className : '' + ' ' + data.alert === true ? 'alert' : ''
        }
        return data
      },
      havePointer() {
        return this.pointer.entityName !== ''
      },
      getPointer() {
        return this.$store.getters['data/entityRowAttribute'](this.pointer);
      },
      storeForm() {
        return {
          formId: this.props.attributes.formName,
          key: this.props.attributes.name,
          value: this.pointer.entityName ? this.getPointer : this.data.description
        }
      },
      editable() {
        return this.data.hasOwnProperty('inPlaceEditor') ? this.data.inPlaceEditor : false
      },
      url() {
        return this.data.hasOwnProperty('url') ? this.data.url : ''
      },
      params() {
        return this.data.hasOwnProperty('fieldMap') ? {...this.data.fieldMap, ...{[this.data.name]: this.editValue}} : {[this.data.name]: this.editValue}
      },
      value() {
        if (this.havePointer) {
          return this.getPointer
        } else {
          if (!this.edited) {
            return this.data.title ? this.data.title : this.data.description
          } else {
            return this.newValue
          }
        }
      }
    },
    methods: {
      toggleEdit() {
        this.editValue = ''
        this.editing = !this.editing
      },
      save() {
        this.$store.dispatch('backOfficeApi/doPost', {uri: this.url, params: this.params}).then(() => {
          if (this.havePointer) {
            this.$store.dispatch('data/setEntityRow', {
              entityName: this.getNestedObject(this.props, ['stPointer', 'stEntityName']),
              primaryKey: this.getNestedObject(this.props, ['stPointer', 'id']),
              data: {[this.data.name]: this.editValue}
            })
          } else {
            this.newValue = this.editValue
            this.edited = true
          }
          this.toggleEdit()
        },() => {
          this.toggleEdit()
        })
      }
    },
    created() {
      // this.$store.dispatch('form/setFieldToForm', this.storeForm)
    },
    watch: {
      props: function () {
        this.pointer = {
          entityName: this.getNestedObject(this.props, ['stPointer', 'stEntityName']),
          id: this.getNestedObject(this.props, ['stPointer', 'id']),
          attribute: this.getNestedObject(this.props, ['stPointer', 'field'])
        }
        // this.$store.dispatch('form/setFieldToForm', this.storeForm)
      }
    }
  }
</script>

<style scoped>

</style>
