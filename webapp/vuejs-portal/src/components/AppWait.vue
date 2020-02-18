<template>
  <div>
    <input type="hidden" id="loader-wrapper-hidden" v-if="!blockUi" v-show="waitAny"/>
    <BlockUI v-if="blockUi" v-show="waitAny" message="Fetching datas...">
      <spinner
          id="loader-wrapper"
          :animation-duration="1200"
          :size="55"
          :color="'#28b3ff'"
      />
    </BlockUI>
  </div>
</template>

<script>
  import constants from '../js/constants'

  export default {
    name: "AppWait",
    computed: {
      blockUi() {
        return constants.blockUi
      },
      waitAny() {
        return this.$wait.any
      }
    },
  }
</script>

<style scoped>

</style>