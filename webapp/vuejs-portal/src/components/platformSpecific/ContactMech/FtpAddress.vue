<template>
  <v-container fluid class="mb-2 ma-0 pa-0">
    <v-toolbar dark color="primary" flat height="30px" class="ma-0 pa-0">
      <v-icon left v-on="on">{{getIcon(icon)}}</v-icon>
      <v-toolbar-title>
        {{label}}
      </v-toolbar-title>
    </v-toolbar>
    <v-list dense class="ma-0 pa-0" sel-label="ftpAddr">
      <v-list-item v-for="ftpAddress in contactMechList"
                   :key="ftpAddress.contactMech.contactMechId">
        <v-list-item-content v-if="!editMode">
          <v-list-item-title>
            {{ftpAddress.ftpAddress.hostname}}:{{ftpAddress.ftpAddress.port}}
          </v-list-item-title>
          <div>
            user: {{ftpAddress.ftpAddress.username}} - pass: {{ftpAddress.ftpAddress.ftpPassword}}
          </div>
          <div>
            {{ftpAddress.ftpAddress.filePath}} - {{ftpAddress.ftpAddress.defaultTimeout}}ms
          </div>
          <div>
            <v-row class="ma-0 pa-0" justify="space-between">
              <v-checkbox class="ma-0 mr-1" hide-details small label="binary" :disabled="!editMode"
                          v-model="ftpAddress.ftpAddress.binaryTransfer"
                          true-value="Y" false-value="N"></v-checkbox>
              <v-checkbox class="ma-0 mr-1" hide-details small label="zip" :disabled="!editMode"
                          v-model="ftpAddress.ftpAddress.zipFile"
                          true-value="Y" false-value="N"></v-checkbox>
              <v-checkbox class="ma-0 mr-1" hide-details small label="passive" :disabled="!editMode"
                          v-model="ftpAddress.ftpAddress.passiveMode"
                          true-value="Y" false-value="N"></v-checkbox>
            </v-row>
          </div>
          <v-list-item-subtitle v-if="ftpAddress.partyContactMechPurposes.length > 0">
            <v-chip class="accent mr-2" x-small v-for="purpose in ftpAddress.partyContactMechPurposes"
                    :key="purpose.contactMechPurposeTypeId">
              {{purpose.contactMechPurposeTypeId}}
            </v-chip>
          </v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-content v-if="editMode">
          <v-form class="ml-3" :lazy-validator="lazy">
            <v-row class="ma-0 pa-0">
              <v-text-field hide-details id="hostname" label="Host name" class="mr-4"
                            :rules="rules.hostname"
                            v-model="ftpAddress.ftpAddress.hostname"></v-text-field>
              <v-text-field hide-details id="port" label="Port" class="" :rules="rules.port"
                            v-model="ftpAddress.ftpAddress.port"></v-text-field>
            </v-row>
            <v-row class="ma-0 pa-0">
              <v-text-field hide-details id="username" label="User Name" class="mr-4"
                            :rules="rules.username"
                            v-model="ftpAddress.ftpAddress.username"></v-text-field>
              <v-text-field hide-details id="ftpPassword" label="Password"
                            :rules="rules.ftpPassword"
                            v-model="ftpAddress.ftpAddress.ftpPassword"></v-text-field>
            </v-row>
            <v-row class="ma-0 pa-0">
              <v-text-field hide-details id="filePath" label="Path" class="mr-4"
                            :rules="rules.filePath"
                            v-model="ftpAddress.ftpAddress.filePath"></v-text-field>
              <v-text-field hide-details id="defaultTimeout" label="Path default timeout" class=""
                            :rules="rules.defaultTimeout"
                            v-model="ftpAddress.ftpAddress.defaultTimeout"></v-text-field>
            </v-row>
            <v-row class="ma-0 pa-0">
              <v-checkbox class="ma-0 mr-1" id="binaryTransfer" label="Binary Transfert" trueValue="Y"
                          falseValue="N"
                          :rules="rules.binaryTransfer"
                          v-model="ftpAddress.ftpAddress.binaryTransfer"></v-checkbox>
              <v-checkbox class="ma-0 mr-1" id="zipFile" label="File compression" trueValue="Y" falseValue="N"
                          :rules="rules.zipFile"
                          v-model="ftpAddress.ftpAddress.zipFile"></v-checkbox>
              <v-checkbox class="ma-0 mr-1" id="passiveMode" label="Passive mode" trueValue="Y" falseValue="N"
                          :rules="rules.passiveMode"
                          v-model="ftpAddress.ftpAddress.passiveMode"></v-checkbox>
            </v-row>
          </v-form>
          <v-list-item-subtitle v-if="editMode && purposeList.length > 0">
            <v-select
                label="purposes"
                v-model="ftpAddress.purposes"
                :items="purposeList"
                multiple
                item-text="description"
                item-value="contactMechPurposeTypeId">
            </v-select>
          </v-list-item-subtitle>
          <v-list-item-subtitle v-if="editMode" class="d-flex flex-row-reverse">
            <v-btn @click="removeContactMech(ftpAddress)" color="error">
              <v-icon id='mdi-delete'>{{getIcon('mdi-delete')}}</v-icon>
              expire
            </v-btn>
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-list-item v-if="editMode">
        <v-list-item-content>
          <v-list-item-subtitle  class="d-flex justify-center">
            <v-btn color="secondary" sel-label="addFtpAddr" @click="addContactMech">
              <v-icon left>{{getIcon('mdi-plus-circle')}}</v-icon>
              Add {{label}}
            </v-btn>
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
    </v-list>
  </v-container>
</template>

<script>
  import icons from '../../../js/icons'

  export default {
    name: "FtpAddress",
    props: ['contactMechList', 'editMode', 'icon', 'label', 'contactMechTypeId', 'showMore', 'purposeList', 'rules'],
    methods: {
      addContactMech() {
        this.$emit('addContactMech')
      },
      removeContactMech(contactMech) {
        this.$emit('removeContactMech', contactMech)
      },
      getIcon(icon) {
        return icons.hasOwnProperty(icon) ? icons[icon] : null
      }
    }
  }
</script>

<style scoped>

</style>