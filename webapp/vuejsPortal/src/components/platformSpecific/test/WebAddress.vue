<template>
  <v-layout wrap xs12>
    <v-flex text-left stretch xs12>
      <h4 class="mb-2">Web Address - ({{props.length}})</h4>
      <v-card>
        <v-simple-table dense text-right dense>
          <thead>
          <tr>
            <th class="col-10">Web Address</th>
            <th class="col-1">Soliciting Ok?</th>
            <th class="col-1">Actions</th>
          </tr>
          </thead>
          <transition-group tag="tbody" name="test">
            <tr class="xs-12" v-for="(web, id) in props" :key="web.contactMech.contactMechId">
              <td>
                {{web.contactMech.infoString}}
              </td>
              <td>
                <v-switch disabled v-model="web.partyContactMech.allowSolicitation"
                          true-value="Y" false-value="N"></v-switch>
              </td>
              <td>
                <v-btn-toggle>
                  <v-btn color="primary" @click="updateWebAddress(web)">
                    <v-icon>mdi-pencil</v-icon>
                  </v-btn>
                  <v-btn color="error" @click="deleteWebAddress(web)">
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
  const updateContactMech = 'https://localhost:8443/partymgrapi/control/updateWebAddress'
  const deleteContactMech = 'https://localhost:8443/partymgrapi/control/deleteContactMech'
  export default {
    name: "WebAddress",
    props: ['props'],
    data() {
      return {
        expansionPanel: 0
      }
    },
    methods: {
      deleteWebAddress(web) {
        this.$http.post(deleteContactMech, {partyId: 'DemoLead3', contactMechId: web.contactMech.contactMechId}).then(
          result => {
            console.log(result.body)
            this.$emit('update')
          },
          error => {
            console.log('Error during web address deletion', error)
            this.$emit('update')
          }
        )
      },
      updateWebAddress(web) {
        this.$http.post(updateContactMech, {partyId: 'DemoLead3', contactMechId: web.contactMech.contactMechId}).then(
          result => {
            console.log(result.body)
          },
          error => {
            console.log('Error during web address update', error)
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
