<template>
  <v-card tile class="ma-1">
    <v-toolbar tile dark color="primary" dense flat v-if="!editMode" class="ma-0 pa-0 screenlet-title-bar">
      <v-toolbar-title class="title">{{getUiLabel('contactMechTitle')}} "{{this.props.partyId}}"</v-toolbar-title>
      <div class="flex-grow-1"></div>
      <v-btn icon @click="toggleEdit">
        <v-icon id='mdi-pencil'>{{getIcon('mdi-pencil')}}</v-icon>
      </v-btn>
    </v-toolbar>
    <v-toolbar tile dark color="primary" dense flat v-if="editMode" class="ma-0 pa-0">
      <v-btn icon @click="toggleEdit">
        <v-icon id='mdi-arrow-left'>{{getIcon('mdi-arrow-left')}}</v-icon>
      </v-btn>
      <v-toolbar-title>Edit contact mech</v-toolbar-title>
      <div class="flex-grow-1"></div>
      <v-dialog v-model="confirmDialog" persistent v-if="toDelete.length !== 0 && modified" max-width="600px">
        <template v-slot:activator="{on}">
          <v-btn icon v-on="on">
            <v-icon color="green" id='mdi-check'>{{getIcon('mdi-check')}}</v-icon>
          </v-btn>
        </template>
        <v-card>
          <v-card-title>
            Are you sure ?
          </v-card-title>
          <v-card-text>
            <p>
              {{toDelete.length}} contact{{toDelete.length > 1 ? 's' : ''}} will be expired.
            </p>
            <p>
              Are you sure you want to proceed ?
            </p>
          </v-card-text>
          <v-card-actions class="d-flex flex-row-reverse">
            <v-btn color="success" class="ma-1" @click="updateAll">Confirm expiration</v-btn>
            <v-btn color="error" class="ma-1" @click="toggleEdit">Cancel changes</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
      <v-btn icon @click="updateAll" v-if="toDelete.length === 0 && modified">
        <v-icon color="green" id='mdi-check'>{{getIcon('mdi-check')}}</v-icon>
      </v-btn>
    </v-toolbar>
    <v-card-text class="pa-1">
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
              :show-less-list="displayParam.TELECOM_NUMBER"
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
              :show-less-list="displayParam.EMAIL_ADDRESS"
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
              sel-label="ipAddr"
              sel-label-add="addIpAddr"
              :show-more="showMore"
              :purpose-list="purposeListByType.IP_ADDRESS"
              :show-less-list="displayParam.IP_ADDRESS"
              @removeContactMech="removeContactMech($event)"
              @addContactMech="addIpAddress"
          ></generic>
          <generic
              icon="mdi-at"
              :contact-mech-list="contactsByType('DOMAIN_NAME')"
              contact-mech-type-id="DOMAIN_NAME"
              :edit-mode="editMode"
              label="Domain name"
              sel-label="domainName"
              sel-label-add="addDomainName"
              :show-more="showMore"
              :purpose-list="purposeListByType.DOMAIN_NAME"
              :show-less-list="displayParam.DOMAIN_NAME"
              @removeContactMech="removeContactMech($event)"
              @addContactMech="addDomainName"
          ></generic>
          <generic
              icon="mdi-file-cloud"
              :contact-mech-list="contactsByType('LDAP_ADDRESS')"
              contact-mech-type-id="LDAP_ADDRESS"
              :edit-mode="editMode"
              label="LDAP address"
              sel-label="ldapAddr"
              sel-label-add="addLdapAddr"
              :show-more="showMore"
              :purpose-list="purposeListByType.LDAP_ADDRESS"
              :show-less-list="displayParam.LDAP_ADDRESS"
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
              :show-less-list="displayParam.POSTAL_ADDRESS"
              :rules="forms.postalAddress.rules"
              :lazy="lazy"
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
              sel-label="intNote"
              sel-label-add="addintNote"
              :show-more="showMore"
              :purpose-list="purposeListByType.INTERNAL_PARTYID"
              :show-less-list="displayParam.INTERNAL_PARTYID"
              @removeContactMech="removeContactMech($event)"
              @addContactMech="addInternalPartyId"
          ></generic>
          <generic
              icon="mdi-web"
              :contact-mech-list="contactsByType('WEB_ADDRESS')"
              contact-mech-type-id="WEB_ADDRESS"
              :edit-mode="editMode"
              label="Web address"
              sel-label="webAddr"
              sel-label-add="addWebAddr"
              :show-more="showMore"
              :purpose-list="purposeListByType.WEB_ADDRESS"
              :show-less-list="displayParam.WEB_ADDRESS"
              @removeContactMech="removeContactMech($event)"
              @addContactMech="addWebAddress"
          ></generic>
          <ftp-address
              icon="mdi-server"
              :contact-mech-list="contactsByType('FTP_ADDRESS')"
              contact-mech-type-id="FTP_ADDRESS"
              :edit-mode="editMode"
              label="FTP address"
              :show-more="showMore"
              :purpose-list="purposeListByType.FTP_ADDRESS"
              :show-less-list="displayParam.FTP_ADDRESS"
              :rules="forms.ftpAddress.rules"
              :lazy="lazy"
              @removeContactMech="removeContactMech($event)"
              @addContactMech="addFtpAddress"
          >
          </ftp-address>
        </v-col>
      </v-row>
      <v-row justify="center">
        <v-btn sel-label="Show more" text @click="toggleShowMore" v-if="!showMore && !editMode">Show more</v-btn>
        <v-btn sel-label="Show less" text @click="toggleShowMore" v-if="showMore && !editMode">Show less</v-btn>
        <v-btn sel-label="Show old" text @click="toggleShowOld" v-if="!showOld && !editMode">Show old</v-btn>
        <v-btn sel-label="Hide old" text @click="toggleShowOld" v-if="showOld && !editMode">Hide old</v-btn>
      </v-row>
    </v-card-text>
  </v-card>
</template>

<script>
  import constants from '../../js/constants'
  import icons from '../../js/icons'

  import Generic from './ContactMech/Generic'
  import TelecomNumber from './ContactMech/TelecomNumber'
  import EmailAddress from './ContactMech/EmailAddress'
  import PostalAddress from './ContactMech/PostalAddress'
  import FtpAddress from './ContactMech/FtpAddress'

  export default {
    name: "ContactMech",
    components: {
      FtpAddress,
      PostalAddress,
      Generic,
      TelecomNumber,
      EmailAddress
    },
    props: ['props', 'updateStore'],
    data() {
      return {
        constants: constants,
        dataSet: {},
        defaultDataSet: {},
        toCreate: [],
        toDelete: [],
        editMode: false,
        showMore: false,
        showOld: false,
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
        lazy: false,
        confirmDialog: false,
        defaultDisplay: {
          //  mode:
          //    count: display the n first contact
          //    purposes: display contacts that have at least 1 purposes included in 'purposes' array
          //    none: don't display in showLess mode
          //    never: never display in showLess nor showMore mods
          //  count: number of contact to display in 'count' mod
          //  purposes: list of purposes that should be displayed in 'purposes' mod
          POSTAL_ADDRESS: {
            mode: 'purposes',
            count: 1,
            purposes: ['SHIPPING_LOCATION']
          },
          TELECOM_NUMBER: {
            mode: 'purposes',
            count: 1,
            purposes: ['PHONE_WORK']
          },
          EMAIL_ADDRESS: {
            mode: 'purposes',
            count: 1,
            purposes: ['PRIMARY_EMAIL']
          },
          IP_ADDRESS: {
            mode: 'never',
            count: 1,
            purposes: []
          },
          DOMAIN_NAME: {
            mode: 'none',
            count: 1,
            purposes: []
          },
          WEB_ADDRESS: {
            mode: 'purposes',
            count: 1,
            purposes: ['PRIMARY_WEB_URL']
          },
          INTERNAL_PARTYID: {
            mode: 'count',
            count: 1,
            purposes: []
          },
          FTP_ADDRESS: {
            mode: 'never',
            count: 1,
            purposes: []
          },
          LDAP_ADDRESS: {
            mode: 'never',
            count: 1,
            purposes: []
          }
        }
      }
    },
    computed: {
      partyId() {
        return this.props.partyId
      },
      getContactMechUrl() {
        return this.props.configParam.hasOwnProperty('getContactMechUrl') ? this.props.configParam.getContactMechUrl : ''
      },
      createContactMechUrl() {
        return this.props.configParam.hasOwnProperty('createContactMechUrl') ? this.props.configParam.createContactMechUrl : ''
      },
      createTelecomNumberUrl() {
        return this.props.configParam.hasOwnProperty('createTelecomNumberUrl') ? this.props.configParam.createTelecomNumberUrl : ''
      },
      createEmailAddressUrl() {
        return this.props.configParam.hasOwnProperty('createEmailAddressUrl') ? this.props.configParam.createEmailAddressUrl : ''
      },
      createPostalAddressUrl() {
        return this.props.configParam.hasOwnProperty('createPostalAddressUrl') ? this.props.configParam.createPostalAddressUrl : ''
      },
      createFtpAddressUrl() {
        return this.props.configParam.hasOwnProperty('createFtpAddressUrl') ? this.props.configParam.createFtpAddressUrl : ''
      },
      updateContactMechUrl() {
        return this.props.configParam.hasOwnProperty('updateContactMechUrl') ? this.props.configParam.updateContactMechUrl : ''
      },
      updateTelecomNumberUrl() {
        return this.props.configParam.hasOwnProperty('updateTelecomNumberUrl') ? this.props.configParam.updateTelecomNumberUrl : ''
      },
      updateEmailAddressUrl() {
        return this.props.configParam.hasOwnProperty('updateEmailAddressUrl') ? this.props.configParam.updateEmailAddressUrl : ''
      },
      updatePostalAddressUrl() {
        return this.props.configParam.hasOwnProperty('updatePostalAddressUrl') ? this.props.configParam.updatePostalAddressUrl : ''
      },
      updateFtpAddressUrl() {
        return this.props.configParam.hasOwnProperty('updateFtpAddressUrl') ? this.props.configParam.updateFtpAddressUrl : ''
      },
      deleteContactMechUrl() {
        return this.props.configParam.hasOwnProperty('deleteContactMechUrl') ? this.props.configParam.deleteContactMechUrl : ''
      },
      getContactMechPurposeTypeUrl() {
        return this.props.configParam.hasOwnProperty('getContactMechPurposeTypeUrl') ? this.props.configParam.getContactMechPurposeTypeUrl : ''
      },
      createContactMechPurposeUrl() {
        return this.props.configParam.hasOwnProperty('createContactMechPurposeUrl') ? this.props.configParam.createContactMechPurposeUrl : ''
      },
      expireContactMechPurposeUrl() {
        return this.props.configParam.hasOwnProperty('expireContactMechPurposeUrl') ? this.props.configParam.expireContactMechPurposeUrl : ''
      },
      displayParam() {
        return this.props.hasOwnProperty('displayParam') ? this.props.displayParam : this.defaultDisplay
      },
      modified() {
        return JSON.stringify(this.dataSet) !== this.defaultDataSet || this.toCreate.length > 0 || this.toDelete.length > 0
      }
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
      getUiLabel(label) {
        return this.props.uiLabel.hasOwnProperty(label) ? this.props.uiLabel[label] : label
      },
      updateDataSet() {
        return new Promise((resolve, reject) => {
          setTimeout(() => {
            this.$store.dispatch('backOfficeApi/doPost', {
              uri: this.getContactMechUrl,
              params: {
                partyId: this.partyId,
                showOld: this.showOld ? 'true' : 'false'
              },
              hideEventMessage: true
            }).then(
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
        this.confirmDialog = false
        if (this.editMode) {
          this.updateDataSet()
          this.toCreate = []
          this.toDelete = []
          this.editMode = !this.editMode
        } else {
          this.showOld = false
          this.updateDataSet().then(() => {
            for (let contactMech of this.dataSet.valueMaps) {
              this.$set(contactMech, 'purposes', contactMech.partyContactMechPurposes.map(purpose => purpose.contactMechPurposeTypeId))
            }
            this.defaultDataSet = JSON.stringify(this.dataSet)
            this.editMode = !this.editMode
          })
        }
        this.$store.dispatch('ui/incrementUpdateCpt')
      },
      toggleShowMore() {
        this.showMore = !this.showMore
        this.$store.dispatch('ui/incrementUpdateCpt')
      },
      toggleShowOld() {
        this.showOld = !this.showOld
        this.updateDataSet()
        this.$store.dispatch('ui/incrementUpdateCpt')
      },
      updateAll() {
        this.confirmDialog = false
        let promises = []
        for (let contactMech of this.dataSet.valueMaps) {
          switch (contactMech.contactMech.contactMechTypeId) {
            case 'POSTAL_ADDRESS':
              // do update
              promises.push(new Promise((resolve, reject) => {
                setTimeout(() => {
                  this.$store.dispatch('backOfficeApi/doPost', {
                    uri: this.updatePostalAddressUrl,
                    params: {
                      contactMechId: contactMech.contactMech.contactMechId,
                      contactMechTypeId: contactMech.contactMech.contactMechTypeId,
                      partyId: this.partyId,
                      toName: contactMech.postalAddress.toName,
                      attnName: contactMech.postalAddress.attnName,
                      address1: contactMech.postalAddress.address1,
                      address2: contactMech.postalAddress.address2,
                      city: contactMech.postalAddress.city,
                      postalCode: contactMech.postalAddress.postalCode
                    },
                    hideEventMessage: true
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
                  this.$store.dispatch('backOfficeApi/doPost', {
                    uri: this.updateTelecomNumberUrl,
                    params: {
                      contactMechId: contactMech.contactMech.contactMechId,
                      contactMechTypeId: contactMech.contactMech.contactMechTypeId,
                      partyId: this.partyId,
                      countryCode: contactMech.telecomNumber.countryCode,
                      contactNumber: contactMech.telecomNumber.contactNumber
                    },
                    hideEventMessage: true
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
                  this.$store.dispatch('backOfficeApi/doPost', {
                    uri: this.updateEmailAddressUrl,
                    params: {
                      contactMechId: contactMech.contactMech.contactMechId,
                      contactMechTypeId: contactMech.contactMech.contactMechTypeId,
                      partyId: this.partyId,
                      emailAddress: contactMech.contactMech.infoString
                    },
                    hideEventMessage: true
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
                  this.$store.dispatch('backOfficeApi/doPost', {
                    uri: this.updateFtpAddressUrl,
                    params: {
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
                    },
                    hideEventMessage: true
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
                  this.$store.dispatch('backOfficeApi/doPost', {
                    uri: this.updateContactMechUrl,
                    params: {
                      contactMechId: contactMech.contactMech.contactMechId,
                      contactMechTypeId: contactMech.contactMech.contactMechTypeId,
                      partyId: this.partyId,
                      infoString: contactMech.contactMech.infoString
                    },
                    hideEventMessage: true
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
                  this.$store.dispatch('backOfficeApi/doPost', {
                    uri: this.createPostalAddressUrl,
                    params: {
                      contactMechTypeId: 'POSTAL_ADDRESS',
                      partyId: this.partyId,
                      toName: contactMech.postalAddress.toName,
                      attnName: contactMech.postalAddress.attnName,
                      address1: contactMech.postalAddress.address1,
                      address2: contactMech.postalAddress.address2,
                      city: contactMech.postalAddress.city,
                      postalCode: contactMech.postalAddress.postalCode,
                    },
                    hideEventMessage: false
                  }).then(
                    (response) => {
                      contactMech.contactMech.contactMechId = response.body.contactMechId
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
                  this.$store.dispatch('backOfficeApi/doPost', {
                    uri: this.createTelecomNumberUrl,
                    params: {
                      contactMechTypeId: 'TELECOM_NUMBER',
                      partyId: this.partyId,
                      countryCode: contactMech.telecomNumber.countryCode,
                      contactNumber: contactMech.telecomNumber.contactNumber,
                    },
                    hideEventMessage: false
                  }).then(
                    (response) => {
                      contactMech.contactMech.contactMechId = response.body.contactMechId
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
                  this.$store.dispatch('backOfficeApi/doPost', {
                    uri: this.createEmailAddressUrl,
                    params: {
                      contactMechTypeId: 'EMAIL_ADDRESS',
                      partyId: this.partyId,
                      emailAddress: contactMech.contactMech.infoString,
                    },
                    hideEventMessage: false
                  }).then(
                    (response) => {
                      contactMech.contactMech.contactMechId = response.body.contactMechId
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
                  this.$store.dispatch('backOfficeApi/doPost', {
                    uri: this.createFtpAddressUrl,
                    params: {
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
                    },
                    hideEventMessage: false
                  }).then(
                    (response) => {
                      contactMech.contactMech.contactMechId = response.body.contactMechId
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
                  this.$store.dispatch('backOfficeApi/doPost', {
                    uri: this.createContactMechUrl,
                    params: {
                      contactMechTypeId: contactMech.contactMech.contactMechTypeId,
                      partyId: this.partyId,
                      infoString: contactMech.contactMech.infoString,
                    },
                    hideEventMessage: false
                  }).then(
                    (response) => {
                      contactMech.contactMech.contactMechId = response.body.contactMechId
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
              this.$store.dispatch('backOfficeApi/doPost', {
                uri: this.deleteContactMechUrl,
                params: {
                  contactMechId: contactMechId,
                  partyId: this.partyId
                },
                hideEventMessage: false
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
                      this.$store.dispatch('backOfficeApi/doPost', {
                        uri: this.createContactMechPurposeUrl,
                        params: {
                          contactMechId: contactMech.contactMech.contactMechId,
                          contactMechPurposeTypeId: purpose,
                          partyId: contactMech.partyContactMech.partyId,
                        },
                        hideEventMessage: true
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
                      this.$store.dispatch('backOfficeApi/doPost', {
                        uri: this.expireContactMechPurposeUrl,
                        params: {
                          contactMechId: contactMech.contactMech.contactMechId,
                          partyId: contactMech.partyContactMech.partyId,
                          fromDate: this.formatDate(purpose.fromDate),
                          contactMechPurposeTypeId: purpose.contactMechPurposeTypeId
                        },
                        hideEventMessage: true
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
          for (let contactMech of this.toCreate) {
            for (let purpose of contactMech.purposes) {
              // do post createPurpose
              promises.push(
                new Promise((resolve, reject) => {
                  setTimeout(() => {
                    this.$store.dispatch('backOfficeApi/doPost', {
                      uri: this.createContactMechPurposeUrl,
                      params: {
                        contactMechId: contactMech.contactMech.contactMechId,
                        contactMechPurposeTypeId: purpose,
                        partyId: contactMech.contactMech.partyId,
                      },
                      hideEventMessage: true
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
        this.$store.dispatch('backOfficeApi/doPost', {
          uri: this.getContactMechPurposeTypeUrl,
          params: {
            contactMechTypeId: type
          },
          hideEventMessage: true
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
