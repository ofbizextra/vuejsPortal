<template>
  <v-container grid-list-md text-center>
    <v-layout wrap justify-space-around>
      <v-flex text-center xs12>
        <ElectronicAddress :props="contactsByType('ELECTRONIC_ADDRESS')" @update="updateDataSet"
                           v-if="countContactsByType('ELECTRONIC_ADDRESS') > 0"></ElectronicAddress>
        <PostalAddress :props="contactsByType('POSTAL_ADDRESS')" @update="updateDataSet"
                       v-if="countContactsByType('POSTAL_ADDRESS') > 0"></PostalAddress>
        <TelecomNumber :props="contactsByType('TELECOM_NUMBER')" @update="updateDataSet"
                       v-if="countContactsByType('TELECOM_NUMBER') > 0"></TelecomNumber>
        <EmailAddress :props="contactsByType('EMAIL_ADDRESS')" @update="updateDataSet"
                      v-if="countContactsByType('EMAIL_ADDRESS') > 0"></EmailAddress>
        <IpAddress :props="contactsByType('IP_ADDRESS')" @update="updateDataSet"
                      v-if="countContactsByType('IP_ADDRESS') > 0"></IpAddress>
        <DomainName :props="contactsByType('DOMAIN_NAME')" @update="updateDataSet"
                   v-if="countContactsByType('DOMAIN_NAME') > 0"></DomainName>
      </v-flex>
      <v-divider class="ma-4"></v-divider>
      <v-flex text-left xs12 md12 lg12>
        <v-row class="mb-4 ml-4">
          <v-icon left>mdi-plus-circle</v-icon>
          <h3>
            Create
          </h3>
        </v-row>
        <v-expansion-panels accordion popout>
          <v-expansion-panel>
            <v-expansion-panel-header>
              Electronic Address
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              <v-form ref="createElectronicAddress" :lazy-validator="lazy" v-model="forms.electronicAddress.valid"
                      v-on:submit.prevent="createElectronicAddress">
                <v-row>
                  <v-text-field name="email" label="Email" :rules="forms.electronicAddress.rules.email"
                                v-model="forms.electronicAddress.fields.email" prepend-icon="mdi-at"></v-text-field>
                </v-row>
                <v-row justify="end">
                  <v-switch name="allowSolicitation" label="Allow Solicitation?" class="ma-2"
                            v-model="forms.electronicAddress.fields.allowSolicitation" trueValue="Y"
                            falseValue="N"></v-switch>
                  <v-btn color="#2196F3" class="ma-2" :disabled="!forms.electronicAddress.valid"
                         @click.native="createElectronicAddress">Create
                  </v-btn>
                </v-row>
              </v-form>
            </v-expansion-panel-content>
          </v-expansion-panel>
          <v-expansion-panel>
            <v-expansion-panel-header>
              Postal Address
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              <v-form ref="createPostalAddress" :lazy-validator="lazy" v-model="forms.postalAddress.valid"
                      v-on:submit.prevent="createPostalAddress">
                <v-row>
                  <v-text-field name="toName" label="To Name" :rules="forms.postalAddress.rules.toName"
                                v-model="forms.postalAddress.fields.toName" class="mr-4"></v-text-field>
                  <v-text-field name="attentionName" label="Attention Name"
                                :rules="forms.postalAddress.rules.attentionName"
                                v-model="forms.postalAddress.fields.attentionName"></v-text-field>
                </v-row>
                <v-row>
                  <v-text-field name="addressLine1" label="Address Line 1 *"
                                :rules="forms.postalAddress.rules.addressLine1"
                                v-model="forms.postalAddress.fields.addressLine1"></v-text-field>
                </v-row>
                <v-row>
                  <v-text-field name="addressLine2" label="Address Line 2"
                                :rules="forms.postalAddress.rules.addressLine2"
                                v-model="forms.postalAddress.fields.addressLine2"></v-text-field>
                </v-row>
                <v-row>
                  <v-text-field name="city" label="City *" v-model="forms.postalAddress.fields.city"
                                :rules="forms.postalAddress.rules.city" class="mr-4"></v-text-field>
                  <v-select name="stateProvince" :items="items" label="State/Province"
                            :rules="forms.postalAddress.rules.stateProvince"
                            v-model="forms.postalAddress.fields.stateProvince" class="mr-4"></v-select>
                  <v-text-field name="zipPostalCode" label="Zip/Postal Code *"
                                v-model="forms.postalAddress.fields.zipPostalCode"
                                :rules="forms.postalAddress.rules.zipPostalCode" class="mr-4"></v-text-field>
                  <v-select name="country" :items="items" label="Country" :rules="forms.postalAddress.rules.countryCode"
                            v-model="forms.postalAddress.fields.country"></v-select>
                </v-row>
                <v-row class="xs12" justify="end">
                  <v-switch name="allowSolicitation" label="Allow Solicitation?" class="mr-4" trueValue="Y"
                            falseValue="N" :rules="forms.postalAddress.rules.allowSolicitation"
                            v-model="forms.postalAddress.fields.allowSolicitation"></v-switch>
                  <v-btn color="#2196F3" :disabled="!forms.postalAddress.valid" class="mt-2"
                         @click.native="createPostalAddress">Create
                  </v-btn>
                </v-row>
              </v-form>
            </v-expansion-panel-content>
          </v-expansion-panel>
          <v-expansion-panel>
            <v-expansion-panel-header>
              Phone Number
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              <v-form ref="createPhoneNumber" :lazy-validator="lazy" v-model="forms.phoneNumber.valid"
                      v-on:submit.prevent="createTelecomNumber">
                <v-row>
                  <v-icon class="mr-4">mdi-phone</v-icon>
                  <v-text-field name="countryCode" label="Country Code" class="mr-4"
                                v-model="forms.phoneNumber.fields.countryCode"></v-text-field>
                  <v-text-field name="areaCode" label="Area Code" class="mr-4"
                                v-model="forms.phoneNumber.fields.areaCode"></v-text-field>
                  <v-text-field name="contactNumber" label="Contact Number *" class="mr-4"
                                v-model="forms.phoneNumber.fields.contactNumber"
                                :rules="forms.phoneNumber.rules.contactNumber"></v-text-field>
                  <v-text-field name="extension" label="Ext"
                                v-model="forms.phoneNumber.fields.extension"></v-text-field>
                </v-row>
                <v-row justify="end">
                  <v-switch label="Allow Solicitation?" class="mr-4"
                            v-model="forms.phoneNumber.fields.allowSolicitation"></v-switch>
                  <v-btn color="#2196F3" align="right" class="mt-2" @click.native="createTelecomNumber"
                         :disabled="!forms.phoneNumber.valid">Create
                  </v-btn>
                </v-row>
              </v-form>
            </v-expansion-panel-content>
          </v-expansion-panel>
          <v-expansion-panel>
            <v-expansion-panel-header>
              Email Address
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              <v-form ref="createEmailAddress" :lazy-validator="lazy" v-model="forms.emailAddress.valid"
                      v-on:submit.prevent="createEmailAddress">
                <v-row>
                  <v-text-field name="email" label="Email" :rules="forms.emailAddress.rules.emailAddress"
                                v-model="forms.emailAddress.fields.emailAddress" prepend-icon="mdi-at"></v-text-field>
                </v-row>
                <v-row justify="end">
                  <v-switch name="allowSolicitation" label="Allow Solicitation?" class="ma-2"
                            v-model="forms.emailAddress.fields.allowSolicitation" trueValue="Y"
                            falseValue="N"></v-switch>
                  <v-btn color="#2196F3" class="ma-2" :disabled="!forms.emailAddress.valid"
                         @click.native="createEmailAddress">Create
                  </v-btn>
                </v-row>
              </v-form>
            </v-expansion-panel-content>
          </v-expansion-panel>
          <v-expansion-panel>
            <v-expansion-panel-header>
              Internet IP Address
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              <v-form ref="createElectronicAddress" :lazy-validator="lazy" v-model="forms.ipAddress.valid"
                      v-on:submit.prevent="createIpAddress">
                <v-row>
                  <v-text-field name="ipAddress" label="Internet IP Address" v-model="forms.ipAddress.fields.ipAddress"
                                :rules="forms.ipAddress.rules.ipAddress"></v-text-field>
                </v-row>
                <v-row justify="end">
                  <v-switch name="allowSolicitation" label="Allow Solicitation?" class="ma-2"
                            v-model="forms.ipAddress.fields.allowSolicitation" trueValue="Y"
                            falseValue="N"></v-switch>
                  <v-btn color="#2196F3" class="ma-2" :disabled="!forms.ipAddress.valid"
                         @click.native="createIpAddress">Create
                  </v-btn>
                </v-row>
              </v-form>
            </v-expansion-panel-content>
          </v-expansion-panel>
          <v-expansion-panel>
            <v-expansion-panel-header>
              Internet Domain Name
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              <v-form ref="createElectronicAddress" :lazy-validator="lazy" v-model="forms.domain.valid"
                      v-on:submit.prevent="createDomain">
                <v-row>
                  <v-text-field name="domain" label="Domain" :rules="forms.domain.rules.domain"
                                v-model="forms.domain.fields.domain"></v-text-field>
                </v-row>
                <v-row justify="end">
                  <v-switch name="allowSolicitation" label="Allow Solicitation?" class="ma-2"
                            v-model="forms.domain.fields.allowSolicitation" trueValue="Y"
                            falseValue="N"></v-switch>
                  <v-btn color="#2196F3" class="ma-2" :disabled="!forms.domain.valid"
                         @click.native="createDomain">Create
                  </v-btn>
                </v-row>
              </v-form>
            </v-expansion-panel-content>
          </v-expansion-panel>
          <v-expansion-panel>
            <v-expansion-panel-header>
              Web URL/Address
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              <v-text-field label="Web URL?Address"></v-text-field>
              <v-switch label="Allow Solicitation?"></v-switch>
              <v-btn color="#2196F3" align="right" dark>Create</v-btn>
            </v-expansion-panel-content>
          </v-expansion-panel>
          <v-expansion-panel>
            <v-expansion-panel-header>
              Internal Party Note
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              <v-text-field label="Internal Party Note"></v-text-field>
              <v-switch label="Allow Solicitation?"></v-switch>
              <v-btn color="#2196F3" align="right" dark>Create</v-btn>
            </v-expansion-panel-content>
          </v-expansion-panel>
          <v-expansion-panel>
            <v-expansion-panel-header>
              File Server
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              <v-text-field label="Host name"></v-text-field>
              <v-text-field label="Port"></v-text-field>
              <v-text-field label="User Name"></v-text-field>
              <v-text-field label="Password"></v-text-field>
              <v-switch label="Binary Transfert"></v-switch>
              <v-text-field label="Path"></v-text-field>
              <v-switch label="File compression"></v-switch>
              <v-switch label="Passive mode"></v-switch>
              <v-text-field label="Pathefault timeout"></v-text-field>
              <v-switch label="Allow Solicitation?"></v-switch>
              <v-btn color="#2196F3" align="right" dark>Create</v-btn>
            </v-expansion-panel-content>
          </v-expansion-panel>
          <v-expansion-panel>
            <v-expansion-panel-header>
              LDAP Address
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              <v-text-field label="LDAP Address"></v-text-field>
              <v-switch label="Allow Solicitation?"></v-switch>
              <v-btn color="#2196F3" align="right" dark>Create</v-btn>
            </v-expansion-panel-content>
          </v-expansion-panel>
        </v-expansion-panels>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
  import ElectronicAddress from './test/ElectronicAddress'
  import PostalAddress from './test/PostalAddress'
  import TelecomNumber from './test/TelecomNumber'
  import EmailAddress from './test/EmailAddress'
  import IpAddress from './test/IpAddress'
  import DomainName from './test/DomainName'

  const getContactMechUrl = 'https://localhost:8443/partymgrapi/control/getcontactmech'
  const createEmailAddressUrl = 'https://localhost:8443/partymgrapi/control/createEmailAddress'
  const createPostalAddressUrl = 'https://localhost:8443/partymgrapi/control/createPostalAddress'
  const createElectroniclAddressUrl = 'https://localhost:8443/partymgrapi/control/createElectronicAddress'
  const createTelecomNumberUrl = 'https://localhost:8443/partymgrapi/control/createTelecomNumber'
  const createContactMech = 'https://localhost:8443/partymgrapi/control/createContactMech'

  export default {
    name: "test",
    components: {ElectronicAddress, PostalAddress, TelecomNumber, EmailAddress, IpAddress, DomainName},
    props: ['props', 'updateStore'],
    data() {
      return {
        dataSet: {},
        items: [
          'item 1',
          'item 2',
          'item 3'
        ],
        contactTypes: [
          "ELECTRONIC_ADDRESS",
          "POSTAL_ADDRESS",
          "TELECOM_NUMBER",
          "EMAIL_ADDRESS",
          "IP_ADDRESS",
          "DOMAIN_NAME",
          "WEB_ADDRESS",
          "INTERNAL_PARTYID",
          "FTP_ADDRESS",
          "LDAP_ADDRESS"
        ],
        forms: {
          electronicAddress: {
            valid: true,
            fields: {
              email: '',
              allowSolicitation: 'N'
            },
            rules: {
              email: [
                v => !!v || 'E-mail is required',
                v => /.+@.+\..+/.test(v) || 'E-mail must be valid',
              ]
            },
          },
          postalAddress: {
            valid: true,
            fields: {
              toName: '',
              attentionName: '',
              addressLine1: '',
              addressLine2: '',
              city: '',
              stateProvince: '',
              zipPostalCode: '',
              country: '',
              allowSolicitation: 'N'
            },
            rules: {
              toName: [],
              attentionName: [],
              addressLine1: [
                v => !!v || 'Address is required',
              ],
              addressLine2: [],
              city: [
                v => !!v || 'City is required',
              ],
              stateProvince: [],
              zipPostalCode: [
                v => !!v || 'Zip/Postal Code is required',
              ],
              countryCode: [],
              allowSolicitation: []
            },
          },
          phoneNumber: {
            valid: true,
            fields: {
              countryCode: '',
              areaCode: '',
              contactNumber: '',
              extension: '',
              allowSolicitation: 'N'
            },
            rules: {
              countryCode: [],
              areaCode: [],
              contactNumber: [
                v => !!v || 'Contact number is required',
              ],
              extension: [],
              allowSolicitation: []
            },
          },
          emailAddress: {
            valid: true,
            fields: {
              emailAddress: '',
              allowSolicitation: 'N'
            },
            rules: {
              emailAddress: [
                v => !!v || 'E-mail is required',
                v => /.+@.+\..+/.test(v) || 'E-mail must be valid',
              ]
            },
          },
          ipAddress: {
            valid: true,
            fields: {
              ipAddress: '',
              allowSolicitation: 'N'
            },
            rules: {
              ipAddress: [
                v => !!v || 'Ip address is required',
                v => /.+\..+\..+\..+/.test(v) || 'Ip address must be valid (ex: 124.75.24.66)',
              ]
            },
          },
          domain: {
            valid: true,
            fields: {
              domain: '',
              allowSolicitation: 'N'
            },
            rules: {
              domain: [
                v => !!v || 'Ip address is required',
                v => /.+\..+/.test(v) || 'Domain must be valid (ex: my-domain.com)',
              ]
            },
          },
        },
        lazy: false
      }
    },
    computed: {},
    methods: {
      contactsByType(type) {
        return this.dataSet.hasOwnProperty('valueMaps') ? this.dataSet.valueMaps.filter(contact => contact.contactMech.contactMechTypeId === type) : []
      },
      countContactsByType(type) {
        return this.dataSet.hasOwnProperty('valueMaps') ? this.dataSet.valueMaps.filter(contact => contact.contactMech.contactMechTypeId === type).length : []
      },
      updateDataSet() {
        this.$http.post(getContactMechUrl, {partyId: 'DemoLead3'}).then(
          result => {
            this.dataSet = result.body
          },
          error => {
            console.log('Error during contactMech acquisition')
          }
        )
      },
      createElectronicAddress() {
        this.$http.post(createContactMech, {
          contactMechTypeId: 'ELECTRONIC_ADDRESS',
          partyId: 'DemoLead3',
          infoString: this.forms.electronicAddress.fields.email,
          allowSolicitation: this.forms.electronicAddress.fields.allowSolicitation
        }).then(
          result => {
            this.updateDataSet()
          },
          error => {
            console.log('Error during contactMech creation')
          }
        )
      },
      createPostalAddress() {
        this.$http.post(createPostalAddressUrl, {
          contactMechTypeId: 'POSTAL_ADDRESS',
          partyId: 'DemoLead3',
          toName: this.forms.postalAddress.fields.toName,
          attnName: this.forms.postalAddress.fields.attentionName,
          address1: this.forms.postalAddress.fields.addressLine1,
          address2: this.forms.postalAddress.fields.addressLine2,
          city: this.forms.postalAddress.fields.city,
          stateProvinceGeoId: this.forms.postalAddress.fields.stateProvince,
          postalCode: this.forms.postalAddress.fields.zipPostalCode,
          courtryGeoId: this.forms.postalAddress.fields.country,
          allowSolicitation: this.forms.postalAddress.fields.allowSolicitation
        }).then(
          result => {
            this.updateDataSet()
          },
          error => {
            console.log('Error during postalAddress creation')
          }
        )
      },
      createTelecomNumber() {
        this.$http.post(createTelecomNumberUrl, {
          contactMechTypeId: 'TELECOM_NUMBER',
          partyId: 'DemoLead3',
          countryCode: this.forms.phoneNumber.fields.countryCode,
          areaCode: this.forms.phoneNumber.fields.areaCode,
          contactNumber: this.forms.phoneNumber.fields.contactNumber,
          extension: this.forms.phoneNumber.fields.extension,
          allowSolicitation: this.forms.phoneNumber.fields.allowSolicitation
        }).then(
          result => {
            this.updateDataSet()
          },
          error => {
            console.log('Error during contactMech creation')
          }
        )
      },
      createEmailAddress() {
        this.$http.post(createEmailAddressUrl, {
          contactMechTypeId: 'EMAIL_ADDRESS',
          partyId: 'DemoLead3',
          emailAddress: this.forms.emailAddress.fields.emailAddress,
          allowSolicitation: this.forms.emailAddress.fields.allowSolicitation
        }).then(
          result => {
            this.updateDataSet()
          },
          error => {
            console.log('Error during email address creation')
          }
        )
      },
      createIpAddress() {
        this.$http.post(createContactMech, {
          contactMechTypeId: 'IP_ADDRESS',
          partyId: 'DemoLead3',
          infoString: this.forms.ipAddress.fields.ipAddress,
          allowSolicitation: this.forms.ipAddress.fields.allowSolicitation
        }).then(
          result => {
            this.updateDataSet()
          },
          error => {
            console.log('Error during contactMech creation')
          }
        )
      },
      createDomain() {
        this.$http.post(createContactMech, {
          contactMechTypeId: 'DOMAIN_NAME',
          partyId: 'DemoLead3',
          infoString: this.forms.domain.fields.domain,
          allowSolicitation: this.forms.domain.fields.allowSolicitation
        }).then(
          result => {
            this.updateDataSet()
          },
          error => {
            console.log('Error during contactMech creation')
          }
        )
      }
    },
    mounted() {
      this.updateDataSet()
    }
  }
</script>

<style scoped lang="sass">
  $expansion-panel-content-padding: 0 10px 10px !default
</style>
