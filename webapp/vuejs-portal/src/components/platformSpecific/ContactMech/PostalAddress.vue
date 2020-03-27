<template>
  <v-container fluid class="mb-2 ma-0 pa-0">
    <v-toolbar dark color="primary" flat height="30px" class="ma-0 pa-0">
      <v-icon left>{{getIcon(icon)}}</v-icon>
      <v-toolbar-title>
        {{label}}
      </v-toolbar-title>
    </v-toolbar>
    <v-list dense sel-label="postalAddr" class="ma-0 pa-0">
      <v-list-item v-for="postalAddress in contactMechList"
                   :key="postalAddress.contactMech.contactMechId">
        <v-list-item-content v-if="!editMode">
          <v-list-item-title>
            {{postalAddress.postalAddress.toName}} {{postalAddress.postalAddress.attnName}}
          </v-list-item-title>
          <div>
            {{postalAddress.postalAddress.address1}}
          </div>
          <div>
            {{postalAddress.postalAddress.address2}}
          </div>
          <div>
            {{postalAddress.postalAddress.city}}, {{postalAddress.postalAddress.postalCode}}
          </div>
          <v-list-item-subtitle v-if="postalAddress.partyContactMechPurposes.length > 0 && !editMode">
            <v-row class="ma-0 pa-0">
              <v-chip class="accent mr-1 mb-1" x-small v-for="purpose in postalAddress.partyContactMechPurposes"
                      :key="purpose.contactMechId + '-' + purpose.contactMechPurposeTypeId">
                {{getPurposeDescription(purpose.contactMechPurposeTypeId)}}
              </v-chip>
            </v-row>
          </v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-content v-if="editMode">
          <v-form :lazy-validator="lazy">
              <v-row class="ma-0 pa-0">
                <v-text-field hide-details id="toName" label="To Name"
                              :rules="rules.toName"
                              v-model="postalAddress.postalAddress.toName" class="mr-4"></v-text-field>
                <v-text-field hide-details id="attentionName" label="Attention Name"
                              :rules="rules.attentionName"
                              v-model="postalAddress.postalAddress.attnName"></v-text-field>
              </v-row>
              <v-row class="ma-0 pa-0">
                <v-text-field hide-details id="addressLine1" label="Address Line 1 *"
                              :rules="rules.addressLine1"
                              v-model="postalAddress.postalAddress.address1"></v-text-field>
              </v-row>
              <v-row class="ma-0 pa-0">
                <v-text-field hide-details id="addressLine2" label="Address Line 2"
                              :rules="rules.addressLine2"
                              v-model="postalAddress.postalAddress.address2"></v-text-field>
              </v-row>
              <v-row class="ma-0 pa-0">
                <v-text-field hide-details id="city" label="City *"
                              v-model="postalAddress.postalAddress.city"
                              :rules="rules.city" class="mr-4"></v-text-field>
                <v-text-field hide-details id="zipPostalCode" label="Zip/Postal Code *"
                              v-model="postalAddress.postalAddress.postalCode"
                              :rules="rules.zipPostalCode" class="mr-4"></v-text-field>
              </v-row>
              <v-list-item-subtitle v-if="editMode && purposeList.length > 0">
                <v-select
                    label="purposes"
                    v-model="postalAddress.purposes"
                    :items="purposeList"
                    multiple
                    item-text="description"
                    item-value="contactMechPurposeTypeId">
                </v-select>
              </v-list-item-subtitle>
              <v-list-item-subtitle v-if="editMode" class="d-flex flex-row-reverse">
                <v-btn @click="removeContactMech(postalAddress)" color="error">
                  <v-icon id='mdi-delete'>{{getIcon('mdi-delete')}}</v-icon>
                  expire
                </v-btn>
              </v-list-item-subtitle>
          </v-form>
        </v-list-item-content>
      </v-list-item>
      <v-list-item v-if="editMode">
        <v-list-item-content>
          <v-list-item-subtitle class="d-flex justify-center">
            <v-btn color="secondary" sel-label="addPostalAddr" @click="addContactMech">
              <v-icon id='mdi-plus-circle' left>{{getIcon('mdi-plus-circle')}}</v-icon>
              Add postal address
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
    name: "PostalAddress",
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
      },
      getPurposeDescription(contactMechPurposeTypeId) {
        return this.purposeList.filter(purpose => purpose.contactMechPurposeTypeId === contactMechPurposeTypeId)[0].description
      },
    }
  }
</script>

<style scoped>

</style>