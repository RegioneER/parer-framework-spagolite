package it.eng.spagoLite.tag.form.list;

import it.eng.spagoCore.configuration.ConfigSingleton;
import it.eng.spagoIFace.Values;
import it.eng.spagoLite.db.base.BaseRowInterface;
import it.eng.spagoLite.db.base.sorting.SortingRule;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.form.list.List;
import it.eng.spagoLite.tag.form.BaseFormTag;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;

public class AbstractListTag extends BaseFormTag<List<SingleValueField<?>>> {

    private static final long serialVersionUID = 1L;

    protected static final String INSERT_IMG = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/insert.png\" title=\"Aggiungi un nuovo record\" alt=\"Aggiungi un nuovo record\" /> Inserisci";
    protected static final String UPDATE_IMG = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/update.png\" title=\"Modifica il record corrente\" alt=\"Modifica il record corrente\" />";
    protected static final String DELETE_IMG = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/delete.png\" title=\"Cancella il record corrente\" alt=\"Cancella il record corrente\" />";
    protected static final String EXCEL_IMG = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/exportExcel.png\" title=\"Esporta la lista in excel\" alt=\"Esporta la lista in excel\" /> Excel";

    // protected static final String BACK =
    // "<img src=\""+CONTEXTPATH+"/img/toolBar/back.png\" title=\"Torna all'elenco\" alt=\"Torna all'elenco\"
    // style=\"vertical-align:middle\"/> Indietro";
    protected static final String BACK_URL_IMG = "" + CONTEXTPATH + "/img/toolBar/back.png";
    protected static final String FIRST = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/first.png\" title=\"Vai al primo record\" alt=\"Vai al primo record\" />";
    protected static final String PREV_PAGE = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/prevPage.png\" title=\"Vai alla pagina precedente\" alt=\"Vai alla pagina precedente\" />";
    protected static final String PREV = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/prev.png\" title=\"Vai al record precedente\" alt=\"Vai alla pagina precedente\" />";
    protected static final String DETTAGLIO = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/dettaglio.png\" title=\"Accedi al dettaglio\" alt=\"Accedi al dettaglio\" />";
    protected static final String ELENCO = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/elenco.gif\" title=\"Torna all'elenco\" alt=\"Torna all'elenco\" />";
    protected static final String NEXT = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/next.png\" title=\"Vai record successivo\" alt=\"Vai record successivo\" style=\"vertical-align:middle\"/>";
    protected static final String NEXT_PAGE = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/nextPage.png\" title=\"Vai alla pagina successiva\" alt=\"Vai alla pagina successiva\" />";
    protected static final String LAST = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/last.png\" title=\"Vai all'ultimo record\" alt=\"Vai all'ultimo record\" />";

    protected static final String BACK_DISABLED = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/backDisabled.png\" title=\"Torna all'elenco\" alt=\"Torna all'elenco\" class=\"backImg\"/>";
    protected static final String FIRST_DISABLED = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/firstDisabled.gif\" title=\"Vai al primo record\" alt=\"Vai al primo record\" />";
    protected static final String PREV_PAGE_DISABLED = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/prevPageDisabled.gif\" title=\"Vai alla pagina precedente\" alt=\"Vai alla pagina precedente\" />";
    protected static final String PREV_DISABLED = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/prevDisabled.gif\" title=\"Vai al record precedente\" alt=\"Vai alla pagina precedente\" />";
    protected static final String ELENCO_DISABLED = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/elencoDisabled.gif\" title=\"Torna all'elenco\" alt=\"Torna all'elenco\" />";
    protected static final String NEXT_DISABLED = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/nextDisabled.gif\" title=\"Vai record successivo\" alt=\"Vai record successivo\" />";
    protected static final String NEXT_PAGE_DISABLED = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/nextPageDisabled.gif\" title=\"Vai alla pagina successiva\" alt=\"Vai alla pagina successiva\" />";
    protected static final String LAST_DISABLED = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/lastDisabled.gif\" title=\"Vai all'ultimo record\" alt=\"Vai all'ultimo record\" />";

    protected static final String EXPORT_XLS = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/exportExcel.png\" title=\"Esporta la lista in excel\" alt=\"Esporta la lista in excel\"/>&nbsp;Excel";
    protected static final String SAVE = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/save.png\" title=\"Salva\" alt=\"Salva\"/>Salva";
    protected static final String CANCEL = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/cancel.png\" title=\"Annulla\" alt=\"Annulla\"/>Annulla";
    protected static final String SELECT_ROW = "<img src=\"" + CONTEXTPATH
            + "/img/toolBar/select.png\" title=\"Seleziona riga\" alt=\"Seleziona riga\"/>Seleziona";
    protected static final String BACK_STYLE = "backButtton";
    protected static final String SAVE_STYLE = "crud save";
    protected static final String CANCEL_STYLE = "crud cancel";
    protected static final String INSERT_STYLE = "crud insert";
    protected static final String UPDATE_STYLE = "crud update";
    protected static final String DELETE_STYLE = "crud delete";
    protected static final String SELECT_STYLE = "crud select";
    protected static final String DESELECT_STYLE = "crud deselect";

    private String mainNavTable = "";

    protected static final String ASC_IMG = "&nbsp;<img src=\"" + CONTEXTPATH
            + "/img/toolBar/asc.png\" title=\"ordina la colonna\" alt=\"ordina la colonna\" />";
    protected static final String DESC_IMG = "&nbsp;<img src=\"" + CONTEXTPATH
            + "/img/toolBar/desc.png\" title=\"ordina la colonna\" alt=\"ordina la colonna\" />";

    private boolean contentVisible;
    private boolean contentEditable;
    private boolean contentDeletable;

    private boolean viewAction;
    private boolean editAction;
    private boolean deleteAction;
    private boolean addAction;
    private boolean removeAction;
    private boolean insertAction;
    private boolean multiRowEdit = false;
    private boolean sortable = true;
    protected boolean abbrLongList = false;

    public String getTitle() {
        return getComponent().getTitle();
    }

    public String getVisibilityProperty() {
        return getComponent().getVisibilityProperty();
    }

    protected void writeLink(String navigationEvent, String img) throws JspException {
        writeln(getLink("", navigationEvent, img, null, null, -1, false, true));
    }

    protected void writeLink(String navigationEvent, String img, String text, String title) throws JspException {
        writeln(getLink("", navigationEvent, img, text, title, -1, false, true));
    }

    protected String getLink(String linkClass, String navigationEvent, String img, String text, String title, int riga,
            boolean forceReload, boolean isMasterList) throws JspException {
        return this.getLink(linkClass, navigationEvent, img, text, title, riga, forceReload, isMasterList, null);
    }

    protected String getLink(String linkClass, String navigationEvent, String img, String text, String title, int riga,
            boolean forceReload, boolean isMasterList, String name2) throws JspException {

        String styleClass = StringUtils.isBlank(linkClass) ? "" : " class=\"" + linkClass + "\" ";
        if (navigationEvent != null) {
            String tagContent = (img != null ? img : "") + (text != null ? text : "");
            tagContent = tagContent.equals("") ? "&nbsp;" : tagContent;

            StringBuilder params = new StringBuilder("");
            params.append("riga=" + ((riga < 0) ? "-1" : String.valueOf(riga)));
            params.append("&amp;forceReload=" + String.valueOf(forceReload));

            String remoteMethod = "listNavigationOnClick";
            if (!isMasterList) {
                remoteMethod = "listDetailNavigationOnClick";
            }

            return "<a " + styleClass + " " + (title != null ? "title=\"" + title + "\"" : "") + " href=\""
                    + getOperationUrl(remoteMethod,
                            "table=" + getName()
                                    + ((name2 != null && !name2.isEmpty()) ? "&amp;mainNavTable=" + name2 : "")
                                    + "&amp;navigationEvent=" + navigationEvent + "&amp;" + params)
                    + "\">" + tagContent + "</a>&nbsp;";

        } else {
            return "<span " + styleClass + ">" + (img != null ? img : "") + "&nbsp;</span>";
        }
    }

    protected String getSubmit(String navigationEvent, String style, String title, String value, int riga,
            boolean forceReload) throws JspException {
        return getSubmit(navigationEvent, style, title, value, riga, forceReload, true);
    }

    protected String getSubmit(String navigationEvent, String style, String title, String value, int riga,
            boolean forceReload, boolean isMasterList) throws JspException {
        if (navigationEvent != null) {
            String remoteMethod = "listNavigationOnClick";
            if (!isMasterList) {
                remoteMethod = "listDetailNavigationOnClick";
            }

            StringBuilder submitName = new StringBuilder("operation" + Values.OPERATION_SEPARATOR + remoteMethod
                    + Values.OPERATION_SEPARATOR + getName() + Values.OPERATION_SEPARATOR + navigationEvent);
            submitName.append(Values.OPERATION_SEPARATOR + ((riga < 0) ? "-1" : String.valueOf(riga)));
            submitName.append(Values.OPERATION_SEPARATOR + String.valueOf(forceReload));

            String button = "<input type=\"submit\" name=\"" + submitName + "\" title=\"" + title + "\" value=\""
                    + value + "\" class=\"" + style + "\"/>\n";
            return button;
        } else {
            return "";
        }
    }

    protected String getSortLink(SingleValueField<?> field) {
        String image = "";

        SortingRule sortingRule = getComponent().getTable().getLastSortingRule();
        String colummName = StringUtils.isEmpty(field.getAlias()) ? field.getName() : field.getAlias();
        if (sortingRule != null && sortingRule.getSortType() == SortingRule.ASC
                && sortingRule.getColumnName().equalsIgnoreCase(colummName)) {
            image = ASC_IMG;
        } else if (sortingRule != null && sortingRule.getSortType() == SortingRule.DESC
                && sortingRule.getColumnName().equalsIgnoreCase(colummName)) {
            image = DESC_IMG;
        }

        return "<a href=\""
                + getOperationUrl("listSortOnClick", "table=" + getName() + "&amp;column=" + field.getName())
                + ((getMainNavTable() != null && !getMainNavTable().isEmpty())
                        ? "&amp;mainNavTable=" + getMainNavTable() : "")
                + "\">" + field.getHtmlDescription() + image + "</a>";
    }

    public boolean isContentVisible() {
        return contentVisible;
    }

    public void setContentVisible(boolean contentVisible) {
        this.contentVisible = contentVisible;
    }

    public boolean isContentEditable() {
        return contentEditable;
    }

    public void setContentEditable(boolean contentEditable) {
        this.contentEditable = contentEditable;
    }

    public boolean isContentDeletable() {
        return contentDeletable;
    }

    public void setContentDeletable(boolean contentDeletable) {
        this.contentDeletable = contentDeletable;
    }

    protected boolean isRowVisible(BaseRowInterface row) {
        getComponent().getRowSmandrupper().smandruppRow(row);
        return getComponent().getRowSmandrupper().isVisible();
    }

    protected boolean isRowEditable(BaseRowInterface row) {
        getComponent().getRowSmandrupper().smandruppRow(row);
        return getComponent().getRowSmandrupper().isEditable();
    }

    protected boolean isRowDeletable(BaseRowInterface row) {
        getComponent().getRowSmandrupper().smandruppRow(row);
        return getComponent().getRowSmandrupper().isDeletable();
    }

    protected boolean isRowInsertable(BaseRowInterface row) {
        getComponent().getRowSmandrupper().smandruppRow(row);
        return getComponent().getRowSmandrupper().isInsertable();
    }

    public String getMainNavTable() {
        return mainNavTable;
    }

    public void setMainNavTable(String mainNavTable) {
        this.mainNavTable = mainNavTable;
    }

    public boolean isViewAction() {
        return viewAction;
    }

    public void setViewAction(boolean viewAction) {
        this.viewAction = viewAction;
    }

    public boolean isEditAction() {
        return editAction;
    }

    public void setEditAction(boolean editAction) {
        this.editAction = editAction;
    }

    public boolean isDeleteAction() {
        return deleteAction;
    }

    public void setDeleteAction(boolean deleteAction) {
        this.deleteAction = deleteAction;
    }

    public boolean isAddAction() {
        return addAction;
    }

    public void setAddAction(boolean addAction) {
        this.addAction = addAction;
    }

    public boolean isRemoveAction() {
        return removeAction;
    }

    public void setRemoveAction(boolean removeAction) {
        this.removeAction = removeAction;
    }

    public boolean isInsertAction() {
        return insertAction;
    }

    public void setInsertAction(boolean insertAction) {
        this.insertAction = insertAction;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    public boolean isSortable() {
        return sortable && (isLazySortEnabled() || isListInMemory());
    }

    public boolean isLazySortEnabled() {
        return ConfigSingleton.getEnableLazySort();
    }

    public boolean isListInMemory() {
        return getComponent().getTable().getLazyListBean() == null
                || getComponent().getTable().size() == getComponent().getTable().getLazyListBean().getCountResultSize();
    }

    public boolean isMultiRowEdit() {
        return multiRowEdit;
    }

    public void setMultiRowEdit(boolean multiRowEdit) {
        this.multiRowEdit = multiRowEdit;
    }

    public boolean isAbbrLongList() {
        return abbrLongList;
    }

    public void setAbbrLongList(boolean abbrLongList) {
        this.abbrLongList = abbrLongList;
    }

    protected boolean controlVisibilityProperty(BaseRowInterface row, String visibilityProperty) {
        boolean result = true;

        if (visibilityProperty != null && !"".equals(visibilityProperty)) {
            boolean not = false;
            if (visibilityProperty.startsWith("!")) {
                not = true;
                visibilityProperty = visibilityProperty.substring(1);

            }
            Object propertyValue = row.getObject(visibilityProperty);
            if (propertyValue instanceof Boolean) {

                result = (Boolean) propertyValue;

            } else {
                result = notIsNull(propertyValue);
            }

            result = (not ? !result : result);
        }

        return result;
    }

    protected boolean notIsNull(Object propertyValue) {
        // TODO Auto-generated method stub
        boolean result = propertyValue == null;
        if (propertyValue instanceof String) {
            String value = (String) propertyValue;
            result = (value == null) || ("0".equals(value) || ("".equals(value)));

        }
        return !result;
    }

}