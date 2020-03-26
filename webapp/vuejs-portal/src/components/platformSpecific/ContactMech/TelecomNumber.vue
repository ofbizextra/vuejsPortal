<template>
  <v-list dense>
    <v-row >
      <v-tooltip top>
        <template v-slot:activator="{ on }">
          <v-icon id='mdi-phone' left v-on="on">{{getIcon('mdi-phone')}}</v-icon>
        </template>
        <span>{{label}}</span>
      </v-tooltip>
      <v-subheader>
        {{label}}
      </v-subheader>
    </v-row>
    <v-list-item v-for="phoneNumber in contactMechList"
                 :key="phoneNumber.contactMech.contactMechId">
      <v-list-item-content>
        <v-list-item-title v-if="!editMode">
          {{display(phoneNumber)}}
        </v-list-item-title>
        <v-list-item-title v-if="editMode">
          <v-row>
            <v-col class="col-3">
              <v-text-field class="ignore-css" hide-details label="Country code"
                            v-model="phoneNumber.telecomNumber.countryCode"></v-text-field>
            </v-col>
            <v-col class="col-9">
              <v-text-field class="ignore-css" hide-details label="Number"
                            v-model="phoneNumber.telecomNumber.contactNumber"></v-text-field>
            </v-col>
          </v-row>
        </v-list-item-title>
        <v-list-item-subtitle v-if="phoneNumber.partyContactMechPurposes.length > 0 && !editMode">
          <v-chip class="primary mr-2" x-small v-for="purpose in phoneNumber.partyContactMechPurposes"
                  :key="purpose.contactMechId + '-' + purpose.contactMechPurposeTypeId">
            {{purpose.contactMechPurposeTypeId}}
          </v-chip>
        </v-list-item-subtitle>
        <v-list-item-subtitle v-if="editMode">
          <v-select
              label="purposes"
              v-model="phoneNumber.purposes"
              :items="purposeList"
              deletable-chips
              chips
              hide-selected
              multiple
              item-text="description"
              item-value="contactMechPurposeTypeId">
          </v-select>
        </v-list-item-subtitle>
        <v-list-item-subtitle v-if="editMode">
          <v-btn icon @click="removeContactMech(phoneNumber)">
            <v-icon color="red">{{getIcon('mdi-delete')}}</v-icon>
          </v-btn>
        </v-list-item-subtitle>
      </v-list-item-content>
    </v-list-item>
    <v-list-item v-if="editMode">
      <v-list-item-content>
        <v-list-item-subtitle @click="addContactMech">
          <v-icon id='mdi-plus-circle' left>{{getIcon('mdi-plus-circle')}}</v-icon>
          Add telecom number
        </v-list-item-subtitle>
      </v-list-item-content>
    </v-list-item>
    <v-list-item>
      <v-divider></v-divider>
    </v-list-item>
  </v-list>
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
      }
    }
  }
</script>

<style scoped>

</style>