/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */
package org.apache.ofbiz.widget.renderer.frontjs;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.GeneralException;
import org.apache.ofbiz.base.util.UtilGenerics;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.webapp.control.RequestHandler;
import org.apache.ofbiz.webapp.taglib.ContentUrlTag;
import org.apache.ofbiz.widget.WidgetWorker;
import org.apache.ofbiz.widget.model.AbstractModelAction;
import org.apache.ofbiz.widget.model.ModelForm;
import org.apache.ofbiz.widget.model.ModelMenu;
import org.apache.ofbiz.widget.model.ModelMenuItem;
import org.apache.ofbiz.widget.model.ModelScreen;
import org.apache.ofbiz.widget.model.ModelScreenWidget;
import org.apache.ofbiz.widget.model.ModelScreenWidget.ColumnContainer;
import org.apache.ofbiz.widget.model.ModelScreenWidget.ScreenImage;
import org.apache.ofbiz.widget.model.ModelTheme;
import org.apache.ofbiz.widget.model.ScreenFactory;
import org.apache.ofbiz.widget.renderer.MenuStringRenderer;
import org.apache.ofbiz.widget.renderer.Paginator;
import org.apache.ofbiz.widget.renderer.ScreenStringRenderer;
import org.apache.ofbiz.widget.renderer.VisualTheme;
import org.xml.sax.SAXException;

public class FrontJsScreenRenderer implements ScreenStringRenderer {
    public static final String module = FrontJsScreenRenderer.class.getName();
    private FrontJsOutput output;
    private String rendererName;
    private int screenLetsIdCounter = 1;

    FrontJsScreenRenderer(String name, FrontJsOutput output) {
        this.output = output;
        rendererName = name;
    }
    public String getRendererName() {
        return rendererName;
    }

    public void renderScreenBegin(Appendable writer, Map<String, Object> context) throws IOException {
        // nothing to do, it's only a human logic readable element 
    }

    public void renderScreenEnd(Appendable writer, Map<String, Object> context) throws IOException {
        // nothing to do, it's only a human logic readable element 
    }

    public void renderSectionBegin(Appendable writer, Map<String, Object> context, ModelScreenWidget.Section section) throws IOException {
        // nothing to do, it's only a human logic readable element 
    }
    public void renderSectionEnd(Appendable writer, Map<String, Object> context, ModelScreenWidget.Section section) throws IOException {
        // nothing to do, it's only a human logic readable element 
    }

    public void renderContainerBegin(Appendable writer, Map<String, Object> context, ModelScreenWidget.Container container) throws IOException {
        String containerId = container.getId(context);
        String type = container.getType(context);
        if (UtilValidate.isNotEmpty(type)) {
            throw new IOException("FrontJsRender: type property in container tag, not yet implemented in container"
                               + ((containerId!=null)? "with id="+containerId : ""));
        }
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", containerId); // used as areaId
        String autoUpdateTarget = container.getAutoUpdateTargetExdr(context);
        if (UtilValidate.isNotEmpty(autoUpdateTarget)) {
            parameters.put("autoUpdateTarget", autoUpdateTarget);
        }
        if (! "2".equals(container.getAutoUpdateInterval(context))) { // 2 is the default value, if empty
            Debug.logWarning("auto-update-interval property is used (="+container.getAutoUpdateInterval(context)+
                             ") in container with id="+containerId+" it's not manage by FrontFjRenderer", module);
            parameters.put("autoUpdateIntervale", container.getAutoUpdateInterval(context));
        }
        if (UtilValidate.isNotEmpty(container.getStyle(context))) {
            Debug.logWarning("style property is used (="+container.getStyle(context)+
                             ") in container with id="+containerId+" it's not manage by FrontFjRenderer", module);
            parameters.put("style", container.getStyle(context));
        }
        this.output.pushScreen("ContainerOpen", parameters);
    }

    public void renderContainerEnd(Appendable writer, Map<String, Object> context, ModelScreenWidget.Container container) throws IOException {
        this.output.popScreen("ContainerClose");
    }

    public void renderLabel(Appendable writer, Map<String, Object> context, ModelScreenWidget.Label label) throws IOException {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("text", label.getText(context));
        if (UtilValidate.isNotEmpty(label.getId(context))) attributes.put("id", label.getId(context));
        if (UtilValidate.isNotEmpty(label.getStyle(context))) attributes.put("style", label.getStyle(context));
        this.output.putScreen("Label", attributes);
    }

    public void renderVueJs(Appendable writer, Map<String, Object> context, ModelScreenWidget.VueJs vuejs) throws IOException {
        Map<String, Object> attributes = vuejs.getParameterMap(context);
        attributes.put("componentName", vuejs.getComponentName(context));
        this.output.putScreen("VueJs", attributes);
    }

    public void renderHorizontalSeparator(Appendable writer, Map<String, Object> context, ModelScreenWidget.HorizontalSeparator separator) throws IOException {
        Map<String, Object> attributes = new HashMap<>();
        if (UtilValidate.isNotEmpty(separator.getId(context))) {
            Debug.logWarning("separator id is used (="+separator.getId(context)+
                             ")  it's not manage by FrontFjRenderer", module);
            attributes.put("id", separator.getId(context));
        }
        if (UtilValidate.isNotEmpty(separator.getName())) {
            Debug.logWarning("separator name is used (="+separator.getName()+
                             ")  it's not manage by FrontFjRenderer", module);
            attributes.put("name", separator.getName());
        }
        if (UtilValidate.isNotEmpty(separator.getStyle(context))) {
            Debug.logWarning("separator style is used (="+separator.getStyle(context)+
                             ")  it's not manage by FrontFjRenderer", module);
            attributes.put("style", separator.getStyle(context));
        }
        this.output.putScreen("HorizontalSeparator", attributes);
    }

    // not yet tested, it's very, very similar to MenuRenderer.renderLink which is tested
    public void renderLink(Appendable writer, Map<String, Object> context, ModelScreenWidget.ScreenLink link) throws IOException {
        HttpServletRequest request = (HttpServletRequest) context.get("request");
        VisualTheme visualTheme = UtilHttp.getVisualTheme(request);
        ModelTheme modelTheme = visualTheme.getModelTheme();

        if (link.getLink().getRequestConfirmation()) {
            throw new IOException("Render (Macro and FrontJs): requestConfirmation is used in a screenLink and it's not yet implemented"
                               + "for link with target="+ link.getTarget(context));
        }

        Map<String, Object> parameters = new HashMap<>();
        String target = link.getTarget(context);
        if (UtilValidate.isNotEmpty(link.getId(context)))    parameters.put("id",    link.getId(context));
        if (UtilValidate.isNotEmpty(link.getStyle(context))) parameters.put("style", link.getStyle(context));
        if (UtilValidate.isNotEmpty(link.getName(context)))  parameters.put("name",  link.getName(context));
        if (UtilValidate.isNotEmpty(link.getText(context)))  parameters.put("text",  link.getText(context));
        String height = link.getHeight();
        if (UtilValidate.isEmpty(height)) {
            height = String.valueOf(modelTheme.getLinkDefaultLayeredModalHeight());
        }
        parameters.put("height", height);
        String width = link.getWidth();
        if (UtilValidate.isEmpty(width)) {
            width = String.valueOf(modelTheme.getLinkDefaultLayeredModalWidth());
        }
        parameters.put("width", width);
        // targetWindow is used for setArea, if link-type="anchor"
        if (UtilValidate.isNotEmpty(link.getTargetWindow(context))) parameters.put("targetWindow", link.getTargetWindow(context));
        if (UtilValidate.isNotEmpty(link.getUrlMode())) parameters.put("urlMode", link.getUrlMode());

        // uniqueItemName is used for link-type='hidden-form' but this link-type is not currently supported by vuejs
        String uniqueItemName = link.getModelScreen().getName() + "_LF_" + UtilMisc.<String>addToBigDecimalInMap(context, "screenUniqueItemIndex", BigDecimal.ONE);
        parameters.put("uniqueItemName", uniqueItemName);
        String linkType = "";
        if (UtilValidate.isNotEmpty(target)) {
            linkType = WidgetWorker.determineAutoLinkType(link.getLinkType(), target, link.getUrlMode(), request);
        }
        // Workaround OH 2019-03-04 currently in VueLink hidden-form is not correctly manage, so use "auto" as link-type not hidden-form
        //   should be study when hidden-form will be manage
        parameters.put("linkType", linkType);
        parameters.put("linkType", link.getLinkType());
        // End of workaround
        // linkUrl is no more sent but if link-type=inter-app it's needed to have String externalLoginKey = (String) request.getAttribute("externalLoginKey"); (cf WidgetWorker.buildHyperlinkUrl)
        parameters.put("target", target);
        parameters.put("parameterMap", link.getParameterMap(context));
        ScreenImage img = link.getImage();
        if (img != null) {
            parameters.put("img", createImageParameters(context, img));
        }
        this.output.putScreen("Link", parameters);
    }
    // Made this a separate method so it can be externalized and reused.
    // copy from MenuRenderer
    private Map<String, Object> createImageParameters(Map<String, Object> context, ScreenImage image) {
        HttpServletResponse response = (HttpServletResponse) context.get("response");
        HttpServletRequest request = (HttpServletRequest) context.get("request");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", image.getId(context));
        parameters.put("style", image.getStyle(context));
        parameters.put("width", image.getWidth(context));
        parameters.put("height", image.getHeight(context));
        parameters.put("border", image.getBorder(context));
        //parameters.put("title", image.getTitleExdr().expandString(context));
        String src = image.getSrc(context);
        if (UtilValidate.isNotEmpty(src) && request != null && response != null) {
            String urlMode = image.getUrlMode();
            if ("ofbiz".equalsIgnoreCase(urlMode)) {
                ServletContext ctx = (ServletContext) request.getAttribute("servletContext");
                RequestHandler rh = (RequestHandler) ctx.getAttribute("_REQUEST_HANDLER_");
                src = rh.makeLink(request, response, src, false, false, false);
            } else if ("content".equalsIgnoreCase(urlMode)) {
                StringBuilder newURL = new StringBuilder();
                ContentUrlTag.appendContentPrefix(request, newURL);
                newURL.append(src);
                src = newURL.toString();
            }
        }
        parameters.put("src", src);
        return parameters;
    }

    public void renderImage(Appendable writer, Map<String, Object> context, ModelScreenWidget.ScreenImage image) throws IOException {
        if (image == null) {
            return ;
        }
        String src = image.getSrc(context);

        String urlMode = image.getUrlMode();
        boolean fullPath = false;
        boolean secure = false;
        boolean encode = false;
        HttpServletResponse response = (HttpServletResponse) context.get("response");
        HttpServletRequest request = (HttpServletRequest) context.get("request");
        String urlString = "";
        if (urlMode != null && "intra-app".equalsIgnoreCase(urlMode)) {
            if (request != null && response != null) {
                ServletContext ctx = (ServletContext) request.getAttribute("servletContext");
                RequestHandler rh = (RequestHandler) ctx.getAttribute("_REQUEST_HANDLER_");
                urlString = rh.makeLink(request, response, src, fullPath, secure, encode);
            } else {
                urlString = src;
            }
        } else  if (urlMode != null && "content".equalsIgnoreCase(urlMode)) {
            if (request != null && response != null) {
                StringBuilder newURL = new StringBuilder();
                ContentUrlTag.appendContentPrefix(request, newURL);
                newURL.append(src);
                urlString = newURL.toString();
            }
        } else {
            urlString = src;
        }
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("id", image.getId(context));
        parameters.put("style", image.getStyle(context));
        parameters.put("wid", image.getWidth(context));
        parameters.put("hgt", image.getHeight(context));
        parameters.put("border", image.getBorder(context));
        parameters.put("alt", image.getAlt(context));
        parameters.put("urlString", urlString);
        this.output.putScreen("Image", parameters);
    }

    public void renderContentBegin(Appendable writer, Map<String, Object> context, ModelScreenWidget.Content content) throws IOException {
        String editRequest = content.getEditRequest(context);
        String enableEditName = content.getEnableEditName(context);
        String enableEditValue = (String)context.get(enableEditName);

        if (Debug.verboseOn()) {
            Debug.logVerbose("directEditRequest:" + editRequest, module);
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("editRequest", editRequest);
        parameters.put("enableEditValue", enableEditValue == null ? "" : enableEditValue);
        parameters.put("editContainerStyle", content.getEditContainerStyle(context));
        this.output.putScreen("ContentBegin", parameters);
    }

    public void renderContentBody(Appendable writer, Map<String, Object> context, ModelScreenWidget.Content content) throws IOException {
        /*
        Locale locale = UtilMisc.ensureLocale(context.get("locale"));
        String mimeTypeId = "text/html";
        String expandedContentId = content.getContentId(context);
        String expandedDataResourceId = content.getDataResourceId(context);
        String renderedContent = null;
        LocalDispatcher dispatcher = (LocalDispatcher) context.get("dispatcher");
        Delegator delegator = (Delegator) context.get("delegator");

        // make a new map for content rendering; so our current map does not get clobbered
        Map<String, Object> contentContext = new HashMap<>();
        contentContext.putAll(context);
        String dataResourceId = (String)contentContext.get("dataResourceId");
        if (Debug.verboseOn()) {
            Debug.logVerbose("expandedContentId:" + expandedContentId, module);
        }

        try {
            if (UtilValidate.isNotEmpty(dataResourceId)) {
                if (WidgetDataResourceWorker.getDataresourceWorker() != null) {
                    renderedContent = WidgetDataResourceWorker.getDataresourceWorker().renderDataResourceAsTextExt(delegator, dataResourceId, contentContext, locale, mimeTypeId, false);
                } else {
                    Debug.logError("Not rendering content, WidgetDataResourceWorker.dataresourceWorker not found.", module);
                }
            } else if (UtilValidate.isNotEmpty(expandedContentId)) {
                if (WidgetContentWorker.getContentWorker() != null) {
                    renderedContent = WidgetContentWorker.getContentWorker().renderContentAsTextExt(dispatcher, expandedContentId, contentContext, locale, mimeTypeId, true);
                } else {
                    Debug.logError("Not rendering content, WidgetContentWorker.contentWorker not found.", module);
                }
            } else if (UtilValidate.isNotEmpty(expandedDataResourceId)) {
                if (WidgetDataResourceWorker.getDataresourceWorker() != null) {
                    renderedContent = WidgetDataResourceWorker.getDataresourceWorker().renderDataResourceAsTextExt(delegator, expandedDataResourceId, contentContext, locale, mimeTypeId, false);
                } else {
                    Debug.logError("Not rendering content, WidgetDataResourceWorker.dataresourceWorker not found.", module);
                }
            }
            if (UtilValidate.isEmpty(renderedContent)) {
                String editRequest = content.getEditRequest(context);
                if (UtilValidate.isNotEmpty(editRequest)) {
                    if (WidgetContentWorker.getContentWorker() != null) {
                        // FIXME : what to do ????
                        // WidgetContentWorker.getContentWorker().renderContentAsTextExt(dispatcher, "NOCONTENTFOUND", writer, contentContext, locale, mimeTypeId, true);
                    } else {
                        Debug.logError("Not rendering content, WidgetContentWorker.contentWorker not found.", module);
                    }
                }
            } else {
                if (content.xmlEscape()) {
                    renderedContent = UtilFormatOut.encodeXmlValue(renderedContent);
                }
                Map<String, Object> cb = new HashMap<>();
                cb.put("content", renderedContent);
                this.output.add("ContentBody", cb);
            }

        } catch (GeneralException |IOException e) {
            String errMsg = "Error rendering included content with id [" + expandedContentId + "] : " + e.toString();
            Debug.logError(e, errMsg, module);
        }
        */
    }

    public void renderContentEnd(Appendable writer, Map<String, Object> context, ModelScreenWidget.Content content) throws IOException {
        /*
        String expandedContentId = content.getContentId(context);
        String editMode = "Edit";
        String editRequest = content.getEditRequest(context);
        String enableEditName = content.getEnableEditName(context);
        String enableEditValue = (String)context.get(enableEditName);
        String urlString = "";
        if (editRequest != null && editRequest.toUpperCase(Locale.getDefault()).indexOf("IMAGE") < 0) {
            editMode += " Image";
        }

        if (UtilValidate.isNotEmpty(editRequest) && "true".equals(enableEditValue)) {
            HttpServletResponse response = (HttpServletResponse) context.get("response");
            HttpServletRequest request = (HttpServletRequest) context.get("request");
            if (request != null && response != null) {
                if (editRequest.indexOf('?') < 0) {
                    editRequest += "?";
                } else {
                    editRequest += "&amp;";
                }
                editRequest += "contentId=" + expandedContentId;
                ServletContext ctx = (ServletContext) request.getAttribute("servletContext");
                RequestHandler rh = (RequestHandler) ctx.getAttribute("_REQUEST_HANDLER_");
                urlString = rh.makeLink(request, response, editRequest, false, false, false);
            }

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("urlString", urlString);
            parameters.put("editMode", editMode);
            parameters.put("editContainerStyle", content.getEditContainerStyle(context));
            parameters.put("editRequest", editRequest);
            parameters.put("enableEditValue", enableEditValue);
            this.output.add("ContentEnd", parameters);
        }
        */
    }

    public void renderContentFrame(Appendable writer, Map<String, Object> context, ModelScreenWidget.Content content) throws IOException {
        /*
        String dataResourceId = content.getDataResourceId(context);
        String urlString = "/ViewSimpleContent?dataResourceId=" + dataResourceId;
        String fullUrlString = "";
        HttpServletRequest request = (HttpServletRequest) context.get("request");
        HttpServletResponse response = (HttpServletResponse) context.get("response");
        if (request != null && response != null) {
            ServletContext ctx = (ServletContext) request.getAttribute("servletContext");
            RequestHandler rh = (RequestHandler) ctx.getAttribute("_REQUEST_HANDLER_");
            fullUrlString = rh.makeLink(request, response, urlString, true, false, false);
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("fullUrl", fullUrlString);
        parameters.put("width", content.getWidth());
        parameters.put("height", content.getHeight());
        parameters.put("border", content.getBorder());
        this.output.add("ContentFrame", parameters);
        */
    }

    public void renderSubContentBegin(Appendable writer, Map<String, Object> context, ModelScreenWidget.SubContent content) throws IOException {
        /*
        String enableEditName = content.getEnableEditName(context);
        String enableEditValue = (String)context.get(enableEditName);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("editContainerStyle", content.getEditContainerStyle(context));
        parameters.put("editRequest", content.getEditRequest(context));
        parameters.put("enableEditValue", enableEditValue == null ? "" : enableEditValue);
        this.output.add("SubContentBegin", parameters);
        */
    }

    public void renderSubContentBody(Appendable writer, Map<String, Object> context, ModelScreenWidget.SubContent content) throws IOException {
        /*
        Locale locale = UtilMisc.ensureLocale(context.get("locale"));
        String mimeTypeId = "text/html";
        String expandedContentId = content.getContentId(context);
        String expandedMapKey = content.getMapKey(context);
        String renderedContent = "";
        LocalDispatcher dispatcher = (LocalDispatcher) context.get("dispatcher");

        // create a new map for the content rendering; so our current context does not get overwritten!
        Map<String, Object> contentContext = new HashMap<>();
        contentContext.putAll(context);

        try {
            if (WidgetContentWorker.getContentWorker() != null) {
                renderedContent = WidgetContentWorker.getContentWorker().renderSubContentAsTextExt(dispatcher, expandedContentId, expandedMapKey, contentContext, locale, mimeTypeId, true);
            } else {
                Debug.logError("Not rendering content, WidgetContentWorker.contentWorker not found.", module);
            }
            if (UtilValidate.isEmpty(renderedContent)) {
                String editRequest = content.getEditRequest(context);
                if (UtilValidate.isNotEmpty(editRequest)) {
                    if (WidgetContentWorker.getContentWorker() != null) {
                        // FIXME what to do ???
                        // WidgetContentWorker.getContentWorker().renderContentAsTextExt(dispatcher, "NOCONTENTFOUND", writer, contentContext, locale, mimeTypeId, true);
                    } else {
                        Debug.logError("Not rendering content, WidgetContentWorker.contentWorker not found.", module);
                    }
                }
            } else {
                if (content.xmlEscape()) {
                    renderedContent = UtilFormatOut.encodeXmlValue(renderedContent);
                }
                Map<String, Object> cb = new HashMap<>();
                cb.put("content", renderedContent);
                this.output.add("SubContentBody", cb);
            }

        } catch (GeneralException | IOException e) {
            String errMsg = "Error rendering included content with id [" + expandedContentId + "] : " + e.toString();
            Debug.logError(e, errMsg, module);
        }
        */
    }

    public void renderSubContentEnd(Appendable writer, Map<String, Object> context, ModelScreenWidget.SubContent content) throws IOException {
        /*
        String editMode = "Edit";
        String editRequest = content.getEditRequest(context);
        String enableEditName = content.getEnableEditName(context);
        String enableEditValue = (String)context.get(enableEditName);
        String expandedContentId = content.getContentId(context);
        String expandedMapKey = content.getMapKey(context);
        String urlString = "";
        if (editRequest != null && !(editRequest.toUpperCase(Locale.getDefault()).indexOf("IMAGE") < 1)) {
            editMode += " Image";
        }
        if (UtilValidate.isNotEmpty(editRequest) && "true".equals(enableEditValue)) {
            HttpServletResponse response = (HttpServletResponse) context.get("response");
            HttpServletRequest request = (HttpServletRequest) context.get("request");
            if (request != null && response != null) {
                if (editRequest.indexOf('?') < 0) {
                    editRequest += "?";
                } else {
                    editRequest += "&amp;";
                }
                editRequest += "contentId=" + expandedContentId;
                if (UtilValidate.isNotEmpty(expandedMapKey)) {
                    editRequest += "&amp;mapKey=" + expandedMapKey;
                }
                ServletContext ctx = (ServletContext) request.getAttribute("servletContext");
                RequestHandler rh = (RequestHandler) ctx.getAttribute("_REQUEST_HANDLER_");
                urlString = rh.makeLink(request, response, editRequest, false, false, false);
            }
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("urlString", urlString);
        parameters.put("editMode", editMode);
        parameters.put("editContainerStyle", content.getEditContainerStyle(context));
        parameters.put("editRequest", editRequest);
        parameters.put("enableEditValue", enableEditValue == null ? "" : enableEditValue);
        this.output.add("SubContentEnd", parameters);
        */
    }


    public void renderScreenletBegin(Appendable writer, Map<String, Object> context, boolean collapsed, ModelScreenWidget.Screenlet screenlet) throws IOException {
        HttpServletRequest request = (HttpServletRequest) context.get("request");
        HttpServletResponse response = (HttpServletResponse) context.get("response");
        //VisualTheme visualTheme = UtilHttp.getVisualTheme(request);
        //ModelTheme modelTheme = visualTheme.getModelTheme();
        //boolean javaScriptEnabled = UtilHttp.isJavaScriptEnabled(request);


        String title = screenlet.getTitle(context);
        boolean collapsible = screenlet.collapsible();
        ModelScreenWidget.Menu tabMenu = screenlet.getTabMenu();
        ModelScreenWidget.Menu navMenu = screenlet.getNavigationMenu();
        ModelScreenWidget.Form navForm = screenlet.getNavigationForm();
        String expandToolTip = "";
        String collapseToolTip = "";
        String fullUrlString = "";
//        boolean showMore = false;
        if (collapsible) {
            Map<String, Object> uiLabelMap = UtilGenerics.cast(context.get("uiLabelMap"));
            if (uiLabelMap != null) {
                expandToolTip = (String) uiLabelMap.get("CommonExpand");
                collapseToolTip = (String) uiLabelMap.get("CommonCollapse");
            }
//            this.getNextElementId();
//            Map<String, Object> paramMap = UtilGenerics.cast(context.get("requestParameters"));
//            Map<String, Object> requestParameters = new HashMap<>(paramMap);
//            if (!javaScriptEnabled) {
//                requestParameters.put(screenlet.getPreferenceKey(context) + "_collapsed", collapsed ? "false" : "true");
//                String queryString = UtilHttp.urlEncodeArgs(requestParameters);
//                fullUrlString = request.getRequestURI() + "?" + queryString;
//            }
        }


        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", title);
        //parameters.put("name", screenlet.getName()); temporary comment
        parameters.put("collapsible", collapsible);
        parameters.put("saveCollapsed", screenlet.saveCollapsed());
        if (UtilValidate.isNotEmpty (screenlet.getId(context))) {
            parameters.put("id", screenlet.getId(context));
            parameters.put("collapsibleAreaId", screenlet.getId(context) + "_col");
        } else {
            parameters.put("id", "screenlet_" + screenLetsIdCounter);
            parameters.put("collapsibleAreaId","screenlet_" + screenLetsIdCounter + "_col");
            screenLetsIdCounter++;
        }
        parameters.put("name", parameters.get("id")); // temporary, to test if with id it works !
        parameters.put("expandToolTip", expandToolTip);
        parameters.put("collapseToolTip", collapseToolTip);
        parameters.put("fullUrlString", fullUrlString);
        parameters.put("padded", screenlet.padded());
        parameters.put("collapsed", collapsed);
        //parameters.put("javaScriptEnabled", javaScriptEnabled);
        parameters.put("showMore", (Boolean) (UtilValidate.isNotEmpty(title) || navMenu != null || navForm != null || collapsible));
        this.output.pushScreen("ScreenletBegin", parameters);

        if (tabMenu != null) {
            ModelMenu menu = tabMenu.getModelMenu(context);
            MenuStringRenderer menuStringRenderer = (MenuStringRenderer)context.get("menuStringRenderer");
            AbstractModelAction.runSubActions(menu.getActions(), context);
            menuStringRenderer.renderMenuOpen(writer, context, menu);

            menuStringRenderer.renderFormatSimpleWrapperOpen(writer, context, menu);
            for (ModelMenuItem item : menu.getMenuItemList()) {
                if (item.shouldBeRendered(context)) {
                    AbstractModelAction.runSubActions(item.getActions(), context);
                    menuStringRenderer.renderMenuItem(writer, context, item);
                }
            }
            menuStringRenderer.renderFormatSimpleWrapperClose(writer, context, menu);
            menuStringRenderer.renderMenuClose(writer, context, menu);
            parameters.put("tabMenu", this.output.getAndRemoveScreen());

        }
        if (UtilValidate.isNotEmpty(title) || navMenu != null || navForm != null ) {
//            showMore = true;
            //StringWriter sb = new StringWriter();
            if (navMenu != null) {
//                MenuStringRenderer savedRenderer = (MenuStringRenderer) context.get("menuStringRenderer");
//                MenuStringRenderer renderer;
//                renderer = new FrontJsMenuRenderer(output, request, response);
//                context.put("menuStringRenderer", renderer);
                ModelMenu menu = navMenu.getModelMenu(context);
                MenuStringRenderer menuStringRenderer = (MenuStringRenderer)context.get("menuStringRenderer");
                AbstractModelAction.runSubActions(menu.getActions(), context);
                menuStringRenderer.renderMenuOpen(writer, context, menu);

                menuStringRenderer.renderFormatSimpleWrapperOpen(writer, context, menu);
                for (ModelMenuItem item : menu.getMenuItemList()) {
                    if (item.shouldBeRendered(context)) {
                        AbstractModelAction.runSubActions(item.getActions(), context);
                        menuStringRenderer.renderMenuItem(writer, context, item);
                    }
                }
                menuStringRenderer.renderFormatSimpleWrapperClose(writer, context, menu);
                menuStringRenderer.renderMenuClose(writer, context, menu);
                parameters.put("navMenu", this.output.getAndRemoveScreen());
//                context.put("menuStringRenderer", savedRenderer);
            } else if (navForm != null) {
                parameters.put("navForm",renderScreenletPaginateMenu(writer, context, navForm));
            }
        }
    }

    public void renderScreenletSubWidget(Appendable writer, Map<String, Object> context, ModelScreenWidget subWidget, ModelScreenWidget.Screenlet screenlet) throws GeneralException, IOException  {
        subWidget.renderWidgetString(writer, context, this);
//        HashMap<String, Object> cb = new HashMap<>();
//        this.output.putScreen("ScreenletSubWidget", cb);

        // Bypass the if/else in comment
        // si getNavigationForm la barre de pagination doit être dans l'entête de la screenlet
        /*
        if (subWidget.equals(screenlet.getNavigationForm())) {
            HttpServletRequest request = (HttpServletRequest) context.get("request");
            HttpServletResponse response = (HttpServletResponse) context.get("response");
            VisualTheme visualTheme = UtilHttp.getVisualTheme(request);
            ModelTheme modelTheme = visualTheme.getModelTheme();
            this.output.add("ScreenletSubWidget", NOT_YET_SUPPORTED);
            if (request != null && response != null) {
                Map<String, Object> globalCtx = UtilGenerics.checkMap(context.get("globalContext"));
                globalCtx.put("NO_PAGINATOR", true);
                FormMapRenderer savedRenderer = (FormMapRenderer) context.get("formStringRenderer");
                FrontJsFormRenderer renderer = new FrontJsFormRenderer(request, response);

                renderer.setRenderPagination(false);
                context.put("formStringRenderer", renderer);
                subWidget.renderWidgetString(writer, context, this);
                context.put("formStringRenderer", savedRenderer);
            }

        } else {
            subWidget.renderWidgetString(writer, context, this);
        }
        */
    }
    public void renderScreenletEnd(Appendable writer, Map<String, Object> context, ModelScreenWidget.Screenlet screenlet) throws IOException {
        this.output.popScreen("ScreenletEnd");
    }

    protected Map<String, Object> renderScreenletPaginateMenu(Appendable writer, Map<String, Object> context, ModelScreenWidget.Form form) throws IOException {
        HttpServletResponse response = (HttpServletResponse) context.get("response");
        HttpServletRequest request = (HttpServletRequest) context.get("request");
        ModelForm modelForm;
        try {
            modelForm = form.getModelForm(context);
        } catch (Exception e) {
            throw new IOException(e);
        }
        modelForm.runFormActions(context);
        Paginator.preparePager(modelForm, context);
        String targetService = modelForm.getPaginateTarget(context);
        if (targetService == null) {
            targetService = "${targetService}";
        }

        // get the parametrized pagination index and size fields
        int paginatorNumber = WidgetWorker.getPaginatorNumber(context);
        String viewIndexParam = modelForm.getMultiPaginateIndexField(context);
        String viewSizeParam = modelForm.getMultiPaginateSizeField(context);

        int viewIndex = Paginator.getViewIndex(modelForm, context);
        int viewSize = Paginator.getViewSize(modelForm, context);
        int listSize = Paginator.getListSize(context);

        int highIndex = Paginator.getHighIndex(context);
        int actualPageSize = Paginator.getActualPageSize(context);

        // if this is all there seems to be (if listSize < 0, then size is unknown)
        if (actualPageSize >= listSize && listSize >= 0) {
            return null;
        }

        // needed for the "Page" and "rows" labels
        Map<String, String> uiLabelMap = UtilGenerics.cast(context.get("uiLabelMap"));
        String ofLabel = "";
        if (uiLabelMap == null) {
            Debug.logWarning("Could not find uiLabelMap in context", module);
        } else {
            ofLabel = uiLabelMap.get("CommonOf");
            ofLabel = ofLabel.toLowerCase(Locale.getDefault());
        }

        // for legacy support, the viewSizeParam is VIEW_SIZE and viewIndexParam is VIEW_INDEX when the fields are "viewSize" and "viewIndex"
        if (("viewIndex" + "_" + paginatorNumber).equals(viewIndexParam)) {
            viewIndexParam = "VIEW_INDEX" + "_" + paginatorNumber;
        }
        if (("viewSize" + "_" + paginatorNumber).equals(viewSizeParam)) {
            viewSizeParam = "VIEW_SIZE" + "_" + paginatorNumber;
        }

        ServletContext ctx = (ServletContext) request.getAttribute("servletContext");
        RequestHandler rh = (RequestHandler) ctx.getAttribute("_REQUEST_HANDLER_");

        Map<String, Object> inputFields = UtilGenerics.cast(context.get("requestParameters"));
        // strip out any multi form fields if the form is of type multi
        if ("multi".equals(modelForm.getType())) {
            inputFields = UtilHttp.removeMultiFormParameters(inputFields);
        }
        String queryString = UtilHttp.urlEncodeArgs(inputFields);
        // strip legacy viewIndex/viewSize params from the query string
        queryString = UtilHttp.stripViewParamsFromQueryString(queryString, "" + paginatorNumber);
        // strip parametrized index/size params from the query string
        HashSet<String> paramNames = new HashSet<>();
        paramNames.add(viewIndexParam);
        paramNames.add(viewSizeParam);
        queryString = UtilHttp.stripNamedParamsFromQueryString(queryString, paramNames);

        String anchor = "";
        String paginateAnchor = modelForm.getPaginateTargetAnchor();
        if (paginateAnchor != null) {
            anchor = "#" + paginateAnchor;
        }

        // preparing the link text, so that later in the code we can reuse this and just add the viewIndex
        String prepLinkText = "";
        prepLinkText = targetService;
        if (prepLinkText.indexOf('?') < 0) {
            prepLinkText += "?";
        } else if (!prepLinkText.endsWith("?")) {
            prepLinkText += "&amp;";
        }
        if (UtilValidate.isNotEmpty(queryString) && !"null".equals(queryString)) {
            prepLinkText += queryString + "&amp;";
        }
        prepLinkText += viewSizeParam + "=" + viewSize + "&amp;" + viewIndexParam + "=";

        String linkText;


        // The current screenlet title bar navigation syling requires rendering
        // these links in reverse order
        // Last button
        String lastLinkUrl = "";
        if (highIndex < listSize) {
            int lastIndex = UtilMisc.getViewLastIndex(listSize, viewSize);
            linkText = prepLinkText + lastIndex + anchor;
            lastLinkUrl = rh.makeLink(request, response, linkText);
        }
        String nextLinkUrl = "";
        if (highIndex < listSize) {
            linkText = prepLinkText + (viewIndex + 1) + anchor;
            // - make the link
            nextLinkUrl = rh.makeLink(request, response, linkText);
        }
        String previousLinkUrl = "";
        if (viewIndex > 0) {
            linkText = prepLinkText + (viewIndex - 1) + anchor;
            previousLinkUrl = rh.makeLink(request, response, linkText);
        }
        String firstLinkUrl = "";
        if (viewIndex > 0) {
            linkText = prepLinkText + 0 + anchor;
            firstLinkUrl = rh.makeLink(request, response, linkText);
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("lowIndex", Paginator.getLowIndex(context));
        parameters.put("actualPageSize", actualPageSize);
        parameters.put("ofLabel", ofLabel);
        parameters.put("listSize", listSize);
        parameters.put("paginateLastStyle", modelForm.getPaginateLastStyle());
        parameters.put("lastLinkUrl", lastLinkUrl);
        parameters.put("paginateLastLabel", modelForm.getPaginateLastLabel(context));
        parameters.put("paginateNextStyle", modelForm.getPaginateNextStyle());
        parameters.put("nextLinkUrl", nextLinkUrl);
        parameters.put("paginateNextLabel", modelForm.getPaginateNextLabel(context));
        parameters.put("paginatePreviousStyle", modelForm.getPaginatePreviousStyle());
        parameters.put("paginatePreviousLabel", modelForm.getPaginatePreviousLabel(context));
        parameters.put("previousLinkUrl", previousLinkUrl);
        parameters.put("paginateFirstStyle", modelForm.getPaginateFirstStyle());
        parameters.put("paginateFirstLabel", modelForm.getPaginateFirstLabel(context));
        parameters.put("firstLinkUrl", firstLinkUrl);
        return parameters;
        //this.output.putScreen("ScreenletPaginateMenu", parameters);
    }

    public void renderPortalPageBegin(Appendable writer, Map<String, Object> context, ModelScreenWidget.PortalPage portalPage) throws GeneralException, IOException {
        String portalPageId = portalPage.getActualPortalPageId(context);
        String originalPortalPageId = portalPage.getOriginalPortalPageId(context);
        String confMode = portalPage.getConfMode(context);

        Map<String, String> uiLabelMap = UtilGenerics.cast(context.get("uiLabelMap"));
        String addColumnLabel = "";
        String addColumnHint = "";
        if (uiLabelMap == null) {
            Debug.logWarning("Could not find uiLabelMap in context", module);
        } else {
            addColumnLabel = uiLabelMap.get("CommonAddColumn");
            addColumnHint = uiLabelMap.get("CommonAddAColumnToThisPortalPage");
        }

        Map<String, Object> cb = new HashMap<>();
        cb.put("originalPortalPageId", originalPortalPageId);
        cb.put("portalPageId", portalPageId);
        cb.put("confMode", confMode);
        cb.put("addColumnLabel", addColumnLabel);
        cb.put("addColumnHint", addColumnHint);
        this.output.pushScreen("PortalPageBegin", cb);
    }

    public void renderPortalPageEnd(Appendable writer, Map<String, Object> context, ModelScreenWidget.PortalPage portalPage) throws GeneralException, IOException {
        this.output.popScreen("PortalPageEnd");
    }

    public void renderPortalPageColumnBegin(Appendable writer, Map<String, Object> context, ModelScreenWidget.PortalPage portalPage, GenericValue portalPageColumn) throws GeneralException, IOException {
        String portalPageId = portalPage.getActualPortalPageId(context);
        String originalPortalPageId = portalPage.getOriginalPortalPageId(context);
        String columnSeqId = portalPageColumn.getString("columnSeqId");
        String columnWidthPercentage = portalPageColumn.getString("columnWidthPercentage");
        String columnWidthPixels = portalPageColumn.getString("columnWidthPixels");
        String confMode = portalPage.getConfMode(context);

        Map<String, String> uiLabelMap = UtilGenerics.cast(context.get("uiLabelMap"));
        String delColumnLabel = "";
        String delColumnHint = "";
        String addPortletLabel = "";
        String addPortletHint = "";
        String colWidthLabel = "";
        String setColumnSizeHint = "";

        if (uiLabelMap == null) {
            Debug.logWarning("Could not find uiLabelMap in context", module);
        } else {
            delColumnLabel = uiLabelMap.get("CommonDeleteColumn");
            delColumnHint = uiLabelMap.get("CommonDeleteThisColumn");

            addPortletLabel = uiLabelMap.get("CommonAddAPortlet");
            addPortletHint = uiLabelMap.get("CommonAddPortletToPage");
            colWidthLabel = uiLabelMap.get("CommonWidth");
            setColumnSizeHint = uiLabelMap.get("CommonSetColumnWidth");
        }

        Map<String, Object> cb = new HashMap<>();
        cb.put("originalPortalPageId", originalPortalPageId);
        cb.put("portalPageId", portalPageId);
        cb.put("columnSeqId", columnSeqId);
        if (UtilValidate.isNotEmpty(columnWidthPixels)) {
            cb.put("width", columnWidthPixels + "px");
        } else if (UtilValidate.isNotEmpty(columnWidthPercentage)) {
            cb.put("width", columnWidthPercentage + "%");
        }
        cb.put("confMode", confMode);
        cb.put("delColumnLabel", delColumnLabel);
        cb.put("delColumnHint", delColumnHint);
        cb.put("addPortletLabel", addPortletLabel);
        cb.put("addPortletHint", addPortletHint);
        cb.put("colWidthLabel", colWidthLabel);
        cb.put("setColumnSizeHint", setColumnSizeHint);
        this.output.pushScreen("PortalPageColumnBegin", cb);
    }

    public void renderPortalPageColumnEnd(Appendable writer, Map<String, Object> context, ModelScreenWidget.PortalPage portalPage, GenericValue portalPageColumn) throws GeneralException, IOException {
        this.output.popScreen("PortalPageColumnEnd");
    }

    public void renderPortalPagePortletBegin(Appendable writer, Map<String, Object> context, ModelScreenWidget.PortalPage portalPage, GenericValue portalPortlet) throws GeneralException, IOException {
        String portalPageId = portalPage.getActualPortalPageId(context);
        //String originalPortalPageId = portalPage.getOriginalPortalPageId(context);
        String portalPortletId = portalPortlet.getString("portalPortletId");
        String portletSeqId = portalPortlet.getString("portletSeqId");
        //String columnSeqId = portalPortlet.getString("columnSeqId");
        //String confMode = portalPage.getConfMode(context);
        //String editFormName = portalPortlet.getString("editFormName");
        //String editFormLocation = portalPortlet.getString("editFormLocation");

//        String prevPortletId = (String) context.get("prevPortletId");
//        String prevPortletSeqId = (String) context.get("prevPortletSeqId");
//        String nextPortletId = (String) context.get("nextPortletId");
//        String nextPortletSeqId = (String) context.get("nextPortletSeqId");
//        String prevColumnSeqId = (String) context.get("prevColumnSeqId");
//        String nextColumnSeqId = (String) context.get("nextColumnSeqId");

//        Map<String, String> uiLabelMap = UtilGenerics.cast(context.get("uiLabelMap"));
//        String delPortletHint = "";
//        String editAttributeHint = "";
//        if (uiLabelMap == null) {
//            Debug.logWarning("Could not find uiLabelMap in context", module);
//        } else {
//            delPortletHint = uiLabelMap.get("CommonDeleteThisPortlet");
//            editAttributeHint = uiLabelMap.get("CommonEditPortletAttributes");
//        }

        Map<String, Object> cb = new HashMap<>();
//        cb.put("originalPortalPageId", originalPortalPageId);
        cb.put("portalPageId", portalPageId);
        cb.put("portalPortletId", portalPortletId);
        cb.put("portletSeqId", portletSeqId);
        if (portalPortlet.containsKey("watcherName")){
            cb.put("watcherName", portalPortlet.get("watcherName"));
        }
//        cb.put("prevPortletId", prevPortletId);
//        cb.put("prevPortletSeqId", prevPortletSeqId);
//        cb.put("nextPortletId", nextPortletId);
//        cb.put("nextPortletSeqId", nextPortletSeqId);
//        cb.put("columnSeqId", columnSeqId);
//        cb.put("prevColumnSeqId", prevColumnSeqId);
//        cb.put("nextColumnSeqId", nextColumnSeqId);
//        cb.put("delPortletHint", delPortletHint);
//        cb.put("editAttributeHint", editAttributeHint);
//        cb.put("confMode", confMode);
        this.output.pushScreen("PortalPagePortletBegin", cb);
    }

    public void renderPortalPagePortletEnd(Appendable writer, Map<String, Object> context, ModelScreenWidget.PortalPage portalPage, GenericValue portalPortlet) throws GeneralException, IOException {
        this.output.popScreen("PortalPagePortletEnd");
    }

    public void renderPortalPagePortletBody(Appendable writer, Map<String, Object> context, ModelScreenWidget.PortalPage portalPage, GenericValue portalPortlet) throws GeneralException, IOException {
        String portalPortletId = portalPortlet.getString("portalPortletId");
        String portletSeqId = portalPortlet.getString("portletSeqId");
        String screenName = portalPortlet.getString("screenName");
        String screenLocation = portalPortlet.getString("screenLocation");
//  in showPortlet Screen      <set field="currentAreaId" value="${parameters.portalPortletId}-${parameters.portletSeqId}"/>
//    so do the same thing
        context.put("currentAreaId", portalPortletId + "-" + portletSeqId);


        ModelScreen modelScreen = null;
        if (UtilValidate.isNotEmpty(screenName) && UtilValidate.isNotEmpty(screenLocation)) {
            try {
                modelScreen = ScreenFactory.getScreenFromLocation(screenLocation, screenName);
            } catch (IOException | SAXException | ParserConfigurationException e) {
                String errMsg = "Error rendering portlet ID [" + portalPortletId + "]: " + e.toString();
                Debug.logError(e, errMsg, module);
                throw new RuntimeException(errMsg);
            }
        }
        if (writer != null && context != null) {
            modelScreen.renderScreenString(writer, context, this);
        } else {
            Debug.logError("Null on some Path: writer" + writer + ", context: " + context, module);
        }
    }

    @Override
    public void renderColumnContainer(Appendable writer, Map<String, Object> context, ColumnContainer columnContainer) throws IOException {
        String id = columnContainer.getId(context);
        String style = columnContainer.getStyle(context);
        Map<String, Object> cb = new HashMap<>();
        cb.put("id", id);
        cb.put("style", style);
        this.output.putScreen("ColumnContainerBegin", cb);
        for (ModelScreenWidget.Column column : columnContainer.getColumns()) {
            id = column.getId(context);
            style = column.getStyle(context);
            cb = new HashMap<>();
            cb.put("id", id);
            cb.put("style", style);
            this.output.putScreen("ColumnBegin", cb);
            for (ModelScreenWidget subWidget : column.getSubWidgets()) {
                try {
                    subWidget.renderWidgetString(writer, context, this);
                } catch (GeneralException e) {
                    throw new IOException(e);
                }
            }
            this.output.putScreen("ColumnEnd", cb);
        }
        this.output.putScreen("ColumnContainerEnd", cb);
    }

    // This is a util method to get the style from a property file
    public static String getFoStyle(String styleName) {
        String value = UtilProperties.getPropertyValue("fo-styles", styleName);
        if (value.equals(styleName)) {
            return "";
        }
        return value;
    }
}
