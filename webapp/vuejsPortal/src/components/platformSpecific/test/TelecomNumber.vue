<template>
  <v-layout wrap xs12>
    <v-flex text-left stretch xs12>
      <h4 class="mb-2">Telecom Number - ({{props.length}})</h4>
      <v-card>
        <v-simple-table dense text-right dense>
          <thead>
          <tr>
            <th class="col-2">Country Code</th>
            <th class="col-2">Area Code</th>
            <th class="col-4">Contact Number</th>
            <th class="col-2">Extension</th>
            <th class="col-1">Soliciting Ok?</th>
            <th class="col-1">Actions</th>
          </tr>
          </thead>
          <transition-group tag="tbody" name="test">
            <tr class="xs-12" v-for="(telecomNumber, id) in props" :key="telecomNumber.contactMech.contactMechId">
              <td>
                {{telecomNumber.telecomNumber.countryCode}}
              </td>
              <td>
                {{telecomNumber.telecomNumber.areaCode}}
              </td>
              <td>
                {{telecomNumber.telecomNumber.contactNumber}}
              </td>
              <td>
                {{telecomNumber.partyContactMech.extension}}
              </td>
              <td>
                <v-switch disabled v-model="telecomNumber.partyContactMech.allowSolicitation"
                          true-value="Y" false-value="N"></v-switch>
              </td>
              <td>
                <v-btn-toggle>
                  <v-btn color="primary" @click="updateTelecomNumber(telecomNumber)">
                    <v-icon>mdi-pencil</v-icon>
                  </v-btn>
                  <v-btn color="error" @click="deleteTelecomNumber(telecomNumber)">
                    <v-icon>mdi-delete</v-icon>
                  </v-btn>
                </v-btn-toggle>
              </td>
            </tr>
          </transition-group>
        </v-simple-table>
      </v-card>
    </v-flex>
  </v-layout>
</template>

<script>
  const updateContactMech = 'https://localhost:8443/partymgrapi/control/updateTelecomNumberAddress'
  const deleteContactMech = 'https://localhost:8443/partymgrapi/control/deleteContactMech'
  export default {
    name: "TelecomNumber",
    props: ['props'],
    data() {
      return {
        expansionPanel: 0
      }
    },
    methods: {
      deleteTelecomNumber(telecomNumber) {
        this.$http.post(deleteContactMech, {partyId: 'DemoLead3', contactMechId: telecomNumber.contactMech.contactMechId}).then(
          result => {
            console.log(result.body)
            this.$emit('update')
          },
          error => {
            console.log('Error during contactMech deletion', error)
            this.$emit('update')
          }
        )
      },
      updateTelecomNumber(telecomNumber) {
        this.$http.post(updateContactMech, {partyId: 'DemoLead3', contactMechId: telecomNumber.contactMech.contactMechId}).then(
          result => {
            console.log(result.body)
          },
          error => {
            console.log('Error during contactMech update', error)
          }
        )
      }
    }
  }
</script>

<style scoped>
  .test-enter-active, .test-leave-active {
    transition: all 300ms;
  }

  .test-enter {
    opacity: 0;
    transform: translateX(-30px);
  }

  .test-leave-to {
    opacity: 0;
    transform: translateX(30px);
  }
</style>
