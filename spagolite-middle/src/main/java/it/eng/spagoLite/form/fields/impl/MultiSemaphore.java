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

package it.eng.spagoLite.form.fields.impl;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.db.decodemap.DecodeMapIF;
import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.fields.IMappableField;
import it.eng.spagoLite.form.fields.MultiValueField;
import javax.servlet.http.HttpServletRequest;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class MultiSemaphore extends MultiValueField<String> implements IMappableField {

    private static final long serialVersionUID = 1L;
    private DecodeMapIF decodeMap;
    private boolean greenChecked = true;
    private boolean yellowChecked = true;
    private boolean redChecked = true;

    /**
     * Costruttore. Per default i checkbox del semaforo sono inizialmente tutti ceccati.
     *
     * @param parent
     *            genitore
     * @param name
     *            nome
     * @param description
     *            descrizione
     * @param alias
     *            value
     * @param type
     *            tipo
     * @param format
     *            formato
     * @param required
     *            boolean true/false se obbligatorio
     * @param hidden
     *            value
     * @param readonly
     *            boolean true/false
     * @param trigger
     *            boolean true/false
     * @param greenChecked
     *            boolean true/false
     * @param yellowChecked
     *            boolean true/false
     * @param redChecked
     *            boolean true/false
     */
    public MultiSemaphore(Component parent, String name, String description, String alias,
            it.eng.spagoLite.xmlbean.form.Field.Type.Enum type, String format, boolean required, boolean hidden,
            boolean readonly, boolean trigger, boolean greenChecked, boolean yellowChecked, boolean redChecked) {
        super(parent, name, description, alias, type, format, required, hidden, readonly, trigger);
        try {
            setGreenChecked(greenChecked);
            setYellowChecked(yellowChecked);
            setRedChecked(redChecked);
        } catch (EMFError e) {
            e.printStackTrace();
        }
    }

    public void setGreenChecked(boolean checked) throws EMFError {
        getValues().remove(MultiSemaphore.State.GREEN.toString());
        if (checked) {
            getDecodedValues().add(MultiSemaphore.State.GREEN.toString());
        }
        this.greenChecked = checked;
    }

    public void setYellowChecked(boolean checked) throws EMFError {
        getDecodedValues().remove(MultiSemaphore.State.YELLOW.toString());
        if (checked) {
            getDecodedValues().add(MultiSemaphore.State.YELLOW.toString());
        }
        this.yellowChecked = checked;

    }

    public void setRedChecked(boolean checked) throws EMFError {
        getDecodedValues().remove(MultiSemaphore.State.RED.toString());
        if (checked) {
            getDecodedValues().add(MultiSemaphore.State.RED.toString());
        }
        this.redChecked = checked;

    }

    public DecodeMapIF getDecodeMap() {
        return decodeMap;
    }

    public void setDecodeMap(DecodeMapIF decodeMap) {
        this.decodeMap = decodeMap;
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
            json.put("type", "MultiSemaphore");
        } catch (JSONException e) {
            throw new EMFError(EMFError.ERROR, "Eccezione nella crezione dell'oggetto JSON", e);
        }

        return json;
    }

    @Override
    public void reset() {
        super.reset();
        setDecodeMap(null);
    }

    /**
     * Enumerator di utility per wrappare {@link Enum}.
     *
     * @author arcella_a
     */
    public enum State {
        NON_DEFINITO(null, "Non definito", "../img/sips/gray.png", "N"),
        GREEN(it.eng.spagoLite.xmlbean.form.Semaphore.State.GREEN, "Valido", "../img/sips/green.png", "V"),
        YELLOW(it.eng.spagoLite.xmlbean.form.Semaphore.State.YELLOW, "Da validare", "../img/sips/orange.png", "G"),
        RED(it.eng.spagoLite.xmlbean.form.Semaphore.State.RED, "Inserito", "../img/sips/red.png", "R");

        private it.eng.spagoLite.xmlbean.form.Semaphore.State.Enum enumState;
        private String title;
        private String imgUrl;
        private String flagSemaforo;

        private State(it.eng.spagoLite.xmlbean.form.Semaphore.State.Enum enumState, String title, String imgUrl,
                String flagSemaforo) {
            this.enumState = enumState;
            this.title = title;
            this.imgUrl = imgUrl;
            this.flagSemaforo = flagSemaforo;
        }

        public String toImgHtml() {
            return "  <img src=\"" + imgUrl + "\" alt=\"" + title + "\" title=\"" + title + "\" />&nbsp;";
        }

        public int intValue() {
            return enumState.intValue();
        }

        @Override
        public String toString() {
            return flagSemaforo;
        }
    }

    @Override
    public void post(HttpServletRequest servletRequest) {
        if (!isReadonly()) {
            getValues().clear();
            String greenValue = servletRequest.getParameter(getName() + State.GREEN.intValue());
            String yellowValue = servletRequest.getParameter(getName() + State.YELLOW.intValue());
            String redValue = servletRequest.getParameter(getName() + State.RED.intValue());
            // if(greenValue != null) selectedStates.add(State.GREEN);
            // if(yellowValue != null) selectedStates.add(State.YELLOW);
            // if(redValue != null) selectedStates.add(State.RED);
            try {
                if (greenValue != null) {
                    getDecodedValues().add(State.GREEN.toString());
                }
                if (yellowValue != null) {
                    getDecodedValues().add(State.YELLOW.toString());
                }
                if (redValue != null) {
                    getDecodedValues().add(State.RED.toString());
                }
            } catch (EMFError e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * @return string del tipo "VRG" con le iniziali dei semafori checkati (Verde Rosso Giallo)
     *
     * @throws EMFError
     *             eccezione generica
     */
    public String getConcatFlagSemaforo() throws EMFError {
        String flags = "";
        for (String flag : getDecodedValues()) {
            flags += flag;
        }
        return flags;

    }

    public boolean isGreenChecked() {
        return greenChecked;
    }

    public boolean isYellowChecked() {
        return yellowChecked;
    }

    public boolean isRedChecked() {
        return redChecked;
    }

}
