<template>
  <span class="field-lookup">
    <div class="autosuggest-container" :style="{display: 'inline-block'}">
      <v-combobox
              :id="id"
              v-model="valueStored"
              :items="items"
              hide-no-data
              hide-selected
              no-filter
              :return-object="false"
              :search-input.sync="search"/>
    </div>
    <v-btn icon @click="showModal"><v-icon>mdi-arrow-expand</v-icon></v-btn>
    <span v-if="tooltip" :id="'0_lookupId_' + id" class="tooltip">{{tooltip}}</span>
    <p>{{modalResult}}</p>
    <modal :name="id + '_modal'" :id="id + '_modal'" :adaptive="true" :resizable="true" height="auto" :scrollable="true">
      <vue-container :props="{attributes: {id: id + '_modalContent'}}"
                     :auto-update-params="{targetUrl: getCurrentApi + '/' + fieldFormName, params: {presentation: 'layer'}}">

      </vue-container>
    </modal>
  </span>
</template>

<script>
  import {mapGetters} from 'vuex'
  import _ from 'lodash'

  export default {
    name: "VueLookupField",
    props: ['props', 'updateStore'],
    data() {
      return {
        term: '',
        wordList: [],
        displayFields: [],
        returnField: '',
        modalResult: '',
        search: ''
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
        getDataFromForm: 'form/fieldInForm',
        getCurrentApi: 'backOfficeApi/currentApi'
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
          term: this.valueStored,
          autocompleterViewSize: "50",
          displayFields: []
        }
      },
      tooltip() {
        let selectedItem = this.wordList.find(item => item[this.name] === this.valueStored)
        if (selectedItem === undefined) {
          return false
        }
        let str = ''
        for (let i = 0; i < this.displayFields.length; i++) {
          str += selectedItem[this.displayFields[i]]
          if (i < this.displayFields.length - 1) {
            str += ' - '
          }
        }
        return str
      },
      items() {
        let items = []
        for (let item of this.wordList) {
          let text = ""
          for (const [index, displayField] of this.displayFields.entries()) {
            if (index > 0) {
              text += ' - '
            }
            text += item[displayField]
          }
          text += ' [' + item[this.returnField] + ']'
          items.push({text: text, value: item[this.returnField]})
        }
        return items
      }
    },
    methods: {
      debounceUpdateWordList: _.debounce(function () {this.updateWordList();}, 250),
      updateWordList() {
          this.$store.dispatch('form/setFieldToForm', {
            formId: this.props.attributes.formName,
            key: this.props.attributes.name,
            value: this.valueStored
          })
          this.$store.dispatch('backOfficeApi/doPost', {
            uri: this.$store.getters['backOfficeApi/apiUrl'] + '/' + this.fieldFormName,
            params: this.params
          }).then(result => {
            this.returnField = result.body.viewScreen[0].attributes.returnField === null ? '' : result.body.viewScreen[0].attributes.returnField
            this.displayFields = result.body.viewScreen[0].attributes.displayFields
            this.wordList = result.body.viewScreen[0].attributes.autocompleteOptions === null ? [] : result.body.viewScreen[0].attributes.autocompleteOptions
            this.$store.dispatch('ui/incrementUpdateCpt')
            return result.body
          }, error => {
            return error.body
          })
      },
      onSelected(item) {
        this.valueStored = item.item[this.name]
        this.$store.dispatch('form/setFieldToForm', {
          formId: this.props.attributes.formName,
          key: this.props.attributes.name,
          value: this.valueStored
        })
      },
      getSuggestionValue(suggestion) {
        return suggestion.item[this.returnField]
      },
      renderSuggestion(suggestion) {
        let str = ''
        for (let i = 0; i < this.displayFields.length; i++) {
          str += suggestion.item[this.displayFields[i]]
          str += ' - '
        }
        str += `[${suggestion.item[this.returnField]}]`
        return str
      },
      showModal() {
        this.$store.dispatch('ui/setArea', {
          areaId: this.id + '_modalContent',
          targetUrl: `${this.$store.getters['backOfficeApi/currentApi']}/${this.fieldFormName}`,
          wait: this.$wait,
          params: {presentation: 'layer'}
        }).then(
          () => {
            this.$modal.show(this.id + '_modal')
          }, error => {
            console.log(error)
          })
        // this.$store.dispatch('backOfficeApi/doPost', {uri: constantes.apiUrl + '/' + this.fieldFormName, params: {presentation: 'layer'}}).then(
        //   result => {
        //     console.log('Modal result : ', result)
        //     this.modalResult = result.body
        //     this.$modal.show(this.id + '_modal')
        //   }, error =>  {
        //     console.log(error)
        //     this.modalResult = error
        //   })
      }
    },
    watch: {
      data: function () {
        this.$store.dispatch('form/setFieldToForm', this.storeForm)
      },
      search: function (val) {
        this.valueStored = val
        this.debounceUpdateWordList()
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
