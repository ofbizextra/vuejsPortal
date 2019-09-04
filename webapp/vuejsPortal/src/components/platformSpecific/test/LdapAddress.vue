<template>
  <v-layout wrap xs12>
    <v-flex text-left stretch xs12>
      <h4 class="mb-2">LDAP Address - ({{props.length}})</h4>
      <v-card>
        <v-simple-table dense text-right dense>
          <thead>
          <tr>
            <th class="col-10">LDAP Address</th>
            <th class="col-1">Soliciting Ok?</th>
            <th class="col-1">Actions</th>
          </tr>
          </thead>
          <transition-group tag="tbody" name="test">
            <tr class="xs-12" v-for="(ldapAddress, id) in props" :key="ldapAddress.contactMech.contactMechId">
              <td>
                {{ldapAddress.contactMech.infoString}}
              </td>
              <td>
                <v-switch disabled v-model="ldapAddress.partyContactMech.allowSolicitation"
                          true-value="Y" false-value="N"></v-switch>
              </td>
              <td>
                <v-btn-toggle>
                  <v-btn color="primary" @click="updateLdapAddress(ldapAddress)">
                    <v-icon>mdi-pencil</v-icon>
                  </v-btn>
                  <v-btn color="error" @click="deleteLdapAddress(ldapAddress)">
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
  const updateContactMech = 'https://localhost:8443/partymgrapi/control/updateLdapAddress'
  const deleteContactMech = 'https://localhost:8443/partymgrapi/control/deleteContactMech'
  export default {
    name: "LdapAddres",
    props: ['props'],
    data() {
      return {
        expansionPanel: 0
      }
    },
    methods: {
      deleteLdapAddress(ldapAddress) {
        this.$http.post(deleteContactMech, {partyId: 'DemoLead3', contactMechId: ldapAddress.contactMech.contactMechId}).then(
          result => {
            console.log(result.body)
            this.$emit('update')
          },
          error => {
            console.log('Error during LDAP Address deletion', error)
            this.$emit('update')
          }
        )
      },
      updateLdapAddress(ldapAddress) {
        this.$http.post(updateContactMech, {partyId: 'DemoLead3', contactMechId: ldapAddress.contactMech.contactMechId}).then(
          result => {
            console.log(result.body)
          },
          error => {
            console.log('Error during LDAP Address update', error)
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
