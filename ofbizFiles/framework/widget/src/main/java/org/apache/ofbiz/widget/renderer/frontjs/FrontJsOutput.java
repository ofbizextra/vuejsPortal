package org.apache.ofbiz.widget.renderer.frontjs;

import org.omg.CORBA.OBJ_ADAPTER;

import java.util.*;

public class FrontJsOutput {
    Map<String, Object> output = new HashMap<>();
    private ArrayList<Map<String, Object>> viewScreen;
    private Map<String, Object> viewEntities = new HashMap<>();
    private Stack<ArrayList<Map<String, Object>>> screensStack;
    private Stack<Map<String, Object>> entitiesStack;
    private Stack<Map<String, Object>> recordsStack;

    FrontJsOutput(String name) {
        viewScreen = new ArrayList<>();
        output.put("viewScreenName", name);
        output.put("viewScreen", viewScreen);
        output.put("viewEntities", viewEntities);
        screensStack = new Stack<>();
        entitiesStack = new Stack<>();
        recordsStack = new Stack<>();
        screensStack.push(viewScreen);
    }

    void putScreen(Map<String, Object> screen) {
        Map<String, Object> temp = new HashMap<>();
        String name = screen.keySet().toArray()[0].toString();
        temp.put("attributes", screen.get(name));
        temp.put("name", name);
        screen = temp;
        screensStack.peek().add(screen);
        if (screen.containsKey("attributes") && ((Map<String, Object>) screen.get("attributes")).containsKey("data") && ((Map<String, Object>) ((Map<String, Object>) screen.get("attributes")).get("data")).containsKey("action") && ((Map<String, Object>) ((Map<String, Object>) screen.get("attributes")).get("data")).get("action").equals("PUT_RECORD")) {
            this.putRecord((ArrayList<Map<String, Object>>) ((Map<String, Object>) ((Map<String, Object>) screen.get("attributes")).get("data")).get("records"));
        }
    }

    void pushScreen(Map<String, Object> screen) {
        Map<String, Object> temp = new HashMap<>();
        String name = screen.keySet().toArray()[0].toString();
        temp.put("attributes", screen.get(name));
        temp.put("children", new ArrayList<Map<String, Object>>());
        temp.put("name", name.replace("Open", ""));
        screen = temp;
        screensStack.peek().add(screen);
        screensStack.push((ArrayList<Map<String, Object>>) screen.get("children"));
        if (screen.containsKey("attributes") && ((Map<String, Object>) screen.get("attributes")).containsKey("data") && ((Map<String, Object>) ((Map<String, Object>) screen.get("attributes")).get("data")).containsKey("action")) {
            if (((Map<String, Object>) ((Map<String, Object>) screen.get("attributes")).get("data")).get("action").equals("NEW_RECORD")) {
                this.newRecord();
            }
            if (((Map<String, Object>) ((Map<String, Object>) screen.get("attributes")).get("data")).get("action").equals("PUT_ENTITY")) {
                Map<String, Object> data = (Map<String, Object>) ((Map<String, Object>) screen.get("attributes")).get("data");
                this.putEntity((String) data.get("entityName"), (List<String>) data.get("primaryKey"));
            }
        }
    }

    void popScreen(Map<String, Object> screen) {
        screensStack.pop();
        String name = screen.keySet().toArray()[0].toString();
        if (((Map<String, Object>) screen.get(name)).containsKey("data") && ((Map<String, Object>) ((Map<String, Object>) screen.get(name)).get("data")).containsKey("action")) {
            if (((Map<String, Object>) ((Map<String, Object>) screen.get(name)).get("data")).get("action").equals("STORE_RECORD")) {
                this.storeRecord();
            }
            if (((Map<String, Object>) ((Map<String, Object>) screen.get(name)).get("data")).get("action").equals("POP_ENTITY")) {
                this.popEntity();
            }
        }
    }

    private void putEntity(String entityName, List<String> primaryKey) {
        // ou modelReader et utiliser la fonction de olivier dans riot
        Map<String, Object> entity;
        if (!viewEntities.containsKey(entityName)) {
            entity = new HashMap<>();
            entity.put("primaryKey", String.join("--", primaryKey));
            entity.put("list", new HashMap<>());
            entity.put("entityName", entityName);
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
        if (!entitiesStack.empty()) {
            recordsStack.push(new HashMap<>());
        }
    }

    private void storeRecord() {
        if (!recordsStack.empty() && recordsStack.peek().get(entitiesStack.peek().get("primaryKey")) != null) {
            ((Map<String, Object>) entitiesStack.peek().get("list")).put((String) recordsStack.peek().get(entitiesStack.peek().get("primaryKey")), recordsStack.peek());
            recordsStack.pop();
        }
    }

    private void putRecord(ArrayList<Map<String, Object>> records) {
        for (Map<String, Object> record : records) {
            if (!recordsStack.empty()) {
                recordsStack.peek().put((String) record.get("key"), record.get("value"));
            }
        }
    }

    public Map<String, Object> output() {
        return this.output;
    }

    public Map<String, Object> getRecordPointer(Map<String, Object> context) {
        if (!this.entitiesStack.empty()) {
            Map<String, Object> data = new HashMap<>();
            String entityName = (String) this.entitiesStack.peek().get("entityName");
            data.put("entity", entityName);
            String primaryKey = ((Map<String, Object>) this.viewEntities.get(entityName)).get("primaryKey").toString();
            data.put("id", context.get(primaryKey));
            return data;
        } else {
            return null;
        }
    }
}