package org.apache.ofbiz.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;

public class CommonFJSServices {
    public static final String module = CommonFJSServices.class.getName();


    public static Map<String, Object> portalPageDetail(DispatchContext dctx, Map<String, ? extends Object> context) {
        final String resource = "CommonUiLabels";
        String portalPageId = (String) context.get("portalPageId");
        Map<String, Object> results = new HashMap<String, Object>();

        List<Map<String , Object>> listColumnPortlet= new ArrayList<>();
        Delegator delegator = dctx.getDelegator();

        try {

            List<GenericValue> columnGvList = EntityQuery.use(delegator).select("portalPageId", "columnSeqId", "columnWidthPixels", "columnWidthPercentage")
                                                                        .from("PortalPageColumn")
                                                                        .where("portalPageId", portalPageId)
                                                                        .orderBy("columnSeqId")
                                                                        .queryList();

            if (UtilValidate.isEmpty(columnGvList)) {
                Locale locale = (Locale) context.get("locale");
                return ServiceUtil.returnError(UtilProperties.getMessage(resource, "CommonPortalPageNotDefined", locale));
            }

            long percentage = 100; // to calculate columnWidthPercentage for the last column 
            for(GenericValue clmn : columnGvList) {
                Map<String, Object> column = new HashMap<String , Object>();
                Long columnWidthPercentage = clmn.getLong("columnWidthPercentage");
                if (columnWidthPercentage != null) {
                    percentage -= columnWidthPercentage;
                } else {
                    columnWidthPercentage = percentage;
                }
                column.putAll(clmn);
                column.put("columnWidthPercentage", columnWidthPercentage);
                String columnSeqId = clmn.getString("columnSeqId");
//                List<GenericValue> portletIdList = EntityQuery.use(delegator).from("PortalPagePortlet").where("portalPageId", portalPageId, "columnSeqId", columnSeqId).orderBy("portletSeqId").queryList();
                List<GenericValue> portletIdList = EntityQuery.use(delegator).select("portalPortletId", "portletSeqId", "watcherName")
                                                                             .from("PortalPagePortlet")
                                                                             .where("portalPageId", portalPageId, "columnSeqId", columnSeqId)
                                                                             .orderBy("sequenceNum")
                                                                             .queryList();

//                List<String> listPortlet = new ArrayList<>();
//                for (GenericValue ptltGv : portletIdList) {
//                    listPortlet.add(ptltGv.getString("portalPortletId"));
//                }
                column.put("listPortlet", portletIdList);
                listColumnPortlet.add(column);
            }
            results.put("listColumnPortlet", listColumnPortlet);
        } catch (GenericEntityException e) {
            Debug.logWarning(e, module);

            Map<String, String> messageMap = UtilMisc.toMap("errMessage", e.getMessage());
            Locale locale = null;
            String errMsg = UtilProperties.getMessage("CommonUiLabels", "CommonDataBasePortalProblem", messageMap, locale);
            return ServiceUtil.returnError(errMsg);
        }
        return results;
    }

}
