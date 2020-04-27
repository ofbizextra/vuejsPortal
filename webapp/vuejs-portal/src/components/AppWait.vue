<template>
  <div>
    <input type="hidden" id="loader-wrapper-hidden" v-if="!blockUi" v-show="waitAny"/>
     <div id="loader-wrapper" class="text-center" v-show="waitAny">
      <v-progress-circular
        :indeterminate="waitAny"
        :size=20
        color="light-blue"
      > </v-progress-circular>
     </div>
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