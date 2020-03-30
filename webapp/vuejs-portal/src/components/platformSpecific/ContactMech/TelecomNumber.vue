<template>
  <v-container fluid class="mb-2 ma-0 pa-0" v-if="editMode || contactMechList.length > 0">
    <v-toolbar dark color="primary" flat height="30px" class="ma-0 pa-0">
      <v-icon left>{{getIcon(icon)}}</v-icon>
      <v-toolbar-title>
        {{label}}
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn v-if="editMode && contactMechList.length === 0" small icon sel-label="addTelecomNumber" @click="addContactMech">
        <v-icon>
          {{getIcon('mdi-plus-circle')}}
        </v-icon>
      </v-btn>
    </v-toolbar>
    <v-list dense class="ma-0 pa-0" sel-label="phoneNumber">
      <v-list-item v-for="phoneNumber in contactMechList"
                   :key="phoneNumber.contactMech.contactMechId">
        <v-list-item-content :class="thruDate(phoneNumber) ? 'grey--text' : ''">
          <v-list-item-title v-if="!editMode">
            {{display(phoneNumber)}}
          </v-list-item-title>
          <v-list-item-title v-if="editMode">
            <v-row class="ma-0 pa-0">
              <v-col class="col-3 ma-0 pa-0">
                <v-text-field class="ignore-css" hide-details id="countryCode" label="Country code"
                              v-model="phoneNumber.telecomNumber.countryCode"></v-text-field>
              </v-col>
              <v-col class="col-9 ma-0 pa-0">
                <v-text-field class="ignore-css" hide-details id="number" label="Number"
                              v-model="phoneNumber.telecomNumber.contactNumber"></v-text-field>
              </v-col>
            </v-row>
          </v-list-item-title>
          <v-list-item-subtitle v-if="phoneNumber.partyContactMechPurposes.length > 0 && !editMode">
            <v-row class="ma-0 pa-0">
              <v-chip class="accent mr-1 mb-1" x-small v-for="purpose in phoneNumber.partyContactMechPurposes"
                      :key="purpose.contactMechId + '-' + purpose.contactMechPurposeTypeId">
                {{getPurposeDescription(purpose.contactMechPurposeTypeId)}}
              </v-chip>
            </v-row>
          </v-list-item-subtitle>
          <v-list-item-subtitle v-if="thruDate(phoneNumber)">
            <v-chip class="secondary mr-1 mb-1" x-small>
              {{'Expired since :  ' + parseDate(phoneNumber.partyContactMech.thruDate)}}
            </v-chip>
          </v-list-item-subtitle>
          <v-list-item-subtitle v-if="editMode && purposeList.length > 0">
            <v-select
                label="purposes"
                v-model="phoneNumber.purposes"
                :items="purposeList"
                multiple
                item-text="description"
                item-value="contactMechPurposeTypeId">
            </v-select>
          </v-list-item-subtitle>
          <v-list-item-subtitle v-if="editMode" class="d-flex flex-row-reverse">
            <v-btn @click="removeContactMech(phoneNumber)" color="error">
              <v-icon id='mdi-delete' >{{getIcon('mdi-delete')}}</v-icon>
              expire
            </v-btn>
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-list-item v-if="editMode && contactMechList.length > 0">
        <v-list-item-content>
          <v-list-item-subtitle  class="d-flex justify-center">
            <v-btn color="secondary" sel-label="addTelecomNumber" @click="addContactMech">
              <v-icon id='mdi-plus-circle' left>{{getIcon('mdi-plus-circle')}}</v-icon>
              Add telecom number
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
    name: "TelecomNumber",
    props: ['contactMechList', 'editMode', 'icon', 'label', 'contactMechTypeId', 'showMore', 'purposeList'],
    computed: {

    },
    methods: {
      addContactMech() {
        this.$emit('addContactMech')
      },
      removeContactMech(contactMech) {
        this.$emit('removeContactMech', contactMech)
      },
      getIcon(icon) {
        return icons.hasOwnProperty(icon) ? icons[icon] : null
      },
      display(phoneNumber) {
        return `${phoneNumber.telecomNumber.countryCode || ''} ${phoneNumber.telecomNumber.areaCode || ''}
          ${phoneNumber.telecomNumber.contactNumber || ''} ${phoneNumber.telecomNumber.extension || ''}`
      },
      getPurposeDescription(contactMechPurposeTypeId) {
        return this.purposeList.filter(purpose => purpose.contactMechPurposeTypeId === contactMechPurposeTypeId)[0].description
      },
      parseDate(timestamp) {
        return new Date(parseInt(timestamp)).toLocaleDateString() + ' - ' + new Date(parseInt(timestamp)).toLocaleTimeString()
      },
      thruDate(contactMech){
          return contactMech.hasOwnProperty('partyContactMech') && contactMech.partyContactMech.hasOwnProperty('thruDate') && contactMech.partyContactMech.thruDate
      }
    }
  }
</script>

<style scoped>

</style>