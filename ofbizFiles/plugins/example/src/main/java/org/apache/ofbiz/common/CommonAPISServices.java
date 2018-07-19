package org.apache.ofbiz.common;

import com.auth0.jwt.JWTExpiredException;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

import org.apache.commons.io.FileUtils;
import org.apache.ofbiz.base.util.Base64;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilGenerics;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.base.util.string.FlexibleStringExpander;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityComparisonOperator;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityFunction;
import org.apache.ofbiz.entity.condition.EntityJoinOperator;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.model.DynamicViewEntity;
import org.apache.ofbiz.entity.model.ModelKeyMap;
import org.apache.ofbiz.entity.util.EntityFindOptions;
import org.apache.ofbiz.entity.util.EntityListIterator;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.entity.util.EntityUtilProperties;
import org.apache.ofbiz.product.product.ProductSearch;
import org.apache.ofbiz.security.Security;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.ServiceUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

public class CommonAPISServices {
	public static final String module = CommonAPISServices.class.getName();
	private static final String  secretKey = UtilProperties.getPropertyValue("commonApi", "authentication.token.key");
	private static final int expiry = UtilProperties.getPropertyAsInteger("commonApi", "authentication.token.expiry", 600);


    protected static String generateUserToken(String userName) {

        JWTSigner signer = new JWTSigner(secretKey);

        JWTSigner.Options options = new JWTSigner.Options().setExpirySeconds(expiry);

        Map<String, Object> claims = UtilMisc.toMap("username", userName);

        String token = signer.sign(claims, options);

        return token;
    }
    
    public static String checkToken(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getParameter("token");
        if (UtilValidate.isNotEmpty(token)) {
            JWTVerifier verifier = new JWTVerifier(secretKey);

            Map<String, Object> claims = ServiceUtil.returnSuccess();

            try {
                claims.putAll(verifier.verify(token));
                String username = (String) claims.get("username");
                request.setAttribute("username", username);
                request.setAttribute("anonymous", (Boolean) claims.get("anonymous"));
                return "success";
            } catch (IOException e) {
                 Debug.logError("Authentication error : " + e.getMessage(), module);
            } catch (InvalidKeyException e) {
                Debug.logError("Authentication error : " + e.getMessage(), module);
            } catch (NoSuchAlgorithmException e) {
                Debug.logError("Authentication error : " + e.getMessage(), module);
            } catch (JWTVerifyException e) {
                if (e instanceof JWTExpiredException) {
                    Debug.logError("tokenError: expired "  + e.getMessage(), module);
                } else {
                    Debug.logError("tokenError : invalidToken " + e.getMessage(), module);
                }
            } catch (SignatureException e) {
                Debug.logError("tokenError: signatureRejected " + e.getMessage(), module);
            }
            return "error";
        }
        return "success";
    }
    
    public static Map<String, Object> apisLogin(DispatchContext dctx, Map<String, ? extends Object> context) {
        Map<String, Object> results = new HashMap<String, Object>();
        try {
            GenericValue userLogin = dctx.getDelegator().findOne("UserLogin", false, "userLoginId", context.get("username"));
            if (UtilValidate.isNotEmpty(userLogin) && UtilValidate.isEmpty(userLogin.getString("lastLocale"))) {
                userLogin.set("lastLocale", "fr");
            }
            
            Map<String, Object> localCtx = dctx.makeValidContext("userLogin", "IN", context);
            localCtx.remove("userLogin");
            localCtx.put("isServiceAuth", Boolean.TRUE);
            localCtx.put("login.username", context.get("username"));
            localCtx.put("login.password", context.get("password"));
            results = dctx.getDispatcher().runSync("userLogin", localCtx);
            if (ServiceUtil.isError(results)) {
                return ServiceUtil.returnError(ServiceUtil.getErrorMessage(results));
            } else {
                Security security =  dctx.getSecurity();
                if(security.hasPermission("SH-SUPPL-PORT_ADMIN", userLogin)) {
                    results.put("token", generateUserToken((String)context.get("username")));
                    results.putAll(ServiceUtil.returnSuccess("indentifiant / mot de passe acceptés"));
                    return results;
                }
                else {
                    return ServiceUtil.returnError("Vous n'êtes pas autorisé à vous connecter à cette application !");
                }
            }
        }
        catch(GenericServiceException gse) {
            return ServiceUtil.returnError("erreur d'authetification : " + gse.getMessage());
        }
        catch(GenericEntityException gee) {
            return ServiceUtil.returnError("erreur d'authetification : " + gee.getMessage());
        }
    }
    
    public static Map<String, Object> checkAuthToken(DispatchContext dctx, Map<String, ? extends Object> context) {

        JWTVerifier verifier = new JWTVerifier(secretKey);

        Map<String, Object> results = ServiceUtil.returnSuccess();

        try {
            results.putAll(verifier.verify((String) context.get("token")));
            if (results.containsKey("exp")) {
                results.remove("exp");
            }
            GenericValue userLogin = dctx.getDelegator().findOne("UserLogin", false, "userLoginId", results.get("username"));
            if (UtilValidate.isNotEmpty(userLogin)) {
                results.remove("exp");
            }
        } catch (IOException e) {
            results = ServiceUtil.returnError("Authentication error : " + e.getMessage());
            results.put("tokenError", "tokenCorrupted");
        } catch (InvalidKeyException e) {
            results = ServiceUtil.returnError("Authentication error : " + e.getMessage());
            results.put("tokenError", "invalidKey");
        } catch (NoSuchAlgorithmException e) {
            results = ServiceUtil.returnError("Authentication error : " + e.getMessage());
            results.put("tokenError", "tokenCorrupted");
        } catch (JWTVerifyException e) {
            results = ServiceUtil.returnError("Authentication error : " + e.getMessage());
            if (e instanceof JWTExpiredException) {
                results.put("tokenError", "expired");
            } else {
                results.put("tokenError", "invalidToken");
            }
        } catch (SignatureException e) {
            results = ServiceUtil.returnError("Authentication error : " + e.getMessage());
            results.put("tokenError", "signatureRejected");
        } catch(Exception e) {
            results = ServiceUtil.returnError("Authentication error : " + e.getMessage());
            results.put("tokenError", "signatureRejected");
        }
        return results;
    }
}
