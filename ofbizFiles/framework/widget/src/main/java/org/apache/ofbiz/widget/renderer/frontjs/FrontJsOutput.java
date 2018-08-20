package org.apache.ofbiz.widget.renderer.frontjs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class FrontJsOutput {
    Map<String, Object> output = new HashMap<>();
    String viewScreenName = "";
    ArrayList<Map<String, Object>> viewScreen = new ArrayList<>();
    Map<String, Object> viewEntities = new HashMap<>();
    // Map<String, Object> currentScreen = new HashMap<>();
    // Map<String, Object> currentEntity = new HashMap<>();
    // Map<String, Object> currentRecord = new HashMap<>();
    Stack<ArrayList<Map<String, Object>>> screensStack = new Stack();
    Stack<Map<String, Object>> entitiesStack = new Stack();
    Stack<Map<String, Object>> recordsStack = new Stack();

    public FrontJsOutput() {
        output.put("viewScreenName", viewScreenName);
        output.put("viewScreen", viewScreen);
        output.put("viewEntities", viewEntities);
        screensStack.push(viewScreen);
    }

    public void putScreen(Map<String, Object> screen) {
        screensStack.peek().add(screen);
        if (screen.containsKey("attributes") && ((Map<String, Object>) screen.get("attributes")).containsKey("data") && ((Map<String, Object>) ((Map<String, Object>) screen.get("attributes")).get("data")).containsKey("action") && ((Map<String, Object>) ((Map<String, Object>) screen.get("attributes")).get("data")).get("action").equals("PUT_RECORD")) {
            this.putRecord((Map<String, Object>) ((Map<String, Object>) ((Map<String, Object>) screen.get("attributes")).get("data")).get("records"));
        }
    }

    public void pushScreen(Map<String, Object> screen) {
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

    public void popScreen(Map<String, Object> screen) {
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

    public void putEntity(String entityName, String primaryKey) {
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

    public void popEntity() {
        entitiesStack.pop();
    }

    public void newRecord() {
        // currentRecord
        recordsStack.push(new HashMap<>());
    }

    public void storeRecord() {
        entitiesStack.peek().put((String) recordsStack.peek().get(entitiesStack.peek().get("primaryKey")), recordsStack.peek());
        recordsStack.pop();
    }

    public void putRecord(Map<String, Object> records) {
        for (String key : records.keySet()) {
            recordsStack.peek().put(key, records.get(key));
        }
    }
}