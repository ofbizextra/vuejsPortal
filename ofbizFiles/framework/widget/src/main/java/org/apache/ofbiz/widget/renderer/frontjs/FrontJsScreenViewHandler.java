
package org.apache.ofbiz.widget.renderer.frontjs;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.apache.ofbiz.base.util.*;
import org.apache.ofbiz.base.util.collections.MapStack;
//import org.apache.ofbiz.entity.GenericEntity;
import org.apache.ofbiz.webapp.view.AbstractViewHandler;
import org.apache.ofbiz.webapp.view.ViewHandlerException;
import org.apache.ofbiz.widget.renderer.*;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FrontJsScreenViewHandler extends AbstractViewHandler {

    public static final String module = FrontJsScreenViewHandler.class.getName();

    protected ServletContext servletContext = null;
    @JsonFilter("dynamicFilter")
    public class DynamicMixIn {
    }

    public void init(ServletContext context) {
        this.servletContext = context;
    }

    private ScreenStringRenderer loadRenderers(LinkedHashMap<String, Object> output,
                       HttpServletRequest request, HttpServletResponse response,
                                            Map<String, Object> context) {
        ScreenStringRenderer screenStringRenderer = new FrontJsScreenRenderer(getName(), output);
        FormStringRenderer formStringRenderer = new FrontJsFormRenderer(output, request, response);
        context.put("formStringRenderer", formStringRenderer);

        TreeStringRenderer treeStringRenderer = new FrontJsTreeRenderer(output);
        context.put("treeStringRenderer", treeStringRenderer);
        MenuStringRenderer menuStringRenderer = new FrontJsMenuRenderer(output, request, response);
        context.put("menuStringRenderer", menuStringRenderer);
        return screenStringRenderer;
    }

    public void render(String name, String page, String info, String contentType, String encoding, HttpServletRequest request, HttpServletResponse response) throws ViewHandlerException {
        try {
            Writer writer = response.getWriter();
            VisualTheme visualTheme = UtilHttp.getVisualTheme(request);

            LinkedHashMap<String, Object> output = new LinkedHashMap<>();
            LinkedHashMap<String, Object> screenOutput = new LinkedHashMap<>();
            MapStack<String> context = MapStack.create();
            //output.put("data", context);
            ScreenRenderer.populateContextForRequest(context, null, request, response, servletContext);
            ScreenStringRenderer screenStringRenderer = loadRenderers(screenOutput, request, response, context);
            ScreenRenderer screens = new ScreenRenderer(writer, context, screenStringRenderer);
            context.put("screens", screens);
            // prepate context data that sould be serialized to client
            Map<String, Object> data = new HashMap<>();
            //output.put("data", context);
            List<String> toExclude = UtilMisc.toList("globalContext", "request", "session", "rootDir", "security",
            "checkLoginUrl", "screens", "javaScriptEnabled", "https", "sessionAttributes",
            "eventMessageList", "externalKeyParam", "webSiteId", "controlPath",
            "delegator", "Request", "timeZone", "JspTaglibs", "contextRoot",
            "serverRoot", "application", "person", "response", "modelTheme", "dispatcher",
            "webappName", "nullField", "screens", "formStringRenderer", "treeStringRenderer",
            "menuStringRenderer", "requestAttributes", "Application", "entityName", "modelReader");
            for(String key : context.keySet()) {
                if ( !toExclude.contains(key) ) {
                    data.put(key, context.get(key));
                }
            }
            context.put("simpleEncoder", UtilCodec.getEncoder(visualTheme.getModelTheme().getEncoder(getName())));
            output.put("viewScreenName", page);
            output.put("viewScreen", screenOutput);

            screenStringRenderer.renderScreenBegin(writer, context);
            screens.render(page);
            screenStringRenderer.renderScreenEnd(writer, context);

            /*
            JSON json = JSON.from(output);
            String jsonStr = json.toString();
            */
            //output.put("data", data);
            ObjectMapper mapper = new ObjectMapper();
            //mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            StringWriter jsonWriter = new StringWriter();
            String[] propertiesToExclude = toExclude.toArray(new String[toExclude.size()]);
            mapper.addMixIn(Object.class, DynamicMixIn.class);
            FilterProvider filterProvider = new SimpleFilterProvider()
                    .addFilter("dynamicFilter", SimpleBeanPropertyFilter.serializeAllExcept(propertiesToExclude));
            mapper.setFilterProvider(filterProvider);
            /*
            SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
                    .serializeAllExcept("intValue");
            */
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.writeValue(jsonWriter, output);
            String jsonStr = jsonWriter.toString();
            response.setContentType("application/json");
            // jsonStr.length is not reliable for unicode characters
            response.setContentLength(jsonStr.getBytes("UTF8").length);
            writer.write(jsonStr);

            writer.flush();
        } catch (IOException e) {
            throw new ViewHandlerException("Error in the response writer/output stream: " + e.toString(), e);
        } catch (SAXException | ParserConfigurationException e) {
            throw new ViewHandlerException("XML Error rendering page: " + e.toString(), e);
        } catch (GeneralException e) {
            throw new ViewHandlerException("Lower level error rendering page: " + e.toString(), e);
        }
    }
}
