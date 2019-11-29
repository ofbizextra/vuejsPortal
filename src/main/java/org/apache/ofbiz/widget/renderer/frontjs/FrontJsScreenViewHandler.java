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
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.ofbiz.base.lang.JSON;
import org.apache.ofbiz.base.util.GeneralException;
import org.apache.ofbiz.base.util.UtilCodec;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.collections.MapStack;
import org.apache.ofbiz.webapp.view.AbstractViewHandler;
import org.apache.ofbiz.webapp.view.ViewHandlerException;
import org.apache.ofbiz.widget.renderer.FormStringRenderer;
import org.apache.ofbiz.widget.renderer.MenuStringRenderer;
import org.apache.ofbiz.widget.renderer.ScreenRenderer;
import org.apache.ofbiz.widget.renderer.ScreenStringRenderer;
import org.apache.ofbiz.widget.renderer.TreeStringRenderer;
import org.apache.ofbiz.widget.renderer.VisualTheme;
import org.xml.sax.SAXException;

//import org.apache.ofbiz.entity.GenericEntity;

public class FrontJsScreenViewHandler extends AbstractViewHandler {

    public static final String module = FrontJsScreenViewHandler.class.getName();

    protected ServletContext servletContext = null;

    public void init(ServletContext context) {
        this.servletContext = context;
    }

    private ScreenStringRenderer loadRenderers(FrontJsOutput output,
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
/*
    private ArrayList<Map<String, Object>> parseViewScreen(ArrayList<Map<String, Object>> arrayListMap) {
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        HashMap<String, Object> temp;
        Iterator iterator = arrayListMap.iterator();
        while (iterator.hasNext()) {
            Map it = (Map) iterator.next();
            Map.Entry entry = (Map.Entry) it.entrySet().toArray()[0];
            temp = new HashMap<>();
            if (entry.getKey().toString().contains("Open")) {
                temp.put("fieldType", entry.getKey().toString().replace("Open", ""));
                temp.put("attributes", entry.getValue());
                temp.put("children", this.parseViewScreenRecursive(iterator, entry));
                result.add(temp);
            } else {
                temp.put("fieldType", entry.getKey());
                temp.put("attributes", entry.getValue());
                temp.put("children", new ArrayList<>());
                result.add(temp);
            }
        }
        return result;
    }

    private ArrayList<Map<String, Object>> parseViewScreenRecursive(Iterator iterator, Map.Entry parent) {
        ArrayList<Map<String, Object>> arrayListMap = new ArrayList<>();
        HashMap<String, Object> temp;
        String parentName = (String) parent.getKey().toString().replace("Open", "");
        while (iterator.hasNext()) {
            temp = new HashMap<>();
            Map currentMap = (Map) iterator.next();
            Map.Entry current = (Map.Entry) currentMap.entrySet().toArray()[0];
            if (current.getKey().toString().contains(parentName.concat("Close"))) {
                return arrayListMap;
            }
            if (current.getKey().toString().contains("Open")) {
                temp.put("fieldType", current.getKey().toString().replace("Open", ""));
                temp.put("attributes", current.getValue());
                temp.put("children", this.parseViewScreenRecursive(iterator, current));
            } else {
                temp.put("fieldType", current.getKey());
                temp.put("attributes", current.getValue());
                temp.put("children", new ArrayList<>());
            }
            arrayListMap.add(temp);
        }
        return arrayListMap;
    }

    private Map<String, Object> parseData(ArrayList<Map<String, Object>> arrayListMap) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> currentEntity = new HashMap<>();
        for (Map<String, Object> current : arrayListMap) {
            if (((Map<String, Object>) current.get("attributes")).get("entityName") != null) {
                currentEntity = new HashMap<>();
                currentEntity.put("entityName", ((Map<String, Object>) current.get("attributes")).get("entityName").toString());
                data.put(((Map<String, Object>) current.get("attributes")).get("entityName").toString(), currentEntity);
            }
            for (Map<String, Object> child : (ArrayList<Map<String, Object>>) current.get("children")) {
                this.parseDataRecursive(child, currentEntity, data);
            }
        }
        return data;
    }

    private void parseDataRecursive(Map<String, Object> current, Map<String, Object> currentEntity, Map<String, Object> data) {
        if (current.get("attributes") != null && ((Map<String, Object>) current.get("attributes")).get("entityName") != null) {
            currentEntity = new HashMap<>();
            currentEntity.put("entityName", ((Map<String, Object>) current.get("attributes")).get("entityName").toString());
            data.put(((Map<String, Object>) current.get("attributes")).get("entityName").toString(), currentEntity);
        }
        if (current.get("attributes") != null && ((Map<String, Object>) current.get("attributes")).get("data") != null) {
            currentEntity.putAll((Map<String, Object>) ((Map<String, Object>) current.get("attributes")).get("data"));
        }
        for (Map<String, Object> child : (ArrayList<Map<String, Object>>) current.get("children")) {
            this.parseDataRecursive(child, currentEntity, data);
        }
        return;
    }
*/
    public void render(String name, String page, String info, String contentType, String encoding, HttpServletRequest request, HttpServletResponse response) throws ViewHandlerException {
        try {
            Writer writer = response.getWriter();
            VisualTheme visualTheme = UtilHttp.getVisualTheme(request);
            FrontJsOutput frontJsOutput = new FrontJsOutput(name);
            MapStack<String> context = MapStack.create();
            //output.put("data", context);
            ScreenRenderer.populateContextForRequest(context, null, request, response, servletContext);
            ScreenStringRenderer screenStringRenderer = loadRenderers(frontJsOutput, request, response, context);
            ScreenRenderer screens = new ScreenRenderer(writer, context, screenStringRenderer);
            context.put("screens", screens);

            context.put("simpleEncoder", UtilCodec.getEncoder(visualTheme.getModelTheme().getEncoder(getName())));

            screenStringRenderer.renderScreenBegin(writer, context);
            screens.render(page);
            screenStringRenderer.renderScreenEnd(writer, context);

            JSON json = JSON.from(frontJsOutput.output());
            String jsonStr = json.toString();

            // set the JSON content type
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
