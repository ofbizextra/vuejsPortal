<template>
  <v-layout wrap xs12>
    <v-flex text-left stretch xs12>
      <h4 class="mb-2">FTP Address - ({{props.length}})</h4>
      <v-card>
        <v-simple-table dense>
          <thead>
          <tr>
            <th class="col-2">Hostname</th>
            <th class="col-1">Port</th>
            <th class="col-1">Username</th>
            <th class="col-1">Password</th>
            <th class="col-1">Path</th>
            <th class="col-1">Timeout</th>
            <th class="col-1">Binary</th>
            <th class="col-1">Compressed</th>
            <th class="col-1">Passive</th>
            <th class="col-1">Soliciting Ok?</th>
            <th class="col-1">Actions</th>
          </tr>
          </thead>
          <transition-group tag="tbody" name="test">
            <tr class="xs-12 test-item" v-for="(FtpAddress, id) in props"
                :key="FtpAddress.contactMech.contactMechId">
              <td>
                {{FtpAddress.ftpAddress.hostname}}
              </td>
              <td>
                {{FtpAddress.ftpAddress.port}}
              </td>
              <td>
                {{FtpAddress.ftpAddress.username}}
              </td>
              <td>
                {{FtpAddress.ftpAddress.ftpPassword}}
              </td>
              <td>
                {{FtpAddress.ftpAddress.filePath}}
              </td>
              <td>
                {{FtpAddress.ftpAddress.defaultTimeout}}
              </td>
              <td>
                <v-switch disabled v-model="FtpAddress.ftpAddress.binaryTransfer"
                          true-value="Y" false-value="N"></v-switch>
              </td>
              <td>
                <v-switch disabled v-model="FtpAddress.ftpAddress.zipFile"
                          true-value="Y" false-value="N"></v-switch>
              </td>
              <td>
                <v-switch disabled v-model="FtpAddress.ftpAddress.passiveMode"
                          true-value="Y" false-value="N"></v-switch>
              </td>
              <td>
                <v-switch disabled label="allow solicitation" v-model="FtpAddress.partyContactMech.allowSolicitation"
                          true-value="Y" false-value="N"></v-switch>
              </td>
              <td>
                <v-btn-toggle>
                  <v-btn color="primary" @click="updateFtpAddress(FtpAddress)">
                    <v-icon>mdi-pencil</v-icon>
                  </v-btn>
                  <v-btn color="error" @click="deleteFtpAddress(FtpAddress)">
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
  const updateContactMech = 'https://localhost:8443/partymgrapi/control/updateFtpAddress'
  const deleteContactMech = 'https://localhost:8443/partymgrapi/control/deleteContactMech'
  export default {
    name: "FtpAddress",
    props: ['props'],
    methods: {
      deleteFtpAddress(FtpAddress) {
        this.$http.post(deleteContactMech, {
          partyId: 'DemoLead3',
          contactMechId: FtpAddress.contactMech.contactMechId
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
      updateFtpAddress(FtpAddress) {
        this.$http.post(updateContactMech, {
          partyId: 'DemoLead3',
          contactMechId: FtpAddress.contactMech.contactMechId
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
