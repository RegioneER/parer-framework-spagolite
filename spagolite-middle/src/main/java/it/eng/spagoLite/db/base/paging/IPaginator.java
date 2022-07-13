package it.eng.spagoLite.db.base.paging;

import it.eng.spagoLite.db.base.BaseTableInterface;

public interface IPaginator {

    BaseTableInterface<?> nextPage(BaseTableInterface<?> tableBean);

    BaseTableInterface<?> prevPage(BaseTableInterface<?> tableBean);

    BaseTableInterface<?> firstPage(BaseTableInterface<?> tableBean);

    BaseTableInterface<?> lastPage(BaseTableInterface<?> tableBean);

    BaseTableInterface<?> goPage(BaseTableInterface<?> tableBean, int page);

    BaseTableInterface<?> sort(BaseTableInterface<?> tableBean);
}
