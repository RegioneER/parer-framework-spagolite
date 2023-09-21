/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

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
