<template>
  <v-container fluid class="mb-2 ma-0 pa-0" v-if="mustBeDisplayed">
    <v-toolbar dark color="primary" flat height="30px" class="ma-0 pa-0">
      <v-icon left>{{getIcon(icon)}}</v-icon>
      <v-toolbar-title>
        {{label}}
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn v-if="editMode && contactMechList.length === 0" small icon :sel-label="selLabelAdd" @click="addContactMech">
        <v-icon>
          {{getIcon('mdi-plus-circle')}}
        </v-icon>
      </v-btn>
    </v-toolbar>
    <v-list dense class="ma-0 pa-0" :sel-label="selLabel">
      <v-list-item v-for="contactMech in filteredContactMechList"
                   :key="contactMech.contactMech.contactMechId">
        <v-list-item-content :class="thruDate(contactMech) ? 'grey--text' : ''">
          <v-list-item-title v-if="!editMode">
            {{contactMech.contactMech.infoString}}
          </v-list-item-title>
          <v-list-item-title v-if="editMode">
            <v-row>
              <v-col>
                <v-text-field hide-details id="infoString" :label="label"
                              v-model="contactMech.contactMech.infoString"></v-text-field>
              </v-col>
            </v-row>
          </v-list-item-title>
          <v-list-item-subtitle v-if="contactMech.partyContactMechPurposes.length > 0 && !editMode">
            <v-row class="ma-0 pa-0">
              <v-chip class="accent mr-1 mb-1" x-small v-for="purpose in contactMech.partyContactMechPurposes"
                      :key="purpose.contactMechId + '-' + purpose.contactMechPurposeTypeId">
                {{getPurposeDescription(purpose.contactMechPurposeTypeId)}}
              </v-chip>
            </v-row>
          </v-list-item-subtitle>
          <v-list-item-subtitle v-if="thruDate(contactMech)">
              <v-chip class="secondary mr-1 mb-1" x-small>
                {{'Expired since :  ' + parseDate(contactMech.partyContactMech.thruDate)}}
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
              <v-icon id='mdi-delete'>{{getIcon('mdi-delete')}}</v-icon>
              expire
            </v-btn>
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <v-list-item v-if="editMode && contactMechList.length > 0">
        <v-list-item-content>
          <v-list-item-subtitle class="d-flex justify-center">
            <v-btn color="secondary" sel-label="addEmailAddr" @click="addContactMech">
              <v-icon id='mdi-plus-circle' left>{{getIcon('mdi-plus-circle')}}</v-icon>
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
    props: ['contactMechList', 'editMode', 'icon', 'label', 'selLabel', 'selLabelAdd', 'contactMechTypeId', 'showMore', 'purposeList', 'showLessList'],
    computed: {
      filteredContactMechList() {
        if (this.showLessList.mode === 'never') {
          return []
        } else if (this.showLessList.mode === 'none' && !this.showMore) {
          return []
        } else if (this.showLessList.mode === 'purposes' && !this.showMore && !this.editMode) {
          return this.contactMechList.filter(contactMech => contactMech.partyContactMechPurposes.find(purpose => this.showLessList.purposes.includes(purpose.contactMechPurposeTypeId)))
        } else {
          return !this.showMore && this.showLessList.mode === 'count' && this.showLessList.count > 0 && !this.editMode ? this.contactMechList.slice(0, this.showLessList.count) : this.contactMechList
        }
      },
      mustBeDisplayed() {
        return this.editMode || this.filteredContactMechList.length > 0
      }
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