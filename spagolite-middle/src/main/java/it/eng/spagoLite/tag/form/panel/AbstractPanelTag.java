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
                        "panel=" + getName() + "&amp;navigationEvent=" + navigationEvent) + "&amp;" + additionalInfo;
            }
        }

        return "#";
    }

}
