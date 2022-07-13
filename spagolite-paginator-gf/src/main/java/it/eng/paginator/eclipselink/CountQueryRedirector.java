package it.eng.paginator.eclipselink;

import it.eng.paginator.ejb.PaginatorInterceptor;
import it.eng.spagoLite.db.base.sorting.SortingRule;
import it.eng.spagoLite.db.base.table.LazyListBean;
import java.math.BigDecimal;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.internal.sessions.AbstractRecord;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.internal.sessions.ArrayRecord;
import org.eclipse.persistence.queries.*;
import org.eclipse.persistence.sessions.*;

/**
 *
 * @author Quaranta_M
 */
public class CountQueryRedirector implements QueryRedirector {

    // private static final String FL_COUNT_QUERY = "fl_count_query";
    @Override
    public Object invokeQuery(DatabaseQuery query, Record arguments, Session session) {
        query.setRedirector(null);
        if (query instanceof ObjectLevelReadQuery) {
            // setto il risultato della count nel bean threadlocal
            LazyListBean llBean = PaginatorInterceptor.getLazyListBean();
            ObjectLevelReadQuery countQuery = (ObjectLevelReadQuery) query.clone();
            // Rimuovo l'order by
            countQuery.setOrderByExpressions(null);

            // Rimuovo la select list
            if (countQuery.isReportQuery()) {
                ((ReportQuery) countQuery).clearItems();
                if (llBean.getCountSelectList() != null) {
                    String[] selectList = llBean.getCountSelectList().split(",");
                    for (String s : selectList) {
                        ((ReportQuery) countQuery).addAttribute(s);
                    }
                } else {
                    ((ReportQuery) countQuery).selectValue1();
                }
            } else if (countQuery.isReadAllQuery()) {
                ReportQuery rq = new ReportQuery(countQuery.getReferenceClass(), countQuery.getExpressionBuilder());
                rq.setSelectionCriteria(countQuery.getSelectionCriteria());
                rq.setDistinctState(countQuery.getDistinctState());
                countQuery = rq;
                // Se è necessario invocare la SELECT con la primarykey
                if (llBean.getCountSelectList() != null) {
                    String[] selectList = llBean.getCountSelectList().split(",");
                    for (String s : selectList) {
                        ((ReportQuery) countQuery).addAttribute(s);
                    }
                } else {
                    ((ReportQuery) countQuery).selectValue1();
                }
            }

            // Pulisco eventuali paginazioni
            countQuery.setFirstResult(0);
            countQuery.setMaxRows(0);

            // costruisco la select count() intorno la query
            StringBuilder countSQL = new StringBuilder("SELECT COUNT(*) FROM (");
            countSQL.append(countQuery.getTranslatedSQLString(session, arguments));
            countSQL.append(")");
            // StringBuilder countSQL = new StringBuilder("SELECT COUNT(*) FROM (");
            // countSQL.append(countQuery.getSQLString());
            // countSQL.append(")");
            DataReadQuery dataQuery = new DataReadQuery(countSQL.toString());
            ArrayRecord count = (ArrayRecord) ((Vector) dataQuery.execute((AbstractSession) session,
                    (AbstractRecord) arguments)).get(0);

            int countResult = ((BigDecimal) count.get("COUNT(*)")).intValueExact();
            llBean.setCountResultSize(countResult);

            ObjectLevelReadQuery origQuery = ((ObjectLevelReadQuery) query);

            // Pagino la query originale - DA
            origQuery.setFirstResult(llBean.getFirstResult());
            // Pagino la query originale - A
            llBean.setMaxResult(origQuery.getMaxRows());
            origQuery.setMaxRows(llBean.getFirstResult() + llBean.getMaxResult());
            // Ordino se necessario
            if (llBean.isSortQuery()) {
                ExpressionBuilder builder = new ExpressionBuilder();
                Expression ex = builder.get(toCamelCase(llBean.getOrderByColumnName(), true));
                origQuery.setOrderByExpressions(null);
                origQuery.addOrdering(
                        llBean.getOrderBySortingRule() == SortingRule.ASC ? ex.ascending() : ex.descending());
            }
            // eseguo la query originale paginata
            return query.execute((AbstractSession) session, (AbstractRecord) arguments);

        } else {
            // eseguo la query originale non paginata
            return query.execute((AbstractSession) session, (AbstractRecord) arguments);

        }

    }

    private String toCamelCase(String value, boolean startWithLowerCase) {
        String[] strings = StringUtils.split(value.toLowerCase(), "_");
        for (int i = startWithLowerCase ? 1 : 0; i < strings.length; i++) {
            strings[i] = StringUtils.capitalize(strings[i]);
        }
        return StringUtils.join(strings);
    }
}