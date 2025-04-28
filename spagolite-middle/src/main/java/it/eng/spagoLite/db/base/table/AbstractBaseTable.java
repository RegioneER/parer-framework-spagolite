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

package it.eng.spagoLite.db.base.table;

import it.eng.spagoLite.FrameElement;
import it.eng.spagoLite.db.base.BaseRowInterface;
import it.eng.spagoLite.db.base.BaseTableInterface;
import it.eng.spagoLite.db.base.sorting.RowComparator;
import it.eng.spagoLite.db.base.sorting.SortingRule;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Element;

public abstract class AbstractBaseTable<T extends BaseRowInterface> extends FrameElement
	implements BaseTableInterface<T> {

    protected static final String BASE_NAME = "ROWS";
    public static final String ABSOLUTE_INDEX = "ABSOLUTE_INDEX";

    private String name;
    protected List<T> list;
    protected int rigaCorrente;
    protected int pageSize;
    protected RowComparator rowComparator;
    protected SortingRule lastSortingRule;
    protected LazyListBean lazyListBean;
    protected LazyListReflectionBean lazyListReflectionBean;

    public AbstractBaseTable() {
	clear();
	this.pageSize = 10;
	this.rowComparator = new RowComparator();
	this.lastSortingRule = new SortingRule();
    }

    protected abstract T createRow();

    public String getName() {
	return this.name;
    }

    public void first() {
	this.rigaCorrente = 0;
    }

    public void prevPage() {
	int capoPrevPag = (int) (Math.floor(this.rigaCorrente / getPageSize()) - 1) * getPageSize();
	if (getPageSize() > 0) {
	    this.rigaCorrente = capoPrevPag >= 0 ? capoPrevPag : getFirstRowPageIndex();
	}
    }

    public void prev() {
	setCurrentRowIndex(getCurrentRowIndex() - 1);
    }

    public void next() {
	setCurrentRowIndex(getCurrentRowIndex() + 1);
    }

    // MEV #33070 - metodo modificato
    public void goPage(int page) {
	// Va settato l'indice della pagina corrente su cui andr� ad atterrare che varia
	// a seconda che la chiamata
	// a questo metodo provenga da una lista paginata o meno.
	// Es. per una lista paginata lazy con 100 record per pagina, passando da pagina
	// 4 a pagina 5 significa
	// atterrare sulla rigaCorrente 100 (indice 0
	// pagina 1, 100 pagina 2, 200 pagine 3, 0 pagina 4, 100 pagina 5)
	// Es. per una lista non paginata lazy con 10 record per pagina, passando da
	// pagina 31 a pagina 32 significa
	// atterrare sulla rica 310 (non avendo blocchi da 300)
	int capoNextPag = (int) (page - 1) * getPageSize();
	int numeroBlocco = (int) Math.floor(capoNextPag / this.size());
	this.rigaCorrente = capoNextPag - (this.size() * numeroBlocco);
    }

    public void nextPage() {
	int capoNextPag = (int) (Math.floor(this.rigaCorrente / getPageSize()) + 1) * getPageSize();
	if (getPageSize() > 0) {
	    this.rigaCorrente = capoNextPag < this.size() ? capoNextPag : getFirstRowPageIndex();
	}
    }

    public void last() {
	this.rigaCorrente = size() - 1;
    }

    public int getCurrentRowIndex() {
	return this.rigaCorrente;
    }

    public void setCurrentRowIndex(int rigaCorrente) {
	if (rigaCorrente >= this.size()) {
	    this.rigaCorrente = this.size() - 1;
	    return;
	}
	if (rigaCorrente < 0) {
	    this.rigaCorrente = 0;
	    return;
	}
	this.rigaCorrente = rigaCorrente;
    }

    public int getPageSize() {
	return this.pageSize;
    }

    public void setPageSize(int pageSize) {
	this.pageSize = pageSize;
    }

    public int getCurrentPageIndex() {
	if (lazyListBean != null) {
	    return (int) Math
		    .ceil((lazyListBean.getFirstResult() + getCurrentRowIndex()) / getPageSize())
		    + 1;
	}
	if (lazyListReflectionBean != null) {
	    return (int) Math.ceil((lazyListReflectionBean.getFirstResult() + getCurrentRowIndex())
		    / getPageSize()) + 1;
	}
	return (int) Math.ceil(getCurrentRowIndex() / getPageSize()) + 1;
    }

    public int getFirstRowPageIndex() {
	return (int) Math.floor(getCurrentRowIndex() / getPageSize()) * getPageSize();
    }

    public int getLastRowPageIndex() {
	return getFirstRowPageIndex() + getPageSize() - 1;
    }

    public int size() {
	return this.list.size();
    }

    public int fullSize() {
	if (lazyListBean != null) {
	    return this.lazyListBean.getCountResultSize();
	}
	if (lazyListReflectionBean != null) {
	    return this.lazyListReflectionBean.getCountResultSize();
	}
	return this.list.size();
    }

    public int getPages() {
	return (int) Math.ceil((double) fullSize() / (double) getPageSize());
    }

    public boolean isEmpty() {
	return this.list.isEmpty();
    }

    public void clear() {
	this.list = new ArrayList<T>();
	this.rigaCorrente = 0;
    }

    public void load(BaseTableInterface<?> table) {
	clear();

	for (BaseRowInterface row : table) {
	    add().copyFromBaseRow(row);
	}
    }

    public T add() {
	return add(null);
    }

    /*
     * (non-Javadoc)
     *
     * @see it.eng.spagoLite.db.base.BaseTableInterface#add(it.eng.spagoLite.db.base.
     * BaseRowInterface)
     */
    public T add(BaseRowInterface baseRowInterface) {
	T row = createRow();

	if (baseRowInterface != null) {
	    row.copyFromBaseRow(baseRowInterface);
	}

	if (getCurrentRowIndex() + 1 < size()) {
	    this.list.add(getCurrentRowIndex() + 1, row);
	} else {
	    this.list.add(row);
	}
	next();
	return row;
    }

    /**
     * Aggiunge record di un tablebean a un tablebean preesistente
     *
     * @param table value
     */
    public void addAll(BaseTableInterface<?> table) {
	for (BaseRowInterface row : table) {
	    add().copyFromBaseRow(row);
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see it.eng.spagoLite.db.base.BaseTableInterface#addFullIdx(it.eng.spagoLite.db.
     * base.BaseRowInterface)
     */
    public T addFullIdx(BaseRowInterface baseRowInterface) {
	T row = createRow();

	if (baseRowInterface != null) {
	    row.copyFromBaseRow(baseRowInterface);
	}
	if (lazyListBean != null) {
	    lazyListBean.incCountResultSize();
	    int rowIndex = (Integer) baseRowInterface.getObject(ABSOLUTE_INDEX);
	    baseRowInterface.setObject(ABSOLUTE_INDEX, null);
	    if (lazyListBean.getFirstResult() <= rowIndex
		    && rowIndex < lazyListBean.getFirstResult() + lazyListBean.getMaxResult()) {
		this.list.add(rowIndex - lazyListBean.getFirstResult(), row);
	    }
	} else {
	    if (getCurrentRowIndex() + 1 < size()) {
		this.list.add(getCurrentRowIndex() + 1, row);
	    } else {
		this.list.add(row);
	    }
	}
	next();
	return row;
    }

    public T remove() {
	return remove(getCurrentRowIndex());
    }

    /*
     * (non-Javadoc)
     *
     * @see it.eng.spagoLite.db.base.BaseTableInterface#remove(int)
     */
    public T remove(int rowIndex) {
	if (size() > 0 && rowIndex < size()) {
	    if (lazyListBean != null) {
		lazyListBean.decCountResultSize();
	    }
	    T row = this.list.remove(rowIndex);
	    if (row.getObject(ABSOLUTE_INDEX) == null) {
		row.setObject(ABSOLUTE_INDEX,
			(lazyListBean != null ? lazyListBean.getFirstResult() : 0) + rowIndex);
	    }
	    setCurrentRowIndex(getCurrentRowIndex());
	    return row;
	}
	return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see it.eng.spagoLite.db.base.BaseTableInterface#removeFullIdx(int)
     */
    public T removeFullIdx(int rowIndex) {
	if (lazyListBean != null) {
	    lazyListBean.decCountResultSize();
	    if (lazyListBean.getFirstResult() <= rowIndex
		    && rowIndex < lazyListBean.getFirstResult() + lazyListBean.getMaxResult()) {
		T row = this.list.remove(rowIndex - lazyListBean.getFirstResult());
		setCurrentRowIndex(getCurrentRowIndex());
		return row;
	    }
	} else if (size() > 0 && rowIndex < size()) {
	    T row = this.list.remove(rowIndex);
	    setCurrentRowIndex(getCurrentRowIndex());
	    return row;
	}

	return null;
    }

    public void removeAll() {
	while (size() > 0) {
	    remove();
	}
    }

    public T getCurrentRow() {
	return getRow(getCurrentRowIndex());
    }

    public T getRow(int index) {
	if (index < 0) {
	    return null;
	}

	if (index > (size() - 1)) {
	    return null;
	}
	return (T) this.list.get(index);

    }

    public Iterator<T> iterator() {
	return this.list.iterator();
    }

    public boolean contains(T o) {
	return this.list.contains(o);
    }

    public void addSortingRule(String columnName, int sortType) {
	this.addSortingRule(new SortingRule(columnName, sortType));
    }

    public void addSortingRule(String columnName) {
	this.addSortingRule(new SortingRule(columnName, SortingRule.ASC));
    }

    public void addSortingRule(SortingRule sortingRule) {
	if (sortingRule == null) {
	    return;
	}
	this.rowComparator.addRule(sortingRule);
    }

    public void addSortingRule(SortingRule[] sortingRules) {
	if (sortingRules == null) {
	    return;
	}
	for (int i = 0; i < sortingRules.length; i++) {
	    addSortingRule(sortingRules[i]);
	}
    }

    public void clearSortingRule() {
	this.rowComparator.clearRule();
	this.lastSortingRule = new SortingRule();
    }

    @SuppressWarnings("unchecked")
    public void sort() {
	if (size() > 0) {
	    BaseRowInterface currentRow = getCurrentRow();
	    Collections.sort(this.list, this.rowComparator);

	    int i = 0;
	    for (T row : this) {
		if (row == currentRow) {
		    setCurrentRowIndex(i++);
		    return;
		}
	    }
	}

    }

    public SortingRule getLastSortingRule() {
	return rowComparator.getLastSortingRule();
    }

    @Override
    public Element asXml() {
	Element element = super.asXml();
	element.addAttribute("size", "" + size());

	for (BaseRowInterface row : list) {
	    element.add(row.asXml());
	}

	return element;
    }

    public BigDecimal sum(String columnName) {
	BigDecimal somma = null;
	for (T row : this) {
	    if (row.getObject(columnName) != null
		    && row.getObject(columnName) instanceof BigDecimal) {
		if (somma == null) {
		    somma = row.getBigDecimal(columnName);
		} else {
		    somma = somma.add(row.getBigDecimal(columnName));
		}
	    }
	}

	return somma;
    }

    public List<Object> toList(String fieldName, SortingRule[] sortingRules) {
	List<Object> result = null;
	if (this.list != null) {
	    addSortingRule(sortingRules);
	    sort();
	    result = new ArrayList<Object>();
	    for (T row : this.list) {
		result.add(row.getObject(fieldName));
	    }
	}
	return result;
    }

    public List<Object> toList(String fieldName) {
	return toList(fieldName, new SortingRule[] {
		getLastSortingRule() });
    }

    public LazyListInterface getLazyListInterface() {
	return lazyListBean != null ? lazyListBean : lazyListReflectionBean;
    }

    public void setLazyListBean(LazyListBean lazyListBean) {
	this.lazyListBean = lazyListBean;
    }

    public LazyListReflectionBean getLazyListReflectionBean() {
	return lazyListReflectionBean;
    }

    public void setLazyListReflectionBean(LazyListReflectionBean lazyListReflectionBean) {
	this.lazyListReflectionBean = lazyListReflectionBean;
    }
}
