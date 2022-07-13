package it.eng.spagoLite.form.fields.impl;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.db.decodemap.DecodeMapIF;
import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.fields.IMappableField;
import it.eng.spagoLite.form.fields.MultiValueField;
import it.eng.spagoLite.util.Casting.Casting;
import it.eng.spagoLite.xmlbean.form.Field.Type.Enum;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiSelect<O> extends MultiValueField<O> implements IMappableField {

    static Logger log = LoggerFactory.getLogger(MultiSelect.class);
    private static final long serialVersionUID = 1L;
    private DecodeMapIF decodeMap;
    private int maxLength;

    public MultiSelect(Component parent, String name, String description, String alias, Enum type, String format,
            boolean required, boolean hidden, boolean readonly, boolean trigger, int maxLength) {
        super(parent, name, description, alias, type, format, required, hidden, readonly, trigger);
        this.maxLength = maxLength;
    }

    public DecodeMapIF getDecodeMap() {
        return decodeMap;
    }

    public void setDecodeMap(DecodeMapIF decodeMap) {
        try {
            this.decodeMap = decodeMap;
            if (getValues() != null && !getValues().isEmpty() && decodeMap != null) {
                List<O> objects = parse();
                Set<String> objectsToKeep = new LinkedHashSet<>();
                for (O obj : objects) {
                    if (decodeMap.containsKey(obj)) {
                        objectsToKeep.add(Casting.format(obj, getType()));
                    }
                }
                setValues(objectsToKeep.toArray(new String[objectsToKeep.size()]));
            }
        } catch (EMFError ex) {
            log.error("DECODE MAP " + getName() + "ERRORE di conversione valore : " + ex.getDescription(), ex);
        }
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public JSONObject asJSON() throws EMFError {
        JSONObject json = super.asJSON();
        try {
            JSONArray sons = new JSONArray();
            if (decodeMap != null) {
                sons.put(decodeMap.asJSON());
            }
            json.put("map", sons);
            json.put("type", "MultiSelect");
        } catch (JSONException e) {
            throw new EMFError(EMFError.ERROR, "Eccezione nella crezione dell'oggetto JSON", e);
        }

        return json;
    }

    @Override
    public Set<String> getDecodedValues() throws EMFError {
        if (check()) {
            List<O> object = parse();
            if (check() && object != null && !object.equals("")) {
                if (getDecodeMap() == null) {
                    return new HashSet<String>((List<String>) object);
                }
                Set<String> set = new HashSet<String>();
                for (O s : object) {
                    set.add(getDecodeMap().getDescrizione(s));
                }
                return set;
            }
        }
        return null;
    }

    @Override
    public void reset() {
        super.reset();
        setDecodeMap(null);
    }

    @Override
    public void setValues(String[] values) {
        try {
            if (getDecodeMap() != null) {
                Set<String> objectsToKeep = new LinkedHashSet<>();
                if (values != null) {
                    for (String obj : values) {
                        Object val = Casting.parse(obj, getType());
                        if (decodeMap.containsKey(val)) {
                            objectsToKeep.add(obj);
                        }
                    }
                }
                super.setValues(objectsToKeep.toArray(new String[objectsToKeep.size()]));
            } else {
                if (values != null) {
                    log.warn(
                            "ATTENZIONE: Sono stati impostati dei valori per la multiselect senza DecodeMap presente.");
                }
                super.setValues(values);
            }
        } catch (EMFError ex) {
            log.error("DECODE MAP " + getName() + "ERRORE di conversione valore : " + ex.getDescription(), ex);
        }
    }

}
