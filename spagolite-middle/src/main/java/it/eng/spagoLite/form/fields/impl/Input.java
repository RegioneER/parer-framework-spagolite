package it.eng.spagoLite.form.fields.impl;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.xmlbean.form.Field.Type;
import it.eng.spagoLite.xmlbean.form.Input.ForceCase;
import it.eng.spagoLite.xmlbean.form.Input.ForceTrim;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Input<O> extends SingleValueField<O> {

    private static final long serialVersionUID = 1L;

    private int maxLength;
    public ForceCase.Enum forceCase;
    public ForceTrim.Enum forceTrim;
    private String regExp;
    private FileItem fileItem;
    private boolean groupingDecimal;
    private String tooltip;

    public Input(Component parent, String name, String description, String alias, Type.Enum type, String format,
            boolean groupingDecimal, boolean required, boolean hidden, boolean readonly, boolean trigger, int maxLength,
            ForceCase.Enum forceCase, ForceTrim.Enum forceTrim, String regExp, String tooltip) {
        super(parent, name, description, alias, type, format, required, hidden, readonly, trigger);
        this.groupingDecimal = groupingDecimal;
        this.maxLength = maxLength;
        this.forceCase = forceCase;
        this.forceTrim = forceTrim;
        this.regExp = regExp;
        this.tooltip = tooltip;
    }

    @Override
    public JSONObject asJSON() throws EMFError {
        String value = ((getValue() == null) ? "" : getValue());
        JSONObject json = super.asJSON();
        try {
            json.put("value", value);
            json.put("type", "Input");
        } catch (JSONException e) {
            throw new EMFError(EMFError.ERROR, "Eccezione nella crezione dell'oggetto JSON", e);
        }
        return json;
    }

    @Override
    public void setValue(String value) {
        if (value != null) {
            if (getForceCase() != null) {
                if (getForceCase().equals(ForceCase.UPPER)) {
                    value = value.toUpperCase();
                } else if (getForceCase().equals(ForceCase.LOWER)) {
                    value = value.toLowerCase();
                } else if (getForceCase().equals(ForceCase.INITCAP)) {
                    StringUtils.capitalize(value);
                }
            }

            if (getForceTrim() != null) {
                if (getForceTrim().equals(ForceTrim.TRIM)) {
                    value = value.trim();
                }
            }

            if (getType().equals(Type.DECIMAL) && value.contains(".")) {
                value = value.replace('.', ',');
            }
        }
        super.setValue(value);
    }

    public ForceCase.Enum getForceCase() {
        return forceCase;
    }

    public void setForceCase(ForceCase.Enum forceCase) {
        this.forceCase = forceCase;
    }

    public ForceTrim.Enum getForceTrim() {
        return forceTrim;
    }

    public void setForceTrim(ForceTrim.Enum forceTrim) {
        this.forceTrim = forceTrim;
    }

    public boolean isGroupingDecimal() {
        return groupingDecimal;
    }

    public void setGroupingDecimal(boolean groupingDecimal) {
        this.groupingDecimal = groupingDecimal;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getRegExp() {
        return regExp;
    }

    public void setRegExp(String regExp) {
        this.regExp = regExp;
    }

    @Override
    public void clear() {
        this.setValue(null);
        this.setFileItem(null);
    }

    @Override
    public boolean check() {
        if (this.getType().equals(Type.DATE) && StringUtils.isNotBlank(getValue())) {
            if (!getValue().matches("^(29/02/(2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26]))))$"
                    + "|^((0[1-9]|1[0-9]|2[0-8])/02/((19|2[0-9])[0-9]{2}))$"
                    + "|^((0[1-9]|[12][0-9]|3[01])/(0[13578]|10|12)/((19|2[0-9])[0-9]{2}))$"
                    + "|^((0[1-9]|[12][0-9]|30)/(0[469]|11)/((19|2[0-9])[0-9]{2}))$")) {
                return false;
            }
        }

        if (this.getRegExp() != null) {
            if (StringUtils.isNotBlank(getValue())) {
                if (!getValue().matches(this.getRegExp())) {
                    return false;
                }
            }
        }
        return super.check();
    }

    @Override
    public void post(HttpServletRequest servletRequest) {
        if (!isReadonly() && isEditMode()) {
            setValue(servletRequest.getParameter(getName()));
        }
    }

    public byte[] getFileBytes() {
        return fileItem != null ? fileItem.get() : null;
    }

    public boolean isFileInMemory() {
        return fileItem != null ? fileItem.isInMemory() : null;
    }

    public void writeUploadedFile(File file) throws Exception {
        if (file == null) {
            throw new IllegalArgumentException("File passed as argument is null");
        }
        if (fileItem != null) {
            fileItem.write(file);
        }
    }

    public void setFileItem(FileItem fi) {
        this.fileItem = fi;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

}
