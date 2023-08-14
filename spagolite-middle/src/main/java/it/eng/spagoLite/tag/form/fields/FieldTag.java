package it.eng.spagoLite.tag.form.fields;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoCore.util.JavaScript;
import it.eng.spagoIFace.Values;
import it.eng.spagoLite.db.decodemap.DecodeMapIF;
import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.fields.Field;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.form.fields.impl.Button;
import it.eng.spagoLite.form.fields.impl.CheckBox;
import it.eng.spagoLite.form.fields.impl.ComboBox;
import it.eng.spagoLite.form.fields.impl.Input;
import it.eng.spagoLite.form.fields.impl.MultiSelect;
import it.eng.spagoLite.form.fields.impl.MultiSemaphore;
import it.eng.spagoLite.form.fields.impl.Radio;
import it.eng.spagoLite.form.fields.impl.Semaphore;
import it.eng.spagoLite.form.fields.impl.TextArea;
import it.eng.spagoLite.tag.form.BaseFormTag;
import it.eng.spagoLite.tag.form.formLayout.ContainerTag;
import it.eng.spagoLite.xmlbean.form.Field.Type;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

public class FieldTag extends BaseFormTag<SingleValueField<?>> {

    private static final long serialVersionUID = 1L;

    public static final String IMG_CHECKBOX_CHECKED_FIELD = "/img/checkbox-field-on.png";
    public static final String IMG_CHECKBOX_UNCHECKED_FIELD = "/img/checkbox-field-off.png";
    public static final String IMG_CHECKBOX_WARNING_FIELD = "/img/checkbox-field-warn.png";
    public static final String IMG_CHECKBOX_CHECKED = "/img/checkbox-on.png";
    public static final String IMG_CHECKBOX_UNCHECKED = "/img/checkbox-off.png";
    public static final String IMG_CHECKBOX_WARNING = "/img/checkbox-warn.png";
    public static final String IMG_RADIO_CHECKED = "/img/radiobutton-on.png";
    public static final String IMG_RADIO_UNCHECKED = "/img/radiobutton-off.png";

    private String controlWidth;
    private String controlPosition;
    private int colSpan = 0;
    private boolean withSearchcomp = false;
    private String tooltip;

    public String getControlWidth() {
        if (controlWidth == null || controlWidth.equals("")) {

            if (ContainerTag.LEFT.equalsIgnoreCase(getControlPosition())) {
                return ContainerTag.WIDTH_60;
            } else {
                return ContainerTag.WIDTH_40;
            }
        } else {
            return controlWidth;
        }
    }

    public void setControlWidth(String controlWidth) {
        this.controlWidth = controlWidth;
    }

    public String getControlPosition() {
        if (controlPosition != null && controlPosition.equalsIgnoreCase(ContainerTag.RIGHT)) {
            return ContainerTag.RIGHT;
        } else {
            return ContainerTag.LEFT;
        }
    }

    public void setControlPosition(String controlPosition) {
        this.controlPosition = controlPosition;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            writeln(Factory.htmlField(getComponent(), getControlWidth(), getControlPosition(), getActionName(), -1,
                    isWithSearchcomp(), getTooltip()));
        } catch (EMFError e) {
            throw new JspException(e);
        }
        return SKIP_BODY;
    }

    private static String getNoscript(Field field, String actionName) {
        String parentName = ((field.getParent() != null) ? field.getParent().getName() + "." : "");
        StringBuilder out = new StringBuilder("  <noscript>\n");
        String name = parentName + JavaScript.stringToHTMLString(field.getName());
        String queryString = Values.OPERATION + Values.OPERATION_SEPARATOR + "trigger"
                + StringUtils.replace(name, ".", "") + "OnTriggerNoAjax";
        if (field.isTrigger()) {
            out.append("  <div class=\"noscript\">\n");
            out.append("   <input class=\"pulsante\" type=\"submit\" name=\"" + queryString
                    + "\" value=\"Ricarica\" />\n");
            out.append("  </div>\n");
        }
        out.append("  </noscript>\n");
        return out.toString();
    }

    private static String getTriggersInput(Field field, String value) {
        String parentName = ((field.getParent() != null) ? field.getParent().getName() + "." : "");
        String name = parentName + JavaScript.stringToHTMLString(field.getName());
        String id = parentName + JavaScript.stringToHTMLString(value);
        String triggers = null;
        if (field.isTrigger()) {
            triggers = "<input type=\"hidden\" id=\"trigger" + id + "\" name=\"trigger" + name + "\" value=\""
                    + JavaScript.stringToHTMLString(value) + "\" />\n";
        }

        return triggers;
    }

    public static class Factory {

        private static final String htmlInput(Input<?> field, String width, String position, String actionName,
                int index, String tooltip) throws EMFError {
            String name = JavaScript.stringToHTMLString(field.getName());
            String className = ContainerTag.LEFT.equalsIgnoreCase(position) ? "slText " + width
                    : "slTextRight " + width;
            String maxLength = field.getMaxLength() > 0 ? "maxlength =\"" + field.getMaxLength() + "\"" : "";
            if (field.getType() != null && field.getType().equals(Type.DATE)) {
                className += " date ";
            }
            String value = (StringUtils.isNotBlank(field.getValue())) ? field.getHtmlValue() : "";

            if (field.isHidden()) {
                className += " displayNone ";
            }

            if (!field.isReadonly() && field.isEditMode()) {
                String input;
                if (field.getType().equals(Type.FILE)) {
                    input = " <input id=\"" + name + "\" name=\"" + name + "\" class=\"" + className
                            + "\" type=\"file\" value=\"" + value + "\" " + maxLength + "/>";
                } else if (field.getType().equals(Type.PASSWORD)) {
                    input = " <input id=\"" + name + "\" name=\"" + name + "\" class=\"" + className
                            + "\" type=\"password\" value=\"" + value + "\" " + maxLength + " autocomplete=\"off\" />";
                } else {
                    input = " <input id=\"" + name + "\" name=\"" + name + "\" class=\"" + className
                            + "\" type=\"text\" value=\"" + value + "\" " + maxLength + "/>";
                }
                if (field.isTrigger()) {
                    input = input + "\n" + getTriggersInput(field, field.getName());
                    input = input + "\n" + getNoscript(field, actionName);
                }
                // tooltip
                input = input + makeTooltip(tooltip);

                return (input);
            } else if (field.getType().equals(Type.PASSWORD)) {
                String fakePwd = StringUtils.leftPad("", 8, "*");
                String input = "<div  id=\"" + name + "\" class=\"" + className
                        + "\" style=\"-webkit-text-security: disc;\" >" + fakePwd + "</div>";
                return input;
            } else {
                String hidden = " <input type=\"hidden\" name=\"" + name + "\" id=\"" + name + "_hidden\" value=\""
                        + value + "\" />\n";
                return (hidden + " <div  id=\"" + name + "\" class=\"" + className + "\" >" + value + "</div>"
                        + makeTooltip(tooltip));
            }

        }

        private static final String EmptyCombo(String name, String className, boolean isEditable, boolean isMultiple) {
            String multiple = isMultiple ? " multiple=\"multiple\" " : "";
            StringBuilder select = new StringBuilder(" <select id=\"" + name + "\" name=\"" + name + "\" class=\""
                    + className + "\" " + multiple + ">\n");
            select.append("  <option value=\"\"></option>\n");
            select.append("</select>\n");
            return isEditable ? select.toString() : "";
        }

        private static final String htmlCombo(ComboBox<?> field, String width, String position, String actionName,
                boolean withSearchComp, String tooltip) throws EMFError {
            String name = JavaScript.stringToHTMLString(field.getName());
            String className = ContainerTag.LEFT.equalsIgnoreCase(position) ? "slText " + width
                    : "slTextRight " + width;
            String value = field.getHtmlValue();

            DecodeMapIF combo = field.getDecodeMap();

            if (field.isHidden()) {
                className += " displayNone ";
            }

            if (combo != null) {
                if (!field.isReadonly() && field.isEditMode()) {
                    StringBuilder out = new StringBuilder("");
                    if (field.isAddBlank()) {
                        out.append("<option value=\"\"></option>\n");
                    }

                    for (Object key : combo.keySet()) {
                        String descrizione = JavaScript.stringToHTMLString(combo.getDescrizione(key));
                        descrizione = org.apache.commons.text.StringEscapeUtils.escapeHtml4(descrizione);
                        out.append("<option title=\"" + descrizione + "\" value=\"" + key + "\"");
                        if (StringUtils.isNotBlank(value)) {
                            // FIXME: ATTENZIONE ALLE COMBO CHE HANNO TIPI DI
                            // DATO != da
                            // String!!! se utilizzo una combo di date esplode
                            if (value.equals(key.toString())) {
                                out.append(" selected=\"selected\"");
                            }
                        }
                        out.append(">" + descrizione + "</option>\n");
                    }

                    String select = " <select id=\"" + name + "\" name=\"" + name + "\" class=\"" + className + "\" >"
                            + out.toString() + "</select>";
                    /*
                     * si disattiva il trigger se presente il componente javascript (select2) che utilizza una custom
                     * function js
                     */
                    if (field.isTrigger() && (!field.isWithSearchComp() && !withSearchComp)) {
                        select = select + "\n" + getTriggersInput(field, field.getName());
                        select = select + "\n" + getNoscript(field, actionName);
                    }
                    // tooltip
                    select = select + makeTooltip(tooltip);

                    return (select);
                } else {
                    return (" <div id=\"" + name + "\" class=\"" + className + "\" >" + field.getHtmlDecodedValue()
                            + makeTooltip(tooltip) + "</div>");
                }
            } else {
                return EmptyCombo(name, className, !field.isReadonly() && field.isEditMode(), false);
            }
        }

        private static final String htmlMultiCombo(MultiSelect<?> field, String width, String position,
                String actionName, String tooltip) {
            String name = JavaScript.stringToHTMLString(field.getName());
            String className = ContainerTag.LEFT.equalsIgnoreCase(position) ? "slText " + width
                    : "slTextRight " + width;
            DecodeMapIF combo = field.getDecodeMap();
            StringBuilder out = new StringBuilder("");

            if (field.isHidden()) {
                className += " displayNone ";
            }

            out.append("<option value=\"\"></option>\n");
            if (combo != null) {
                for (Object key : combo.keySet()) {
                    String descrizione = JavaScript.stringToHTMLString(combo.getDescrizione(key));
                    org.apache.commons.text.StringEscapeUtils.escapeHtml4(descrizione);
                    out.append("<option title=\"" + descrizione + "\" value=\"" + key + "\"");
                    // FIXME: ATTENZIONE ALLE COMBO CHE HANNO TIPI DI DATO != da
                    // String!!!
                    // se utilizzo una combo di date esplode
                    if (field.getValues() != null && field.getValues().contains(key.toString())) {
                        out.append(" selected=\"selected\"");
                    }
                    out.append(">" + descrizione + "</option>\n");
                }
            }
            String options = out.toString();

            if (!field.isReadonly() && field.isEditMode()) {
                String select = " <select multiple=\"multiple\" id=\"" + name + "\" name=\"" + name + "\" class=\""
                        + className + "\">" + options + "</select>";
                if (field.isTrigger()) {
                    select = select + "\n" + getTriggersInput(field, field.getName());
                    select = select + "\n" + getNoscript(field, actionName);
                }
                // tooltip
                select = select + makeTooltip(tooltip);
                return (select);

            } else {
                StringBuilder decodedValues = new StringBuilder("");
                if (combo != null) {
                    decodedValues.append("  <ul id=\"" + name + "\" class=\"list-container " + className + "\">\n");
                    for (Object key : combo.keySet()) {
                        if (field.getValues() != null && field.getValues().contains(key.toString())) {
                            decodedValues.append(
                                    "    <li>" + JavaScript.stringToHTMLString(combo.getDescrizione(key)) + "</li>\n");
                        }
                    }
                    decodedValues.append("  </ul>\n");
                }
                // tooltip
                decodedValues.append(makeTooltip(tooltip));
                return decodedValues.toString();
            }
        }

        private static final String htmlButton(Button field, String width, String position, String actionName) {
            String name = JavaScript.stringToHTMLString(WordUtils.uncapitalize(field.getName()));
            String description = JavaScript.stringToHTMLString(field.getDescription());
            String disabled = "";
            String className = " pulsante ";

            if (field.isHidden() || field.isViewMode()) {
                className += " displayNone ";
            }
            String submitName = Values.OPERATION + Values.OPERATION_SEPARATOR + name;
            StringBuilder stringBuilder = new StringBuilder("");
            if (field.isDisableHourGlass()) {
                className += "disableHourGlass";
            }
            if (field.isReadonly()) {
                disabled = "disabled =\"disabled\"";
            }
            stringBuilder.append("<input type=\"submit\" name=\"" + submitName + "\" value=\"" + description
                    + "\" class=\"" + className + "\" " + disabled + " />\n");
            return stringBuilder.toString();
        }

        private static final String htmlCheckBox(CheckBox<?> field, String width, String position, String actionName,
                int index, String tooltip) {
            String name = JavaScript.stringToHTMLString(field.getName());
            String className = ContainerTag.LEFT.equalsIgnoreCase(position) ? "slText " + width
                    : "slTextRight " + width;
            String checked = field.isChecked() ? "checked=\"checked\"" : "";
            StringBuilder id = new StringBuilder(name);
            String src = "";
            if (index == -1) {
                src = CONTEXTPATH + (field.isChecked() ? IMG_CHECKBOX_CHECKED_FIELD
                        : (field.isWarning() ? IMG_CHECKBOX_WARNING_FIELD : IMG_CHECKBOX_UNCHECKED_FIELD));
            } else {
                src = CONTEXTPATH + (field.isChecked() ? IMG_CHECKBOX_CHECKED
                        : (field.isWarning() ? IMG_CHECKBOX_WARNING : IMG_CHECKBOX_UNCHECKED));
                id.append("_");
                id.append(index);
            }
            String alt = field.isChecked() ? "Selezionato" : (field.isWarning() ? "Warning" : "Non selezionato");
            String value;
            if (index < 0) {
                if (field.isChecked()) {
                    value = CheckBox.valueChecked;
                } else if (field.isWarning()) {
                    value = CheckBox.valueWarning;
                } else {
                    value = CheckBox.valueUnchecked;
                }
            } else {
                value = Integer.toString(index);
            }
            if (field.isHidden()) {
                className += " displayNone ";
            }

            if (!field.isReadonly() && field.isEditMode()) {
                String input = " <input id=\"" + id + "\" class=\"" + className + "\" name=\"" + name
                        + "\" type=\"checkbox\" value=\"" + value + "\" " + checked + " />";
                if (field.isTrigger()) {
                    if (index < 0) {
                        input = input + "\n" + getTriggersInput(field, name);
                    } else {
                        input = input + "\n" + getTriggersInput(field, name + "_" + index);
                    }

                    input = input + "\n" + getNoscript(field, actionName);
                }
                // tooltip
                input = input + makeTooltip(tooltip);

                return (input);
            } else {
                // FIXME: probiblmente l'input hidden non serve.
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(" <div id=\"" + id + "\" class=\"" + className + "\" >&nbsp;<img src=\"" + src
                        + "\" alt=\" " + alt + "\" />\n");
                stringBuilder.append("   <input type=\"hidden\" value=\"" + value + "\" name=\"" + name + "\" id=\""
                        + id + "_HIDDEN\"/>\n");
                // tooltip
                stringBuilder.append(makeTooltip(tooltip));
                stringBuilder.append(" </div>\n");
                return (stringBuilder.toString());
            }
        }

        /*
         * Renders a SINGLE radio Element
         */
        private static final String htmlRadio(Radio<?> field, String width, String position, String actionName) {
            String htmlId = JavaScript.stringToHTMLString(field.getName());
            String htmlName = field.getElementGroup();
            String className = ContainerTag.LEFT.equalsIgnoreCase(position) ? "slText " + width
                    : "slTextRight " + width;
            String checked = field.isChecked() ? "checked=\"checked\" " : "";
            String src = CONTEXTPATH + (field.isChecked() ? IMG_RADIO_CHECKED : IMG_RADIO_UNCHECKED);
            String alt = field.isChecked() ? "Selezionato" : "Non selezionato";
            if (field.isHidden()) {
                className += " displayNone ";
            }

            if (!field.isReadonly() && field.isEditMode()) {
                String input = "    <input id=\"" + htmlId + "\" name=\"" + htmlName + "\" class=\"" + className
                        + "\" type=\"radio\" value=\"" + htmlId + "\" " + checked + "/>\n";
                if (field.isTrigger()) {
                    input = input + "\n" + getTriggersInput(field, field.getName());
                    input = input + "\n" + getNoscript(field, actionName);
                }
                return input;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(" <div id=\"" + htmlId + "\" class=\"" + className + "\" >\n");
                stringBuilder.append("   &nbsp;<img src=\"" + src + "\" alt=\" " + alt + "\" />\n");
                stringBuilder.append(" </div>\n");
                return (stringBuilder.toString());
            }
        }

        private static final String htmlTextArea(TextArea<?> field, String width, String position, String actionName,
                String tooltip) throws EMFError {
            String name = JavaScript.stringToHTMLString(field.getName());

            String className = ContainerTag.LEFT.equalsIgnoreCase(position) ? "slText " + width
                    : "slTextRight " + width;
            String rows = field.getRows() > 0 ? "rows =\"" + field.getRows() + "\"" : "";
            String cols = field.getRows() > 0 ? "cols =\"" + field.getCols() + "\"" : "";
            if (field.getType() != null && field.getType().equals(Type.DATE)) {
                className += " date ";
            }
            String value = (StringUtils.isNotBlank(field.getValue())) ? field.getHtmlValue() : "";
            value = value.replaceAll("&lt;br/&gt;", "\n");
            if (field.isHidden()) {
                className += " displayNone ";
            }

            if (!field.isReadonly() && field.isEditMode()) {
                String input = " <textarea id=\"" + name + "\" name=\"" + name + "\" class=\"" + className + "\" "
                        + rows + " " + cols + ">" + value + "</textarea>";
                if (field.isTrigger()) {
                    input = input + "\n" + getTriggersInput(field, field.getName());
                    input = input + "\n" + getNoscript(field, actionName);
                }
                // tooltip
                input = input + makeTooltip(tooltip);

                return (input);
            } else {
                return (StringUtils.isNotBlank(value) ? " <pre id=\"" + name + "\" class=\"" + className + "\"><code>"
                        + value + "</code></pre>" + makeTooltip(tooltip) : "");
            }
        }

        private static final String htmlSemaphore(Semaphore field, String width, String position, String actionName)
                throws EMFError {
            return field.getHtmlValue();

        }

        private static String makeHtmlCheckbox(MultiSemaphore.State enu, String name, boolean checked) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(
                    "<label for=\"" + name + enu.intValue() + "\" class=\"multiSemaphoreLabel\">" + enu.toImgHtml()
                            + "</label><input id=\"" + name + enu.intValue() + "\" name=\"" + name + enu.intValue()
                            + "\" type=\"checkbox\" value=\"S\" " + (checked ? " checked=\"checked\"" : "") + " />");
            return stringBuilder.toString();
        }

        private static final String htmlMultiSemaphore(MultiSemaphore field, String width, String position,
                String actionName) throws EMFError {
            String name = JavaScript.stringToHTMLString(field.getName());

            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("<span id=\"" + field.getName() + "\">");
            if (!field.isReadonly() && field.isEditMode()) {
                stringBuilder.append(makeHtmlCheckbox(MultiSemaphore.State.GREEN, name,
                        field.getDecodedValues().contains(MultiSemaphore.State.GREEN.toString())));
                stringBuilder.append(makeHtmlCheckbox(MultiSemaphore.State.YELLOW, name,
                        field.getDecodedValues().contains(MultiSemaphore.State.YELLOW.toString())));
                stringBuilder.append(makeHtmlCheckbox(MultiSemaphore.State.RED, name,
                        field.getDecodedValues().contains(MultiSemaphore.State.RED.toString())));
            }
            stringBuilder.append("</span>");
            return stringBuilder.toString();
        }

        private static final String makeTooltip(String tooltip) {
            if (StringUtils.isNotBlank(tooltip)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("\n" + "<i aria-hidden=\"true\" class=\"fa fa-question-circle fa-lg tooltip\"> "
                        + " <span class=\"tooltiptext\">" + JavaScript.stringToHTMLString(tooltip) + "</span></i>");
                return stringBuilder.toString();
            } else {
                return StringUtils.EMPTY;
            }
        }

        public static final String htmlField(Component component, String width, String position, String actionName,
                int index, boolean withsearchcomp, String tooltip) throws EMFError {
            if (component instanceof Input) {
                return htmlInput((Input<?>) component, width, position, actionName, index, tooltip);
            }
            if (component instanceof ComboBox) {
                return htmlCombo((ComboBox<?>) component, width, position, actionName, withsearchcomp, tooltip);
            }
            if (component instanceof MultiSelect) {
                return htmlMultiCombo((MultiSelect<?>) component, width, position, actionName, tooltip);
            }
            if (component instanceof CheckBox) {
                return htmlCheckBox((CheckBox<?>) component, width, position, actionName, index, tooltip);
            }
            if (component instanceof TextArea) {
                return htmlTextArea((TextArea<?>) component, width, position, actionName, tooltip);
            }
            if (component instanceof Button) {
                return htmlButton((Button) component, width, position, actionName);
            }
            if (component instanceof Radio) {
                return htmlRadio((Radio<?>) component, width, position, actionName);
            }
            if (component instanceof Semaphore) {
                return htmlSemaphore((Semaphore) component, width, position, actionName);
            }
            if (component instanceof MultiSemaphore) {
                return htmlMultiSemaphore((MultiSemaphore) component, width, position, actionName);
            }
            return "";

        }

    }

    @Override
    public int doEndTag() throws JspException {
        int eval = super.doEndTag();

        if (eval == EVAL_PAGE) {
            if ((getComponent() instanceof ComboBox) && (isWithSearchcomp()
                    /* from tag */ || ((ComboBox) getComponent()).isWithSearchComp())/* from component */) {
                String name = getComponent().getName();
                Field field = (ComboBox<?>) getComponent();
                // editable
                if (!field.isReadonly() && field.isEditMode()) {
                    writeln(" <script type=\"text/javascript\" >" + "$('#" + name
                            + "').select2({theme: \"classic\", placeholder: '<i class=\"fa fa-search\"></i> Ricerca e seleziona elemento dalla lista...', allowClear: true, "
                            + " dropdownCssClass: \"select2-customdrop\", "
                            + "	       dropdownAutoWidth: true, width: 'auto', \"language\": \"it\", escapeMarkup: function(m){ return m; } })"
                            + ".on('select2:unselecting', function () {  $(this).data('unselecting', true); }) "
                            + ".on('select2:opening', function (e) {  if ($(this).data('unselecting')) {  $(this).removeData('unselecting'); e.preventDefault(); } });"
                            + "</script>");
                    // html5 placeholder
                    writeln(" <script type=\"text/javascript\" > " + "$('#" + name + "')"
                            + ".on(\"select2:open\", function(e) {$('input.select2-search__field').prop('placeholder', 'Effettua ricerca:'); });"
                            + " </script>");
                }
            }
        }

        return eval;
    }

    public int getColSpan() {
        return colSpan;
    }

    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }

    public boolean isWithSearchcomp() {
        return withSearchcomp;
    }

    public void setWithSearchcomp(boolean withSearchcomp) {
        this.withSearchcomp = withSearchcomp;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

}
