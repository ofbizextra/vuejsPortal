<template>
  <div id="list">
    <!-- Begin Macro renderFormatItemRowOpen from file HtmlFormMacroLibrary.ftl -->
    <tr  >
      <!-- Begin Macro renderFormatItemRowCellOpen from file HtmlFormMacroLibrary.ftl -->
      <td >
        <!-- Begin Macro makeHyperlinkString from file HtmlFormMacroLibrary.ftl -->
        <a class="buttontext"
           href="/exampleapi/control/EditExample?exampleId=EX01"



           title="Example ID">
          EX01</a>
        <vue-tooltip


        />  <!-- Begin Macro renderFormatItemRowCellClose from file HtmlFormMacroLibrary.ftl -->
      </td>
      <!-- Begin Macro renderFormatItemRowCellOpen from file HtmlFormMacroLibrary.ftl -->
      <td >
        <vue-label
          imageLocation=""
          alert="false"
          type="text"
          idName="ListExamples_exampleName_o_0"
          description="Example 1"
          title=""
          class=""
          inPlaceEditorUrl=""
          inPlaceEditorParams=""
          formName="ListExamples"
        /><vue-tooltip


      />  <!-- Begin Macro renderFormatItemRowCellClose from file HtmlFormMacroLibrary.ftl -->
      </td>
      <!-- Begin Macro renderFormatItemRowCellOpen from file HtmlFormMacroLibrary.ftl -->
      <td >
        <vue-label
          imageLocation=""
          alert="false"
          type=""
          idName="ListExamples_exampleTypeId_o_0"
          description="Contrived"
          title=""
          class=""
          inPlaceEditorUrl=""
          inPlaceEditorParams=""
          formName="ListExamples"
        /><vue-tooltip


      />  <!-- Begin Macro renderFormatItemRowCellClose from file HtmlFormMacroLibrary.ftl -->
      </td>
      <!-- Begin Macro renderFormatItemRowCellOpen from file HtmlFormMacroLibrary.ftl -->
      <td >
        <vue-label
          imageLocation=""
          alert="false"
          type=""
          idName="ListExamples_statusId_o_0"
          description="In Design"
          title=""
          class=""
          inPlaceEditorUrl=""
          inPlaceEditorParams=""
          formName="ListExamples"
        /><vue-tooltip


      />  <!-- Begin Macro renderFormatItemRowCellClose from file HtmlFormMacroLibrary.ftl -->
      </td>
      <!-- Begin Macro renderFormatItemRowCellOpen from file HtmlFormMacroLibrary.ftl -->
      <td >
        <vue-label
          imageLocation=""
          alert="false"
          type="text"
          idName="ListExamples_description_o_0"
          description=""
          title=""
          class=""
          inPlaceEditorUrl=""
          inPlaceEditorParams=""
          formName="ListExamples"
        /><vue-tooltip


      />  <!-- Begin Macro renderFormatItemRowCellClose from file HtmlFormMacroLibrary.ftl -->
      </td>
      <!-- Begin Macro renderFormatItemRowCellOpen from file HtmlFormMacroLibrary.ftl -->
      <td >
        <vue-label
          imageLocation=""
          alert="false"
          type="text"
          idName="ListExamples_conditionalDesc_o_0"
          description=""
          title=""
          class=""
          inPlaceEditorUrl=""
          inPlaceEditorParams=""
          formName="ListExamples"
        /><vue-tooltip


      />  <!-- Begin Macro renderFormatItemRowCellClose from file HtmlFormMacroLibrary.ftl -->
      </td>
      <!-- Begin Macro renderFormatItemRowClose from file HtmlFormMacroLibrary.ftl -->
    </tr>
  </div>
</template>

<script>
export default {
  name: 'list',
  data () {
    return {
      all: {
        itemA: { id: 1, name: 'item A', description: "item portant la lettre A ..." },
        itemB: { id: 2, name: 'item B', description: "item portant la lettre B ..." },
        itemC: { id: 3, name: 'item C', description: "item portant la lettre C ..." }
      }
    }
  }
}
</script>

<style>

</style>
