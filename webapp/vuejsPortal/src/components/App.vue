<template>
  <div id="app">
    <div v-for="module in Object.keys(xmlParsed)" v-bind:is="'vue-' + module" v-bind:props="xmlParsed[module]">
    </div>
    <p>{{data}}</p>
    <div v-for="form in parsedData" v-if="form.type === 'element' && form.tagName === 'form'" v-bind:is="form.tagName">
      <div v-for="table in form.children" v-if="table.type === 'element' && table.tagName === 'table'" v-bind:is="table.tagName">
        <div v-for="row in table.children" v-if="row.type === 'element' && row.tagName === 'tr'" v-bind:is="row.tagName">
          <div v-for="cell in row.children" v-if="cell.type === 'element' && cell.tagName === 'td'" v-bind:is="cell.tagName">
            <div v-for="dropdown in cell.children" v-if="dropdown.type === 'element' && dropdown.tagName === 'vue-drop-down'" v-bind:is="dropdown.tagName">

            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Xml2js from 'xml2js'
  import constantes from './../js/constantes'
  import {parse} from 'himalaya'

  export default {
  name: 'app',
  data () {
    return {
      xml: '<form name="FindExamples" type="single" target="FindExample" default-entity-name="Example">\n' +
      '        <field name="noConditionFind"><hidden value="Y"/><!-- if this isn\'t there then with all fields empty no query will be done --></field>\n' +
      '        <field name="exampleId" title="${uiLabelMap.ExampleExampleId}"><text-find/></field>\n' +
      '        <field name="exampleName" title="${uiLabelMap.CommonName}"><text-find/></field>\n' +
      '        <field name="exampleTypeId" title="${uiLabelMap.CommonType}">\n' +
      '            <drop-down allow-empty="true" current-description="">\n' +
      '                <entity-options key-field-name="exampleTypeId" entity-name="ExampleType">\n' +
      '                    <entity-order-by field-name="description"/>\n' +
      '                </entity-options>\n' +
      '            </drop-down>\n' +
      '        </field>\n' +
      '        <field name="statusId" title="${uiLabelMap.CommonStatus}">\n' +
      '            <drop-down allow-empty="true" current-description="">\n' +
      '                <entity-options key-field-name="statusId" entity-name="ExampleStatusItem"/>\n' +
      '            </drop-down>\n' +
      '        </field>\n' +
      '        <field name="listOtherElements">\n' +
      '            <radio>\n' +
      '                <option key="items"/>\n' +
      '                <option key="features"/>\n' +
      '                <option key="status"/>\n' +
      '            </radio>\n' +
      '        </field>\n' +
      '        <field name="searchButton"><submit/></field>\n' +
      '        <field name="exportButton" position="2" widget-style="smallSubmit" title=" ">\n' +
      '            <hyperlink also-hidden="false" target-type="plain" description="${uiLabelMap.CommonExport}"\n' +
      '                       target="javascript: document.FindExamples.action=\'ListExampleExport\'; document.FindExamples.submit();"/>\n' +
      '        </field>\n' +
      '    </form>',
      xmlParsed: {},
      data: '',
      parsedData: {}
    }
  },
  mounted () {
    Xml2js.parseString(this.xml, (err, result) => {
      this.xmlParsed = result
    })
    this.$http.get(constantes.ApiUrl + constantes.editExampleLayer.path).then(response => {
      console.log(response.body)
      this.data = response.body
      this.parsedData = parse(this.data)
    }, error => {
      console.log(error.body)
    })
  }
}
</script>

<style>

</style>
