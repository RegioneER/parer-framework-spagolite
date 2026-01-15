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

package it.eng.spagoLite.form.fields;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoCore.util.JavaScript;
import it.eng.spagoLite.db.base.BaseRowInterface;
import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.fields.impl.Input;
import it.eng.spagoLite.form.fields.impl.Link;
import it.eng.spagoLite.message.Message;
import it.eng.spagoLite.message.Message.MessageLevel;
import it.eng.spagoLite.util.Casting.Casting;
import it.eng.spagoLite.util.Format;
import it.eng.spagoLite.xmlbean.form.Field.Type;
import it.eng.spagoLite.xmlbean.form.Field.Type.Enum;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.dom4j.Element;

public abstract class SingleValueField<O extends Object> extends Field {

    private static final long serialVersionUID = 1L;

    private String value;
    private String iconUrl;

    public SingleValueField(Component parent, String name, String description, String alias,
            Enum type, String format, boolean required, boolean hidden, boolean readonly,
            boolean trigger) {
        this(parent, name, description, alias, type, format, required, hidden, readonly, trigger,
                null);
    }

    public SingleValueField(Component parent, String name, String description, String alias,
            Enum type, String format, boolean required, boolean hidden, boolean readonly,
            boolean trigger, String iconUrl) {
        super(parent, name, description, alias, type, format, required, hidden, readonly, trigger);

        this.iconUrl = iconUrl;
    }

    public String getValue() {
        return value;
    }

    public String getHtmlValue() throws EMFError {
        String htmlValue = JavaScript.stringToHTMLString(getValue());
        if ((this instanceof Input || this instanceof Link) && StringUtils.isNotBlank(htmlValue)
                && check()) {
            if (getType().equals(Type.INTEGER) || getType().equals(Type.DECIMAL)) {
                DecimalFormat decimalFormatter;
                DecimalFormatSymbols itSymbols = new DecimalFormatSymbols(Locale.ITALIAN);
                itSymbols.setDecimalSeparator(',');
                itSymbols.setGroupingSeparator('.');
                if (StringUtils.isNotBlank(getFormat())) {
                    decimalFormatter = new DecimalFormat(getFormat(), itSymbols);
                } else {
                    decimalFormatter = new DecimalFormat(Format.DECIMAL_FORMAT, itSymbols);
                }
                if (isViewMode()) {
                    boolean groupingDecimal = true;
                    if (this instanceof Input) {
                        Input<O> input = (Input<O>) this;
                        groupingDecimal = input.isGroupingDecimal();
                    }
                    // Input<O> input = (Input<O>) this;
                    decimalFormatter.setGroupingUsed(groupingDecimal);
                    decimalFormatter.setGroupingSize(3);
                }
                NumberFormat format = NumberFormat.getInstance(Locale.ITALY);
                Number number;
                try {
                    number = format.parse(htmlValue);
                } catch (ParseException ex) {
                    throw new EMFError(EMFError.ERROR, "Errore inatteso nel parsing del valore",
                            ex);
                }
                htmlValue = decimalFormatter.format(number.doubleValue());
            } else if (getType().equals(Type.DATE) || getType().equals(Type.DATETIME)) {
                htmlValue = Casting.format(parse(), getType(), getFormat());
            }
        }

        String htmlIcon = getIconUrl() == null ? ""
                : "<img src=\"" + getIconUrl() + "\" alt=\"" + htmlValue + "\" title=\"" + htmlValue
                        + "\" />&nbsp;";

        return htmlIcon + htmlValue;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDecodedValue() throws EMFError {
        return getValue();
    }

    public String getHtmlDecodedValue() throws EMFError {
        return getHtmlValue();
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public boolean check() {
        return Casting.check(getValue(), getType());
    }

    @Override
    public Message validate() {
        if (getType().equals(Type.CAP) && !check()) {
            return new Message(MessageLevel.ERR, "Il Codice di Avviamento Postale ("
                    + getDescription() + ") deve essere di 5 cifre");
        }

        if (getType().equals(Type.CODFISCALE) && !check()) {
            return new Message(MessageLevel.ERR, "Il codice fiscale inserito (" + getDescription()
                    + ") non &egrave; formalmente corretto");
        }

        if (getType().equals(Type.PARTITAIVA) && !check()) {
            return new Message(MessageLevel.ERR, "La partita iva inserita (" + getDescription()
                    + ") non &egrave; formalmente corretta");
        }

        if (getType().equals(Type.CODFISCPIVA) && !check()) {
            return new Message(MessageLevel.ERR, "Il codice inserito (" + getDescription()
                    + ") non &egrave; formalmente corretto");
        }

        if (getType().equals(Type.EMAIL) && !check()) {
            return new Message(MessageLevel.ERR,
                    "L'email inserita (" + getDescription() + ") non &egrave; valida");
        }

        if (getType().equals(Type.TELEFONO) && !check()) {
            return new Message(MessageLevel.ERR, "Il numero di telefono o fax (" + getDescription()
                    + ") inserito non &egrave; valido");
        }

        if (getType().equals(Type.PREFISSOTEL) && !check()) {
            return new Message(MessageLevel.ERR, "Il prefesso di telefono o fax ("
                    + getDescription() + ") inserito non &egrave; valido");
        }

        if ((getType().equals(Type.DATE) || getType().equals(Type.DATETIME)) && !check()) {

            return new Message(MessageLevel.ERR,
                    "Il campo (" + getDescription() + ") inserito non &egrave; valido");
        }

        if (!check()) {
            return new Message(MessageLevel.ERR,
                    "Campo '" + getDescription() + "' formalmente errato");
        }

        if (isRequired() && StringUtils.isBlank(getValue())) {
            return new Message(MessageLevel.ERR, "Campo  '" + getDescription() + "' obbligatorio");
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public O parse() throws EMFError {
        return (O) Casting.parse(getValue(), getType());
    }

    public String format(Object value) throws EMFError {
        setValue(Casting.format(value, getType(), getFormat()));
        return this.value;
    }

    @SuppressWarnings("unchecked")
    public String format(BaseRowInterface row) throws EMFError {
        return format((O) row.getObject(getAlias()));
    }

    @Override
    public void post(HttpServletRequest servletRequest) {
        if (!isReadonly() && isEditMode()) {
            setValue(servletRequest.getParameter(getName()));
        }
    }

    @Override
    public Element asXml() {
        Element element = super.asXml();
        element.addAttribute("value", getValue() == null ? "" : getValue());
        element.addAttribute("iconUrl", getIconUrl() == null ? "" : getIconUrl());

        return element;
    }

    @Override
    public JSONObject asJSON() throws EMFError {
        JSONObject result = new JSONObject();
        try {
            result.put("name", getName());
            result.put("description", getDescription());
            result.put("value", getValue());
            String state;
            if (isHidden()) {
                state = "hidden";
            } else if (isReadonly()) {
                state = "readonly";
            } else if (isViewMode()) {
                state = "view";
            } else {
                state = "edit";
            }
            result.put("required", isRequired());
            result.put("state", state);
        } catch (JSONException e) {
            throw new EMFError(EMFError.ERROR, "Eccezione nella creazione dell'oggetto JSON", e);
        }
        return result;
    }

    @Override
    public void clear() {
        setValue(null);
    }

    @Override
    public void reset() {
        clear();
    }

}
