<template>
  <v-layout wrap xs12>
    <v-flex text-left stretch xs12>
      <h4 class="mb-2">Email Address - ({{props.length}})</h4>
      <v-card>
        <v-simple-table dense text-right dense>
          <thead>
          <tr>
            <th class="col-10">Email Address</th>
            <th class="col-1">Soliciting Ok?</th>
            <th class="col-1">Actions</th>
          </tr>
          </thead>
          <transition-group tag="tbody" name="test">
            <tr class="xs-12" v-for="(email, id) in props" :key="email.contactMech.contactMechId">
              <td>
                {{email.contactMech.infoString}}
              </td>
              <td>
                <v-switch disabled v-model="email.partyContactMech.allowSolicitation"
                          true-value="Y" false-value="N"></v-switch>
              </td>
              <td>
                <v-btn-toggle>
                  <v-btn color="primary" @click="updateEmailAddress(email)">
                    <v-icon>mdi-pencil</v-icon>
                  </v-btn>
                  <v-btn color="error" @click="deleteEmailAddress(email)">
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
  const updateEmailAddress = 'https://localhost:8443/partymgrapi/control/updateEmailAddress'
  const deleteEmailAddress = 'https://localhost:8443/partymgrapi/control/deleteContactMech'
  export default {
    name: "EmailAddress",
    props: ['props'],
    data() {
      return {
        expansionPanel: 0
      }
    },
    methods: {
      deleteEmailAddress(email) {
        this.$http.post(deleteEmailAddress, {partyId: 'DemoLead3', contactMechId: email.contactMech.contactMechId}).then(
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
      updateEmailAddress(email) {
        this.$http.post(updateEmailAddress, {partyId: 'DemoLead3', contactMechId: email.contactMech.contactMechId}).then(
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
