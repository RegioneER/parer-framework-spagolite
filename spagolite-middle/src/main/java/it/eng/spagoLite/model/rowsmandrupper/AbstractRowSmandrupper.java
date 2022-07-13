package it.eng.spagoLite.model.rowsmandrupper;

public abstract class AbstractRowSmandrupper implements RowSmandrupperInterface {

    private boolean visible;
    private boolean editable;
    private boolean deletable;
    private boolean insertable;

    public AbstractRowSmandrupper() {
        this.visible = false;
        this.editable = false;
        this.deletable = false;
        this.insertable = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isEditable() {
        return editable;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public boolean isInsertable() {
        return insertable;
    }

    protected void setVisible(boolean visible) {
        this.visible = visible;
    }

    protected void setEditable(boolean editable) {
        this.editable = editable;
    }

    protected void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    protected void setInsertable(boolean insertable) {
        this.insertable = insertable;
    }

}
