package it.eng.paginator.hibernate;

import it.eng.paginator.util.QueryUtils;
import org.hibernate.EmptyInterceptor;

public class OracleSqlInterceptor extends EmptyInterceptor {

    @Override
    public String onPrepareStatement(String sql) {
        return QueryUtils.fixOracleMultiColumnDistinct(sql);
    }
}
