package it.eng.spagoLite.db.base;

import it.eng.spagoLite.db.base.sorting.SortingRule;
import it.eng.spagoLite.db.base.table.LazyListBean;

import java.io.Serializable;

public interface BaseTableInterface<T extends BaseRowInterface> extends Iterable<T>, Serializable {

    /**
     * Imposta come riga corrente la prima riga dell'elenco
     */
    public void first();

    /**
     * Imposta riga corrente sulla prima riga della pagina passata come parametro.
     *
     * @param page
     */
    public void goPage(int page);

    /**
     * Imposta riga corrente sulla prima riga della pagina precedente a quella corrente.
     *
     */
    public void prevPage();

    /**
     * Decrementa la riga corente.
     */
    public void prev();

    /**
     * Incrementa la riga corrente.
     */
    public void next();

    /**
     * Imposta riga corrente sulla prima riga della pagina successiva a quella corrente.
     *
     */
    public void nextPage();

    /**
     * Imposta come riga corrente l'ultima riga dell'elenco.
     */
    public void last();

    /**
     * Ritorna il numero della riga corrente (0 - n).
     *
     * @return currentRowIndex
     */
    public int getCurrentRowIndex();

    /**
     * Imposta il numero della riga corrente (0 - n).
     *
     * @param rigaCorrente
     */
    public void setCurrentRowIndex(int rigaCorrente);

    /**
     * @return Returns the pageSize.
     */
    public int getPageSize();

    /**
     * @param pageSize
     *            The pageSize to set.
     */
    public void setPageSize(int pageSize);

    /**
     * @return la pagina corrente
     */
    public int getCurrentPageIndex();

    /**
     * @return ritorna la prima riga della pagina corrente
     */
    public int getFirstRowPageIndex();

    /**
     * @return ritorna l'ultima riga della pagina corrente
     */
    public int getLastRowPageIndex();

    /**
     * Ritorna il numero di righe attualmente caricate
     *
     * @return dimensione
     */
    public int size();

    /**
     * Ritorna il numero di righe totali
     *
     * @return numero totale
     */
    public int fullSize();

    /**
     * Ritorna il numero di pagine attualmente caricate
     *
     * @return numero di pagine
     */
    public int getPages();

    /**
     * @return se l'elenco è vuoto
     */
    public boolean isEmpty();

    /**
     * Ripulisce l'elenco
     */
    public void clear();

    /**
     * Carica il table bean a partire dalla tbella passata
     *
     * @param table
     */
    void load(BaseTableInterface<?> table);

    /**
     * Carica il bean da DB
     *
     * @param query
     * 
     * @throws EMFError
     * @throws SQLException
     */
    // public void load(ISelectQuery query) throws EMFError, EMFError, SQLException;

    /**
     * Carica il bean da DB
     *
     * @param dataConnection
     * @param query
     * 
     * @throws SQLException
     * @throws EMFError
     */
    // public void load(DataConnection dataConnection, ISelectQuery query) throws EMFError, EMFError, SQLException;

    /**
     * Aggiunge una riga all'elenco
     *
     * @return oggetto generico T aggiunto
     */
    public T add();

    /**
     * Aggiunge una riga all'elenco
     *
     * @param row
     * 
     * @return oggetto generico T aggiunto
     */
    public T add(BaseRowInterface row);

    /**
     * Rimuove una riga all'elenco
     *
     * @return oggetto generico T rimosso
     */
    public T remove();

    /**
     * Rimuove la riga con indice relativo rowIndex dalla lista e decrementa la "fullSize". Ritorna la riga rimossa
     * aggiungendogli un attributo che indica l'indice assoluto della riga rispetto la lista se tale indice non era già
     * stato settato
     *
     * @param rowIndex
     *            indice relativo della riga
     * 
     * @return la riga rimossa
     */
    public T remove(int rowIndex);

    /**
     * Rimuove tutte le righe
     */
    public void removeAll();

    /**
     * Ritorna la riga corrente
     *
     * @return riga corrente
     */
    public T getCurrentRow();

    /**
     * Ritorna la riga passata. Se:
     * <ul>
     * <li>la tabella è vuota</li>
     * <li>o index è < 0</li>
     * <li>o il numero delle righe è minore di index</li>
     * </ul>
     *
     * ritorna null.
     *
     * @param index
     *            indice riga
     * 
     * @return riga passata
     */
    public T getRow(int index);

    /**
     * Aggiunge un criterio di ordinamento
     *
     * @param columnName
     * @param sortType
     */
    public void addSortingRule(String columnName, int sortType);

    public SortingRule getLastSortingRule();

    /**
     * Pulisce i criteri di ordinamento
     */
    public void clearSortingRule();

    /**
     * Ordina la tabella
     *
     */
    public void sort();

    /**
     * Get bean for paging.
     *
     * @return bean per la paginazione.
     */
    public LazyListBean getLazyListBean();

    /**
     * Rimuove la riga con indice assoluto RowIndex dalla lista e decrementa la "fullsize"
     *
     * @param rowIndex
     *            indice assoluto della riga
     * 
     * @return la riga rimossa
     */
    public T removeFullIdx(int rowIndex);

    /**
     * Aggiunge una riga alla lista nella posizione assoluta specificata dall'attributo ABSOLUTE_INDEX settato
     * all'interno della riga stessa. Provvede a ripulire l'indice.
     *
     * @param baseRowInterface
     * 
     * @return la riga aggiunta
     */
    public T addFullIdx(BaseRowInterface baseRowInterface);

}
