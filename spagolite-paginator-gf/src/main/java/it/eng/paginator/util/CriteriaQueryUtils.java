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

package it.eng.paginator.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;

import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

import it.eng.spagoLite.db.base.sorting.SortingRule;
import it.eng.spagoLite.db.base.table.Param;

public abstract class CriteriaQueryUtils {

    private static volatile int aliasCount = 0;

    /**
     *
     * @param em
     * @param criteria
     *
     * @return
     */
    public static <T> int count(EntityManager em, CriteriaQuery<T> criteria) {
        return count(em, criteria, Collections.emptySet());
    }

    /**
     * Result count from a CriteriaQuery
     *
     * @param em
     *            Entity Manager
     * @param criteria
     *            Criteria Query to count results
     *
     * @param params
     *
     * @return row count
     */
    public static <T> int count(EntityManager em, CriteriaQuery<T> criteria, Set<Param> params) {

        final TypedQuery<Long> query = em.createQuery(countCriteria(em, criteria));
        params.stream().forEach(p -> p.setParameter(query));
        return query.getSingleResult().intValue();
    }

    /**
     * Create a row count CriteriaQuery from a CriteriaQuery
     *
     * @param em
     *            entity manager
     * @param criteria
     *            source criteria
     *
     * @return row count CriteriaQuery
     */
    public static <T> CriteriaQuery<Long> countCriteria(EntityManager em, CriteriaQuery<T> criteria) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        copyCriteriaWithoutSelectionAndOrder(criteria, countCriteria, false);

        Expression<Long> countExpression;

        if (criteria.isDistinct()) {
            countExpression = builder.countDistinct(findRoot(countCriteria, criteria.getResultType()));
        } else {
            countExpression = builder.count(findRoot(countCriteria, criteria.getResultType()));
        }

        return countCriteria.select(countExpression);
    }

    /**
     * Gets The result alias, if none set a default one and return it
     *
     * @param selection
     *
     * @return root alias or generated one
     */
    private static synchronized <T> String getOrCreateAlias(Selection<T> selection) {
        // reset alias count
        if (aliasCount > 1000)
            aliasCount = 0;

        String alias = selection.getAlias();
        if (alias == null) {
            alias = "PARER_generatedAlias" + aliasCount++;
            selection.alias(alias);
        }
        return alias;
    }

    /**
     * Find the Root with type class on CriteriaQuery Root Set
     *
     * @param <T>
     *            root type
     * @param query
     *            criteria query
     * @param clazz
     *            root type
     *
     * @return Root<T> of null if none
     */
    @SuppressWarnings("unchecked")
    private static <T> Root<T> findRoot(CriteriaQuery<?> query, Class<T> clazz) {

        for (Root<?> r : query.getRoots()) {
            if (clazz.equals(r.getJavaType())) {
                return (Root<T>) r.as(clazz);
            }
        }
        return null;
    }

    /**
     * Copy criteria without selection and order.
     *
     * @param from
     *            source Criteria.
     * @param to
     *            destination Criteria.
     */
    private static void copyCriteriaWithoutSelectionAndOrder(CriteriaQuery<?> from, CriteriaQuery<?> to,
            boolean copyFetches) {
        if (isEclipseLink(from) && from.getRestriction() != null) {
            // EclipseLink adds roots from predicate paths to critera. Skip copying
            // roots as workaround.
        } else {
            // Copy Roots
            for (Root<?> root : from.getRoots()) {
                Root<?> dest = to.from(root.getJavaType());
                dest.alias(getOrCreateAlias(root));
                copyJoins(root, dest);
                if (copyFetches)
                    copyFetches(root, dest);
            }
        }

        to.groupBy(from.getGroupList());
        to.distinct(from.isDistinct());

        if (from.getGroupRestriction() != null)
            to.having(from.getGroupRestriction());

        Predicate predicate = from.getRestriction();
        if (predicate != null)
            to.where(predicate);
    }

    private static boolean isEclipseLink(CriteriaQuery<?> from) {
        return from.getClass().getName().contains("org.eclipse.persistence");
    }

    /**
     * Copy Joins
     *
     * @param from
     *            source Join
     * @param to
     *            destination Join
     */
    private static void copyJoins(From<?, ?> from, From<?, ?> to) {
        for (Join<?, ?> j : from.getJoins()) {
            Join<?, ?> toJoin = to.join(j.getAttribute().getName(), j.getJoinType());
            toJoin.alias(getOrCreateAlias(j));

            copyJoins(j, toJoin);
        }
    }

    /**
     * Copy Fetches
     *
     * @param from
     *            source From
     * @param to
     *            destination From
     */
    private static void copyFetches(From<?, ?> from, From<?, ?> to) {
        for (Fetch<?, ?> f : from.getFetches()) {
            Fetch<?, ?> toFetch = to.fetch(f.getAttribute().getName());
            copyFetches(f, toFetch);
        }
    }

    /**
     * Copy Fetches
     *
     * @param from
     *            source Fetch
     * @param to
     *            dest Fetch
     */
    private static void copyFetches(Fetch<?, ?> from, Fetch<?, ?> to) {
        for (Fetch<?, ?> f : from.getFetches()) {
            Fetch<?, ?> toFetch = to.fetch(f.getAttribute().getName());
            // recursively copy fetches
            copyFetches(f, toFetch);
        }
    }

    /**
     * Initialize a entity.
     *
     * @param em
     *            entity manager to use
     * @param entity
     *            entity to initialize
     * @param depth
     *            max depth on recursion
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void initialize(EntityManager em, Object entity, int depth) {
        // return on nulls, depth = 0 or already initialized objects
        if (entity == null || depth == 0) {
            return;
        }

        PersistenceUnitUtil unitUtil = em.getEntityManagerFactory().getPersistenceUnitUtil();
        EntityType entityType = em.getMetamodel().entity(entity.getClass());
        Set<Attribute> attributes = entityType.getDeclaredAttributes();

        Object id = unitUtil.getIdentifier(entity);

        if (id != null) {
            Object attached = em.find(entity.getClass(), unitUtil.getIdentifier(entity));

            for (Attribute a : attributes) {
                if (!unitUtil.isLoaded(entity, a.getName())) {
                    if (a.isCollection()) {
                        intializeCollection(em, entity, attached, a, depth);
                    } else if (a.isAssociation()) {
                        intialize(em, entity, attached, a, depth);
                    }
                }
            }
        }
    }

    /**
     * Initialize entity attribute
     *
     * @param em
     * @param entity
     * @param a
     * @param depth
     */
    @SuppressWarnings("rawtypes")
    private static void intialize(EntityManager em, Object entity, Object attached, Attribute a, int depth) {
        Object value = PropertyAccessorFactory.forDirectFieldAccess(attached).getPropertyValue(a.getName());
        if (!em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(value)) {
            em.refresh(value);
        }

        PropertyAccessorFactory.forDirectFieldAccess(entity).setPropertyValue(a.getName(), value);

        initialize(em, value, depth - 1);
    }

    /**
     * Initialize collection
     *
     * @param em
     * @param entity
     * @param a
     * @param depth
     */
    @SuppressWarnings("rawtypes")
    private static void intializeCollection(EntityManager em, Object entity, Object attached, Attribute a, int depth) {
        PropertyAccessor accessor = PropertyAccessorFactory.forDirectFieldAccess(attached);
        Collection c = (Collection) accessor.getPropertyValue(a.getName());

        for (Object o : c)
            initialize(em, o, depth - 1);

        PropertyAccessorFactory.forDirectFieldAccess(entity).setPropertyValue(a.getName(), c);
    }

    // *************************************
    private static Path getOrderByPath(CriteriaQuery cq, String sortColumnName) {
        final String columnFieldName = QueryUtils.toCamelCase(sortColumnName);
        final Optional<Path> optionalPath = cq.getRoots().stream().map(root -> {
            final Root r = (Root) root;
            try {
                return r.get(columnFieldName);
            } catch (IllegalStateException | IllegalArgumentException e) {
                return null;
            }
        }).filter(Objects::nonNull).findFirst();

        if (optionalPath.isPresent()) {
            return optionalPath.get();
        }
        throw new IllegalArgumentException("Impossibile ordinare per " + columnFieldName + ": campo inesistente");
    }

    /**
     *
     * @param cq
     * @param sortColumnName
     * @param sortingRule
     * @param cb
     */
    public static void handleOrderBy(CriteriaQuery cq, String sortColumnName, int sortingRule, CriteriaBuilder cb) {
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(sortColumnName)) {
            final Path path = getOrderByPath(cq, sortColumnName);
            if (path != null) {
                final Order orderBy;
                if (SortingRule.DESC == sortingRule) {
                    orderBy = cb.desc(path);
                } else {
                    orderBy = cb.asc(path);
                }
                cq.orderBy(orderBy);
            }
        }
    }

}
