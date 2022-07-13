package it.eng.spagoLite.form.fields;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.form.Component;
import it.eng.spagoLite.form.base.BaseComponent;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Section extends BaseComponent {

    private String legend;
    private boolean hidden;
    private boolean borderHidden;
    private boolean showButton;
    private boolean loadOpened;
    private boolean editMode;

    public Section(Component parent, String name, String description) {
        super(parent, name, description);
    }

    public Section(Component parent, String name, String description, String legend, boolean hidden,
            boolean borderHidden, boolean showButton, boolean loadOpened, boolean editMode) {
        super(parent, name, description);
        this.legend = legend;
        this.hidden = hidden;
        this.borderHidden = borderHidden;
        this.showButton = showButton;
        this.loadOpened = loadOpened;
        this.editMode = editMode;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isBorderHidden() {
        return borderHidden;
    }

    public void setBorderHidden(boolean borderHidden) {
        this.borderHidden = borderHidden;
    }

    public boolean isShowButton() {
        return showButton;
    }

    public void setShowButton(boolean showButton) {
        this.showButton = showButton;
    }

    public boolean isLoadOpened() {
        return loadOpened;
    }

    public void setLoadOpened(boolean loadOpened) {
        this.loadOpened = loadOpened;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public boolean isViewMode() {
        return !editMode;
    }

    public void setEditMode() {
        this.editMode = true;
    }

    public void setViewMode() {
        this.editMode = false;
    }

    @Override
    public JSONObject asJSON() throws EMFError {
        JSONObject json = super.asJSON();
        try {
            json.put("type", "Section");
        } catch (JSONException e) {
            throw new EMFError(EMFError.ERROR, "Eccezione nella crezione dell'oggetto JSON", e);
        }
        return json;
    }

}
