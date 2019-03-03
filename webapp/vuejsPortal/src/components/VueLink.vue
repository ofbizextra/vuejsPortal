<template>
  <div id="vue-link">
    <!--method="post"-->
    <!--:action="actionUrl"-->
    <!--:target="targetWindow"-->
    <form
      v-if="linkType === 'hidden-form'"
      v-on:click.prevent=""
      :name="uniqueItemName"
    >
      <input
        v-for="(parameter, id) in parameterList"
        :key="id"
        :name="parameter.name"
        :value="parameter.value"
        type="hidden"
      />
    </form>
    <!--:data-dialog-url="linkUrl"-->
    <a
      v-if="uniqueItemName"
      v-bind:id="id + '_link'"
      :data-dialog-params="params"
      :data-dialog-width="width"
      :data-dialog-height="height"
      :data-dialog-title="text"
      :class="style"
      v-bind:href="`${href}#${href}`"
      v-on:click="redirect"
    >
      <img :src="imgSrc" alt="" v-if="hasImage">
      {{text}}
    </a>
    <a
      v-else-if="linkType === 'hidden-form' || linkUrl.length > 0"
      v-bind:id="id"
      v-bind:class="style"
      v-bind:name="name"
      v-bind:target="targetWindow"
      v-bind:href="`${href}#${href}`"
      v-on:click.prevent="redirect"
    >
      <img :src="imgSrc" alt="" v-if="hasImage">
    {{' ' + text}}
    </a>
  </div>
</template>

<script>
  import constantes from '../js/constantes'

  export default {
    name: "VueLink",
    props: ['props', 'updateStore'],
    data() {
      return {
        constantes
      }
    },
    computed: {
      data() {
        let data = this.props.attributes
        delete data['value']
        return data
      },
      linkUrl() {
        return this.data.hasOwnProperty('linkUrl') ? this.data.linkUrl : ''
      },
      parameterList() {
        return this.data.hasOwnProperty('parameterList') ? this.data.parameterList : ''
      },
      parameterMap() {
        return this.data.hasOwnProperty('parameterMap') ? this.data.parameterMap : {}
      },
      targetWindow() {
        return this.data.hasOwnProperty('targetWindow') ? this.data.targetWindow : ''
      },
      uniqueItemName() {
        return this.data.hasOwnProperty('uniqueItemName') ? this.data.uniqueItemName : ''
      },
      actionUrl() {
        return this.data.hasOwnProperty('actionUrl') ? this.data.actionUrl : ''
      },
      linkType() {
        return this.data.hasOwnProperty('linkType') ? this.data.linkType : ''
      },
      id() {
        return this.data.hasOwnProperty('id') ? this.data.id : ''
      },
      style() {
        return this.data.hasOwnProperty('style') ? this.data.style : ''
      },
      name() {
        return this.data.hasOwnProperty('name') ? this.data.name : ''
      },
      height() {
        return this.data.hasOwnProperty('height') ? this.data.height : ''
      },
      width() {
        return this.data.hasOwnProperty('width') ? this.data.width : ''
      },
      text() {
        return this.data.hasOwnProperty('text') ? this.data.text : ''
      },
      hasImage() {
        return this.data.hasOwnProperty('imgSrc') && this.data.imgSrc !== ''
      },
      imgSrc() {
        return this.data.hasOwnProperty('imgSrc') ? this.data.imgSrc : ''
      },
      params() {
        if (this.uniqueItemName.length > 0) {
          return ""
        }
        let params = "{&quot;presentation&quot;:&quot;layer&quot; "
        for (let parameter in this.parameterList) {
          params += `,&quot;${parameter.name}&quot;: &quot;${parameter.value}&quot;`
        }
        params += "}"
        return params
      },
      href() {
        if (this.linkType === 'hidden-form') {
          // return `javascript:document.${this.uniqueItemName}.submit()` // todo: no yet supported
          return ''
        }
        return this.linkUrl
      }
    },
    methods: {
      redirect() {
        console.log('click on link : ', this.data.text)
        if (this.data.hasOwnProperty('targetWindow') && this.data.hasOwnProperty('target')) {
          this.$store.dispatch('ui/setArea', {
            areaId: this.data.targetWindow,
            targetUrl: `/exampleapi/control/${this.data.target}`,
            wait: this.$wait,
            params: this.parameterMap
          })
        }
      }
    }
  }
</script>

<style scoped>

</style>
