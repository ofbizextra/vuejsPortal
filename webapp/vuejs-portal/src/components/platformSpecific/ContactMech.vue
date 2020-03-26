<template>
  <v-card class="ma-0 pa-0">
    <v-toolbar dense flat v-if="!editMode" class="ma-0 pa-0">
      <v-toolbar-title>Contact mech "{{this.props.partyId}}"</v-toolbar-title>
      <div class="flex-grow-1"></div>
      <v-btn color="warning" icon @click="toggleEdit">
        <v-icon v-on="on" id='mdi-pencil'>{{getIcon('mdi-pencil')}}</v-icon>
      </v-btn>
    </v-toolbar>
    <v-toolbar dense flat v-if="editMode" class="ma-0 pa-0">
      <v-btn color="error" icon @click="toggleEdit">
        <v-icon id='mdi-arrow-left'>{{getIcon('mdi-arrow-left')}}</v-icon>
      </v-btn>
      <v-toolbar-title>Edit contact mech</v-toolbar-title>
      <div class="flex-grow-1"></div>
      <v-btn color="success" icon @click="updateAll">
        <v-icon id='mdi-check'>{{getIcon('mdi-check')}}</v-icon>
      </v-btn>
    </v-toolbar>
    <v-card-text class="mt-2 ma-0 pa-0">
      <v-row stretch dense>
        <v-col cols="12" lg="6" align-self="start">
          <telecom-number
              icon="mdi-phone"
              :contact-mech-list="contactsByType('TELECOM_NUMBER')"
              contact-mech-type-id="TELECOM_NUMBER"
              :edit-mode="editMode"
              label="Telecom number"
              :show-more="showMore"
              :purpose-list="purposeListByType.TELECOM_NUMBER"
              @removeContactMech="removeContactMech($event)"
              @addContactMech="addTelecomNumber"
          ></telecom-number>
          <email-address
              icon="mdi-email"
              :contact-mech-list="contactsByType('EMAIL_ADDRESS')"
              contact-mech-type-id="EMAIL_ADDRESS"
              :edit-mode="editMode"
              label="Email address"
              :show-more="showMore"
              :purpose-list="purposeListByType.EMAIL_ADDRESS"
              @removeContactMech="removeContactMech($event)"
              @addContactMech="addEmailAddress"
          >
          </email-address>
          <generic
              icon="mdi-desktop-tower"
              :contact-mech-list="contactsByType('IP_ADDRESS')"
              contact-mech-type-id="IP_ADDRESS"
              :edit-mode="editMode"
              label="IP address"
              :show-more="showMore"
              :purpose-list="purposeListByType.IP_ADDRESS"
              @removeContactMech="removeContactMech($event)"
              @addContactMech="addIpAddress"
          ></generic>
          <generic
              icon="mdi-at"
              :contact-mech-list="contactsByType('DOMAIN_NAME')"
              contact-mech-type-id="DOMAIN_NAME"
              :edit-mode="editMode"
              label="Domain name"
              :show-more="showMore"
              :purpose-list="purposeListByType.DOMAIN_NAME"
              @removeContactMech="removeContactMech($event)"
              @addContactMech="addDomainName"
          ></generic>
          <generic
              icon="mdi-file-cloud"
              :contact-mech-list="contactsByType('LDAP_ADDRESS')"
              contact-mech-type-id="LDAP_ADDRESS"
              :edit-mode="editMode"
              label="LDAP address"
              :show-more="showMore"
              :purpose-list="purposeListByType.LDAP_ADDRESS"
              @removeContactMech="removeContactMech($event)"
              @addContactMech="addLdapAddress"
          ></generic>
        </v-col>
        <v-col cols="12" lg="6" align-self="start">
          <postal-address
              icon="mdi-map-marker"
              :contact-mech-list="contactsByType('POSTAL_ADDRESS')"
              contact-mech-type-id="POSTAL_ADDRESS"
              :edit-mode="editMode"
              label="Postal address"
              :show-more="showMore"
              :purpose-list="purposeListByType.POSTAL_ADDRESS"
              :rules="forms.postalAddress.rules"
              @removeContactMech="removeContactMech($event)"
              @addContactMech="addPostalAddress"
          >
          </postal-address>
          <generic
              icon="mdi-note-text"
              :contact-mech-list="contactsByType('INTERNAL_PARTYID')"
              contact-mech-type-id="INTERNAL_PARTYID"
              :edit-mode="editMode"
              label="Internal note"
              :show-more="showMore"
              :purpose-list="purposeListByType.INTERNAL_PARTYID"
              @removeContactMech="removeContactMech($event)"
              @addContactMech="addInternalPartyId"
          ></generic>
          <generic
              icon="mdi-web"
              :contact-mech-list="contactsByType('WEB_ADDRESS')"
              contact-mech-type-id="WEB_ADDRESS"
              :edit-mode="editMode"
              label="Web address"
              :show-more="showMore"
              :purpose-list="purposeListByType.WEB_ADDRESS"
              @removeContactMech="removeContactMech($event)"
              @addContactMech="addWebAddress"
          ></generic>
          <v-list dense>
            <v-list-item v-for="ftpAddress in ftpAddressList"
                         :key="ftpAddress.contactMech.contactMechId">
              <v-list-item-icon>
                <v-tooltip top>
                  <template v-slot:activator="{ on }">
                    <v-icon id='mdi-server' left v-on="on">{{getIcon('mdi-server')}}</v-icon>
                  </template>
                  <span>FTP server</span>
                </v-tooltip>
              </v-list-item-icon>
              <v-list-item-content v-if="!editMode">
                <v-list-item-title>
                  {{ftpAddress.ftpAddress.hostname}}:{{ftpAddress.ftpAddress.port}}
                </v-list-item-title>
                <div>
                  user: {{ftpAddress.ftpAddress.username}} - pass: {{ftpAddress.ftpAddress.ftpPassword}}
                </div>
                <div>
                  {{ftpAddress.ftpAddress.filePath}} - {{ftpAddress.ftpAddress.defaultTimeout}}ms
                </div>
                <div>
                  <v-row class="ml-1" justify="space-around">
                    <v-checkbox class="ma-0 mr-1" hide-details small label="binary" :disabled="!editMode"
                                v-model="ftpAddress.ftpAddress.binaryTransfer"
                                true-value="Y" false-value="N"></v-checkbox>
                    <v-checkbox class="ma-0 mr-1" hide-details small label="zip" :disabled="!editMode"
                                v-model="ftpAddress.ftpAddress.zipFile"
                                true-value="Y" false-value="N"></v-checkbox>
                    <v-checkbox class="ma-0 mr-1" hide-details small label="passive" :disabled="!editMode"
                                v-model="ftpAddress.ftpAddress.passiveMode"
                                true-value="Y" false-value="N"></v-checkbox>
                  </v-row>
                </div>
                <v-list-item-subtitle v-if="ftpAddress.partyContactMechPurposes.length > 0">
                  <v-chip class="primary mr-2" x-small v-for="purpose in ftpAddress.partyContactMechPurposes"
                          :key="purpose.contactMechId + '-' + purpose.contactMechPurpostTypeId">
                    {{purpose.contactMechPurposeTypeId}}
                  </v-chip>
                </v-list-item-subtitle>
              </v-list-item-content>
              <v-list-item-content v-if="editMode">
                <v-form class="ml-3" :lazy-validator="lazy">
                  <v-row>
                    <v-text-field hide-details name="hostname" label="Host name" class="mr-4"
                                  :rules="forms.ftpAddress.rules.hostname"
                                  v-model="ftpAddress.ftpAddress.hostname"></v-text-field>
                    <v-text-field hide-details name="port" label="Port" class="" :rules="forms.ftpAddress.rules.port"
                                  v-model="ftpAddress.ftpAddress.port"></v-text-field>
                  </v-row>
                  <v-row>
                    <v-text-field hide-details name="username" label="User Name" class="mr-4"
                                  :rules="forms.ftpAddress.rules.username"
                                  v-model="ftpAddress.ftpAddress.username"></v-text-field>
                    <v-text-field hide-details name="ftpPassword" label="Password"
                                  :rules="forms.ftpAddress.rules.ftpPassword"
                                  v-model="ftpAddress.ftpAddress.ftpPassword"></v-text-field>
                  </v-row>
                  <v-row>
                    <v-text-field hide-details name="filePath" label="Path" class="mr-4"
                                  :rules="forms.ftpAddress.rules.filePath"
                                  v-model="ftpAddress.ftpAddress.filePath"></v-text-field>
                    <v-text-field hide-details name="defaultTimeout" label="Path default timeout" class=""
                                  :rules="forms.ftpAddress.rules.defaultTimeout"
                                  v-model="ftpAddress.ftpAddress.defaultTimeout"></v-text-field>
                  </v-row>
                  <v-row>
                    <v-checkbox class="ma-0 mr-1" name="binaryTransfer" label="Binary Transfert" trueValue="Y"
                                falseValue="N"
                                :rules="forms.ftpAddress.rules.binaryTransfer"
                                v-model="ftpAddress.ftpAddress.binaryTransfer"></v-checkbox>
                    <v-checkbox class="ma-0 mr-1" name="zipFile" label="File compression" trueValue="Y" falseValue="N"
                                :rules="forms.ftpAddress.rules.zipFile"
                                v-model="ftpAddress.ftpAddress.zipFile"></v-checkbox>
                    <v-checkbox class="ma-0 mr-1" name="passiveMode" label="Passive mode" trueValue="Y" falseValue="N"
                                :rules="forms.ftpAddress.rules.passiveMode"
                                v-model="ftpAddress.ftpAddress.passiveMode"></v-checkbox>
                  </v-row>
                </v-form>
              </v-list-item-content>
              <v-list-item-action v-if="editMode">
                <v-btn icon @click="removeContactMech(ftpAddress)">
                  <v-icon id='mdi-delete' color="red">{{getIcon('mdi-delete')}}</v-icon>
                </v-btn>
              </v-list-item-action>
            </v-list-item>
            <v-list-item v-if="editMode">
              <v-list-item-icon></v-list-item-icon>
              <v-list-item-content>
                <v-list-item-subtitle @click="addFtpAddress">
                  <v-icon id='mdi-plus-circle' left>{{getIcon('mdi-plus-circle')}}</v-icon>
                  Add FTP address
                </v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
            <v-divider inset
                       v-if="(ftpAddressList.length > 0 && showMore) || (editMode && showMore)"></v-divider>
          </v-list>
        </v-col>
      </v-row>
      <v-row justify="center">
        <v-btn sel-label="Show more" text @click="toggleShowMore" v-if="!showMore">Show more</v-btn>
        <v-btn sel-label="Show less" text @click="toggleShowMore" v-if="showMore">Show less</v-btn>
      </v-row>
    </v-card-text>
  </v-card>
</template>

<script>

  import PostalAddress from './ContactMech/PostalAddress'

  const getContactMechUrl = '/partymgrfjs/control/getcontactmech'
  const createEmailAddressUrl = '/partymgrfjs/control/createEmailAddress'
  const createPostalAddressUrl = '/partymgrfjs/control/createPostalAddress'
  const createTelecomNumberUrl = '/partymgrfjs/control/createTelecomNumber'
  const createContactMech = '/partymgrfjs/control/createContactMech'
  const createFtpAddressUrl = '/partymgrfjs/control/createFtpAddress'
  const updateContactMechUrl = '/partymgrfjs/control/updateContactMech'
  const updatePostalAddressUrl = '/partymgrfjs/control/updatePostalAddress'
  const updateTelecomNumberUrl = '/partymgrfjs/control/updateTelecomNumber'
  const updateEmailAddressUrl = '/partymgrfjs/control/updateEmailAddress'
  const updateFtpAddressUrl = '/partymgrfjs/control/updateFtpAddress'
  const deleteContactMechUrl = '/partymgrfjs/control/deleteContactMech'
  const getContactMechPurposeTypeUrl = '/partymgrfjs/control/getContactmechPurposeType'
  const createPartyContactMechPurposeUrl = '/partymgrfjs/control/createPartyContactMechPurpose'
  const expirePartyContactMechPurposeUrl = '/partymgrfjs/control/expirePartyContactMechPurpose'
  import icons from '../../js/icons'

  import Generic from './ContactMech/Generic'
  import TelecomNumber from './ContactMech/TelecomNumber'
  import EmailAddress from './ContactMech/EmailAddress'

  export default {
    name: "ContactMech",
    components: {
      PostalAddress,
      Generic,
      TelecomNumber,
      EmailAddress
    },
    props: ['props', 'updateStore'],
    data() {
      return {
        dataSet: {},
        toCreate: [],
        toDelete: [],
        editMode: false,
        showMore: false,
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
        purposeListByType: {
          "ELECTRONIC_ADDRESS": [],
          "POSTAL_ADDRESS": [],
          "TELECOM_NUMBER": [],
          "EMAIL_ADDRESS": [],
          "IP_ADDRESS": [],
          "DOMAIN_NAME": [],
          "WEB_ADDRESS": [],
          "INTERNAL_PARTYID": [],
          "FTP_ADDRESS": [],
          "LDAP_ADDRESS": []
        },
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
          webAddress: {
            valid: true,
            fields: {
              webAddress: '',
              allowSolicitation: 'N'
            },
            rules: {
              webAddress: [
                v => !!v || 'Ip address is required',
                v => /.+\..+/.test(v) || 'Domain must be valid (ex: www.my-example.com)',
              ]
            },
          },
          internalNote: {
            valid: true,
            fields: {
              internalNote: '',
              allowSolicitation: 'N'
            },
            rules: {
              internalNote: [
                v => !!v || 'Ip address is required',
              ]
            },
          },
          ftpAddress: {
            valid: true,
            fields: {
              hostname: '',
              port: '',
              username: '',
              ftpPassword: '',
              binaryTransfer: '',
              filePath: '',
              zipFile: '',
              passiveMode: '',
              defaultTimeout: 0,
              allowSolicitation: 'N'
            },
            rules: {
              hostname: [
                v => !!v || 'Hostname is required',
              ],
              port: [],
              username: [],
              ftpPassword: [],
              binaryTransfer: [],
              filePath: [],
              zipFile: [],
              passiveMode: [],
              defaultTimeout: [],
              allowSolicitation: []
            },
          },
          ldapAddress: {
            valid: true,
            fields: {
              ldapAddress: '',
              allowSolicitation: 'N'
            },
            rules: {
              ldapAddress: [
                v => !!v || 'LDAP address is required',
              ]
            },
          },
        },
        lazy: false
      }
    },
    computed: {
      partyId() {
        return this.props.partyId
      },
      contactMechList() {
        return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps, ...this.toCreate] : []
      },
      telecomNumberList() {
        if (this.showMore) {
          return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps, ...this.toCreate].filter(contact => contact.contactMech.contactMechTypeId === 'TELECOM_NUMBER') : []
        }
        return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps.filter(contact => contact.contactMech.contactMechTypeId === 'TELECOM_NUMBER').splice(0, 1), ...this.toCreate.filter(contact => contact.contactMech.contactMechTypeId === 'TELECOM_NUMBER')] : []
      },
      emailAddressList() {
        if (this.showMore) {
          return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps, ...this.toCreate].filter(contact => contact.contactMech.contactMechTypeId === 'EMAIL_ADDRESS') : []
        }
        return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps.filter(contact => contact.contactMech.contactMechTypeId === 'EMAIL_ADDRESS').splice(0, 1), ...this.toCreate.filter(contact => contact.contactMech.contactMechTypeId === 'EMAIL_ADDRESS')] : []
      },
      postalAddressList() {
        if (this.showMore) {
          return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps, ...this.toCreate].filter(contact => contact.contactMech.contactMechTypeId === 'POSTAL_ADDRESS') : []
        }
        return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps.filter(contact => contact.contactMech.contactMechTypeId === 'POSTAL_ADDRESS').splice(0, 1), ...this.toCreate.filter(contact => contact.contactMech.contactMechTypeId === 'POSTAL_ADDRESS')] : []
      },
      ftpAddressList() {
        if (this.showMore) {
          return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps, ...this.toCreate].filter(contact => contact.contactMech.contactMechTypeId === 'FTP_ADDRESS') : []
        }
        return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps.filter(contact => contact.contactMech.contactMechTypeId === 'FTP_ADDRESS').splice(0, 1), ...this.toCreate.filter(contact => contact.contactMech.contactMechTypeId === 'FTP_ADDRESS')] : []
      },
      ipAddressList() {
        if (this.showMore) {
          return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps, ...this.toCreate].filter(contact => contact.contactMech.contactMechTypeId === 'IP_ADDRESS') : []
        }
        return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps.filter(contact => contact.contactMech.contactMechTypeId === 'IP_ADDRESS').splice(0, 1), ...this.toCreate.filter(contact => contact.contactMech.contactMechTypeId === 'IP_ADDRESS')] : []
      },
      webAddressList() {
        if (this.showMore) {
          return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps, ...this.toCreate].filter(contact => contact.contactMech.contactMechTypeId === 'WEB_ADDRESS') : []
        }
        return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps.filter(contact => contact.contactMech.contactMechTypeId === 'WEB_ADDRESS').splice(0, 1), ...this.toCreate.filter(contact => contact.contactMech.contactMechTypeId === 'WEB_ADDRESS')] : []
      },
      internalPartyIdList() {
        if (this.showMore) {
          return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps, ...this.toCreate].filter(contact => contact.contactMech.contactMechTypeId === 'INTERNAL_PARTYID') : []
        }
        return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps.filter(contact => contact.contactMech.contactMechTypeId === 'INTERNAL_PARTYID').splice(0, 1), ...this.toCreate.filter(contact => contact.contactMech.contactMechTypeId === 'INTERNAL_PARTYID')] : []
      },
      domainNameList() {
        if (this.showMore) {
          return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps, ...this.toCreate].filter(contact => contact.contactMech.contactMechTypeId === 'DOMAIN_NAME') : []
        }
        return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps.filter(contact => contact.contactMech.contactMechTypeId === 'DOMAIN_NAME').splice(0, 1), ...this.toCreate.filter(contact => contact.contactMech.contactMechTypeId === 'DOMAIN_NAME')] : []
      },
      ldapAddressList() {
        if (this.showMore) {
          return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps, ...this.toCreate].filter(contact => contact.contactMech.contactMechTypeId === 'LDAP_ADDRESS') : []
        }
        return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps.filter(contact => contact.contactMech.contactMechTypeId === 'LDAP_ADDRESS').splice(0, 1), ...this.toCreate.filter(contact => contact.contactMech.contactMechTypeId === 'LDAP_ADDRESS')] : []
      },
    },
    methods: {
      contactsByType(type) {
        return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps, ...this.toCreate].filter(contact => contact.contactMech.contactMechTypeId === type) : []
      },
      countContactsByType(type) {
        return this.dataSet.hasOwnProperty('valueMaps') ? this.dataSet.valueMaps.filter(contact => contact.contactMech.contactMechTypeId === type).length : []
      },
      getIcon(icon) {
        return icons.hasOwnProperty(icon) ? icons[icon] : null
      },
      updateDataSet() {
        return new Promise((resolve, reject) => {
          setTimeout(() => {
            this.$http.post(getContactMechUrl, {partyId: this.partyId}).then(
              result => {
                this.dataSet = result.body
                resolve()
              },
              () => {
                reject()
              }
            )
          }, 0)
        })
      },
      createElectronicAddress() {
        this.$http.post(createContactMech, {
          contactMechTypeId: 'ELECTRONIC_ADDRESS',
          partyId: this.partyId,
          infoString: this.forms.electronicAddress.fields.email,
          allowSolicitation: this.forms.electronicAddress.fields.allowSolicitation
        }).then(
          () => {
            this.updateDataSet()
          }
        )
      },
      createPostalAddress() {
        this.$http.post(createPostalAddressUrl, {
          contactMechTypeId: 'POSTAL_ADDRESS',
          partyId: this.partyId,
          toName: this.forms.postalAddress.fields.toName,
          attnName: this.forms.postalAddress.fields.attentionName,
          address1: this.forms.postalAddress.fields.addressLine1,
          address2: this.forms.postalAddress.fields.addressLine2,
          city: this.forms.postalAddress.fields.city,
          stateProvinceGeoId: this.forms.postalAddress.fields.stateProvince,
          postalCode: this.forms.postalAddress.fields.zipPostalCode,
          countryGeoId: this.forms.postalAddress.fields.country,
          allowSolicitation: this.forms.postalAddress.fields.allowSolicitation
        }).then(
          () => {
            this.updateDataSet()
          },
        )
      },
      createTelecomNumber() {
        this.$http.post(createTelecomNumberUrl, {
          contactMechTypeId: 'TELECOM_NUMBER',
          partyId: this.partyId,
          countryCode: this.forms.phoneNumber.fields.countryCode,
          areaCode: this.forms.phoneNumber.fields.areaCode,
          contactNumber: this.forms.phoneNumber.fields.contactNumber,
          extension: this.forms.phoneNumber.fields.extension,
          allowSolicitation: this.forms.phoneNumber.fields.allowSolicitation
        }).then(
          () => {
            this.updateDataSet()
          }
        )
      },
      createEmailAddress() {
        this.$http.post(createEmailAddressUrl, {
          contactMechTypeId: 'EMAIL_ADDRESS',
          partyId: this.partyId,
          emailAddress: this.forms.emailAddress.fields.emailAddress,
          allowSolicitation: this.forms.emailAddress.fields.allowSolicitation
        }).then(
          () => {
            this.updateDataSet()
          }
        )
      },
      createIpAddress() {
        this.$http.post(createContactMech, {
          contactMechTypeId: 'IP_ADDRESS',
          partyId: this.partyId,
          infoString: this.forms.ipAddress.fields.ipAddress,
          allowSolicitation: this.forms.ipAddress.fields.allowSolicitation
        }).then(
          () => {
            this.updateDataSet()
          }
        )
      },
      createDomain() {
        this.$http.post(createContactMech, {
          contactMechTypeId: 'DOMAIN_NAME',
          partyId: this.partyId,
          infoString: this.forms.domain.fields.domain,
          allowSolicitation: this.forms.domain.fields.allowSolicitation
        }).then(
          () => {
            this.updateDataSet()
          }
        )
      },
      createWebAddress() {
        this.$http.post(createContactMech, {
          contactMechTypeId: 'WEB_ADDRESS',
          partyId: this.partyId,
          infoString: this.forms.webAddress.fields.webAddress,
          allowSolicitation: this.forms.webAddress.fields.allowSolicitation
        }).then(
          () => {
            this.updateDataSet()
          }
        )
      },
      createInternalNote() {
        this.$http.post(createContactMech, {
          contactMechTypeId: 'INTERNAL_PARTYID',
          partyId: this.partyId,
          infoString: this.forms.internalNote.fields.internalNote,
          allowSolicitation: this.forms.internalNote.fields.allowSolicitation
        }).then(
          () => {
            this.updateDataSet()
          }
        )
      },
      createFtpAddress() {
        this.$http.post(createFtpAddressUrl, {
          contactMechTypeId: 'FTP_ADDRESS',
          partyId: this.partyId,
          hostname: this.forms.ftpAddress.fields.hostname,
          port: this.forms.ftpAddress.fields.port,
          username: this.forms.ftpAddress.fields.username,
          ftpPassword: this.forms.ftpAddress.fields.ftpPassword,
          binaryTransfer: this.forms.ftpAddress.fields.binaryTransfer,
          filePath: this.forms.ftpAddress.fields.filePath,
          zipFile: this.forms.ftpAddress.fields.zipFile,
          passiveMode: this.forms.ftpAddress.fields.passiveMode,
          defaultTimeout: this.forms.ftpAddress.fields.defaultTimeout,
          allowSolicitation: this.forms.ftpAddress.fields.allowSolicitation
        }).then(
          () => {
            this.updateDataSet()
          }
        )
      },
      createLdapAddress() {
        this.$http.post(createContactMech, {
          contactMechTypeId: 'LDAP_ADDRESS',
          partyId: this.partyId,
          infoString: this.forms.ldapAddress.fields.ldapAddress,
          allowSolicitation: this.forms.ldapAddress.fields.allowSolicitation
        }).then(
          () => {
            this.updateDataSet()
          }
        )
      },
      addEmailAddress() {
        this.toCreate.push({
          contactMech: {
            partyId: this.partyId,
            contactMechTypeId: 'EMAIL_ADDRESS',
            infoString: ''
          },
          partyContactMechPurposes: [],
          purposes: []
        })
      },
      addTelecomNumber() {
        this.toCreate.push({
          contactMech: {
            partyId: this.partyId,
            contactMechTypeId: 'TELECOM_NUMBER'
          },
          partyContactMechPurposes: [],
          telecomNumber: {
            contactNumber: '',
            countryCode: ''
          },
          purposes: []
        })
      },
      addPostalAddress() {
        this.toCreate.push({
          contactMech: {
            partyId: this.partyId,
            contactMechTypeId: 'POSTAL_ADDRESS'
          },
          partyContactMechPurposes: [],
          postalAddress: {
            toName: '',
            attnName: '',
            address1: '',
            address2: '',
            city: '',
            postalCode: ''
          },
          purposes: []
        })
        this.$store.dispatch('ui/incrementUpdateCpt')
      },
      addIpAddress() {
        this.toCreate.push({
          contactMech: {
            partyId: this.partyId,
            contactMechTypeId: 'IP_ADDRESS',
            infoString: ''
          },
          partyContactMechPurposes: [],
          purposes: []
        })
      },
      addWebAddress() {
        this.toCreate.push({
          contactMech: {
            partyId: this.partyId,
            contactMechTypeId: 'WEB_ADDRESS',
            infoString: ''
          },
          partyContactMechPurposes: [],
          purposes: []
        })
      },
      addDomainName() {
        this.toCreate.push({
          contactMech: {
            partyId: this.partyId,
            contactMechTypeId: 'DOMAIN_NAME',
            infoString: ''
          },
          partyContactMechPurposes: [],
          purposes: []
        })
      },
      addInternalPartyId() {
        this.toCreate.push({
          contactMech: {
            partyId: this.partyId,
            contactMechTypeId: 'INTERNAL_PARTYID',
            infoString: ''
          },
          partyContactMechPurposes: [],
          purposes: []
        })
      },
      addFtpAddress() {
        this.toCreate.push({
          contactMech: {
            partyId: this.partyId,
            contactMechTypeId: 'FTP_ADDRESS'
          },
          partyContactMechPurposes: [],
          ftpAddress: {
            hostname: '',
            port: '',
            username: '',
            ftpPassword: '',
            filePath: '',
            defaultTimeout: '',
            binaryTransfer: 'N',
            zipFile: 'N',
            passiveMode: 'N',
          },
          purposes: []
        })
      },
      addLdapAddress() {
        this.toCreate.push({
          contactMech: {
            partyId: this.partyId,
            contactMechTypeId: 'LDAP_ADDRESS',
            infoString: ''
          },
          partyContactMechPurposes: [],
          purposes: []
        })
      },
      removeContactMech(contactMech) {
        if (contactMech.contactMech.hasOwnProperty('contactMechId')) {
          this.toDelete.push(contactMech.contactMech.contactMechId)
          this.dataSet.valueMaps.splice(this.dataSet.valueMaps.indexOf(contactMech), 1)
        } else {
          this.toCreate.splice(this.toCreate.indexOf(contactMech), 1)
        }
        this.$store.dispatch('ui/incrementUpdateCpt')

      },
      displayPurpose(contactMechTypeId, purposeTypeId) {
        return this.purposeListByType[contactMechTypeId].filter(item => item.contactMechPurposeTypeId === purposeTypeId)[0].description
      },
      formatDate(timestamp) {
        let d = new Date(timestamp)
        return `${d.getFullYear()}-${d.getMonth() < 9 ? '0' : ''}${d.getMonth() + 1}-${d.getDate() < 10 ? '0' : ''}${d.getDate()} ${d.getHours()}:${d.getMinutes()}:${d.getSeconds()}.${d.getMilliseconds()}`
      },
      toggleEdit() {
        if (this.editMode) {
          this.updateDataSet()
          this.toCreate = []
          this.toDelete = []
        } else {
          for (let contactMech of this.dataSet.valueMaps) {
            this.$set(contactMech, 'purposes', contactMech.partyContactMechPurposes.map(purpose => purpose.contactMechPurposeTypeId))
          }
        }
        this.editMode = !this.editMode
        this.$store.dispatch('ui/incrementUpdateCpt')
      },
      toggleShowMore() {
        this.showMore = !this.showMore
        this.$store.dispatch('ui/incrementUpdateCpt')
      },
      updateAll() {
        let promises = []
        for (let contactMech of this.dataSet.valueMaps) {
          switch (contactMech.contactMech.contactMechTypeId) {
            case 'POSTAL_ADDRESS':
              // do update
              promises.push(new Promise((resolve, reject) => {
                setTimeout(() => {
                  this.$http.post(updatePostalAddressUrl, {
                    contactMechId: contactMech.contactMech.contactMechId,
                    contactMechTypeId: contactMech.contactMech.contactMechTypeId,
                    partyId: this.partyId,
                    toName: contactMech.postalAddress.toName,
                    attnName: contactMech.postalAddress.attnName,
                    address1: contactMech.postalAddress.address1,
                    address2: contactMech.postalAddress.address2,
                    city: contactMech.postalAddress.city,
                    postalCode: contactMech.postalAddress.postalCode
                  }).then(
                    () => {
                      resolve()
                    },
                    () => {
                      reject()
                    }
                  )
                }, 0)
              }))
              break
            case 'TELECOM_NUMBER':
              // do update
              promises.push(new Promise((resolve, reject) => {
                setTimeout(() => {
                  this.$http.post(updateTelecomNumberUrl, {
                    contactMechId: contactMech.contactMech.contactMechId,
                    contactMechTypeId: contactMech.contactMech.contactMechTypeId,
                    partyId: this.partyId,
                    countryCode: contactMech.telecomNumber.countryCode,
                    contactNumber: contactMech.telecomNumber.contactNumber
                  }).then(
                    () => {
                      resolve()
                    },
                    () => {
                      reject()
                    }
                  )
                }, 0)
              }))
              break
            case 'EMAIL_ADDRESS':
              // do update
              promises.push(new Promise((resolve, reject) => {
                setTimeout(() => {
                  this.$http.post(updateEmailAddressUrl, {
                    contactMechId: contactMech.contactMech.contactMechId,
                    contactMechTypeId: contactMech.contactMech.contactMechTypeId,
                    partyId: this.partyId,
                    emailAddress: contactMech.contactMech.infoString
                  }).then(
                    () => {
                      resolve()
                    },
                    () => {
                      reject()
                    }
                  )
                }, 0)
              }))
              break
            case 'FTP_ADDRESS':
              // do update
              promises.push(new Promise((resolve, reject) => {
                setTimeout(() => {
                  this.$http.post(updateFtpAddressUrl, {
                    contactMechId: contactMech.contactMech.contactMechId,
                    contactMechTypeId: contactMech.contactMech.contactMechTypeId,
                    partyId: this.partyId,
                    hostname: contactMech.ftpAddress.hostname,
                    port: contactMech.ftpAddress.port,
                    username: contactMech.ftpAddress.username,
                    ftpPassword: contactMech.ftpAddress.ftpPassword,
                    filePath: contactMech.ftpAddress.filePath,
                    defaultTimeout: contactMech.ftpAddress.defaultTimeout,
                    binaryTransfer: contactMech.ftpAddress.binaryTransfer,
                    zipFile: contactMech.ftpAddress.zipFile,
                    passiveMode: contactMech.ftpAddress.passiveMode,
                  }).then(
                    () => {
                      resolve()
                    },
                    () => {
                      reject()
                    }
                  )
                }, 0)
              }))
              break
            default:
              // do update
              promises.push(new Promise((resolve, reject) => {
                setTimeout(() => {
                  this.$http.post(updateContactMechUrl, {
                    contactMechId: contactMech.contactMech.contactMechId,
                    contactMechTypeId: contactMech.contactMech.contactMechTypeId,
                    partyId: this.partyId,
                    infoString: contactMech.contactMech.infoString
                  }).then(
                    () => {
                      resolve()
                    },
                    () => {
                      reject()
                    }
                  )
                }, 0)
              }))
              break
          }
        }
        for (let contactMech of this.toCreate) {
          switch (contactMech.contactMech.contactMechTypeId) {
            case 'POSTAL_ADDRESS':
              // do creation
              promises.push(new Promise((resolve, reject) => {
                setTimeout(() => {
                  this.$http.post(createPostalAddressUrl, {
                    contactMechTypeId: 'POSTAL_ADDRESS',
                    partyId: this.partyId,
                    toName: contactMech.postalAddress.toName,
                    attnName: contactMech.postalAddress.attnName,
                    address1: contactMech.postalAddress.address1,
                    address2: contactMech.postalAddress.address2,
                    city: contactMech.postalAddress.city,
                    postalCode: contactMech.postalAddress.postalCode,
                  }).then(
                    () => {
                      resolve()
                    },
                    () => {
                      reject()
                    }
                  )
                }, 0)
              }))
              break
            case 'TELECOM_NUMBER':
              // do creation
              promises.push(new Promise((resolve, reject) => {
                setTimeout(() => {
                  this.$http.post(createTelecomNumberUrl, {
                    contactMechTypeId: 'TELECOM_NUMBER',
                    partyId: this.partyId,
                    countryCode: contactMech.telecomNumber.countryCode,
                    contactNumber: contactMech.telecomNumber.contactNumber,
                  }).then(
                    () => {
                      resolve()
                    },
                    () => {
                      reject()
                    }
                  )
                }, 0)
              }))
              break
            case 'EMAIL_ADDRESS':
              // do creation
              promises.push(new Promise((resolve, reject) => {
                setTimeout(() => {
                  this.$http.post(createEmailAddressUrl, {
                    contactMechTypeId: 'EMAIL_ADDRESS',
                    partyId: this.partyId,
                    emailAddress: contactMech.contactMech.infoString,
                  }).then(
                    () => {
                      resolve()
                    },
                    () => {
                      reject()
                    }
                  )
                }, 0)
              }))
              break
            case 'FTP_ADDRESS':
              // do creation
              promises.push(new Promise((resolve, reject) => {
                setTimeout(() => {
                  this.$http.post(createFtpAddressUrl, {
                    contactMechTypeId: 'FTP_ADDRESS',
                    partyId: this.partyId,
                    hostname: contactMech.ftpAddress.hostname,
                    port: contactMech.ftpAddress.port,
                    username: contactMech.ftpAddress.username,
                    ftpPassword: contactMech.ftpAddress.ftpPassword,
                    filePath: contactMech.ftpAddress.filePath,
                    defaultTimeout: contactMech.ftpAddress.defaultTimeout,
                    binaryTransfer: contactMech.ftpAddress.binaryTransfer,
                    zipFile: contactMech.ftpAddress.zipFile,
                    passiveMode: contactMech.ftpAddress.passiveMode,
                  }).then(
                    () => {
                      resolve()
                    },
                    () => {
                      reject()
                    }
                  )
                }, 0)
              }))
              break
            default:
              // do creation
              promises.push(new Promise((resolve, reject) => {
                setTimeout(() => {
                  this.$http.post(createContactMech, {
                    contactMechTypeId: contactMech.contactMech.contactMechTypeId,
                    partyId: this.partyId,
                    infoString: contactMech.contactMech.infoString,
                  }).then(
                    () => {
                      resolve()
                    },
                    () => {
                      reject()
                    }
                  )
                }, 0)
              }))
              break
          }
        }
        for (let contactMechId of this.toDelete) {
          promises.push(new Promise((resolve, reject) => {
            setTimeout(() => {
              this.$http.post(deleteContactMechUrl, {
                contactMechId: contactMechId,
                partyId: this.partyId
              }).then(
                () => {
                  resolve()
                },
                () => {
                  reject()
                }
              )
            }, 0)
          }))
        }
        Promise.all(promises).then(() => {
          promises = []
          for (let contactMech of this.dataSet.valueMaps) {
            for (let purpose of contactMech.purposes) {
              if (contactMech.partyContactMechPurposes.filter(item => item.contactMechPurposeTypeId === purpose).length === 0) {
                // do post createPurpose
                promises.push(
                  new Promise((resolve, reject) => {
                    setTimeout(() => {
                      this.$http.post(createPartyContactMechPurposeUrl, {
                        contactMechId: contactMech.contactMech.contactMechId,
                        contactMechPurposeTypeId: purpose,
                        partyId: contactMech.partyContactMech.partyId,
                      }).then(
                        () => {
                          resolve()
                        },
                        () => {
                          reject()
                        }
                      )
                    }, 0)
                  })
                )
              }
            }
            for (let purpose of contactMech.partyContactMechPurposes) {
              if (!contactMech.purposes.includes(purpose.contactMechPurposeTypeId)) {
                // do post expirePurpose
                promises.push(
                  new Promise((resolve, reject) => {
                    setTimeout(() => {
                      this.$http.post(expirePartyContactMechPurposeUrl, {
                        contactMechId: contactMech.contactMech.contactMechId,
                        partyId: contactMech.partyContactMech.partyId,
                        fromDate: this.formatDate(purpose.fromDate),
                        contactMechPurposeTypeId: purpose.contactMechPurposeTypeId
                      }).then(
                        () => {
                          resolve()
                        },
                        () => {
                          reject()
                        }
                      )
                    }, 0)
                  })
                )
              }
            }
          }
          Promise.all(promises).then(() => {
            this.toggleEdit()
            this.toCreate = []
          })
        })
      }
    },
    mounted() {
      this.updateDataSet()
      for (let type of this.contactTypes) {
        this.$http.post(getContactMechPurposeTypeUrl, {
          contactMechTypeId: type
        }).then(response => {
          this.purposeListByType[type] = response.body.purposeTypeList
        })
      }
    },
    watch: {
      partyId() {
        if (this.editMode) {
          this.toggleEdit()
        }
        this.updateDataSet()
      }
    }
  }
</script>

<style scoped>
</style>
