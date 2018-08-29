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

import org.apache.ofbiz.base.util.*;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.webapp.control.RequestHandler;
import org.apache.ofbiz.webapp.taglib.ContentUrlTag;
import org.apache.ofbiz.widget.WidgetWorker;
import org.apache.ofbiz.widget.model.*;
import org.apache.ofbiz.widget.model.ModelScreenWidget.ColumnContainer;
import org.apache.ofbiz.widget.renderer.MenuStringRenderer;
import org.apache.ofbiz.widget.renderer.Paginator;
import org.apache.ofbiz.widget.renderer.ScreenStringRenderer;
import org.apache.ofbiz.widget.renderer.VisualTheme;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.*;

public class FrontJsScreenRenderer implements ScreenStringRenderer {
    private static final String NOT_YET_SUPPORTED = "Not yet supported";
    public static final String module = FrontJsScreenRenderer.class.getName();
    private int elementId = 999;
    private FrontJsOutput output;
    private String rendererName;
    private int screenLetsIdCounter = 1;

    FrontJsScreenRenderer(String name, FrontJsOutput output) {
        this.output = output;
        rendererName = name;
    }
    private String getNextElementId() {
        elementId++;
        return "hsr" + elementId;
    }
    /*
    private void executeMacro(Appendable writer, String macro) throws IOException {
        executeMacro(output, macro, null);
    }
    private void executeMacro(Appendable writer, String macro, Map<String, Object> parameters) throws IOException {
        this.output.add(macro, NOT_YET_SUPPORTED);
    }
    */
    public String getRendererName() {
        return rendererName;
    }

    public void renderScreenBegin(Appendable writer, Map<String, Object> context) throws IOException {
        // this.output.add("ScreenBegin", new HashMap<>());
    }

    public void renderScreenEnd(Appendable writer, Map<String, Object> context) throws IOException {
        // this.output.add("ScreenEnd", new HashMap<>());
    }

    public void renderSectionBegin(Appendable writer, Map<String, Object> context, ModelScreenWidget.Section section) throws IOException {
        // this.output.add("SectionBegin", new HashMap<>());
    }
    public void renderSectionEnd(Appendable writer, Map<String, Object> context, ModelScreenWidget.Section section) throws IOException {
        // this.output.add("SectionEnd", new HashMap<>());
    }

    public void renderContainerBegin(Appendable writer, Map<String, Object> context, ModelScreenWidget.Container container) throws IOException {
        /*
        String containerId = container.getId(context);
        String autoUpdateTarget = container.getAutoUpdateTargetExdr(context);
        HttpServletRequest request = (HttpServletRequest) context.get("request");
        String autoUpdateLink = "";
        if (UtilValidate.isNotEmpty(autoUpdateTarget) && UtilHttp.isJavaScriptEnabled(request)) {
            if (UtilValidate.isEmpty(containerId)) {
                containerId = getNextElementId();
            }
            HttpServletResponse response = (HttpServletResponse) context.get("response");
            ServletContext ctx = (ServletContext) request.getAttribute("servletContext");
            RequestHandler rh = (RequestHandler) ctx.getAttribute("_REQUEST_HANDLER_");
            autoUpdateLink = rh.makeLink(request, response, autoUpdateTarget);
        }
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", containerId);
        parameters.put("style", container.getStyle(context));
        parameters.put("autoUpdateLink", autoUpdateLink);
        parameters.put("autoUpdateInterval", container.getAutoUpdateInterval(context));
        this.output.add("ContainerBegin", parameters);
        */
    }

    public void renderContainerEnd(Appendable writer, Map<String, Object> context, ModelScreenWidget.Container container) throws IOException {
        // this.output.add("renderContainerEnd", new HashMap<>());
    }

    public void renderLabel(Appendable writer, Map<String, Object> context, ModelScreenWidget.Label label) throws IOException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("text", label.getText(context));
        parameters.put("id", label.getId(context));
        parameters.put("style", label.getStyle(context));
        HashMap<String, Object> hashMapStringObject = new HashMap<>();
        hashMapStringObject.put("Label", parameters);
        this.output.putScreen(hashMapStringObject);
    }

    public void renderHorizontalSeparator(Appendable writer, Map<String, Object> context, ModelScreenWidget.HorizontalSeparator separator) throws IOException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", separator.getId(context));
        parameters.put("style", separator.getStyle(context));
        HashMap<String, Object> hashMapStringObject = new HashMap<>();
        hashMapStringObject.put("HorizontalSeparator", parameters);
        this.output.putScreen(hashMapStringObject);
    }

    public void renderLink(Appendable writer, Map<String, Object> context, ModelScreenWidget.ScreenLink link) throws IOException {
        HttpServletResponse response = (HttpServletResponse) context.get("response");
        HttpServletRequest request = (HttpServletRequest) context.get("request");
        VisualTheme visualTheme = UtilHttp.getVisualTheme(request);
        ModelTheme modelTheme = visualTheme.getModelTheme();
        String targetWindow = link.getTargetWindow(context);
        String target = link.getTarget(context);

        String uniqueItemName = link.getModelScreen().getName() + "_LF_" + UtilMisc.<String>addToBigDecimalInMap(context, "screenUniqueItemIndex", BigDecimal.ONE);

        String linkType = WidgetWorker.determineAutoLinkType(link.getLinkType(), target, link.getUrlMode(), request);
        String linkUrl = "";
        String actionUrl = "";
        StringBuilder parameters = new StringBuilder();
        String width = link.getWidth();
        if (UtilValidate.isEmpty(width)) {
            width = String.valueOf(modelTheme.getLinkDefaultLayeredModalWidth());
        }
        String height = link.getHeight();
        if (UtilValidate.isEmpty(height)) {
            height = String.valueOf(modelTheme.getLinkDefaultLayeredModalHeight());
        }
        if ("hidden-form".equals(linkType) || "layered-modal".equals(linkType)) {
            StringBuilder sb = new StringBuilder();
            WidgetWorker.buildHyperlinkUrl(sb, target, link.getUrlMode(), null, link.getPrefix(context),
                    link.getFullPath(), link.getSecure(), link.getEncode(), request, response, context);
            actionUrl = sb.toString();
            parameters.append("[");
            for (Map.Entry<String, String> parameter: link.getParameterMap(context).entrySet()) {
                if (parameters.length() >1) {
                    parameters.append(",");
                }
                parameters.append("{'name':'");
                parameters.append(parameter.getKey());
                parameters.append("'");
                parameters.append(",'value':'");
                parameters.append(parameter.getValue());
                parameters.append("'}");
            }
            parameters.append("]");
        }
        String id = link.getId(context);
        String style = link.getStyle(context);
        String name = link.getName(context);
        String text = link.getText(context);
        if (UtilValidate.isNotEmpty(target)) {
            if (!"hidden-form".equals(linkType)) {
                StringBuilder sb = new StringBuilder();
                WidgetWorker.buildHyperlinkUrl(sb, target, link.getUrlMode(), link.getParameterMap(context), link.getPrefix(context),
                        link.getFullPath(), link.getSecure(), link.getEncode(), request, response, context);
                linkUrl = sb.toString();
            }
        }
        String imgStr = "";
        ModelScreenWidget.ScreenImage img = link.getImage();
        if (img != null) {
            StringWriter sw = new StringWriter();
            renderImage(writer, context, img);
            imgStr = sw.toString();
        }
        Map<String, Object> cb = new HashMap<>();
        cb.put("parameterList", parameters.length()==0? "\"\"" : parameters.toString());
        cb.put("targetWindow", targetWindow);
        cb.put("target", target);
        cb.put("uniqueItemName", uniqueItemName);
        cb.put("linkType", linkType);
        cb.put("actionUrl", actionUrl);
        cb.put("id", id);
        cb.put("style", style);
        cb.put("name", name);
        if (UtilValidate.isNotEmpty(width)) {
            cb.put("width", width);
        }
        if (UtilValidate.isNotEmpty(height)) {
            cb.put("height", height);
        }
        cb.put("linkUrl", linkUrl);
        cb.put("text", text);
        cb.put("imgStr", imgStr.replaceAll("\"", "\\\\\""));
        HashMap<String, Object> hashMapStringObject = new HashMap<>();
        hashMapStringObject.put("Link", cb);
        this.output.putScreen(hashMapStringObject);
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
        HashMap<String, Object> hashMapStringObject = new HashMap<>();
        hashMapStringObject.put("Image", parameters);
        this.output.putScreen(hashMapStringObject);
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
        HashMap<String, Object> hashMapStringObject = new HashMap<>();
        hashMapStringObject.put("ContentBegin", parameters);
        this.output.putScreen(hashMapStringObject);
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
        boolean javaScriptEnabled = UtilHttp.isJavaScriptEnabled(request);
        ModelScreenWidget.Menu tabMenu = screenlet.getTabMenu();
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

        }

        String title = screenlet.getTitle(context);
        boolean collapsible = screenlet.collapsible();
        ModelScreenWidget.Menu navMenu = screenlet.getNavigationMenu();
        ModelScreenWidget.Form navForm = screenlet.getNavigationForm();
        String expandToolTip = "";
        String collapseToolTip = "";
        String fullUrlString = "";
        boolean showMore = false;
        if (UtilValidate.isNotEmpty(title) || navMenu != null || navForm != null || collapsible) {
            showMore = true;
            if (collapsible) {
                this.getNextElementId();
                Map<String, Object> uiLabelMap = UtilGenerics.checkMap(context.get("uiLabelMap"));
                Map<String, Object> paramMap = UtilGenerics.checkMap(context.get("requestParameters"));
                Map<String, Object> requestParameters = new HashMap<>(paramMap);
                if (uiLabelMap != null) {
                    expandToolTip = (String) uiLabelMap.get("CommonExpand");
                    collapseToolTip = (String) uiLabelMap.get("CommonCollapse");
                }
                if (!javaScriptEnabled) {
                    requestParameters.put(screenlet.getPreferenceKey(context) + "_collapsed", collapsed ? "false" : "true");
                    String queryString = UtilHttp.urlEncodeArgs(requestParameters);
                    fullUrlString = request.getRequestURI() + "?" + queryString;
                }
            }
            //StringWriter sb = new StringWriter();
            if (navMenu != null) {
                MenuStringRenderer savedRenderer = (MenuStringRenderer) context.get("menuStringRenderer");
                MenuStringRenderer renderer;
                renderer = new FrontJsMenuRenderer(output, request, response);
                context.put("menuStringRenderer", renderer);
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
                context.put("menuStringRenderer", savedRenderer);
            } else if (navForm != null) {
                renderScreenletPaginateMenu(writer, context, navForm);
            }
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", title);
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
        parameters.put("expandToolTip", expandToolTip);
        parameters.put("collapseToolTip", collapseToolTip);
        parameters.put("fullUrlString", fullUrlString);
        parameters.put("padded", screenlet.padded());
        parameters.put("showMore", showMore);
        parameters.put("collapsed", collapsed);
        parameters.put("javaScriptEnabled", javaScriptEnabled);
        HashMap<String, Object> hashMapStringObject = new HashMap<>();
        hashMapStringObject.put("ScreenletBegin", parameters);
        this.output.pushScreen(hashMapStringObject);
    }

    public void renderScreenletSubWidget(Appendable writer, Map<String, Object> context, ModelScreenWidget subWidget, ModelScreenWidget.Screenlet screenlet) throws GeneralException, IOException  {
        HashMap<String, Object> hashMapStringObject = new HashMap<>();
        hashMapStringObject.put("ScreenletSubWidget", NOT_YET_SUPPORTED);
        this.output.putScreen(hashMapStringObject);
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
        HashMap<String, Object> hashMapStringObject = new HashMap<>();
        hashMapStringObject.put("ScreenletEnd", null);
        this.output.popScreen(hashMapStringObject);
    }

    protected void renderScreenletPaginateMenu(Appendable writer, Map<String, Object> context, ModelScreenWidget.Form form) throws IOException {
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
            return;
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

        Map<String, Object> inputFields = UtilGenerics.toMap(context.get("requestParameters"));
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
        HashMap<String, Object> hashMapStringObject = new HashMap<>();
        hashMapStringObject.put("ScreenletPaginateMenu", parameters);
        this.output.putScreen(hashMapStringObject);
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
        HashMap<String, Object> hashMapStringObject = new HashMap<>();
        hashMapStringObject.put("PortalPageBegin", cb);
        this.output.pushScreen(hashMapStringObject);
    }

    public void renderPortalPageEnd(Appendable writer, Map<String, Object> context, ModelScreenWidget.PortalPage portalPage) throws GeneralException, IOException {
        HashMap<String, Object> hashMapStringObject = new HashMap<>();
        hashMapStringObject.put("PortalPageEnd", new HashMap<>());
        this.output.popScreen(hashMapStringObject);
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
        HashMap<String, Object> hashMapStringObject = new HashMap<>();
        hashMapStringObject.put("PortalPageColumnBegin", cb);
        this.output.pushScreen(hashMapStringObject);
    }

    public void renderPortalPageColumnEnd(Appendable writer, Map<String, Object> context, ModelScreenWidget.PortalPage portalPage, GenericValue portalPageColumn) throws GeneralException, IOException {
        HashMap<String, Object> hashMapStringObject = new HashMap<>();
        hashMapStringObject.put("PortalPageColumnEnd", new HashMap<>());
        this.output.popScreen(hashMapStringObject);
    }

    public void renderPortalPagePortletBegin(Appendable writer, Map<String, Object> context, ModelScreenWidget.PortalPage portalPage, GenericValue portalPortlet) throws GeneralException, IOException {
        String portalPageId = portalPage.getActualPortalPageId(context);
        String originalPortalPageId = portalPage.getOriginalPortalPageId(context);
        String portalPortletId = portalPortlet.getString("portalPortletId");
        String portletSeqId = portalPortlet.getString("portletSeqId");
        String columnSeqId = portalPortlet.getString("columnSeqId");
        String confMode = portalPage.getConfMode(context);
        //String editFormName = portalPortlet.getString("editFormName");
        //String editFormLocation = portalPortlet.getString("editFormLocation");

        String prevPortletId = (String) context.get("prevPortletId");
        String prevPortletSeqId = (String) context.get("prevPortletSeqId");
        String nextPortletId = (String) context.get("nextPortletId");
        String nextPortletSeqId = (String) context.get("nextPortletSeqId");
        String prevColumnSeqId = (String) context.get("prevColumnSeqId");
        String nextColumnSeqId = (String) context.get("nextColumnSeqId");

        Map<String, String> uiLabelMap = UtilGenerics.cast(context.get("uiLabelMap"));
        String delPortletHint = "";
        String editAttributeHint = "";
        if (uiLabelMap == null) {
            Debug.logWarning("Could not find uiLabelMap in context", module);
        } else {
            delPortletHint = uiLabelMap.get("CommonDeleteThisPortlet");
            editAttributeHint = uiLabelMap.get("CommonEditPortletAttributes");
        }

        Map<String, Object> cb = new HashMap<>();
        cb.put("originalPortalPageId", originalPortalPageId);
        cb.put("portalPageId", portalPageId);
        cb.put("portalPortletId", portalPortletId);
        cb.put("portletSeqId", portletSeqId);
        cb.put("prevPortletId", prevPortletId);
        cb.put("prevPortletSeqId", prevPortletSeqId);
        cb.put("nextPortletId", nextPortletId);
        cb.put("nextPortletSeqId", nextPortletSeqId);
        cb.put("columnSeqId", columnSeqId);
        cb.put("prevColumnSeqId", prevColumnSeqId);
        cb.put("nextColumnSeqId", nextColumnSeqId);
        cb.put("delPortletHint", delPortletHint);
        cb.put("editAttributeHint", editAttributeHint);
        cb.put("confMode", confMode);
        HashMap<String, Object> hashMapStringObject = new HashMap<>();
        hashMapStringObject.put("PortalPagePortletBegin", cb);
        this.output.pushScreen(hashMapStringObject);
    }

    public void renderPortalPagePortletEnd(Appendable writer, Map<String, Object> context, ModelScreenWidget.PortalPage portalPage, GenericValue portalPortlet) throws GeneralException, IOException {
        String confMode = portalPage.getConfMode(context);

        Map<String, Object> cb = new HashMap<>();
        cb.put("confMode", confMode);
        HashMap<String, Object> hashMapStringObject = new HashMap<>();
        hashMapStringObject.put("PortalPagePortletEnd", cb);
        this.output.popScreen(hashMapStringObject);
    }

    public void renderPortalPagePortletBody(Appendable writer, Map<String, Object> context, ModelScreenWidget.PortalPage portalPage, GenericValue portalPortlet) throws GeneralException, IOException {
        String portalPortletId = portalPortlet.getString("portalPortletId");
        String screenName = portalPortlet.getString("screenName");
        String screenLocation = portalPortlet.getString("screenLocation");

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
        HashMap<String, Object> hashMapStringObject = new HashMap<>();
        hashMapStringObject.put("ColumnContainerBegin", cb);
        this.output.putScreen(hashMapStringObject);
        for (ModelScreenWidget.Column column : columnContainer.getColumns()) {
            id = column.getId(context);
            style = column.getStyle(context);
            cb = new HashMap<>();
            cb.put("id", id);
            cb.put("style", style);
            hashMapStringObject.clear();
            hashMapStringObject.put("ColumnBegin", cb);
            this.output.putScreen(hashMapStringObject);
            for (ModelScreenWidget subWidget : column.getSubWidgets()) {
                try {
                    subWidget.renderWidgetString(writer, context, this);
                } catch (GeneralException e) {
                    throw new IOException(e);
                }
            }
            hashMapStringObject.clear();
            hashMapStringObject.put("ColumnEnd", cb);
            this.output.putScreen(hashMapStringObject);
        }
        hashMapStringObject.clear();
        hashMapStringObject.put("ColumnContainerEnd", cb);
        this.output.putScreen(hashMapStringObject);
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