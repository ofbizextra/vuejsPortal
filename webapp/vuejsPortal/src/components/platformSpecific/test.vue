<template>
  <v-container grid-list-xs text-center>
    <v-layout wrap justify-space-around v-if="cardMode">
      <v-flex text-left stretch xs12>
        <v-card>
          <v-toolbar flat v-if="!editMode">
            <v-toolbar-title>Contact mech</v-toolbar-title>
            <div class="flex-grow-1"></div>
            <v-btn icon @click="toggleEdit">
              <v-icon>mdi-pencil</v-icon>
            </v-btn>
          </v-toolbar>
          <v-toolbar flat v-if="editMode">
            <v-btn icon @click="toggleEdit">
              <v-icon>mdi-arrow-left</v-icon>
            </v-btn>
            <v-toolbar-title>Edit contact mech</v-toolbar-title>
            <div class="flex-grow-1"></div>
            <v-btn icon @click="updateAll">
              <v-icon>mdi-check</v-icon>
            </v-btn>
          </v-toolbar>
          <v-divider></v-divider>
          <v-card-text>
            <v-row stretch dense>
              <v-col cols="12" md="6" align-self="start">
                <v-list dense>
                  <v-list-item v-for="phoneNumber in telecomNumberList"
                               :key="phoneNumber.contactMech.contactMechId">
                    <v-list-item-icon>
                      <v-icon left>mdi-phone</v-icon>
                    </v-list-item-icon>
                    <v-list-item-content>
                      <v-list-item-title v-if="!editMode">
                        {{`${phoneNumber.telecomNumber.countryCode || ''} ${phoneNumber.telecomNumber.areaCode || ''}
                        ${phoneNumber.telecomNumber.contactNumber || ''} ${phoneNumber.telecomNumber.extension || ''}`}}
                      </v-list-item-title>
                      <v-list-item-title v-if="editMode">
                        <v-row>
                          <v-col class="col-3">
                            <v-text-field hide-details label="Country code"
                                          v-model="phoneNumber.telecomNumber.countryCode"></v-text-field>
                          </v-col>
                          <v-col class="col-9">
                            <v-text-field hide-details label="Number"
                                          v-model="phoneNumber.telecomNumber.contactNumber"></v-text-field>
                          </v-col>
                        </v-row>
                      </v-list-item-title>
                      <v-list-item-subtitle v-if="phoneNumber.partyContactMechPurposes.length > 0">
                        <v-chip class="primary mr-2" x-small v-for="purpose in phoneNumber.partyContactMechPurposes"
                                :key="purpose.contactMechId + '-' + purpose.contactMechPurposeTypeId">
                          {{purpose.contactMechPurposeTypeId}}
                        </v-chip class="primary" x-small>
                      </v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                  <v-list-item v-if="editMode">
                    <v-list-item-icon></v-list-item-icon>
                    <v-list-item-content>
                      <v-list-item-subtitle @click="addTelecomNumber">
                        <v-icon left>mdi-plus-circle</v-icon>
                        Add
                      </v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                  <v-divider inset></v-divider>
                  <v-list-item v-for="email in contactsByType('EMAIL_ADDRESS')"
                               :key="email.contactMech.contactMechId">
                    <v-list-item-icon>
                      <v-icon left>mdi-at</v-icon>
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
                      <v-list-item-subtitle v-if="email.partyContactMechPurposes.length > 0">
                        <v-chip class="primary mr-2" x-small v-for="purpose in email.partyContactMechPurposes"
                                :key="purpose.contactMechId + '-' + purpose.contactMechPurpostTypeId">
                          {{purpose.contactMechPurposeTypeId}}
                        </v-chip class="primary" x-small>
                      </v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                  <v-list-item v-for="ipAddress in contactsByType('IP_ADDRESS')"
                               :key="ipAddress.contactMech.contactMechId" v-if="showMore">
                    <v-list-item-icon>
                      <v-icon left>mdi-web</v-icon>
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
                        </v-chip class="primary" x-small>
                      </v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                  <v-list-item v-for="domainName in contactsByType('DOMAIN_NAME')"
                               :key="domainName.contactMech.contactMechId" v-if="showMore">
                    <v-list-item-icon>
                      <v-icon left>mdi-web</v-icon>
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
                        </v-chip class="primary" x-small>
                      </v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                  <v-list-item v-for="ldapAddress in contactsByType('LDAP_ADDRESS')"
                               :key="ldapAddress.contactMech.contactMechId" v-if="showMore">
                    <v-list-item-icon>
                      <v-icon left>mdi-at</v-icon>
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
                      <v-list-item-subtitle v-if="ldapAddress.partyContactMechPurposes.length > 0">
                        <v-chip class="primary mr-2" x-small v-for="purpose in ldapAddress.partyContactMechPurposes"
                                :key="purpose.contactMechId + '-' + purpose.contactMechPurpostTypeId">
                          {{purpose.contactMechPurposeTypeId}}
                        </v-chip class="primary" x-small>
                      </v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                </v-list>
              </v-col>
              <v-col cols="12" md="6" align-self="start">
                <v-list dense>
                  <v-list-item v-for="postalAddress in contactsByType('POSTAL_ADDRESS')"
                               :key="postalAddress.contactMech.contactMechId">
                    <v-list-item-icon>
                      <v-icon left>mdi-mailbox</v-icon>
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
                      <v-list-item-subtitle v-if="postalAddress.partyContactMechPurposes.length > 0">
                        <v-chip class="primary mr-2" x-small v-for="purpose in postalAddress.partyContactMechPurposes"
                                :key="purpose.contactMechId + '-' + purpose.contactMechPurpostTypeId">
                          {{purpose.contactMechPurposeTypeId}}
                        </v-chip class="primary" x-small>
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
                      </v-form>
                    </v-list-item-content>
                  </v-list-item>
                  <span v-if="showMore">
              <v-divider inset></v-divider>
              <v-list-item v-for="internalNote in contactsByType('INTERNAL_PARTYID')"
                           :key="internalNote.contactMech.contactMechId">
                <v-list-item-icon>
                  <v-icon left>mdi-note</v-icon>
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
                    </v-chip class="primary" x-small>
                  </v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
                <v-list-item v-for="webAddress in contactsByType('WEB_ADDRESS')"
                             :key="webAddress.contactMech.contactMechId" v-if="showMore">
                <v-list-item-icon>
                  <v-icon left>mdi-web</v-icon>
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
                  <v-list-item-subtitle v-if="webAddress.partyContactMechPurposes.length > 0">
                    <v-chip class="primary mr-2" x-small v-for="purpose in webAddress.partyContactMechPurposes"
                            :key="purpose.contactMechId + '-' + purpose.contactMechPurpostTypeId">
                      {{purpose.contactMechPurposeTypeId}}
                    </v-chip class="primary" x-small>
                  </v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
                <v-list-item v-for="ftpAddress in contactsByType('FTP_ADDRESS')"
                             :key="ftpAddress.contactMech.contactMechId">
                <v-list-item-icon>
                  <v-icon left>mdi-mailbox</v-icon>
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
                    <v-row inset class="justify-space-around">
                      <v-switch small label="binary" :disabled="!editMode"
                                v-model="ftpAddress.ftpAddress.binaryTransfer"
                                true-value="Y" false-value="N"></v-switch>
                      <v-switch small label="zip" :disabled="!editMode" v-model="ftpAddress.ftpAddress.zipFile"
                                true-value="Y" false-value="N"></v-switch>
                      <v-switch small label="passive" :disabled="!editMode" v-model="ftpAddress.ftpAddress.passiveMode"
                                true-value="Y" false-value="N"></v-switch>
                    </v-row>
                  </div>
                  <v-list-item-subtitle v-if="ftpAddress.partyContactMechPurposes.length > 0">
                    <v-chip class="primary mr-2" x-small v-for="purpose in ftpAddress.partyContactMechPurposes"
                            :key="purpose.contactMechId + '-' + purpose.contactMechPurpostTypeId">
                      {{purpose.contactMechPurposeTypeId}}
                    </v-chip class="primary" x-small>
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
                    <v-row justify="space-around">
                      <v-switch name="binaryTransfer" label="Binary Transfert" class="mr-4" trueValue="Y" falseValue="N"
                                :rules="forms.ftpAddress.rules.binaryTransfer"
                                v-model="ftpAddress.ftpAddress.binaryTransfer"></v-switch>
                      <v-switch name="zipFile" label="File compression" class="mr-4" trueValue="Y" falseValue="N"
                                :rules="forms.ftpAddress.rules.zipFile"
                                v-model="ftpAddress.ftpAddress.zipFile"></v-switch>
                      <v-switch name="passiveMode" label="Passive mode" trueValue="Y" falseValue="N"
                                :rules="forms.ftpAddress.rules.passiveMode"
                                v-model="ftpAddress.ftpAddress.passiveMode"></v-switch>
                    </v-row>
                  </v-form>
                </v-list-item-content>
              </v-list-item>
            </span>
                </v-list>
              </v-col>
            </v-row>
            <v-row justify="center">
              <v-btn text @click="toggleShowMore" v-if="!showMore">Show more</v-btn>
              <v-btn text @click="toggleShowMore" v-if="showMore">Show less</v-btn>
            </v-row>
          </v-card-text>
        </v-card>
      </v-flex>
    </v-layout>
    <v-layout wrap justify-space-around v-else>
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
        <WebAddress :props="contactsByType('WEB_ADDRESS')" @update="updateDataSet"
                    v-if="countContactsByType('WEB_ADDRESS') > 0"></WebAddress>
        <InternalNote :props="contactsByType('INTERNAL_PARTYID')" @update="updateDataSet"
                      v-if="countContactsByType('INTERNAL_PARTYID') > 0"></InternalNote>
        <FtpAddress :props="contactsByType('FTP_ADDRESS')" @update="updateDataSet"
                    v-if="countContactsByType('FTP_ADDRESS') > 0"></FtpAddress>
        <LdapAddress :props="contactsByType('LDAP_ADDRESS')" @update="updateDataSet"
                     v-if="countContactsByType('LDAP_ADDRESS') > 0"></LdapAddress>
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
              <v-form ref="createWebAddress" :lazy-validator="lazy" v-model="forms.webAddress.valid"
                      v-on:submit.prevent="createWebAddress">
                <v-row>
                  <v-text-field name="webAddress" label="Web URL?Address" :rules="forms.webAddress.rules.webAddress"
                                v-model="forms.webAddress.fields.webAddress"></v-text-field>
                </v-row>
                <v-row justify="end">
                  <v-switch name="allowSolicitation" label="Allow Solicitation?" class="ma-2"
                            v-model="forms.webAddress.fields.allowSolicitation" trueValue="Y"
                            falseValue="N"></v-switch>
                  <v-btn color="#2196F3" class="ma-2" :disabled="!forms.webAddress.valid"
                         @click.native="createWebAddress">Create
                  </v-btn>
                </v-row>
              </v-form>
            </v-expansion-panel-content>
          </v-expansion-panel>
          <v-expansion-panel>
            <v-expansion-panel-header>
              Internal Party Note
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              <v-form ref="createInternalNote" :lazy-validator="lazy" v-model="forms.internalNote.valid"
                      v-on:submit.prevent="createInternalNote">
                <v-row>
                  <v-text-field name="internalNote" label="Internal Party Note"
                                :rules="forms.internalNote.rules.internalNote"
                                v-model="forms.internalNote.fields.internalNote"></v-text-field>
                </v-row>
                <v-row justify="end">
                  <v-switch name="allowSolicitation" label="Allow Solicitation?" class="ma-2"
                            v-model="forms.internalNote.fields.allowSolicitation" trueValue="Y"
                            falseValue="N"></v-switch>
                  <v-btn color="#2196F3" class="ma-2" :disabled="!forms.internalNote.valid"
                         @click.native="createInternalNote">Create
                  </v-btn>
                </v-row>
              </v-form>
            </v-expansion-panel-content>
          </v-expansion-panel>
          <v-expansion-panel>
            <v-expansion-panel-header>
              File Server
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              <v-form ref="createFtpAddress" :lazy-validator="lazy" v-model="forms.ftpAddress.valid"
                      v-on:submit.prevent="createFtpAddress">
                <v-row>
                  <v-text-field name="hostname" label="Host name" class="mr-4" :rules="forms.ftpAddress.rules.hostname"
                                v-model="forms.ftpAddress.fields.hostname"></v-text-field>
                  <v-text-field name="port" label="Port" class="" :rules="forms.ftpAddress.rules.port"
                                v-model="forms.ftpAddress.fields.port"></v-text-field>
                </v-row>
                <v-row>
                  <v-text-field name="username" label="User Name" class="mr-4" :rules="forms.ftpAddress.rules.username"
                                v-model="forms.ftpAddress.fields.username"></v-text-field>
                  <v-text-field name="ftpPassword" label="Password" :rules="forms.ftpAddress.rules.ftpPassword"
                                v-model="forms.ftpAddress.fields.ftpPassword"></v-text-field>
                </v-row>
                <v-row>
                  <v-text-field name="filePath" label="Path" class="mr-4" :rules="forms.ftpAddress.rules.filePath"
                                v-model="forms.ftpAddress.fields.filePath"></v-text-field>
                  <v-text-field name="defaultTimeout" label="Path default timeout" class=""
                                :rules="forms.ftpAddress.rules.defaultTimeout"
                                v-model="forms.ftpAddress.fields.defaultTimeout"></v-text-field>
                </v-row>
                <v-row justify="space-around">
                  <v-switch name="binaryTransfer" label="Binary Transfert" class="mr-4" trueValue="Y" falseValue="N"
                            :rules="forms.ftpAddress.rules.binaryTransfer"
                            v-model="forms.ftpAddress.fields.binaryTransfer"></v-switch>
                  <v-switch name="zipFile" label="File compression" class="mr-4" trueValue="Y" falseValue="N"
                            :rules="forms.ftpAddress.rules.zipFile"
                            v-model="forms.ftpAddress.fields.zipFile"></v-switch>
                  <v-switch name="passiveMode" label="Passive mode" trueValue="Y" falseValue="N"
                            :rules="forms.ftpAddress.rules.passiveMode"
                            v-model="forms.ftpAddress.fields.passiveMode"></v-switch>
                </v-row>
                <v-row justify="end">
                  <v-switch name="allowSolicitation" label="Allow Solicitation?" class="ma-2"
                            v-model="forms.ftpAddress.fields.allowSolicitation" trueValue="Y"
                            falseValue="N"></v-switch>
                  <v-btn color="#2196F3" class="ma-2" :disabled="!forms.ftpAddress.valid"
                         @click.native="createFtpAddress">Create
                  </v-btn>
                </v-row>
              </v-form>
            </v-expansion-panel-content>
          </v-expansion-panel>
          <v-expansion-panel>
            <v-expansion-panel-header>
              LDAP Address
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              <v-form ref="createLdapAddress" :lazy-validator="lazy" v-model="forms.internalNote.valid"
                      v-on:submit.prevent="createLdapAddress">
                <v-row>
                  <v-text-field name="LdapAddress" label="LDAP Address" :rules="forms.ldapAddress.rules.ldapAddress"
                                v-model="forms.ldapAddress.fields.ldapAddress"></v-text-field>
                </v-row>
                <v-row justify="end">
                  <v-switch name="allowSolicitation" label="Allow Solicitation?" class="ma-2"
                            v-model="forms.ldapAddress.fields.allowSolicitation" trueValue="Y"
                            falseValue="N"></v-switch>
                  <v-btn color="#2196F3" class="ma-2" :disabled="!forms.ldapAddress.valid"
                         @click.native="createLdapAddress">Create
                  </v-btn>
                </v-row>
              </v-form>
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
  import WebAddress from './test/WebAddress'
  import InternalNote from './test/InternalNote'
  import FtpAddress from './test/FtpAddress'
  import LdapAddress from './test/LdapAddress'

  const getContactMechUrl = 'https://localhost:8443/partymgrapi/control/getcontactmech'
  const createEmailAddressUrl = 'https://localhost:8443/partymgrapi/control/createEmailAddress'
  const createPostalAddressUrl = 'https://localhost:8443/partymgrapi/control/createPostalAddress'
  const createElectroniclAddressUrl = 'https://localhost:8443/partymgrapi/control/createElectronicAddress'
  const createTelecomNumberUrl = 'https://localhost:8443/partymgrapi/control/createTelecomNumber'
  const createContactMech = 'https://localhost:8443/partymgrapi/control/createContactMech'
  const createFtpAddressUrl = 'https://localhost:8443/partymgrapi/control/createFtpAddress'
  const updateContactMechUrl = 'https://localhost:8443/partymgrapi/control/updateContactMech'
  const updatePostalAddressUrl = 'https://localhost:8443/partymgrapi/control/updatePostalAddress'
  const updateTelecomNumberUrl = 'https://localhost:8443/partymgrapi/control/updateTelecomNumber'
  const updateEmailAddressUrl = 'https://localhost:8443/partymgrapi/control/updateEmailAddress'
  const updateFtpAddressUrl = 'https://localhost:8443/partymgrapi/control/updateFtpAddress'

  export default {
    name: "test",
    components: {
      ElectronicAddress,
      PostalAddress,
      TelecomNumber,
      EmailAddress,
      IpAddress,
      DomainName,
      WebAddress,
      InternalNote,
      FtpAddress,
      LdapAddress
    },
    props: ['props', 'updateStore'],
    data() {
      return {
        dataSet: {},
        toCreate: [],
        cardMode: true,
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
      telecomNumberList() {
        if (this.showMore) {
          return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps, ...this.toCreate].filter(contact => contact.contactMech.contactMechTypeId === 'TELECOM_NUMBER') : []
        }
        return this.dataSet.hasOwnProperty('valueMaps') ? [...this.dataSet.valueMaps.filter(contact => contact.contactMech.contactMechTypeId === 'TELECOM_NUMBER').splice(0, 1), ...this.toCreate.filter(contact => contact.contactMech.contactMechTypeId === 'TELECOM_NUMBER')] : []
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
      updateDataSet() {
        return new Promise((resolve, reject) => {
          setTimeout(() => {
            this.$http.post(getContactMechUrl, {partyId: 'DemoLead3'}).then(
              result => {
                this.dataSet = result.body
                resolve()
              },
              error => {
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
      },
      createWebAddress() {
        this.$http.post(createContactMech, {
          contactMechTypeId: 'WEB_ADDRESS',
          partyId: 'DemoLead3',
          infoString: this.forms.webAddress.fields.webAddress,
          allowSolicitation: this.forms.webAddress.fields.allowSolicitation
        }).then(
          result => {
            this.updateDataSet()
          },
          error => {
            console.log('Error during contactMech creation')
          }
        )
      },
      createInternalNote() {
        this.$http.post(createContactMech, {
          contactMechTypeId: 'INTERNAL_PARTYID',
          partyId: 'DemoLead3',
          infoString: this.forms.internalNote.fields.internalNote,
          allowSolicitation: this.forms.internalNote.fields.allowSolicitation
        }).then(
          result => {
            this.updateDataSet()
          },
          error => {
            console.log('Error during internal note creation')
          }
        )
      },
      createFtpAddress() {
        this.$http.post(createFtpAddressUrl, {
          contactMechTypeId: 'FTP_ADDRESS',
          partyId: 'DemoLead3',
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
          result => {
            this.updateDataSet()
          },
          error => {
            console.log('Error during FTP Address creation')
          }
        )
      },
      createLdapAddress() {
        this.$http.post(createContactMech, {
          contactMechTypeId: 'LDAP_ADDRESS',
          partyId: 'DemoLead3',
          infoString: this.forms.ldapAddress.fields.ldapAddress,
          allowSolicitation: this.forms.ldapAddress.fields.allowSolicitation
        }).then(
          result => {
            this.updateDataSet()
          },
          error => {
            console.log('Error during internal note creation')
          }
        )
      },
      addEmailAddress() {

      },
      addTelecomNumber() {
        this.toCreate.push({
          contactMech: {
            partyId: 'DemoLead3',
            contactMechTypeId: 'TELECOM_NUMBER'
          },
          partyContactMechPurposes: [],
          telecomNumber: {
            contactNumber: '',
            countryCode: ''
          }
        })
      },
      addPostalAddress() {

      },
      addIpAddress() {

      },
      addWebAddress() {

      },
      addDomainName() {

      },
      addInternalNote() {

      },
      addFtpAddress() {

      },
      addLdapAddress() {

      },
      toggleEdit() {
        this.editMode = !this.editMode
      },
      toggleShowMore() {
        this.showMore = !this.showMore
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
                    partyId: 'DemoLead3',
                    toName: contactMech.postalAddress.toName,
                    attnName: contactMech.postalAddress.attnName,
                    address1: contactMech.postalAddress.address1,
                    address2: contactMech.postalAddress.address2,
                    city: contactMech.postalAddress.city,
                    postalCode: contactMech.postalAddress.postalCode
                  }).then(
                    result => {
                      resolve()
                    },
                    error => {
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
                    partyId: 'DemoLead3',
                    countryCode: contactMech.telecomNumber.countryCode,
                    contactNumber: contactMech.telecomNumber.contactNumber
                  }).then(
                    result => {
                      resolve()
                    },
                    error => {
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
                    partyId: 'DemoLead3',
                    emailAddress: contactMech.contactMech.infoString
                  }).then(
                    result => {
                      resolve()
                    },
                    error => {
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
                    partyId: 'DemoLead3',
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
                    result => {
                      resolve()
                    },
                    error => {
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
                    partyId: 'DemoLead3',
                    infoString: contactMech.contactMech.infoString
                  }).then(
                    result => {
                      resolve()
                    },
                    error => {
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
              break
            case 'TELECOM_NUMBER':
              // do creation
              promises.push(new Promise((resolve, reject) => {
                setTimeout(() => {
                  this.$http.post(createTelecomNumberUrl, {
                    contactMechTypeId: 'TELECOM_NUMBER',
                    partyId: 'DemoLead3',
                    countryCode: contactMech.telecomNumber.countryCode,
                    contactNumber: contactMech.telecomNumber.contactNumber,
                  }).then(
                    result => {
                      resolve()
                    },
                    error => {
                      console.log('Error during telecom number creation')
                      reject()
                    }
                  )
                }, 0)
              }))
              break
            case 'EMAIL_ADDRESS':
              // do creation
              break
            case 'FTP_ADDRESS':
              // do creation
              break
            default:
              // do creation
              break
          }
        }
        this.toggleEdit()
        this.toCreate = []
        Promise.all(promises).then(() => {
          this.updateDataSet()
        })
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
