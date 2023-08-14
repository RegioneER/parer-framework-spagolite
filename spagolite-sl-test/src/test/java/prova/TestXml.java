/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prova;

import it.eng.parer.sacerlog.ejb.SacerLogEjb;
import it.eng.parer.sacerlog.ejb.helper.SacerLogHelper;
import it.eng.parer.sacerlog.entity.LogFotoOggettoEvento;
import it.eng.parer.sacerlog.entity.LogOggettoEvento;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Properties;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Iacolucci_M
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestXml {

    private EJBContainer ejbContainer = null;

    @EJB
    private SacerLogEjb sacerLog = null;
    @EJB
    private SacerLogHelper sacerLogHelper = null;
    @Resource
    private UserTransaction userTransaction;
    @PersistenceContext
    public EntityManager entityManager;

    private static final Logger log = LoggerFactory.getLogger(TestXml.class);
    public static final String XSD_FOTO_OGGETTO = "fotoOggetto.xsd";
    public static final String GENERATED_DIR = "generated";

    public TestXml() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws NamingException, FileNotFoundException, IOException {
        FileInputStream in = null;
        Properties props = new Properties();
        String locationPath = TestXml.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        String fileProperties = locationPath + "jndi.properties";

        in = new FileInputStream(fileProperties);
        props.load(in);
        in.close();
        ejbContainer = EJBContainer.createEJBContainer(props);
        ejbContainer.getContext().bind("inject", this);
    }

    @After
    public void tearDown() {
        ejbContainer.close();
    }

    @Test
    public void loadLogOggettoEvento() {
        long id = 237899L;
        LogFotoOggettoEvento log = entityManager.find(LogFotoOggettoEvento.class, id);
        Assert.assertNotNull(log);
        Assert.assertEquals(id, log.getIdFotoOggettoEvento());
        System.out.println(log.getBlFotoOggetto());
    }

    /*
     * @Test public void testEstraiChiaveXml() { String xml =
     * "<fotoOggetto><versioneLogRecord>1.0</versioneLogRecord><recordMaster><tipoRecord>Tipo Unità Documentaria</tipoRecord><idRecord>8005</idRecord><keyRecord><datoKey><colonnaKey>nm_ambiente</colonnaKey><labelKey>Ambiente</labelKey><valoreKey>PARER_TEST</valoreKey></datoKey><datoKey><colonnaKey>nm_ente</colonnaKey><labelKey>Ente</labelKey><valoreKey>ente_test</valoreKey></datoKey><datoKey><colonnaKey>nm_strut</colonnaKey><labelKey>Struttura</labelKey><valoreKey>COPIA STRUTTURA MARCO</valoreKey></datoKey><datoKey><colonnaKey>nm_tipo_unita_doc</colonnaKey><labelKey>Tipo unità documentaria</labelKey><valoreKey>FATTURA PASSIVA</valoreKey></datoKey></keyRecord><datoRecord><colonnaDato>ds_tipo_unita_doc</colonnaDato><labelDato>Descrizione tipo unità documentaria</labelDato><valoreDato>Fattura passiva</valoreDato></datoRecord><datoRecord><colonnaDato>fl_forza_collegamento</colonnaDato><labelDato>Forzatura collegamento</labelDato><valoreDato>false</valoreDato></datoRecord><datoRecord><colonnaDato>ti_save_file</colonnaDato><labelDato>Tipo salvataggio file</labelDato><valoreDato>BLOB</valoreDato></datoRecord><datoRecord><colonnaDato>dt_istituz</colonnaDato><labelDato>Data istituzione</labelDato><valoreDato>2016-06-13</valoreDato></datoRecord><datoRecord><colonnaDato>dt_soppres</colonnaDato><labelDato>Data soppressione</labelDato><valoreDato>2444-12-31</valoreDato></datoRecord><datoRecord><colonnaDato>cd_subcateg_tipo_unita_doc</colonnaDato><labelDato>Sottocategoria</labelDato><valoreDato>documentazione_contabile</valoreDato></datoRecord><datoRecord><colonnaDato>cd_categ_tipo_unita_doc</colonnaDato><labelDato>Categoria</labelDato><valoreDato>documentazione_amministrativa</valoreDato></datoRecord><datoRecord><colonnaDato>fl_crea_tipo_serie_standard</colonnaDato><labelDato>Crea tipo serie standard</labelDato><valoreDato>1</valoreDato></datoRecord><datoRecord><colonnaDato>nm_modello_tipo_serie</colonnaDato><labelDato>Modello tipo serie</labelDato><valoreDato>null</valoreDato></datoRecord><datoRecord><colonnaDato>nm_tipo_serie_da_creare</colonnaDato><labelDato>Tipo serie da creare</labelDato><valoreDato>Fatture passive - Registro</valoreDato></datoRecord><datoRecord><colonnaDato>ds_tipo_serie_da_creare</colonnaDato><labelDato>Descrizione tipo serie da creare</labelDato><valoreDato>Serie annuale delle fatture passive del registro</valoreDato></datoRecord><datoRecord><colonnaDato>cd_serie_da_creare</colonnaDato><labelDato>Codice serie da creare</labelDato><valoreDato>fatture_passive-registro</valoreDato></datoRecord><datoRecord><colonnaDato>ds_serie_da_creare</colonnaDato><labelDato>Descrizione serie da creare</labelDato><valoreDato>Serie annuale delle fatture passive versate nel sistema di conservazione e afferenti al registro</valoreDato></datoRecord></recordMaster><recordChild><tipoRecord>Registri ammessi</tipoRecord><child><idRecord>15348</idRecord><keyRecord><datoKey><colonnaKey>cd_registro_unita_doc</colonnaKey><labelKey>Registro</labelKey><valoreKey>ACQUISTI</valoreKey></datoKey></keyRecord></child></recordChild><recordChild><tipoRecord>Tipi struttura unità documentaria</tipoRecord><child><idRecord>4785</idRecord><keyRecord><datoKey><colonnaKey>nm_tipo_strut_unita_doc</colonnaKey><labelKey>Tipo struttura unità documentaria</labelKey><valoreKey>FATTURA PASSIVA</valoreKey></datoKey></keyRecord><datoRecord><colonnaDato>ds_tipo_strut_unita_doc</colonnaDato><labelDato>Descrizione tipo struttura</labelDato><valoreDato>Fattura passiva</valoreDato></datoRecord><datoRecord><colonnaDato>dt_istituz</colonnaDato><labelDato>Data istituzione</labelDato><valoreDato>2016-06-13</valoreDato></datoRecord><datoRecord><colonnaDato>dt_soppres</colonnaDato><labelDato>Data soppressione</labelDato><valoreDato>2444-12-31</valoreDato></datoRecord><recordChild><tipoRecord>Tipi documenti ammessi</tipoRecord><child><idRecord>9338</idRecord><keyRecord><datoKey><colonnaKey>nm_tipo_doc</colonnaKey><labelKey>Tipo documento</labelKey><valoreKey>FATTURA</valoreKey></datoKey><datoKey><colonnaKey>ti_doc</colonnaKey><labelKey>Elemento</labelKey><valoreKey>PRINCIPALE</valoreKey></datoKey></keyRecord><datoRecord><colonnaDato>fl_obbl</colonnaDato><labelDato>Obbligatorio</labelDato><valoreDato>false</valoreDato></datoRecord></child></recordChild></child></recordChild><recordChild><tipoRecord>XSD dati specifici</tipoRecord></recordChild></fotoOggetto>"
     * ; String risultato=SacerLogEjb.getChiaveRecordFoto(xml); System.out.println("CHIAVE[" + risultato + "]");
     * Assert.assertEquals(risultato,
     * "Ambiente=PARER_TEST~~Ente=ente_test~~Struttura=COPIA STRUTTURA MARCO~~Tipo unità documentaria=FATTURA PASSIVA");
     * }
     */
    /*
     * @Test public void b_testDiffFotoRegistriRecordMaster() { String registro_primo =
     * leggiFile("test_registro_record_master/test-Registro-record-master-primo.xml"); String registro_secondo =
     * leggiFile("test_registro_record_master/test-Registro-record-master-secondo.xml"); // String risultatoAtteso =
     * leggiFile("test_registro_record_master/DELTA-Registro-record-master.xml"); String delta =
     * sacerLogHelper.getDeltaFotoAsClob(registro_primo, registro_secondo); log.info("DELTA {}", delta);
     * scriviFile("DELTA-Registro-record-master.xml", delta); // Assert.assertEquals(risultatoAtteso, delta); }
     * 
     * @Test public void c_testDiffFotoRegistriCancRecFiglio() { String registro_primo =
     * leggiFile("test_registro_canc_rec_figlio/test-Registro-canc-rec-figlio-primo.xml"); String registro_secondo =
     * leggiFile("test_registro_canc_rec_figlio/test-Registro-canc-rec-figlio-secondo.xml"); // String risultatoAtteso =
     * leggiFile("test_registro_canc_rec_figlio/DELTA-Registro-canc-rec-figlio.xml"); String delta =
     * sacerLogHelper.getDeltaFotoAsClob(registro_primo, registro_secondo); log.info("DELTA {}", delta);
     * scriviFile("DELTA-Registro-canc-rec-figlio.xml", delta); // Assert.assertEquals(risultatoAtteso, delta); }
     * 
     * @Test public void d_testDiffFotoRegistriInsRecFiglio() { String registro_primo =
     * leggiFile("test_registro_ins_rec_figlio/test-Registro-ins-rec-figlio-primo.xml"); String registro_secondo =
     * leggiFile("test_registro_ins_rec_figlio/test-Registro-ins-rec-figlio-secondo.xml"); // String risultatoAtteso =
     * leggiFile("test_registro_ins_rec_figlio/DELTA-Registro-ins-rec-figlio.xml"); String delta =
     * sacerLogHelper.getDeltaFotoAsClob(registro_primo, registro_secondo); log.info("DELTA {}", delta);
     * scriviFile("DELTA-Registro-ins-rec-figlio.xml", delta); // Assert.assertEquals(risultatoAtteso, delta); }
     */
    /*
     * @Test public void d1_testDiffFotoSimulatoSoloCancellazioni() { String XML1 =
     * "test_completo_simulato/test-simulato-completo-solo-canc-1.xml"; String XML2 =
     * "test_completo_simulato/test-simulato-completo-solo-canc-2.xml"; String DELTA =
     * "DELTA-ATTESO-simulato-completo-solo-canc.xml"; String DELTA_ATTESO = "test_completo_simulato/" + DELTA; String
     * registro_primo = leggiFile(XML1); String registro_secondo = leggiFile(XML2); boolean resultXml1 =
     * validaXml(XML1); Assert.assertTrue(resultXml1); boolean resultXml2 = validaXml(XML2);
     * Assert.assertTrue(resultXml1 && resultXml2); String risultatoAtteso = leggiFile(DELTA_ATTESO); String delta =
     * sacerLogHelper.getDeltaFotoAsClob(registro_primo, registro_secondo); log.info("DELTA {}", delta);
     * scriviFile(GENERATED_DIR+"/"+DELTA, delta); Assert.assertEquals(risultatoAtteso, delta); }
     * 
     * @Test public void d2_testDiffFotoSimulatoSoloInserimenti() { String XML1 =
     * "test_completo_simulato/test-simulato-completo-solo-ins-1.xml"; String XML2 =
     * "test_completo_simulato/test-simulato-completo-solo-ins-2.xml"; String DELTA =
     * "DELTA-ATTESO-simulato-completo-solo-ins.xml"; String DELTA_ATTESO = "test_completo_simulato/" + DELTA; String
     * registro_primo = leggiFile(XML1); String registro_secondo = leggiFile(XML2); boolean resultXml1 =
     * validaXml(XML1); Assert.assertTrue(resultXml1); boolean resultXml2 = validaXml(XML2);
     * Assert.assertTrue(resultXml1 && resultXml2); String risultatoAtteso = leggiFile(DELTA_ATTESO); String delta =
     * sacerLogHelper.getDeltaFotoAsClob(registro_primo, registro_secondo); log.info("DELTA {}", delta);
     * scriviFile(GENERATED_DIR+"/"+DELTA, delta); Assert.assertEquals(risultatoAtteso, delta); }
     */
    /*
     * @Test public void d3_testDiffFotoSimulatoSoloModifiche() { String XML1 =
     * "test_completo_simulato/test-simulato-completo-solo-mod-1.xml"; String XML2 =
     * "test_completo_simulato/test-simulato-completo-solo-mod-2.xml"; String DELTA =
     * "DELTA-ATTESO-simulato-completo-solo-mod.xml"; String DELTA_ATTESO = "test_completo_simulato/" + DELTA; String
     * registro_primo = leggiFile(XML1); String registro_secondo = leggiFile(XML2); boolean resultXml1 =
     * validaXml(XML1); Assert.assertTrue(resultXml1); boolean resultXml2 = validaXml(XML2);
     * Assert.assertTrue(resultXml1 && resultXml2); String risultatoAtteso = leggiFile(DELTA_ATTESO); String delta =
     * sacerLogHelper.getDeltaFotoAsClob(registro_primo, registro_secondo); log.info("DELTA {}", delta);
     * scriviFile(GENERATED_DIR+"/"+DELTA, delta); // Assert.assertEquals(risultatoAtteso, delta); }
     * 
     * @Test public void d3_testDiffFotoAssertSimulatoSoloModifiche() { String XML1 =
     * "test_completo_simulato/test-simulato-completo-solo-mod-1.xml"; String XML2 =
     * "test_completo_simulato/test-simulato-completo-solo-mod-2.xml"; String DELTA =
     * "DELTA-ATTESO-assert-simulato-completo-solo-mod.xml"; String DELTA_ATTESO = "test_completo_simulato/" + DELTA;
     * String registro_primo = leggiFile(XML1); String registro_secondo = leggiFile(XML2); boolean resultXml1 =
     * validaXml(XML1); Assert.assertTrue(resultXml1); boolean resultXml2 = validaXml(XML2);
     * Assert.assertTrue(resultXml1 && resultXml2); String risultatoAtteso = leggiFile(DELTA_ATTESO); String delta =
     * sacerLogHelper.getDeltaFotoAssertAsClob(registro_primo, registro_secondo); log.info("DELTA {}", delta);
     * scriviFile(GENERATED_DIR+"/"+DELTA, delta); // Assert.assertEquals(risultatoAtteso, delta); }
     */
    /*
     * @Test public void d4_testDiffFotoSimulatoSoloRecordMaster() { String XML1 =
     * "test_completo_simulato/test-simulato-completo-solo-record-master-1.xml"; String XML2 =
     * "test_completo_simulato/test-simulato-completo-solo-record-master-2.xml"; String DELTA =
     * "DELTA-ATTESO-simulato-completo-solo-record-master.xml"; String DELTA_ATTESO = "test_completo_simulato/" + DELTA;
     * String registro_primo = leggiFile(XML1); String registro_secondo = leggiFile(XML2); boolean resultXml1 =
     * validaXml(XML1); Assert.assertTrue(resultXml1); boolean resultXml2 = validaXml(XML2);
     * Assert.assertTrue(resultXml1 && resultXml2); String risultatoAtteso = leggiFile(DELTA_ATTESO); String delta =
     * sacerLogHelper.getDeltaFotoAsClob(registro_primo, registro_secondo); log.info("DELTA {}", delta);
     * scriviFile(GENERATED_DIR+"/"+DELTA, delta); Assert.assertEquals(risultatoAtteso, delta); }
     */

    /*
     * @Test public void testVerificaBug_DeltaFoto() { String XML1 = "TEST_VERIFICA_BUG/FOTO_VECCHIA.xml"; String XML2 =
     * "TEST_VERIFICA_BUG/FOTO_NUOVA.xml"; String DELTA = "DELTA-ATTESO-VERIFICA_BUG.xml"; // String DELTA_ATTESO =
     * "test_completo_simulato/" + DELTA; String xml1 = leggiFile(XML1); String xml2 = leggiFile(XML2); // String
     * risultatoAtteso = leggiFile(DELTA_ATTESO); // boolean resultXml1 = validaXml(XML1); //
     * Assert.assertTrue(resultXml1); // boolean resultXml2 = validaXml(XML2); // Assert.assertTrue(resultXml1 &&
     * resultXml2); String delta = sacerLogHelper.getDeltaFotoAsClob(xml1, xml2); log.info("DELTA {}", delta);
     * scriviFile(GENERATED_DIR + "/" + DELTA, delta); // Assert.assertEquals(risultatoAtteso, delta); }
     * 
     * 
     * @Test public void testVerificaBug_DeltaFoto_Assert() { String XML1 = "TEST_VERIFICA_BUG/FOTO_VECCHIA.xml"; String
     * XML2 = "TEST_VERIFICA_BUG/FOTO_NUOVA.xml"; String DELTA = "DELTA-ASSERT-ATTESO-VERIFICA_BUG.xml"; // String
     * DELTA_ATTESO = "test_completo_simulato/" + DELTA; String xml1 = leggiFile(XML1); String xml2 = leggiFile(XML2);
     * // String risultatoAtteso = leggiFile(DELTA_ATTESO); // boolean resultXml1 = validaXml(XML1); //
     * Assert.assertTrue(resultXml1); // boolean resultXml2 = validaXml(XML2); // Assert.assertTrue(resultXml1 &&
     * resultXml2); try { String delta = sacerLogHelper.getDeltaFotoAssertAsClob(xml1, xml2); log.info("DELTA {}",
     * delta); scriviFile(GENERATED_DIR + "/" + DELTA, delta); } catch (Throwable ex) { log.error("ECCEZIONE!", ex);
     * Assert.assertTrue(false); } // Assert.assertEquals(risultatoAtteso, delta); }
     *
     * /*
     * 
     * @Test public void d5_testDiffFotoAssertSimulatoTOTALE() { String XML1 =
     * "test_completo_simulato/test-simulato-completo-TUTTO-1.xml"; String XML2 =
     * "test_completo_simulato/test-simulato-completo-TUTTO-2.xml"; String DELTA_ASSERT =
     * "DELTA-ATTESO-assert-simulato-completo-TUTTO.xml"; String DELTA = "DELTA-ATTESO-simulato-completo-TUTTO.xml";
     * String DELTA_ATTESO = "test_completo_simulato/" + DELTA; String registro_primo = leggiFile(XML1); String
     * registro_secondo = leggiFile(XML2); // String risultatoAtteso = leggiFile(DELTA_ATTESO); // boolean resultXml1 =
     * validaXml(XML1); // Assert.assertTrue(resultXml1); // boolean resultXml2 = validaXml(XML2); //
     * Assert.assertTrue(resultXml1 && resultXml2); String delta =
     * sacerLogHelper.getDeltaFotoAssertAsClob(registro_primo, registro_secondo); log.info("DELTA ASSERT {}", delta);
     * scriviFile(GENERATED_DIR + "/" + DELTA_ASSERT, delta); delta = sacerLogHelper.getDeltaFotoAsClob(registro_primo,
     * registro_secondo); log.info("DELTA {}", delta); scriviFile(GENERATED_DIR + "/" + DELTA, delta); //
     * Assert.assertEquals(risultatoAtteso, delta); }
     */
    /*
     * @Test public void d6_testTrasformazioneHTMLDiffFotoSimulatoTOTALE() { String DELTA =
     * "DELTA-ATTESO-simulato-completo-TUTTO.xml"; String XSL = "deltaHtml.xsl"; // String DELTA_ATTESO =
     * "test_completo_simulato/" + DELTA; // String risultatoAtteso = leggiFile(DELTA_ATTESO); boolean result =
     * trasformaInHtml("test_completo_simulato", DELTA, XSL); Assert.assertTrue(result); // String delta =
     * sacerLogHelper.getDeltaFotoAsClob(registro_primo, registro_secondo); // log.info("DELTA {}", delta); //
     * scriviFile(DELTA, delta); // Assert.assertEquals(risultatoAtteso, delta); }
     */
    /*
     * @Test public void d7_testTrasformazioneHTMLAssertDiffFotoSimulatoTOTALE() { String DELTA =
     * "DELTA-ATTESO-assert-simulato-completo-TUTTO.xml"; String XSL = "deltaAssertHtml.xsl"; // String DELTA_ATTESO =
     * "test_completo_simulato/" + DELTA; // String risultatoAtteso = leggiFile(DELTA_ATTESO); boolean result =
     * trasformaInHtml("test_completo_simulato", DELTA, XSL); Assert.assertTrue(result); // String delta =
     * sacerLogHelper.getDeltaFotoAsClob(registro_primo, registro_secondo); // log.info("DELTA {}", delta); //
     * scriviFile(DELTA, delta); // Assert.assertEquals(risultatoAtteso, delta); }
     */
    /*
     * @Test public void testValidazioneTestSimulatoCOMPLETO() { boolean
     * resultXml1=validaXml("test_completo_simulato/test-simulato-completo-1.xml"); Assert.assertTrue(resultXml1);
     * boolean resultXml2=validaXml("test_completo_simulato/test-simulato-completo-2.xml"); Assert.assertTrue(resultXml1
     * && resultXml2); }
     */
    public String leggiFile(String nomeFile) {
        String locationPath = TestXml.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        byte[] b = null;
        try {
            RandomAccessFile f = new RandomAccessFile(locationPath + nomeFile, "r");
            b = new byte[(int) f.length()];
            f.read(b);
        } catch (Exception ex) {
            log.error("leggiFile:", ex);
        }
        return (b != null) ? new String(b) : null;
    }

    public boolean validaXml(String fileXml) {
        return validaXml(fileXml, XSD_FOTO_OGGETTO);
    }

    public boolean validaXml(String fileXml, String fileXsd) {
        try {
            String locationPath = TestXml.class.getProtectionDomain().getCodeSource().getLocation().getFile();
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(locationPath + fileXsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(locationPath + fileXml));
            return true;
        } catch (Exception ex) {
            log.error("XX", ex);
            return false;
        }
    }

    public boolean trasformaInHtml(String cartellaXml, String fileXml, String fileXslt) {
        try {
            String locationPath = TestXml.class.getProtectionDomain().getCodeSource().getLocation().getFile();
            TransformerFactory factory = TransformerFactory.newInstance();
            Source xslt = new StreamSource(new File(locationPath + fileXslt));
            Transformer transformer = factory.newTransformer(xslt);

            Source text = new StreamSource(new File(locationPath + cartellaXml + "/" + fileXml));
            transformer.transform(text,
                    new StreamResult(new File(locationPath + "../../src/test/resources/" + fileXml + ".html")));
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Errore trasformazione", ex);
            return false;
        }
    }

    public void scriviFile(String nomeFile, String contenuto) {
        String locationPath = TestXml.class.getProtectionDomain().getCodeSource().getLocation().getFile()
                + "../../src/test/resources/";

        File file = new File(locationPath + nomeFile);
        file.delete();

        RandomAccessFile f = null;
        try {
            f = new RandomAccessFile(locationPath + nomeFile, "rw");
            f.writeBytes(contenuto);
        } catch (Exception ex) {
            log.error("leggiFile:", ex);
        } finally {
            try {
                f.close();
            } catch (Exception ex) {
                log.error("Errore chiusura file", ex);
            }
        }
    }

}
