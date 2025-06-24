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
package it.eng.spagoLite.actions.form;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoLite.db.base.BaseTableInterface;
import it.eng.spagoLite.db.base.paging.IPaginator;
import it.eng.spagoLite.db.base.sorting.SortingRule;
import it.eng.spagoLite.form.Form;
import it.eng.spagoLite.form.fields.SingleValueField;
import it.eng.spagoLite.form.list.List;
import it.eng.spagoLite.message.MessageBox.ViewMode;
import it.eng.spagoLite.security.IUser;
import it.eng.spagoLite.util.NavigationValidator;

public abstract class ListAction<T extends Form, U extends IUser<?>> extends FormAction<T, U> {

    @Autowired
    private IPaginator paginator;
    public static final String NE_ELENCO = "elenco";
    public static final String NE_DETTAGLIO_VIEW = "dettaglioView";
    public static final String NE_DETTAGLIO_UPDATE = "update";
    public static final String NE_DETTAGLIO_INSERT = "dettaglioInsert";
    public static final String NE_DETTAGLIO_DELETE = "delete";
    public static final String NE_DETTAGLIO_CONFIRM_DELETE = "confirmdelete";
    public static final String NE_DETTAGLIO_SELECT = "select";
    public static final String NE_EXPORT_XLS = "esportaExcel";
    public static final String NE_FIRST = "first";
    public static final String NE_PREV_PAGE = "prevPage";
    public static final String NE_PREV = "prev";
    public static final String NE_NEXT = "next";
    public static final String NE_NEXT_PAGE = "nextPage";
    public static final String NE_GOTO_PAGE = "gotoPage";
    public static final String NE_SET_PAGE_SIZE = "setPageSize";
    public static final String NE_LAST = "last";
    public static final String NE_GOTO_ROW = "gotoRow";
    public static final String NE_DETTAGLIO_SAVE = "dettaglioSave";
    public static final String NE_DETTAGLIO_CANCEL = "dettaglioUndo";
    public static final String NE_UPDATED_ROW = "updatedRow";
    public static final String NE_SHOW_INACTIVE_ROWS = "showInactiveRows";
    public static final String NE_HIDE_INACTIVE_ROWS = "hideInactiveRows";
    //
    private static final String CONFIRM_DELETE_PAGE = "/confirmDeletePage";
    private static final long serialVersionUID = 4683122278656206133L;

    public abstract void loadDettaglio() throws EMFError;

    public abstract void undoDettaglio() throws EMFError;

    public abstract void insertDettaglio() throws EMFError;

    public abstract void update(List<?> list) throws EMFError;

    public abstract void delete(List<?> list) throws EMFError;

    public abstract void saveDettaglio() throws EMFError;

    public abstract void select(List<?> list) throws EMFError;

    public abstract void dettaglioOnClick() throws EMFError;

    public abstract void elencoOnClick() throws EMFError;

    public abstract void initOnClick() throws EMFError;

    public abstract void filterInactiveRecords(List<?> list) throws EMFError;

    public abstract void goToPageNavigation(List<?> list) throws EMFError;

    //
    public void confirmDelete() throws EMFError {
        setParameters();
        listNavigationOnClick(getTableName(), getNavigationEvent(), getRiga(), getForceReload());
    }

    public void cancelDelete() {
        goBack();
    }

    /**
     * Metodo di utility che viene eseguito dopo ogni query di paginazione lazy
     *
     * @param list
     *            value
     *
     * @throws EMFError
     *             eccezione generica
     */
    public abstract void postLazyLoad(List<?> list) throws EMFError;

    /**
     * Gestisce l'ordinamento sull'ordinamento delle liste
     *
     * @throws EMFError
     *             eccezione generica
     */
    public void listSortOnClick() throws EMFError {
        String tableName = getRequest().getParameter("table");
        String columnName = getRequest().getParameter("column");

        List<SingleValueField<Object>> list = (List<SingleValueField<Object>>) getForm().getComponent(tableName);

        if (!StringUtils.isEmpty(list.getComponent(columnName).getAlias())) {
            columnName = list.getComponent(columnName).getAlias();
        }

        SortingRule lastSortingRule = list.getTable().getLastSortingRule();
        list.getTable().clearSortingRule();
        if (lastSortingRule != null && lastSortingRule.getColumnName().equalsIgnoreCase(columnName)) {
            list.getTable().addSortingRule(columnName, -lastSortingRule.getSortType());
        } else {
            list.getTable().addSortingRule(columnName, SortingRule.ASC);
        }
        if (!this.lazySortCurrentPage(list)) {
            list.getTable().sort();
        }
        postLoad();

        forwardToPublisher(getLastPublisher());
    }

    public void listDetailNavigationOnClick() throws EMFError {
        setParameters();
        listDetailNavigationOnClick(getTableName(), getNavigationEvent(), getRiga(), getForceReload());
    }

    public void listDetailNavigationOnClick(String[] param) throws EMFError {
        setParameters(param);
        listDetailNavigationOnClick(getTableName(), getNavigationEvent(), getRiga(), getForceReload());
    }

    public void listNavigationOnClick() throws EMFError {
        setParameters();
        listNavigationOnClick(getTableName(), getNavigationEvent(), getRiga(), getForceReload());
    }

    // probabilmente questa è una submit
    public void listNavigationOnClick(String[] param) throws EMFError {
        setParameters(param);
        listNavigationOnClick(getTableName(), getNavigationEvent(), getRiga(), getForceReload());
    }

    /**
     * Gestisce gli eventi di navigazione sulle liste
     *
     * @param tableName
     *            nome tabella
     * @param navigationEvent
     *            evento
     * @param riga
     *            riga
     * @param forceReload
     *            reload
     *
     * @throws EMFError
     *             eccezione generica
     */
    protected void listNavigationOnClick(String tableName, String navigationEvent, String riga, String forceReload)
            throws EMFError {
        List<?> list = (List<?>) getForm().getComponent(tableName);
        // Forzo il goback se la tabella nella lista è null...
        // l'utente potrebbe aver cliccato il tasto back del browser
        if (list.getTable() == null) {
            getMessageBox().addWarning(
                    "Si è verificato un errore (probabilmente) a seguito dell'utilizzo del tasto Indietro del browser.\n E' stata recuperata l'ultima pagina aperta correttamente.");
            getMessageBox().setViewMode(ViewMode.plain);
            this.goBack(true);
            return;
        }
        calculateAuthorization(tableName);
        /* aggiunto per garantire il tipo di http method utilizzato in base a navigationEvent */
        if (!authorizeHttpMethodOnNavigationEvent(navigationEvent)) {
            return;
        }
        forwardToPublisher(getLastPublisher());
        if (navigationEvent.equalsIgnoreCase(NE_ELENCO)) {
            elencoOnClick();
        } else if (navigationEvent.equalsIgnoreCase(NE_DETTAGLIO_VIEW)) {
            list.getTable().setCurrentRowIndex(Integer.parseInt(riga));
            if ("true".equalsIgnoreCase(forceReload)) {
                initOnClick();
            }
            loadDettaglio();
            if (isViewAction()) {
                dettaglioOnClick();
            }
        } else if (navigationEvent.equalsIgnoreCase(NE_DETTAGLIO_UPDATE)) {
            list.getTable().setCurrentRowIndex(Integer.parseInt(riga));
            loadDettaglio();
            dettaglioOnClick();
            // Check security
            list.getRowSmandrupper().smandruppRow(list.getTable().getCurrentRow());
            if (list.getRowSmandrupper().isEditable() && isEditAction()) {
                update(list);
            }
        } else if (navigationEvent.equalsIgnoreCase(NE_DETTAGLIO_INSERT)) {
            loadDettaglio();
            dettaglioOnClick();

            // Check security
            list.getRowSmandrupper().smandruppRow(list.getTable().getCurrentRow());
            if (list.getRowSmandrupper().isInsertable() && isInsertAction()) {
                insertDettaglio();
            }
        } else if (navigationEvent.equalsIgnoreCase(NE_DETTAGLIO_CONFIRM_DELETE)) {
            list.getTable().setCurrentRowIndex(Integer.parseInt(riga));
            // Check security
            list.getRowSmandrupper().smandruppRow(list.getTable().getCurrentRow());
            if (list.getRowSmandrupper().isDeletable() && isDeleteAction()) {
                forwardToDeletePage();
            }
        } else if (navigationEvent.equalsIgnoreCase(NE_DETTAGLIO_DELETE)) {
            list.getTable().setCurrentRowIndex(Integer.parseInt(riga));
            dettaglioOnClick();
            // Check security
            list.getRowSmandrupper().smandruppRow(list.getTable().getCurrentRow());
            if (list.getRowSmandrupper().isDeletable() && isDeleteAction()) {
                delete(list);
            }
        } else if (navigationEvent.equalsIgnoreCase(NE_FIRST)) {
            if (!this.lazyLoadFirstPage(list)) {
                list.getTable().first();
            }
            loadDettaglio();
        } else if (navigationEvent.equalsIgnoreCase(NE_PREV_PAGE)) {
            if (!this.lazyLoadPrevPage(list)) {
                list.getTable().prevPage();
            }
            loadDettaglio();
        } else if (navigationEvent.equalsIgnoreCase(NE_PREV)) {
            // evento invocato se sto paginando dal dettaglio
            // invoco il lazyloading solo se è una tabella paginata e se sono alla prima riga
            if (list.getTable().getLazyListInterface() != null && list.getTable().getCurrentRowIndex() == 0) {
                this.lazyLoadPrevPage(list);
            } else {
                list.getTable().prev();
            }
            loadDettaglio();
        } else if (navigationEvent.equalsIgnoreCase(NE_NEXT)) {
            // evento invocato se sto paginando dal dettaglio
            // invoco il lazyloading solo se è una tabella paginata e se sono all'ultima riga (ie. l'ultima riga della
            // pagina +1 è maggiore del numero di risultati relativi)
            if (list.getTable().getLazyListInterface() != null && list.getTable().getCurrentRowIndex()
                    + 1 > list.getTable().getLazyListInterface().getMaxResult() - 1) {
                this.lazyLoadNextPage(list);
            } else {
                list.getTable().next();
            }
            loadDettaglio();
        } else if (navigationEvent.equalsIgnoreCase(NE_NEXT_PAGE)) {
            // evento invocato se sto paginando dalla tabella
            if (!this.lazyLoadNextPage(list)) {
                list.getTable().nextPage();
            }
            loadDettaglio();
        }
        // MEV #33070
        else if (navigationEvent.equalsIgnoreCase(NE_GOTO_PAGE)) {
            // Effetto i controlli formali sul parametro
            NavigationValidator validatore = new NavigationValidator(getMessageBox());

            Integer goToNumPagList = validatore.getValidNavigationInteger(getRequest(), list.getTable().getPages(),
                    list.getName());

            // Se il numero è corretto (un intero e che non supera il numero di pagine disponibili)
            if (!getMessageBox().hasError()) {
                // Mi posiziono alla pagina desiderata
                if (!this.lazyLoadGoPage(list, goToNumPagList)) {
                    list.getTable().goPage(goToNumPagList);
                }
            }

            // eventuale chiamata al goToPageNavigation(list) di cui eseguire l'override nella action
        } else if (navigationEvent.equalsIgnoreCase(NE_GOTO_ROW)) {
            list.getTable().setCurrentRowIndex(Integer.parseInt(riga));
            loadDettaglio();
        } else if (navigationEvent.equalsIgnoreCase(NE_LAST)) {
            if (!this.lazyLoadLastPage(list)) {
                list.getTable().last();
            }
            loadDettaglio();
        } else if (navigationEvent.equalsIgnoreCase(NE_EXPORT_XLS)) {
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
            String fileName = StringUtils.isNotBlank(list.getExcelFileName())
                    ? StringEscapeUtils.escapeJava(list.getExcelFileName())
                    : list.getName() + "_" + formatter.format(Calendar.getInstance().getTime());
            getResponse().setContentType("application/xls");
            getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".xls");
            list.writeExcel(getServletOutputStream());
            freeze();
        } else if (navigationEvent.equalsIgnoreCase(NE_DETTAGLIO_SAVE)) {
            saveDettaglio();
        } else if (navigationEvent.equalsIgnoreCase(NE_DETTAGLIO_CANCEL)) {
            list.getTable().setCurrentRowIndex(Integer.parseInt(riga));
            undoDettaglio();
        } else if (navigationEvent.equalsIgnoreCase(NE_DETTAGLIO_SELECT)) {
            list.getTable().setCurrentRowIndex(Integer.parseInt(riga));
            select(list);
        } else if (navigationEvent.equalsIgnoreCase(NE_SET_PAGE_SIZE)) {
            list.getTable().setPageSize(Integer.parseInt(getRiga()));
        } else if (navigationEvent.equalsIgnoreCase(NE_SHOW_INACTIVE_ROWS)) {
            list.setFilterValidRecords(false);
            filterInactiveRecords(list);
        } else if (navigationEvent.equalsIgnoreCase(NE_HIDE_INACTIVE_ROWS)) {
            list.setFilterValidRecords(true);
            filterInactiveRecords(list);
        }
        postLoad();
    }

    private void forwardToDeletePage() {
        //
        getRequest().setAttribute("table", getTableName());
        getRequest().setAttribute("riga", getRiga());
        getRequest().setAttribute("forceReload", getForceReload());

        forwardToPublisherSkipSetLast(CONFIRM_DELETE_PAGE);
    }

    private boolean lazySortCurrentPage(List<?> list) throws EMFError {
        BaseTableInterface<?> table = list.getTable();
        // invoco l'ordinamento lazy se è una tabella paginata
        if (table.getLazyListInterface() != null
                && (table.size() < table.getLazyListInterface().getCountResultSize())) {
            table = getPaginator().sort(table);
            list.setTable(table);
            postLazyLoad(list);
            return true;
        }
        return false;

    }

    private boolean lazyLoadNextPage(List<?> list) throws EMFError {
        BaseTableInterface<?> table = list.getTable();
        // invoco la paginazione lazy se è una tabella paginata e se la pagina successiva non è contenuta
        // interamente nella lista che ho in memoria
        if (table.getLazyListInterface() != null && table.getLastRowPageIndex() + table.getPageSize() + 1 > table
                .getLazyListInterface().getMaxResult()) {
            table = getPaginator().nextPage(table);
            list.setTable(table);
            postLazyLoad(list);
            return true;
        }
        return false;

    }

    private boolean lazyLoadPrevPage(List<?> list) throws EMFError {
        BaseTableInterface<?> table = list.getTable();
        // invoco la paginazione lazy se è una tabella paginata e se la pagina successiva non è contenuta
        // interamente nella lista che ho in memoria
        if (table.getLazyListInterface() != null && table.getFirstRowPageIndex() - table.getPageSize() < 0) {
            table = getPaginator().prevPage(table);
            list.setTable(table);
            postLazyLoad(list);
            return true;
        }
        return false;
    }

    private boolean lazyLoadLastPage(List<?> list) throws EMFError {
        BaseTableInterface<?> table = list.getTable();
        if (table.getLazyListInterface() != null && table.fullSize() > table.getLazyListInterface().getFirstResult()
                + table.getLazyListInterface().getMaxResult()) {
            table = getPaginator().lastPage(table);
            list.setTable(table);
            postLazyLoad(list);
            return true;
        }
        return false;

    }

    private boolean lazyLoadFirstPage(List<?> list) throws EMFError {
        BaseTableInterface<?> table = list.getTable();
        if (table.getLazyListInterface() != null && table.getLazyListInterface().getFirstResult() > 0) {
            table = getPaginator().firstPage(table);
            list.setTable(table);
            postLazyLoad(list);
            return true;
        }
        return false;

    }

    protected boolean lazyLoadGoPage(List<?> list, int page) throws EMFError {
        BaseTableInterface<?> table = list.getTable();
        // invoco la paginazione lazy se è una tabella paginata
        if (table.getLazyListInterface() != null && // se il primo index della lista che ho in memoria è maggiore del
                                                    // primo index della pagina di destinazione
                ((table.getLazyListInterface().getFirstResult() > (page - 1) * table.getPageSize()) || // se l'ultimo
                                                                                                       // index
                                                                                                       // della lista
                                                                                                       // che ho
                                                                                                       // in memoria è
                                                                                                       // minore
                                                                                                       // dell'ultimo
                                                                                                       // index
                                                                                                       // della pagina
                                                                                                       // di
                                                                                                       // destinazione
                        (table.getLazyListInterface().getFirstResult()
                                + table.getLazyListInterface().getMaxResult() < page * table.getPageSize() - 1))) {
            table = getPaginator().goPage(table, page);

            // MEV #33070 - aggiungo il calcolo del corretto currentRowIndex della lista lazy
            // Essendo blocchi di dimensione predefinita (es. 300), il currentRowIndex dovrà sempre essere un valore
            // compreso tra 0 e 299
            int currentRowIndex = ((page * table.getPageSize())
                    - ((int) (Math.floor((page * table.getPageSize()) / table.getLazyListInterface().getMaxResult()))
                            * table.getLazyListInterface().getMaxResult()));
            if (currentRowIndex == 0) {
                currentRowIndex = table.getLazyListInterface().getMaxResult() - table.getPageSize();
            } else {
                currentRowIndex = currentRowIndex - table.getPageSize();
            }
            table.setCurrentRowIndex(currentRowIndex);

            list.setTable(table);
            postLazyLoad(list);
            return true;
        }
        return false;

    }

    protected void listDetailNavigationOnClick(String tableName, String navigationEvent, String riga,
            String forceReload) throws EMFError {
        List<?> list = (List<?>) getForm().getComponent(tableName);
        forwardToPublisher(getLastPublisher());
        if (navigationEvent.equalsIgnoreCase(NE_DETTAGLIO_UPDATE)) {
            if (list.postAndValidate(getRequest(), getMessageBox())) {
                updateEditableRows(list);
                list.getTable().setCurrentRowIndex(Integer.parseInt(riga)); // Cambio
                // riga
                update(list);
            }
        } else if (navigationEvent.equalsIgnoreCase(NE_DETTAGLIO_DELETE)) {
            list.getTable().setCurrentRowIndex(Integer.parseInt(riga));
            delete(list);
        } else if (navigationEvent.equalsIgnoreCase(NE_DETTAGLIO_CONFIRM_DELETE)) {
            list.getTable().setCurrentRowIndex(Integer.parseInt(riga));
            forwardToDeletePage();
        }
    }

    private void updateEditableRows(List<?> list) throws EMFError {
        for (SingleValueField<?> field : list) {
            if (field.isEditMode()) {
                list.getTable().getCurrentRow().setObject(field.getName(), list.getColumn(field.getName()).parse());
                Object value = list.getTable().getCurrentRow().getObject(field.getName());
                list.getColumn(field.getName()).setValue((value != null) ? value.toString() : null);

            }
        }
    }

    // meccanismo di "sicurezza" per garantire che il singolo *navigationEvent* risponda
    // ad un certo http method (e.g. evitare che la delete di un elemento in lista venga effettuata
    // via GET anzichè POST)
    private boolean authorizeHttpMethodOnNavigationEvent(String navigationEvent) throws EMFError {
        boolean result = true; // default
        if (NE_DETTAGLIO_DELETE.equals(navigationEvent)) {
            result = HttpMethod.POST.name().equalsIgnoreCase(getRequest().getMethod());
        }
        if (!result) {
            try {
                getResponse().sendError(HttpStatus.METHOD_NOT_ALLOWED.value());
            } catch (IOException e) {
                throw new EMFError(EMFError.ERROR, e.getMessage());
            }
        }
        return result;
    }

    @Override
    public void process() throws EMFError {
    }

    public IPaginator getPaginator() {
        return paginator;
    }

    public void setPaginator(IPaginator paginator) {
        this.paginator = paginator;
    }

}
