<template>
  <v-container fluid class="mb-2 ma-0 pa-0">
    <v-toolbar dark color="primary" flat height="30px" class="ma-0 pa-0">
      <v-icon left v-on="on">{{getIcon(icon)}}</v-icon>
      <v-toolbar-title>
        {{label}}
      </v-toolbar-title>
    </v-toolbar>
    <v-list dense class="ma-0 pa-0">
      <v-list-item v-for="contactMech in contactMechList"
                   :key="contactMech.contactMech.contactMechId">
        <v-list-item-content>
          <v-list-item-title v-if="!editMode">
            {{contactMech.contactMech.infoString}}
          </v-list-item-title>
          <v-list-item-title v-if="editMode">
            <v-row>
              <v-col>
                <v-text-field hide-details :label="label"
                              v-model="contactMech.contactMech.infoString"></v-text-field>
              </v-col>
            </v-row>
          </v-list-item-title>
          <v-list-item-subtitle v-if="contactMech.partyContactMechPurposes.length > 0 && !editMode">
            <v-chip class="primary mr-2" x-small v-for="purpose in contactMech.partyContactMechPurposes"
                    :key="purpose.contactMechId + '-' + purpose.contactMechPurpostTypeId">
              {{purpose.contactMechPurposeTypeId}}
            </v-chip>
          </v-list-item-subtitle>
          <v-list-item-subtitle v-if="editMode && purposeList.length > 0">
            <v-select
                label="purposes"
                v-model="contactMech.purposes"
                :items="purposeList"
                multiple
                item-text="description"
                item-value="contactMechPurposeTypeId">
            </v-select>
          </v-list-item-subtitle>
          <v-list-item-subtitle v-if="editMode" class="d-flex flex-row-reverse">
            <v-btn @click="removeContactMech(contactMech)" color="error">
              <v-icon id='mdi-delete' >{{getIcon('mdi-delete')}}</v-icon>
              expire
            </v-btn>
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-list-item v-if="editMode">
        <v-list-item-content>
          <v-list-item-subtitle  class="d-flex justify-center">
            <v-btn color="secondary" @click="addContactMech">
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
    name: "Generic",
    props: ['contactMechList', 'editMode', 'icon', 'label', 'contactMechTypeId', 'showMore', 'purposeList'],
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