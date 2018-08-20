
package org.apache.ofbiz.widget.renderer.frontjs;

import org.apache.ofbiz.base.util.StringUtil;
import org.apache.ofbiz.base.util.UtilGenerics;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.webapp.control.RequestHandler;
import org.apache.ofbiz.webapp.taglib.ContentUrlTag;
import org.apache.ofbiz.widget.WidgetWorker;
import org.apache.ofbiz.widget.model.ModelTree;
import org.apache.ofbiz.widget.renderer.ScreenRenderer;
import org.apache.ofbiz.widget.renderer.ScreenStringRenderer;
import org.apache.ofbiz.widget.renderer.TreeStringRenderer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrontJsTreeRenderer implements TreeStringRenderer {

    public static final String module = FrontJsTreeRenderer.class.getName();
    private FrontJsOutput output;
    FrontJsTreeRenderer(FrontJsOutput output) {
        this.output = output;
    }
    public void renderNodeBegin(Appendable writer, Map<String, Object> context, ModelTree.ModelNode node, int depth) throws IOException {
        String currentNodeTrailPiped;
        List<String> currentNodeTrail = UtilGenerics.toList(context.get("currentNodeTrail"));

        String style = "";
        if (node.isRootNode()) {
            style = "basic-tree";
        }

        Map<String, Object> cb = new HashMap<>();
        cb.put("style", style);

        HashMap<String, Object> hashMapStringObject = new HashMap<>();
        hashMapStringObject.put("NodeBegin", cb);
        this.output.pushScreen(hashMapStringObject);

        String pkName = node.getPkName(context);
        String entityId;
        String entryName = node.getEntryName();
        if (UtilValidate.isNotEmpty(entryName)) {
            Map<String, String> map = UtilGenerics.checkMap(context.get(entryName));
            entityId = map.get(pkName);
        } else {
            entityId = (String) context.get(pkName);
        }
        boolean hasChildren = node.hasChildren(context);

        // check to see if this node needs to be expanded.
        if (hasChildren && node.isExpandCollapse()) {
            // FIXME: Using a widget model in this way is an ugly hack.
            ModelTree.ModelNode.Link expandCollapseLink = null;
            String targetEntityId = null;
            List<String> targetNodeTrail = UtilGenerics.toList(context.get("targetNodeTrail"));
            if (depth < targetNodeTrail.size()) {
                targetEntityId = targetNodeTrail.get(depth);
            }

            int openDepth = node.getModelTree().getOpenDepth();
            if (depth >= openDepth && (targetEntityId == null || !targetEntityId.equals(entityId))) {
                // Not on the trail
                if (node.showPeers(depth, context)) {
                    context.put("processChildren", Boolean.FALSE);
                    currentNodeTrailPiped = StringUtil.join(currentNodeTrail, "|");
                    StringBuilder target = new StringBuilder(node.getModelTree().getExpandCollapseRequest(context));
                    String trailName = node.getModelTree().getTrailName(context);
                    if (target.indexOf("?") < 0) {
                        target.append("?");
                    } else {
                        target.append("&");
                    }
                    target.append(trailName).append("=").append(currentNodeTrailPiped);
                    expandCollapseLink = new ModelTree.ModelNode.Link("collapsed", target.toString(), " ");
                }
            } else {
                context.put("processChildren", Boolean.TRUE);
                String lastContentId = currentNodeTrail.remove(currentNodeTrail.size() - 1);
                currentNodeTrailPiped = StringUtil.join(currentNodeTrail, "|");
                if (currentNodeTrailPiped == null) {
                    currentNodeTrailPiped = "";
                }
                StringBuilder target = new StringBuilder(node.getModelTree().getExpandCollapseRequest(context));
                String trailName = node.getModelTree().getTrailName(context);
                if (target.indexOf("?") < 0) {
                    target.append("?");
                } else {
                    target.append("&");
                }
                target.append(trailName).append("=").append(currentNodeTrailPiped);
                expandCollapseLink = new ModelTree.ModelNode.Link("expanded", target.toString(), " ");
                // add it so it can be remove in renderNodeEnd
                currentNodeTrail.add(lastContentId);
            }
            if (expandCollapseLink != null) {
                renderLink(writer, context, expandCollapseLink);
            }
        } else if (!hasChildren) {
            context.put("processChildren", Boolean.FALSE);
            ModelTree.ModelNode.Link expandCollapseLink = new ModelTree.ModelNode.Link("leafnode", "", " ");
            renderLink(writer, context, expandCollapseLink);
        }
    }

    public void renderNodeEnd(Appendable writer, Map<String, Object> context, ModelTree.ModelNode node) {
        Boolean processChildren = (Boolean) context.get("processChildren");
        Map<String, Object> cb = new HashMap<>();
        cb.put("processChildren", Boolean.toString(processChildren));
        cb.put("isRootNode", Boolean.toString(node.isRootNode()));
        HashMap<String, Object> hashMapStringObject = new HashMap<>();
        hashMapStringObject.put("NodeEnd", cb);
        this.output.popScreen(hashMapStringObject);
    }

    public void renderLastElement(Appendable writer, Map<String, Object> context, ModelTree.ModelNode node) {
        Boolean processChildren = (Boolean) context.get("processChildren");
        if (processChildren) {
            Map<String, Object> cb = new HashMap<>();
            cb.put("style", "basic-tree");
            HashMap<String, Object> hashMapStringObject = new HashMap<>();
            hashMapStringObject.put("LastElement", cb);
            this.output.putScreen(hashMapStringObject);
        }
    }

    public void renderLabel(Appendable writer, Map<String, Object> context, ModelTree.ModelNode.Label label) {
        String id = label.getId(context);
        String style = label.getStyle(context);
        String labelText = label.getText(context);

        Map<String, Object> cb = new HashMap<>();
        cb.put("id", id);
        cb.put("style", style);
        cb.put("labelText", labelText);
        HashMap<String, Object> hashMapStringObject = new HashMap<>();
        hashMapStringObject.put("Label", cb);
        this.output.putScreen(hashMapStringObject);
    }

    public void renderLink(Appendable writer, Map<String, Object> context, ModelTree.ModelNode.Link link) throws IOException {
        String target = link.getTarget(context);
        StringBuilder linkUrl = new StringBuilder();
        HttpServletResponse response = (HttpServletResponse) context.get("response");
        HttpServletRequest request = (HttpServletRequest) context.get("request");

        if (UtilValidate.isNotEmpty(target)) {
            WidgetWorker.buildHyperlinkUrl(linkUrl, target, link.getUrlMode(), link.getParameterMap(context), link.getPrefix(context),
                    link.getFullPath(), link.getSecure(), link.getEncode(), request, response, context);
        }

        String id = link.getId(context);
        String style = link.getStyle(context);
        String name = link.getName(context);
        String title = link.getTitle(context);
        String targetWindow = link.getTargetWindow(context);
        String linkText = link.getText(context);

        String imgStr = "";
        ModelTree.ModelNode.Image img = link.getImage();
        if (img != null) {
            StringWriter sw = new StringWriter();
            renderImage(writer, context, img);
            imgStr = sw.toString();
        }

        Map<String, Object> cb = new HashMap<>();
        cb.put("id", id);
        cb.put("style", style);
        cb.put("name", name);
        cb.put("title", title);
        cb.put("targetWindow", targetWindow);
        cb.put("linkUrl", linkUrl);
        cb.put("linkText", linkText);
        cb.put("imgStr", imgStr.replaceAll("\"", "\\\\\""));
        HashMap<String, Object> hashMapStringObject = new HashMap<>();
        hashMapStringObject.put("Link", cb);
        this.output.putScreen(hashMapStringObject);
    }

    public void renderImage(Appendable writer, Map<String, Object> context, ModelTree.ModelNode.Image image) {
        if (image == null) {
            return;
        }
        HttpServletResponse response = (HttpServletResponse) context.get("response");
        HttpServletRequest request = (HttpServletRequest) context.get("request");

        String urlMode = image.getUrlMode();
        String src = image.getSrc(context);
        String id = image.getId(context);
        String style = image.getStyle(context);
        String wid = image.getWidth(context);
        String hgt = image.getHeight(context);
        String border = image.getBorder(context);
        String alt = ""; //TODO add alt to tree images image.getAlt(context);

        String urlString = "";

        if ("intra-app".equalsIgnoreCase(urlMode)) {
            if (request != null && response != null) {
                ServletContext ctx = (ServletContext) request.getAttribute("servletContext");
                RequestHandler rh = (RequestHandler) ctx.getAttribute("_REQUEST_HANDLER_");
                urlString = rh.makeLink(request, response, src, false, false, false);
            } else {
                urlString = src;
            }
        } else  if ("content".equalsIgnoreCase(urlMode)) {
            if (request != null && response != null) {
                StringBuilder newURL = new StringBuilder();
                ContentUrlTag.appendContentPrefix(request, newURL);
                newURL.append(src);
                urlString = newURL.toString();
            }
        } else {
            urlString = src;
        }
        Map<String, Object> cb = new HashMap<>();
        cb.put("src", src);
        cb.put("id", id);
        cb.put("style", style);
        cb.put("wid", wid);
        cb.put("hgt", hgt);
        cb.put("border", border);
        cb.put("alt", alt);
        cb.put("urlString", urlString);
        HashMap<String, Object> hashMapStringObject = new HashMap<>();
        hashMapStringObject.put("Image", cb);
        this.output.putScreen(hashMapStringObject);
    }

    public ScreenStringRenderer getScreenStringRenderer(Map<String, Object> context) {
        ScreenRenderer screenRenderer = (ScreenRenderer)context.get("screens");
        if (screenRenderer != null) {
            return screenRenderer.getScreenStringRenderer();
        }
        return null;
    }
}
