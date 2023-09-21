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

package it.eng.spagoLite.form;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.FrameElementInterface;

import org.codehaus.jettison.json.JSONObject;
import org.dom4j.Element;

public interface Component extends Cloneable, FrameElementInterface {
    public Component getParent();

    public String getName();

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

    public Element asXml();

    public JSONObject asJSON() throws EMFError;
}
