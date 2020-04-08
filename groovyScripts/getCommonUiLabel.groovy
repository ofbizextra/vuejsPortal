/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.ofbiz.base.util.UtilProperties
import org.apache.ofbiz.base.util.Debug

uiLabelMap = UtilProperties.getResourceBundleMap("CommonUiLabels", locale)

//Debug.logInfo("DEBUG request: ${request}", "FindContactMechPuroseType.groovy")
vuejsUiLabel = [:]
vuejsUiLabel.expandToolTip   = uiLabelMap["CommonExpand"]   // screenletExpand
vuejsUiLabel.collapseToolTip = uiLabelMap["CommonCollapse"] // screenletCollapse
vuejsUiLabel.ofLabel         = uiLabelMap["CommonOf"]       // pagination
vuejsUiLabel.TELECOM_NUMBER = "TELECOM_NUMBER"       // pagination
vuejsUiLabel.EMAIL_ADDRESS = "EMAIL_ADDRESS"       // pagination
vuejsUiLabel.POSTAL_ADDRESS = "POSTAL_ADDRESS"       // pagination
vuejsUiLabel.IP_ADDRESS = "IP_ADDRESS"       // pagination
vuejsUiLabel.DOMAIN_NAME = "DOMAIN_NAME"       // pagination
vuejsUiLabel.LDAP_ADDRESS = "LDAP_ADDRESS"       // pagination
vuejsUiLabel.INTERNAL_PARTYID = "INTERNAL_PARTYID"       // pagination
vuejsUiLabel.WEB_ADDRESS = "WEB_ADDRESS"       // pagination
vuejsUiLabel.FTP_ADDRESS = "FTP_ADDRESS"       // pagination
vuejsUiLabel.expire = 'invalider'
request.setAttribute("commonUiLabel", vuejsUiLabel)
return "success"
