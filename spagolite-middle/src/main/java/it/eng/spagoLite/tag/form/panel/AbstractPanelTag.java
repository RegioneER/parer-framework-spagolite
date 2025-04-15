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

package it.eng.spagoLite.tag.form.panel;

import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.form.list.List;
import it.eng.spagoLite.tag.form.BaseFormTag;

public class AbstractPanelTag extends BaseFormTag<List<SingleValueField<?>>> {
    private static final long serialVersionUID = 1L;

    protected static final String LOAD_ENTITA = "loadEntita";
    protected static final String LOAD_ENTITA_DETAIL = "loadDetail";
    public static final String TIPO_ENTITA = "tipoEntita";
    public static final String ID_ENTITA = "IdEntita";
    public static final String DT_INIZIO = "DtInizio";
    public static final String AJAX = "ajax";

    protected String getLink(String navigationEvent, String additionalInfo) {
	if (navigationEvent != null) {
	    if (additionalInfo == null) {
		return getOperationUrl("panelNavigationOnClick",
			"panel=" + getName() + "&amp;navigationEvent=" + navigationEvent);
	    } else {
		return getOperationUrl("panelNavigationOnClick",
			"panel=" + getName() + "&amp;navigationEvent=" + navigationEvent) + "&amp;"
			+ additionalInfo;
	    }
	}

	return "#";
    }

}
