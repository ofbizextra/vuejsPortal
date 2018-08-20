package org.apache.ofbiz.widget.renderer.frontjs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class FrontJsOutput {
    Map<String, Object> output = new HashMap<>();
    private ArrayList<Map<String, Object>> viewScreen = new ArrayList<>();
    private Map<String, Object> viewEntities = new HashMap<>();
    private Stack<ArrayList<Map<String, Object>>> screensStack;
    private Stack<Map<String, Object>> entitiesStack;
    private Stack<Map<String, Object>> recordsStack;

    FrontJsOutput(String name) {
        output.put("viewScreenName", name);
        output.put("viewScreen", viewScreen);
        output.put("viewEntities", viewEntities);
        screensStack = new Stack<>();
        entitiesStack = new Stack<>();
        recordsStack = new Stack<>();
        screensStack.push(viewScreen);
    }

    void putScreen(Map<String, Object> screen) {
        screensStack.peek().add(screen);
        if (screen.containsKey("attributes") && ((Map<String, Object>) screen.get("attributes")).containsKey("data") && ((Map<String, Object>) ((Map<String, Object>) screen.get("attributes")).get("data")).containsKey("action") && ((Map<String, Object>) ((Map<String, Object>) screen.get("attributes")).get("data")).get("action").equals("PUT_RECORD")) {
            this.putRecord((Map<String, Object>) ((Map<String, Object>) ((Map<String, Object>) screen.get("attributes")).get("data")).get("records"));
        }
    }

    void pushScreen(Map<String, Object> screen) {
        screensStack.peek().add(screen);
        screensStack.push((ArrayList<Map<String, Object>>) screen.get("children"));
        if (screen.containsKey("attributes") && ((Map<String, Object>) screen.get("attributes")).containsKey("data") && ((Map<String, Object>) ((Map<String, Object>) screen.get("attributes")).get("data")).containsKey("action")) {
            if (((Map<String, Object>) ((Map<String, Object>) screen.get("attributes")).get("data")).get("action").equals("NEW_RECORD")) {
                this.newRecord();
            }
            if (((Map<String, Object>) ((Map<String, Object>) screen.get("attributes")).get("data")).get("action").equals("PUT_ENTITY")) {
                Map<String, Object> data = (Map<String, Object>) ((Map<String, Object>) screen.get("attributes")).get("data");
                this.putEntity((String) data.get("entityName"), (String) data.get("primaryKey"));
            }
        }
    }

    void popScreen(Map<String, Object> screen) {
        screensStack.pop();
        if (screen.containsKey("attributes") && ((Map<String, Object>) screen.get("attributes")).containsKey("data") && ((Map<String, Object>) ((Map<String, Object>) screen.get("attributes")).get("data")).containsKey("action")) {
            if (((Map<String, Object>) ((Map<String, Object>) screen.get("attributes")).get("data")).get("action").equals("STORE_RECORD")) {
                this.storeRecord();
            }
            if (((Map<String, Object>) ((Map<String, Object>) screen.get("attributes")).get("data")).get("action").equals("POP_ENTITY")) {
                this.popEntity();
            }
        }
    }

    private void putEntity(String entityName, String primaryKey) {
        // ou modelReader et utiliser la fonction de olivier dans riot
        Map<String, Object> entity;

        if (!viewEntities.containsKey(entityName)) {
            entity = new HashMap<>();
            entity.put("primaryKey", primaryKey);
            entity.put("list", new HashMap<>());
            viewEntities.put(entityName, entity);
        } else {
            entity = (Map<String, Object>) viewEntities.get(entityName);
        }
        entitiesStack.push(entity);
    }

    private void popEntity() {
        entitiesStack.pop();
    }

    private void newRecord() {
        // currentRecord
        recordsStack.push(new HashMap<>());
    }

    private void storeRecord() {
        entitiesStack.peek().put((String) recordsStack.peek().get(entitiesStack.peek().get("primaryKey")), recordsStack.peek());
        recordsStack.pop();
    }

    private void putRecord(Map<String, Object> records) {
        for (String key : records.keySet()) {
            recordsStack.peek().put(key, records.get(key));
        }
    }

    public Map<String, Object> output() {
        return this.output;
    }
}