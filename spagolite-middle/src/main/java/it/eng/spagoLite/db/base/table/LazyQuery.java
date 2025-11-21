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

import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.Map;

public class LazyQuery implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String queryString;
    private final Map<String, Object> queryParams;
    private final String countQueryString;

    private final CriteriaQuery<?> criteriaQuery; // Buona pratica usare il wildcard
    private final CriteriaQuery<Long> countCriteriaQuery; // Buona pratica usare il tipo corretto

    // ===================================================================================
    // // MEV #39292 - NUOVI CAMPI per supportare le query native
    // ===================================================================================
    private final boolean nativeQuery;
    private final String resultSetMappingName;

    // MEV #39292 - Costruttore originale per query JPQL. Mantiene la retrocompatibilità.
    public LazyQuery(String queryString, String countQueryString, Map<String, Object> queryParams) {
	this.queryString = queryString;
	this.countQueryString = countQueryString;
	this.queryParams = queryParams;
	this.criteriaQuery = null;
	this.countCriteriaQuery = null;

	// Per default, le query create con questo costruttore non sono native.
	this.nativeQuery = false;
	this.resultSetMappingName = null;
    }

    // MEV #39292 - NUOVO COSTRUTTORE: Specifico per gestire sia JPQL che query native.
    public LazyQuery(String queryString, String countQueryString, Map<String, Object> queryParams,
	    boolean isNative, String resultSetMappingName) {
	this.queryString = queryString;
	this.countQueryString = countQueryString;
	this.queryParams = queryParams;
	this.criteriaQuery = null;
	this.countCriteriaQuery = null;

	// Memorizza le nuove informazioni
	this.nativeQuery = isNative;
	this.resultSetMappingName = resultSetMappingName;
    }

    // MEV #39292 - Costruttore originale per query JPQL. Mantiene la retrocompatibilità.
    public LazyQuery(CriteriaQuery<?> criteriaQuery, CriteriaQuery<Long> countCriteriaQuery) {
	this.queryString = null;
	this.countQueryString = null;
	this.queryParams = null;
	this.criteriaQuery = criteriaQuery;
	this.countCriteriaQuery = countCriteriaQuery;

	// Le CriteriaQuery non sono mai "native" in questo contesto.
	this.nativeQuery = false;
	this.resultSetMappingName = null;
    }

    public String getQueryString() {
	return queryString;
    }

    public Map<String, Object> getQueryParams() {
	return queryParams;
    }

    public String getCountQueryString() {
	return countQueryString;
    }

    public CriteriaQuery<?> getCriteriaQuery() {
	return criteriaQuery;
    }

    public CriteriaQuery<Long> getCountCriteriaQuery() {
	return countCriteriaQuery;
    }

    public boolean isCriteriaQuery() {
	return criteriaQuery != null;
    }

    // ===================================================================================
    // MEV #39292 - NUOVI GETTER per accedere alle nuove informazioni
    // ===================================================================================

    /**
     * @return true se la query è una query SQL nativa, false se è JPQL.
     */
    public boolean isNativeQuery() {
	return nativeQuery;
    }

    /**
     * @return Il nome del @SqlResultSetMapping da usare per mappare i risultati, o null se non
     *         specificato.
     */
    public String getResultSetMappingName() {
	return resultSetMappingName;
    }
}