package it.eng.paginator.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class QueryUtilsTest {

    @PersistenceContext
    private EntityManager em;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, CriteriaQueryUtilsTest.class.getSimpleName() + ".war")
                .addAsResource(CriteriaQueryUtilsTest.class.getClassLoader().getResource("persistence.xml"),
                        "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addClass(org.apache.commons.lang3.StringUtils.class)
                .addPackages(false, "org.springframework.beans", "org.springframework.util")
                .addPackages(true, "it.eng.spagoLite.db.base", "it.eng.paginator.util");
    }

    @Test
    public void entityManagerIsHere() {
        Assert.assertNotNull(em);
    }

    @Test
    public void getQueryStringFromNamedQuery() {
        Query query = em.createNamedQuery("PigAmbienteVers.findAll", PigAmbienteVers.class);
        assertEquals("SELECT v FROM PigAmbienteVers v ORDER BY v.nmAmbienteVers ASC",
                QueryUtils.queryStringFrom(query));
    }

    @Test
    public void getQueryStringFromNamedQueryWithParameter() {
        Query query = em.createNamedQuery("PigAmbienteVers.findGreaterThanId", PigAmbienteVers.class);
        query.setParameter("idAmbienteVers", 1L);
        assertEquals(
                "SELECT v FROM PigAmbienteVers v WHERE v.idAmbienteVers > :idAmbienteVers ORDER BY v.nmAmbienteVers ASC",
                QueryUtils.queryStringFrom(query));
    }

    @Test
    public void getQueryStringFromNativeQuery() {
        final String expected = "SELECT v FROM PIG_AMBIENTE_VERS v " + " WHERE ID_AMBIENTE_VERS = 3";
        Query query = em.createNativeQuery(expected, PigAmbienteVers.class);
        Assert.assertEquals(expected, QueryUtils.queryStringFrom(query));
    }

    @Test
    public void getQueryStringFromNativeQueryWithParameters() {
        final String expected = "SELECT v FROM PIG_AMBIENTE_VERS v " + " WHERE ID_AMBIENTE_VERS = :id";
        Query query = em.createNativeQuery(expected, PigAmbienteVers.class);
        query.setParameter("id", 1L);
        Assert.assertEquals(expected, QueryUtils.queryStringFrom(query));
    }

    @Test
    public void getQueryStringFromTypedQuery() {
        TypedQuery query = getTypedQuery();
        Assert.assertEquals(
                "select distinct generatedAlias0 from PigAmbienteVers as generatedAlias0 where generatedAlias0.idAmbienteVers=3L order by generatedAlias0.nmAmbienteVers desc",
                QueryUtils.queryStringFrom(query));
    }

    @Test
    public void getCountQueryStringFromTypedQuery() {
        Assert.assertEquals(
                "SELECT COUNT(*) from PigAmbienteVers as generatedAlias0 where generatedAlias0.idAmbienteVers=3L",
                QueryUtils.selectToCount(QueryUtils.queryStringFrom(getTypedQuery())));

    }

    @Test
    public void getQueryStringFromQuery() {
        final String queryStr = "SELECT v FROM PigAmbienteVers v WHERE v.idAmbienteVers = 3 ORDER BY v.nmAmbienteVers DESC ";
        Query query = em.createQuery(queryStr, PigAmbienteVers.class);
        Assert.assertEquals(queryStr, QueryUtils.queryStringFrom(query));

    }

    @Test
    public void getQueryStringFromQueryWithParameter() {
        final String queryStr = "SELECT v FROM PigAmbienteVers v WHERE v.idAmbienteVers = :id";
        Query query = em.createQuery(queryStr, PigAmbienteVers.class);
        query.setParameter("id", 1L);
        Assert.assertEquals(queryStr, QueryUtils.queryStringFrom(query));

    }

    @Test
    public void getCountQueryStringFromQuery() {
        final String queryStr = "SELECT v FROM PigAmbienteVers v WHERE v.idAmbienteVers = 1 ORDER BY v.nmAmbienteVers DESC ";
        Query query = em.createQuery(queryStr, PigAmbienteVers.class);
        Assert.assertEquals("SELECT COUNT(*) FROM PigAmbienteVers v WHERE v.idAmbienteVers = 1",
                QueryUtils.selectToCount(QueryUtils.queryStringFrom(query)));

    }

    @Test
    public void getCountQueryStringFromQueryWithParameter() {
        final String queryStr = "SELECT v FROM PigAmbienteVers v WHERE v.idAmbienteVers = :id ORDER BY v.nmAmbienteVers DESC ";

        Query query = em.createQuery(queryStr, PigAmbienteVers.class);
        query.setParameter("id", 1L);
        Assert.assertEquals("SELECT COUNT(*) FROM PigAmbienteVers v WHERE v.idAmbienteVers = :id",
                QueryUtils.selectToCount(QueryUtils.queryStringFrom(query)));

    }

    @Test
    public void getCountQueryStringFromQueryAndDistinctField() {
        String queryStr = "SELECT DISTINCT p FROM PigAmbienteVers p";
        System.out.println(QueryUtils.selectToCount(queryStr, "idAmbienteVers"));
        Assert.assertEquals("SELECT COUNT(DISTINCT idAmbienteVers) FROM PigAmbienteVers p",
                QueryUtils.selectToCount(queryStr, "idAmbienteVers"));

    }

    private TypedQuery getTypedQuery() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(PigAmbienteVers.class);
        Root entity = cq.from(PigAmbienteVers.class);
        cq.select(entity);
        cq.distinct(true);
        cq.orderBy(cb.desc(entity.get("nmAmbienteVers")));
        List<Predicate> condizioni = new ArrayList<>();
        condizioni.add(cb.equal(entity.get("idAmbienteVers"), 3L));
        cq.where(condizioni.toArray(new Predicate[] {}));
        return em.createQuery(cq);
    }

    @Test
    public void removeMultipleSpaceFromQueryString() {
        final String expectedQueryString = "SELECT u FROM AroVLisArchivUnitaDoc u WHERE u.idUnitaDoc = :idud ORDER BY u.dsClassif, u.cdFascic";
        final String toNormalize = "SELECT    u     FROM AroVLisArchivUnitaDoc u WHERE     u.idUnitaDoc = :idud ORDER   BY u.dsClassif, u.cdFascic";
        assertEquals(expectedQueryString, QueryUtils.normalizeQueryString(toNormalize));
    }

    @Test
    public void queryRemoveOrderBy() {
        final String selectQuery = "SELECT u FROM    AroVLisArchivUnitaDoc u WHERE     u.idUnitaDoc = :idud ORDER BY u.dsClassif, u.cdFascic";
        final String expected = "SELECT u FROM AroVLisArchivUnitaDoc u WHERE u.idUnitaDoc = :idud";
        assertEquals(expected, QueryUtils.removeOrderBy(selectQuery));
    }

    @Test
    public void queryRemoveOrderByWhenTheresNoOrderBy() {
        final String selectQuery = "SELECT u FROM    AroVLisArchivUnitaDoc u WHERE     u.idUnitaDoc = :idud";
        final String expected = "SELECT u FROM AroVLisArchivUnitaDoc u WHERE u.idUnitaDoc = :idud";
        assertEquals(expected, QueryUtils.removeOrderBy(selectQuery));
    }

    @Test
    public void selectQueryToCountQuery() {
        final String selectQuery = "SELECT u FROM AroVLisArchivUnitaDoc u      where u.idUnitaDoc = :idud ORDER BY u.dsClassif,u.cdFascic";
        final String expected = "SELECT COUNT(*) FROM AroVLisArchivUnitaDoc u where u.idUnitaDoc = :idud";
        assertEquals(expected, QueryUtils.selectToCount(selectQuery));
    }

    @Test
    public void countQueryWithOneFieldForDistinct() {
        final String selectQuery = "SELECT u FROM AroVLisArchivUnitaDoc u      where u.idUnitaDoc = :idud ORDER BY u.dsClassif,u.cdFascic";
        final String expected = "SELECT COUNT(DISTINCT idUnitaDoc) FROM AroVLisArchivUnitaDoc u where u.idUnitaDoc = :idud";
        assertEquals(expected, QueryUtils.selectToCount(selectQuery, "idUnitaDoc"));
    }

    @Test
    public void countQueryWithEmptyStringForDistinctIsTheSameOfNoDistinctFields() {
        final String selectQuery = "SELECT u FROM AroVLisArchivUnitaDoc u      where u.idUnitaDoc = :idud ORDER BY u.dsClassif,u.cdFascic";
        final String expected = "SELECT COUNT(*) FROM AroVLisArchivUnitaDoc u where u.idUnitaDoc = :idud";
        assertEquals(expected, QueryUtils.selectToCount(selectQuery, ""));
    }

    @Test
    public void countQueryWithNullDistinctIsTheSameOfNoDistinctFields() {
        final String selectQuery = "SELECT u FROM AroVLisArchivUnitaDoc u      where u.idUnitaDoc = :idud ORDER BY u.dsClassif,u.cdFascic";
        final String expected = "SELECT COUNT(*) FROM AroVLisArchivUnitaDoc u where u.idUnitaDoc = :idud";
        assertEquals(expected, QueryUtils.selectToCount(selectQuery, null));
    }

    @Test
    public void fixCountDistinctMultipleColumns() {
        final String selectDistinct = "SELECT COUNT(DISTINCT column1,column2,column3,column4   ) FROM table WHERE col1 = '2'";
        final String expected = "SELECT COUNT(*) FROM (SELECT DISTINCT column1,column2,column3,column4 FROM table WHERE col1 = '2')";
        assertEquals(expected, QueryUtils.fixOracleMultiColumnDistinct(selectDistinct));
    }

    @Test
    public void fixCountDistinctMultipleColumnsWithAlias() {
        final String selectDistinct = "select count(distinct monvlisudn0_.AA_KEY_UNITA_DOC, monvlisudn0_.CD_KEY_UNITA_DOC, monvlisudn0_.CD_REGISTRO_KEY_UNITA_DOC, monvlisudn0_.ID_STRUT) as col_0_0_ from MON_V_LIS_UD_NON_VERS_IAM monvlisudn0_ where monvlisudn0_.ID_AMBIENTE=? and monvlisudn0_.ID_ENTE=? and monvlisudn0_.ID_STRUT=? and monvlisudn0_.ID_USER_IAM=?";
        final String expected = "SELECT COUNT(*) as col_0_0_ FROM (SELECT distinct monvlisudn0_.AA_KEY_UNITA_DOC, monvlisudn0_.CD_KEY_UNITA_DOC, monvlisudn0_.CD_REGISTRO_KEY_UNITA_DOC, monvlisudn0_.ID_STRUT from MON_V_LIS_UD_NON_VERS_IAM monvlisudn0_ where monvlisudn0_.ID_AMBIENTE=? and monvlisudn0_.ID_ENTE=? and monvlisudn0_.ID_STRUT=? and monvlisudn0_.ID_USER_IAM=?)";
        assertEquals(expected, QueryUtils.fixOracleMultiColumnDistinct(selectDistinct));
    }

    @Test
    public void fixCountDistinctSingleColumnDoNothing() {
        final String selectDistinct = "SELECT COUNT(DISTINCT column1) FROM table WHERE col1 = '2'";
        assertEquals(selectDistinct, QueryUtils.fixOracleMultiColumnDistinct(selectDistinct));
    }

    @Test
    public void fixCountStarDoNothing() {
        final String selectDistinct = "SELECT COUNT(DISTINCT column1) FROM table WHERE col1 = '2'";
        assertEquals(selectDistinct, QueryUtils.fixOracleMultiColumnDistinct(selectDistinct));
    }
}
