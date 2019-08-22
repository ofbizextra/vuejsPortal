<template>
  <v-layout wrap xs12>
    <v-flex text-left stretch xs12>
      <h4 class="mb-2">Postal Address - ({{props.length}})</h4>
      <v-card>
        <v-simple-table dense>
          <thead>
          <tr>
            <th class="col-1">To Name</th>
            <th class="col-1">Attention Name</th>
            <th class="col-3">Address Line 1</th>
            <th class="col-3">Address Line 2</th>
            <th class="col-1">City</th>
            <th class="col-1">Zip/Postal Code</th>
            <th class="col-1">Soliciting Ok?</th>
            <th class="col-1">Actions</th>
          </tr>
          </thead>
          <transition-group tag="tbody" name="test">
            <tr class="xs-12 test-item" v-for="(postalAddress, id) in props"
                :key="postalAddress.contactMech.contactMechId">
              <td>
                {{postalAddress.postalAddress.toName}}
              </td>
              <td>
                {{postalAddress.postalAddress.attnName}}
              </td>
              <td>
                {{postalAddress.postalAddress.address1}}
              </td>
              <td>
                {{postalAddress.postalAddress.address2}}
              </td>
              <td>
                {{postalAddress.postalAddress.city}}
              </td>
              <td>
                {{postalAddress.postalAddress.postalCode}}
              </td>
              <td>
                <v-switch disabled label="allow solicitation" v-model="postalAddress.partyContactMech.allowSolicitation"
                          true-value="Y" false-value="N"></v-switch>
              </td>
              <td>
                <v-btn-toggle>
                  <v-btn color="primary" @click="updatePostalAddress(postalAddress)">
                    <v-icon>mdi-pencil</v-icon>
                  </v-btn>
                  <v-btn color="error" @click="deletePostalAddress(postalAddress)">
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
  const updateContactMech = 'https://localhost:8443/partymgrapi/control/updatePostalAddressAddress'
  const deleteContactMech = 'https://localhost:8443/partymgrapi/control/deleteContactMech'
  export default {
    name: "PostalAddress",
    props: ['props'],
    methods: {
      deletePostalAddress(postalAddress) {
        this.$http.post(deleteContactMech, {
          partyId: 'DemoLead3',
          contactMechId: postalAddress.contactMech.contactMechId
        }).then(
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
      updatePostalAddress(postalAddress) {
        this.$http.post(updateContactMech, {
          partyId: 'DemoLead3',
          contactMechId: postalAddress.contactMech.contactMechId
        }).then(
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
