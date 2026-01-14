/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna <p/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version. <p/> This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. <p/> You should
 * have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
 */

package it.eng.spagoLite.form.fields.impl;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.xmlbean.form.Field.Type.Enum;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Link<O> extends SingleValueField<O> {

    private static final long serialVersionUID = 1L;
    private String target;
    private String tooltip;
    private boolean targetAList;
    private String visibilityProperty = null;
    private String externalLinkParamApplic = null;
    private String externalLinkParamId = null;
    private String genericLinkId = null;

    public Link(Component parent, String name, String description, String alias, Enum type,
            String format, boolean required, boolean hidden, boolean readonly, boolean trigger) {
        super(parent, name, description, alias, type, format, required, hidden, readonly, trigger);
    }

    public Link(Component parent, String name, String description, String alias, Enum type,
            String format, boolean required, boolean hidden, boolean readonly, boolean trigger,
            String targetList) {
        super(parent, name, description, alias, type, format, required, hidden, readonly, trigger);
        this.target = targetList;
    }

    public Link(Component parent, String name, String description, String alias, Enum type,
            String format, boolean required, boolean hidden, boolean readonly, boolean trigger,
            String targetList, String visibilityProperty) {
        super(parent, name, description, alias, type, format, required, hidden, readonly, trigger);
        this.target = targetList;
        this.visibilityProperty = visibilityProperty;
    }

    public Link(Component parent, String name, String description, String alias, Enum type,
            String format, boolean required, boolean hidden, boolean readonly, boolean trigger,
            String targetList, String tooltip, boolean targetAList) {
        super(parent, name, description, alias, type, format, required, hidden, readonly, trigger);
        this.target = targetList;
        this.tooltip = tooltip;
        this.targetAList = targetAList;
    }

    public Link(Component parent, String name, String description, String alias, Enum type,
            String format, boolean required, boolean hidden, boolean readonly, boolean trigger,
            String targetList, String tooltip, boolean targetAList, String visibilityProperty) {
        super(parent, name, description, alias, type, format, required, hidden, readonly, trigger);
        this.target = targetList;
        this.tooltip = tooltip;
        this.targetAList = targetAList;
        this.visibilityProperty = visibilityProperty;
    }

    public Link(Component parent, String name, String description, String alias, Enum type,
            String format, boolean required, boolean hidden, boolean readonly, boolean trigger,
            String targetList, String tooltip, boolean targetAList, String visibilityProperty,
            String externalLinkParamApplic, String externalLinkParamId) {
        super(parent, name, description, alias, type, format, required, hidden, readonly, trigger);
        this.target = targetList;
        this.tooltip = tooltip;
        this.targetAList = targetAList;
        this.visibilityProperty = visibilityProperty;
        this.externalLinkParamApplic = externalLinkParamApplic;
        this.externalLinkParamId = externalLinkParamId;
    }

    public Link(Component parent, String name, String description, String alias, Enum type,
            String format, boolean required, boolean hidden, boolean readonly, boolean trigger,
            String targetList, String tooltip, boolean targetAList, String visibilityProperty,
            String externalLinkParamApplic, String externalLinkParamId, String genericLinkId) {
        super(parent, name, description, alias, type, format, required, hidden, readonly, trigger);
        this.target = targetList;
        this.tooltip = tooltip;
        this.targetAList = targetAList;
        this.visibilityProperty = visibilityProperty;
        this.externalLinkParamApplic = externalLinkParamApplic;
        this.externalLinkParamId = externalLinkParamId;
        this.genericLinkId = genericLinkId;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public JSONObject asJSON() throws EMFError {
        JSONObject json = super.asJSON();
        try {
            json.put("type", "Link");
        } catch (JSONException e) {
            throw new EMFError(EMFError.ERROR, "Eccezione nella crezione dell'oggetto JSON", e);
        }
        return json;
    }

    @Override
    public void reset() {
        this.setValue(null);
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public boolean isTargetAList() {
        return targetAList;
    }

    public void setTargetAList(boolean targetAList) {
        this.targetAList = targetAList;
    }

    public String getVisibilityProperty() {
        return visibilityProperty;
    }

    public void setVisibilityProperty(String visibilityProperty) {
        this.visibilityProperty = visibilityProperty;
    }

    /**
     * @return the externalLinkParamApplic
     */
    public String getExternalLinkParamApplic() {
        return externalLinkParamApplic;
    }

    /**
     * @param externalLinkParamApplic the externalLinkParamApplic to set
     */
    public void setExternalLinkParamApplic(String externalLinkParamApplic) {
        this.externalLinkParamApplic = externalLinkParamApplic;
    }

    /**
     * @return the externalLinkParamId
     */
    public String getExternalLinkParamId() {
        return externalLinkParamId;
    }

    /**
     * @param externalLinkParamId the externalLinkParamId to set
     */
    public void setExternalLinkParamId(String externalLinkParamId) {
        this.externalLinkParamId = externalLinkParamId;
    }

    public void setGenericLinkId(String genericLinkId) {
        this.genericLinkId = genericLinkId;
    }

    public String getGenericLinkId() {
        return genericLinkId;
    }

}
