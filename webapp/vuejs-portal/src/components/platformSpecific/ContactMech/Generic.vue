<template>
  <v-list dense>
    <v-row >
      <v-tooltip top>
        <template v-slot:activator="{ on }">
          <v-icon left v-on="on">{{getIcon(icon)}}</v-icon>
        </template>
        <span>{{label}}</span>
      </v-tooltip>
      <v-subheader>
        {{label}}
      </v-subheader>
    </v-row>
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
      </v-list-item-content>
      <v-list-item-action v-if="editMode">
        <v-btn icon @click="removeContactMech(contactMech)">
          <v-icon id='mdi-delete' color="red">{{getIcon('mdi-delete')}}</v-icon>
        </v-btn>
      </v-list-item-action>
    </v-list-item>
    <v-list-item v-if="editMode">
      <v-list-item-content>
        <v-list-item-subtitle @click="addContactMech">
          <v-btn>
            <v-icon left>{{getIcon('mdi-plus-circle')}}</v-icon>
            Add {{label}}
          </v-btn>
        </v-list-item-subtitle>
      </v-list-item-content>
    </v-list-item>
    <v-divider></v-divider>
  </v-list>
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