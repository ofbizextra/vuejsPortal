<template>
  <div id="app">
    <div v-for="module in Object.keys(xmlParsed)" v-bind:is="'vue-' + module" v-bind:props="xmlParsed[module]">
    </div>
  </div>
</template>

<script>
  import Xml2js from 'xml2js'

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
      xmlParsed: {}
    }
  },
  mounted () {
    Xml2js.parseString(this.xml, (err, result) => {
      this.xmlParsed = result
    })
  }
}
</script>

<style>

</style>
