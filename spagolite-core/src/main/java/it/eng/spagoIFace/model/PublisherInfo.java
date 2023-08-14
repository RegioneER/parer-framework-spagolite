package it.eng.spagoIFace.model;

import org.codehaus.jettison.json.JSONObject;

public class PublisherInfo {

    public enum Override {
        forward, actionForward, actionRedirect, actionProfiledRedirect, redirect, ajaxRedirect, freeze,
        forwardSkipSetLast
    }

    private Override type;
    private String destination;
    private JSONObject jsonObject;

    public Override getType() {
        return type;
    }

    public void setType(Override type) {
        this.type = type;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public PublisherInfo(Override type, String destination, JSONObject jsonObject) {
        this.type = type;
        this.destination = destination;
        this.jsonObject = jsonObject;
    }

}
