<template>
  <v-container grid-list-xs text-center>
    <v-layout wrap justify-space-around>
      <v-flex text-left stretch xs12>
        <v-card>
          <v-toolbar flat v-if="!editMode">
            <v-toolbar-title>Contact mech "{{this.props.partyId}}"</v-toolbar-title>
            <div class="flex-grow-1"></div>
            <v-btn icon @click="toggleEdit">
              <v-icon v-on="on">{{getIcon('mdi-pencil')}}</v-icon>
            </v-btn>
          </v-toolbar>
          <v-toolbar flat v-if="editMode">
            <v-btn icon @click="toggleEdit">
              <v-icon>{{getIcon('mdi-arrow-left')}}</v-icon>
            </v-btn>
            <v-toolbar-title>Edit contact mech</v-toolbar-title>
            <div class="flex-grow-1"></div>
            <v-btn icon @click="updateAll">
              <v-icon>{{getIcon('mdi-check')}}</v-icon>
            </v-btn>
          </v-toolbar>
          <v-divider></v-divider>
          <v-card-text>
            <v-row stretch dense>
              <v-col cols="12" md="6" align-self="start">
                <v-list dense>
                  <v-list-item v-for="phoneNumber in telecomNumberList"
                               :key="phoneNumber.contactMech.contactMechId">
                    <v-tooltip top>
                      <template v-slot:activator="{ on }">
                        <v-list-item-icon v-on="on">
                          <v-icon left>{{getIcon('mdi-phone')}}</v-icon>
                        </v-list-item-icon>
                      </template>
                      <span>Telecom number</span>
                    </v-tooltip>
                    <v-list-item-content>
                      <v-list-item-title v-if="!editMode">
                        {{`${phoneNumber.telecomNumber.countryCode || ''} ${phoneNumber.telecomNumber.areaCode || ''}
                        ${phoneNumber.telecomNumber.contactNumber || ''} ${phoneNumber.telecomNumber.extension || ''}`}}
                      </v-list-item-title>
                      <v-list-item-title v-if="editMode">
                        <v-row>
                          <v-col class="col-3">
                            <v-text-field class="ignore-css" hide-details label="Country code"
                                          v-model="phoneNumber.telecomNumber.countryCode"></v-text-field>
                          </v-col>
                          <v-col class="col-9">
                            <v-text-field class="ignore-css" hide-details label="Number"
                                          v-model="phoneNumber.telecomNumber.contactNumber"></v-text-field>
                          </v-col>
                        </v-row>
                      </v-list-item-title>
                      <v-list-item-subtitle v-if="phoneNumber.partyContactMechPurposes.length > 0 && !editMode">
                        <v-chip class="primary mr-2" x-small v-for="purpose in phoneNumber.partyContactMechPurposes"
                                :key="purpose.contactMechId + '-' + purpose.contactMechPurposeTypeId">
                          {{displayPurpose('TELECOM_NUMBER', purpose.contactMechPurposeTypeId)}}
                        </v-chip>
                      </v-list-item-subtitle>
                      <v-list-item-subtitle v-if="editMode">
                        <v-select
                          label="purposes"
                          v-model="phoneNumber.purposes"
                          :items="purposeListByType.TELECOM_NUMBER"
                          deletable-chips
                          chips
                          hide-selected
                          multiple
                          item-text="description"
                          item-value="contactMechPurposeTypeId">
                        </v-select>
                      </v-list-item-subtitle>
                    </v-list-item-content>
                    <v-list-item-action v-if="editMode">
                      <v-btn icon @click="removeContactMech(phoneNumber)">
                        <v-icon color="red">{{getIcon('mdi-delete')}}</v-icon>
                      </v-btn>
                    </v-list-item-action>
                  </v-list-item>
                  <v-list-item v-if="editMode">
                    <v-list-item-icon></v-list-item-icon>
                    <v-list-item-content>
                      <v-list-item-subtitle @click="addTelecomNumber">
                        <v-icon left>{{getIcon('mdi-plus-circle')}}</v-icon>
                        Add telecom number
                      </v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                  <v-divider inset v-if="telecomNumberList.length > 0 || editMode"></v-divider>
                  <v-list-item v-for="email in emailAddressList"
                               :key="email.contactMech.contactMechId">
                    <v-list-item-icon>
                      <v-tooltip top>
                        <template v-slot:activator="{ on }">
                          <v-icon left v-on="on">{{getIcon('mdi-email')}}</v-icon>
                        </template>
                        <span>Email address</span>
                      </v-tooltip>
                    </v-list-item-icon>
                    <v-list-item-content>
                      <v-list-item-title v-if="!editMode">
                        {{email.contactMech.infoString}}
                      </v-list-item-title>
                      <v-list-item-title v-if="editMode">
                        <v-row>
                          <v-col>
                            <v-text-field hide-details label="Email address"
                                          v-model="email.contactMech.infoString"></v-text-field>
                          </v-col>
                        </v-row>
                      </v-list-item-title>
                      <v-list-item-subtitle v-if="email.partyContactMechPurposes.length > 0 && !editMode">
                        <v-chip class="primary mr-2" x-small v-for="purpose in email.partyContactMechPurposes"
                                :key="purpose.contactMechId + '-' + purpose.contactMechPurposeTypeId">
                          {{displayPurpose('EMAIL_ADDRESS', purpose.contactMechPurposeTypeId)}}
                        </v-chip>
                      </v-list-item-subtitle>
                      <v-list-item-subtitle v-if="editMode">
                        <v-select
                          label="purposes"
                          v-model="email.purposes"
                          :items="purposeListByType.EMAIL_ADDRESS"
                          deletable-chips
                          chips
                          hide-selected
                          multiple
                          item-text="description"
                          item-value="contactMechPurposeTypeId">
                        </v-select>
                      </v-list-item-subtitle>
                    </v-list-item-content>
                    <v-list-item-action v-if="editMode">
                      <v-btn icon @click="removeContactMech(email)">
                        <v-icon color="red">{{getIcon('mdi-delete')}}</v-icon>
                      </v-btn>
                    </v-list-item-action>
                  </v-list-item>
                  <v-list-item v-if="editMode">
                    <v-list-item-icon></v-list-item-icon>
                    <v-list-item-content>
                      <v-list-item-subtitle @click="addEmailAddress">
                        <v-icon left>{{getIcon('mdi-plus-circle')}}</v-icon>
                        Add email address
                      </v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                  <v-divider inset v-if="emailAddressList.length > 0 || editMode"></v-divider>
                  <div v-if="showMore">
                    <v-list-item v-for="ipAddress in ipAddressList"
                                 :key="ipAddress.contactMech.contactMechId">
                      <v-list-item-icon>
                        <v-tooltip top>
                          <template v-slot:activator="{ on }">
                            <v-icon left v-on="on">{{getIcon('mdi-desktop-tower')}}</v-icon>
                          </template>
                          <span>IP address</span>
                        </v-tooltip>
                      </v-list-item-icon>
                      <v-list-item-content>
                        <v-list-item-title v-if="!editMode">
                          {{ipAddress.contactMech.infoString}}
                        </v-list-item-title>
                        <v-list-item-title v-if="editMode">
                          <v-row>
                            <v-col>
                              <v-text-field hide-details label="IP address"
                                            v-model="ipAddress.contactMech.infoString"></v-text-field>
                            </v-col>
                          </v-row>
                        </v-list-item-title>
                        <v-list-item-subtitle v-if="ipAddress.partyContactMechPurposes.length > 0">
                          <v-chip class="primary mr-2" x-small v-for="purpose in ipAddress.partyContactMechPurposes"
                                  :key="purpose.contactMechId + '-' + purpose.contactMechPurpostTypeId">
                            {{purpose.contactMechPurposeTypeId}}
                          </v-chip>
                        </v-list-item-subtitle>
                      </v-list-item-content>
                      <v-list-item-action v-if="editMode">
                        <v-btn icon @click="removeContactMech(ipAddress)">
                          <v-icon color="red">{{getIcon('mdi-delete')}}</v-icon>
                        </v-btn>
                      </v-list-item-action>
                    </v-list-item>
                  </div>
                  <v-list-item v-if="editMode && showMore">
                    <v-list-item-icon></v-list-item-icon>
                    <v-list-item-content>
                      <v-list-item-subtitle @click="addIpAddress">
                        <v-icon left>{{getIcon('mdi-plus-circle')}}</v-icon>
                        Add IP address
                      </v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                  <v-divider inset v-if="(ipAddressList.length > 0 && showMore) || (editMode && showMore)"></v-divider>
                  <div v-if="showMore">
                    <v-list-item v-for="domainName in domainNameList"
                                 :key="domainName.contactMech.contactMechId">
                      <v-list-item-icon>
                        <v-tooltip top>
                          <template v-slot:activator="{ on }">
                            <v-icon left v-on="on">{{getIcon('mdi-at')}}</v-icon>
                          </template>
                          <span>Domain Name</span>
                        </v-tooltip>
                      </v-list-item-icon>
                      <v-list-item-content>
                        <v-list-item-title v-if="!editMode">
                          {{domainName.contactMech.infoString}}
                        </v-list-item-title>
                        <v-list-item-title v-if="editMode">
                          <v-row>
                            <v-col>
                              <v-text-field hide-details label="Domain Name"
                                            v-model="domainName.contactMech.infoString"></v-text-field>
                            </v-col>
                          </v-row>
                        </v-list-item-title>
                        <v-list-item-subtitle v-if="domainName.partyContactMechPurposes.length > 0">
                          <v-chip class="primary mr-2" x-small v-for="purpose in domainName.partyContactMechPurposes"
                                  :key="purpose.contactMechId + '-' + purpose.contactMechPurpostTypeId">
                            {{purpose.contactMechPurposeTypeId}}
                          </v-chip>
                        </v-list-item-subtitle>
                      </v-list-item-content>
                      <v-list-item-action v-if="editMode">
                        <v-btn icon @click="removeContactMech(domainName)">
                          <v-icon color="red">{{getIcon('mdi-delete')}}</v-icon>
                        </v-btn>
                      </v-list-item-action>
                    </v-list-item>
                  </div>
                  <v-list-item v-if="editMode && showMore">
                    <v-list-item-icon></v-list-item-icon>
                    <v-list-item-content>
                      <v-list-item-subtitle @click="addDomainName">
                        <v-icon left>{{getIcon('mdi-plus-circle')}}</v-icon>
                        Add domain name
                      </v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                  <v-divider inset v-if="(domainNameList.length > 0 && showMore) || (showMore && editMode)"></v-divider>
                  <div v-if="showMore">
                    <v-list-item v-for="ldapAddress in ldapAddressList"
                                 :key="ldapAddress.contactMech.contactMechId">
                      <v-list-item-icon>
                        <v-tooltip top>
                          <template v-slot:activator="{ on }">
                            <v-icon left v-on="on">{{getIcon('mdi-file-cloud')}}</v-icon>
                          </template>
                          <span>LDAP address</span>
                        </v-tooltip>
                      </v-list-item-icon>
                      <v-list-item-content>
                        <v-list-item-title v-if="!editMode">
                          {{ldapAddress.contactMech.infoString}}
                        </v-list-item-title>
                        <v-list-item-title v-if="editMode">
                          <v-row>
                            <v-col>
                              <v-text-field hide-details label="LDAP address"
                                            v-model="ldapAddress.contactMech.infoString"></v-text-field>
                            </v-col>
                          </v-row>
                        </v-list-item-title>
                        <v-list-item-subtitle v-if="ldapAddress.partyContactMechPurposes.length > 0 && !editMode">
                          <v-chip class="primary mr-2" x-small v-for="purpose in ldapAddress.partyContactMechPurposes"
                                  :key="purpose.contactMechId + '-' + purpose.contactMechPurposeTypeId">
                            {{displayPurpose('LDAP_ADDRESS', purpose.contactMechPurposeTypeId)}}
                          </v-chip>
                        </v-list-item-subtitle>
                        <v-list-item-subtitle v-if="editMode">
                          <v-select
                            label="purposes"
                            v-model="ldapAddress.purposes"
                            :items="purposeListByType.LDAP_ADDRESS"
                            deletable-chips
                            chips
                            hide-selected
                            multiple
                            item-text="description"
                            item-value="contactMechPurposeTypeId">
                          </v-select>
                        </v-list-item-subtitle>
                      </v-list-item-content>
                      <v-list-item-action v-if="editMode">
                        <v-btn icon @click="removeContactMech(ldapAddress)">
                          <v-icon color="red">{{getIcon('mdi-delete')}}</v-icon>
                        </v-btn>
                      </v-list-item-action>
                    </v-list-item>
                  </div>
                  <v-list-item v-if="editMode && showMore">
                    <v-list-item-icon></v-list-item-icon>
                    <v-list-item-content>
                      <v-list-item-subtitle @click="addLdapAddress">
                        <v-icon left>{{getIcon('mdi-plus-circle')}}</v-icon>
                        Add LDAP address
                      </v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                  <v-divider inset
                             v-if="(ldapAddressList.length > 0 && showMore) || (showMore && editMode)"></v-divider>
                </v-list>
              </v-col>
              <v-col cols="12" md="6" align-self="start">
                <v-list dense sel-label="postalAddr">
                  <v-list-item v-for="postalAddress in postalAddressList"
                               :key="postalAddress.contactMech.contactMechId">
                    <v-list-item-icon>
                      <v-tooltip top>
                        <template v-slot:activator="{ on }">
                          <v-icon left v-on="on">{{getIcon('mdi-map-marker')}}</v-icon>
                        </template>
                        <span>Postal address</span>
                      </v-tooltip>
                    </v-list-item-icon>
                    <v-list-item-content v-if="!editMode">
                      <v-list-item-title>
                        {{postalAddress.postalAddress.toName}} {{postalAddress.postalAddress.attnName}}
                      </v-list-item-title>
                      <div>
                        {{postalAddress.postalAddress.address1}}
                      </div>
                      <div>
                        {{postalAddress.postalAddress.address2}}
                      </div>
                      <div>
                        {{postalAddress.postalAddress.city}}, {{postalAddress.postalAddress.postalCode}}
                      </div>
                      <v-list-item-subtitle v-if="postalAddress.partyContactMechPurposes.length > 0 && !editMode">
                        <v-chip class="primary mr-2" x-small v-for="purpose in postalAddress.partyContactMechPurposes"
                                :key="purpose.contactMechId + '-' + purpose.contactMechPurposeTypeId">
                          {{displayPurpose('POSTAL_ADDRESS', purpose.contactMechPurposeTypeId)}}
                        </v-chip>
                      </v-list-item-subtitle>
                    </v-list-item-content>
                    <v-list-item-content v-if="editMode">
                      <v-form class="ml-3" :lazy-validator="lazy">
                        <v-row>
                          <v-text-field hide-details name="toName" label="To Name"
                                        :rules="forms.postalAddress.rules.toName"
                                        v-model="postalAddress.postalAddress.toName" class="mr-4"></v-text-field>
                          <v-text-field hide-details name="attentionName" label="Attention Name"
                                        :rules="forms.postalAddress.rules.attentionName"
                                        v-model="postalAddress.postalAddress.attnName"></v-text-field>
                        </v-row>
                        <v-row>
                          <v-text-field hide-details name="addressLine1" label="Address Line 1 *"
                                        :rules="forms.postalAddress.rules.addressLine1"
                                        v-model="postalAddress.postalAddress.address1"></v-text-field>
                        </v-row>
                        <v-row>
                          <v-text-field hide-details name="addressLine2" label="Address Line 2"
                                        :rules="forms.postalAddress.rules.addressLine2"
                                        v-model="postalAddress.postalAddress.address2"></v-text-field>
                        </v-row>
                        <v-row>
                          <v-text-field hide-details name="city" label="City *"
                                        v-model="postalAddress.postalAddress.city"
                                        :rules="forms.postalAddress.rules.city" class="mr-4"></v-text-field>
                          <v-text-field hide-details name="zipPostalCode" label="Zip/Postal Code *"
                                        v-model="postalAddress.postalAddress.postalCode"
                                        :rules="forms.postalAddress.rules.zipPostalCode" class="mr-4"></v-text-field>
                        </v-row>
                        <v-list-item-subtitle v-if="editMode">
                          <v-select
                            label="purposes"
                            v-model="postalAddress.purposes"
                            :items="purposeListByType.POSTAL_ADDRESS"
                            deletable-chips
                            chips
                            hide-selected
                            multiple
                            item-text="description"
                            item-value="contactMechPurposeTypeId">
                          </v-select>
                        </v-list-item-subtitle>
                      </v-form>
                    </v-list-item-content>
                    <v-list-item-action v-if="editMode">
                      <v-btn icon @click="removeContactMech(postalAddress)">
                        <v-icon color="red">{{getIcon('mdi-delete')}}</v-icon>
                      </v-btn>
                    </v-list-item-action>
                  </v-list-item>
                  <v-list-item v-if="editMode">
                    <v-list-item-icon></v-list-item-icon>
                    <v-list-item-content>
                      <v-list-item-subtitle sel-label="addPostalAddr" @click="addPostalAddress">
                        <v-icon left>{{getIcon('mdi-plus-circle')}}</v-icon>
                        Add postal address
                      </v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                  <v-divider inset v-if="postalAddressList.length > 0"></v-divider>
                  <span v-if="showMore">
              <v-list-item v-for="internalNote in internalPartyIdList"
                           :key="internalNote.contactMech.contactMechId">
                <v-list-item-icon>
                  <v-tooltip top>
                    <template v-slot:activator="{ on }">
                      <v-icon left v-on="on">{{getIcon('mdi-note-text')}}</v-icon>
                    </template>
                    <span>Internal note</span>
                  </v-tooltip>
                </v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title v-if="!editMode">
                    {{internalNote.contactMech.infoString}}
                  </v-list-item-title>
                  <v-list-item-title v-if="editMode">
                    <v-row>
                      <v-col>
                        <v-text-field hide-details label="Internal note"
                                      v-model="internalNote.contactMech.infoString"></v-text-field>
                      </v-col>
                    </v-row>
                  </v-list-item-title>
                  <v-list-item-subtitle v-if="internalNote.partyContactMechPurposes.length > 0">
                    <v-chip class="primary mr-2" x-small v-for="purpose in internalNote.partyContactMechPurposes"
                            :key="purpose.contactMechId + '-' + purpose.contactMechPurpostTypeId">
                          {{purpose.contactMechPurposeTypeId}}
                    </v-chip>
                  </v-list-item-subtitle>
                </v-list-item-content>
                <v-list-item-action v-if="editMode">
                      <v-btn icon @click="removeContactMech(internalNote)">
                        <v-icon color="red">{{getIcon('mdi-delete')}}</v-icon>
                      </v-btn>
                    </v-list-item-action>
              </v-list-item>
                    <v-list-item v-if="editMode">
                    <v-list-item-icon></v-list-item-icon>
                    <v-list-item-content>
                      <v-list-item-subtitle @click="addInternalPartyId">
                        <v-icon left>{{getIcon('mdi-plus-circle')}}</v-icon>
                        Add internal note
                      </v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                    <v-divider inset v-if="(internalPartyIdList.length > 0 && showMore) || (editMode && showMore)"></v-divider>
                    <div v-if="showMore">
                  <v-list-item v-for="webAddress in webAddressList"
                               :key="webAddress.contactMech.contactMechId">
                  <v-list-item-icon>
                    <v-tooltip top>
                      <template v-slot:activator="{ on }">
                        <v-icon left v-on="on">{{getIcon('mdi-web')}}</v-icon>
                      </template>
                      <span>Web address</span>
                    </v-tooltip>
                  </v-list-item-icon>
                  <v-list-item-content>
                    <v-list-item-title v-if="!editMode">
                      {{webAddress.contactMech.infoString}}
                    </v-list-item-title>
                    <v-list-item-title v-if="editMode">
                      <v-row>
                        <v-col>
                          <v-text-field hide-details label="Web Address"
                                        v-model="webAddress.contactMech.infoString"></v-text-field>
                        </v-col>
                      </v-row>
                    </v-list-item-title>
                    <v-list-item-subtitle v-if="webAddress.partyContactMechPurposes.length > 0 && !editMode">
                          <v-chip class="primary mr-2" x-small v-for="purpose in webAddress.partyContactMechPurposes"
                                  :key="purpose.contactMechId + '-' + purpose.contactMechPurposeTypeId">
                            {{displayPurpose('WEB_ADDRESS', purpose.contactMechPurposeTypeId)}}
                          </v-chip>
                        </v-list-item-subtitle>
                        <v-list-item-subtitle v-if="editMode">
                          <v-select
                            label="purposes"
                            v-model="webAddress.purposes"
                            :items="purposeListByType.WEB_ADDRESS"
                            deletable-chips
                            chips
                            hide-selected
                            multiple
                            item-text="description"
                            item-value="contactMechPurposeTypeId">
                          </v-select>
                        </v-list-item-subtitle>
                  </v-list-item-content>
                    <v-list-item-action v-if="editMode">
                        <v-btn icon @click="removeContactMech(webAddress)">
                          <v-icon color="red">{{getIcon('mdi-delete')}}</v-icon>
                        </v-btn>
                      </v-list-item-action>
                </v-list-item>
                    </div>
                    <v-list-item v-if="editMode">
                    <v-list-item-icon></v-list-item-icon>
                    <v-list-item-content>
                      <v-list-item-subtitle @click="addWebAddress">
                        <v-icon left>{{getIcon('mdi-plus-circle')}}</v-icon>
                        Add web address
                      </v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                    <v-divider inset v-if="(webAddressList.length > 0 && showMore) || (editMode && showMore)"></v-divider>
                <v-list-item v-for="ftpAddress in ftpAddressList"
                             :key="ftpAddress.contactMech.contactMechId">
                <v-list-item-icon>
                  <v-tooltip top>
                    <template v-slot:activator="{ on }">
                      <v-icon left v-on="on">{{getIcon('mdi-server')}}</v-icon>
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
                        <v-icon color="red">{{getIcon('mdi-delete')}}</v-icon>
                      </v-btn>
                    </v-list-item-action>
              </v-list-item>
              <v-list-item v-if="editMode">
                <v-list-item-icon></v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-subtitle @click="addFtpAddress">
                    <v-icon left>{{getIcon('mdi-plus-circle')}}</v-icon>
                    Add FTP address
                  </v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
                    <v-divider inset v-if="(ftpAddressList.length > 0 && showMore) || (editMode && showMore)"></v-divider>
            </span>
                </v-list>
              </v-col>
            </v-row>
            <v-row justify="center">
              <v-btn sel-label="Show more" text @click="toggleShowMore" v-if="!showMore">Show more</v-btn>
              <v-btn sel-label="Show less" text @click="toggleShowMore" v-if="showMore">Show less</v-btn>
            </v-row>
          </v-card-text>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>

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
  const getContactmechPurposeTypeUrl = '/partymgrfjs/control/getContactmechPurposeType'
  const createPartyContactMechPurposeUrl = '/partymgrfjs/control/createPartyContactMechPurpose'
  const expirePartyContactMechPurposeUrl = '/partymgrfjs/control/expirePartyContactMechPurpose'

  import icons from '../../js/icons'

  export default {
    name: "ContactMech",
    components: {
    },
    props: ['props', 'updateStore'],
    data() {
      return {
        dataSet: {},
        toCreate: [],
        toDelete: [],
        editMode: false,
        showMore: false,
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
        if (this.showMore) {
          return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps, ...this.toCreate].filter(contact => contact.contactMech.contactMechTypeId === type) : []
        }
        return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps, ...this.toCreate].filter(contact => contact.contactMech.contactMechTypeId === type).splice(0, 1) : []
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
                console.log('Error during contactMech acquisition')
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
          },
          () => {
            console.log('Error during contactMech creation')
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
          () => {
            console.log('Error during postalAddress creation')
          }
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
          },
          () => {
            console.log('Error during contactMech creation')
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
          },
          () => {
            console.log('Error during email address creation')
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
          },
          () => {
            console.log('Error during contactMech creation')
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
          },
          () => {
            console.log('Error during contactMech creation')
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
          },
          () => {
            console.log('Error during contactMech creation')
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
          },
          () => {
            console.log('Error during internal note creation')
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
          },
          () => {
            console.log('Error during FTP Address creation')
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
          },
          () => {
            console.log('Error during internal note creation')
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
                      console.log('Error during contactMech update.')
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
                      console.log('Error during contactMech update.')
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
                      console.log('Error during contactMech update.')
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
                      console.log('Error during contactMech update.')
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
                      console.log('Error during contactMech update.')
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
                      console.log('Error during postal address creation')
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
                      console.log('Error during telecom number creation')
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
                      console.log('Error during email address creation')
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
                      console.log('Error during FTP address creation')
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
                      console.log('Error during contactMech creation')
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
                  console.log('Error during contactMech deletion')
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
                        error => {
                          console.log(error)
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
                        error => {
                          console.log(error)
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
        this.$http.post(getContactmechPurposeTypeUrl, {
          contactMechTypeId: type
        }).then(response => {
          this.purposeListByType[type] = response.body.purposeTypeList
        }, error => {
          console.log('Error during purposes acquisition...', error)
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
