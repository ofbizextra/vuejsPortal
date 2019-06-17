<template>
  <span class="field-lookup">
    <div class="autosuggest-container" :style="{display: 'inline-block'}">
      <vue-autosuggest v-model="term" :name="data.name" :id="data.id" :size="data.size" @input="updateWordList"
                       :suggestions="[{data: [...wordList]}]" :input-props="{id: data.id, name: data.name, size: data.size}"
                       @selected="onSelected" :get-suggestion-value="getSuggestionValue" :render-suggestion="renderSuggestion">
      </vue-autosuggest>
    </div>
    <a href="#" :style="{display: 'inline-block'}"></a>
  </span>
</template>

<script>
  import {mapGetters} from 'vuex'
  import constantes from '../js/constantes'

  export default {
    name: "VueLookupField",
    props: ['props', 'updateStore'],
    data() {
      return {
        term: '',
        wordList: []
      }
    },
    computed: {
      data() {
        let data = this.props.attributes
        //delete data['value']
        if (data.className || (data.alert && data.alert === true)) {
          data.class = data.className ? data.className : '' + ' ' + data.alert === true ? 'alert' : ''
        }
        return data
      },
      storeForm() {
        return {
          formId: this.formName,
          key: this.name,
          value: this.value
        }
      },
      valueStored: {
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
        getForm: 'form/form',
        getDataFromForm: 'form/fieldInForm'
      }),
      id() {
        return this.data.hasOwnProperty('id') ? this.data.id : ''
      },
      name() {
        return this.data.hasOwnProperty('name') ? this.data.name : ''
      },
      value() {
        return this.data.hasOwnProperty('value') ? this.data.value : ''
      },
      formName() {
        return this.data.hasOwnProperty('formName') ? this.data.formName : ''
      },
      ajaxUrl() {
        return this.data.hasOwnProperty('ajaxUrl') ? this.data.ajaxUrl : ''
      },
      fieldFormName() {
        return this.data.hasOwnProperty('fieldFormName') ? this.data.fieldFormName : ''
      },
      ajaxLookup() {
        return this.data.hasOwnProperty('ajaxEnabled') ? this.data.ajaxEnabled ? 'Y' : 'N' : 'N'
      },
      searchValueFieldName() {
        return this.data.hasOwnProperty('fieldFormName') ? this.data.fieldFormName : ''
      },
      params() {
        return {
          ajaxLookup: 'Y',//this.ajaxLookup,
          searchValueFieldName: this.name, //this.name
          term: this.term,
          autocompleterViewSize: "50",
          displayFieldsSet: []
        }
      }
    },
    methods: {
      updateWordList() {
        this.$store.dispatch('form/setFieldToForm', {
          formId: this.props.attributes.formName,
          key: this.props.attributes.name,
          value: this.term
        })
        this.$store.dispatch('backOfficeApi/doPost', {
          uri: constantes.apiUrl + '/' + this.fieldFormName,
          params: this.params
        }).then(result => {
          this.displayFields = result.body.viewScreen[0].attributes.displayFieldsSet
          this.wordList = result.body.viewScreen[0].attributes.autocompleteOptions
          return result.body
        }, error => {
          return error.body
        })
      },
      onSelected(item) {
        this.term = item.item[this.name]
        this.$store.dispatch('form/setFieldToForm', {
          formId: this.props.attributes.formName,
          key: this.props.attributes.name,
          value: this.term
        })
      },
      getSuggestionValue(suggestion) {
        return suggestion.item[this.name]
      },
      renderSuggestion(suggestion) {
        let str = ''
        for (let i = 0; i < this.displayFields.length; i++) {
          str += suggestion.item[this.displayFields[i]]
          if (i < this.displayFields.length - 1) {
            str += ' - '
          }
        }
        return str
      }
    },
    watch: {
      data: function (from, to) {
        this.$store.dispatch('form/setFieldToForm', this.storeForm)
      }
    },
    created() {
      this.$store.dispatch('form/setFieldToForm', this.storeForm)
    }
  }
</script>

<style>
  .autosuggest__results-item--highlighted {
    background-color: rgb(255, 233, 58);
  }

  .autosuggest__results {
    overflow: scroll;
    max-height: 250px;
  }

  .autosuggest-container {
    display: flex;
    justify-content: center;
  }
</style>
