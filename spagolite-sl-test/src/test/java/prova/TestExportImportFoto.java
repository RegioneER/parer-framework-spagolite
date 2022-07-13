/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prova;

import it.eng.parer.sacerlog.ejb.helper.ExportImportFotoHelper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Properties;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Assert;

/**
 *
 * @author Iacolucci_M
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestExportImportFoto {

    private EJBContainer ejbContainer = null;
            
    @EJB
    private ExportImportFotoHelper helper = null;
    
    @Resource
    private UserTransaction userTransaction;
    @PersistenceContext
    public EntityManager entityManager;


    private static final Logger log = LoggerFactory.getLogger(TestExportImportFoto.class);

    public TestExportImportFoto() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws NamingException {
        FileInputStream in = null;
        try {

            Properties props = new Properties();
            String locationPath = TestExportImportFoto.class.getProtectionDomain().getCodeSource().getLocation().getFile();
            String fileProperties = locationPath + "jndi.properties";

            in = new FileInputStream(fileProperties);
            props.load(in);
            in.close();
//            String driver = props.getProperty("saceriamDs.JdbcDriver");
//            if (driver != null) {
//                Class.forName(driver);
//            }
//             String url = props.getProperty("saceriamDs.JdbcUrl");
//             String username = props.getProperty("saceriamDs.UserName");
//             String password = props.getProperty("saceriamDs.Password");
//             con = DriverManager.getConnection(url, username, password);

//             eseguiBatch(fileCaricamento);
            // EJB
            ejbContainer=EJBContainer.createEJBContainer(props);
            ejbContainer.getContext().bind("inject", this);

//          DECOMMENTARE PER PULIRE TUTTE LE TABELLE DEL LOG CON LE FOTO

/*
            userTransaction.begin();
            Query q = entityManager.createNamedQuery("LogFotoOggettoEvento.deleteAll", LogFotoOggettoEvento.class);
            q.executeUpdate();
            q = entityManager.createNamedQuery("LogEventoByScript.deleteAll", LogEventoByScript.class);
            q.executeUpdate();
            q = entityManager.createNamedQuery("LogChiaveAccessoEvento.deleteAll", LogChiaveAccessoEvento.class);
            q.executeUpdate();
            q = entityManager.createNamedQuery("LogOggettoEvento.deleteAll", LogOggettoEvento.class);
            q.executeUpdate();
            q = entityManager.createNamedQuery("LogEvento.deleteAll", LogEvento.class);
            q.executeUpdate();
            q = entityManager.createNamedQuery("LogAgenteEvento.deleteAll", LogAgenteEvento.class);
            q.executeUpdate();
            q = entityManager.createNamedQuery("LogDeltaFoto.deleteAll", LogDeltaFoto.class);
            q.executeUpdate();
            userTransaction.commit();
*/
            
        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug(null, ex);
        } finally {
            /*            
             try {
             in.close();
             } catch (IOException ex) {
             log.debug(null, ex);
             }
             */
        }

    }

    @After
    public void tearDown() {
        ejbContainer.close();
    }
/*
    @Test
    public void esportaFoto() throws Exception {
        
        userTransaction.begin();
        String str=helper.exportFoto(new BigDecimal(1201), "SACER_PING.ESPORTA_FOTO_VERSATORE");
        HashMap mappa=new HashMap();
        mappa.put("NM_AMBIENTE_VERS", "PARER_TEST");
        mappa.put("NM_VERS", "NOME_VERSATORE_NUOVO");
        mappa.put("DS_VERS", "DESCRIZIONE_VERSATORE_NUOVO");
        mappa.put("DS_PATH_INPUT_FTP", "PATH_INPUT_FTP_NUOVO");
        mappa.put("DS_PATH_OUTPUT_FTP", "PATH_OUTPUT_FTP_NUOVO");
        mappa.put("DS_PATH_TRASF", "PATH_TRASF_NUOVO");
        str=helper.sostituisciTutto(str, mappa);
        scriviFile("FILE_XML_DA_IMPORTARE.xml", str);
        long idOggetto=helper.importFoto(str, "SACER_PING.IMPORTA_FOTO_VERSATORE");
        log.info("Oggetto importato con ID {}", idOggetto);
        userTransaction.commit();

        Assert.assertTrue(true);

    }
*/

    
/*    
    @Test
    public void _estraiRuoli() throws Exception {
        userTransaction.begin();
//        DateTime dt = new DateTime(2017, 9, 30,0,0,0,0);
        List<String> l=sacerLogHelper.findRuoliUtenteAllaData("SACER_IAM", "SACER", "proffe2", new Date());
        for (String string : l) {
            log.info(">>>>>>>>>>>>>>>>>>>>Ruolo...: "+string);
        }
        userTransaction.commit();
        log.info(">>>>>>>>>>>>>>>>>>> Ruoli ottenuti!");
        Assert.assertTrue(true);
    }
 */
    
    @Test
    public void dummyTest() {
        
    }
    /*
     private String leggiFile(String path) throws Exception {
     BufferedReader br = new BufferedReader(new FileReader(path));
     try {
     StringBuilder sb = new StringBuilder();
     String line = br.readLine();

     while (line != null) {
     sb.append(line);
     sb.append(System.lineSeparator());
     line = br.readLine();
     }
     return sb.toString();
     } finally {
     br.close();
     }
     }
     */
    
    
    public void scriviFile(String nomeFile, String contenuto) {
        String locationPath = TestXml.class.getProtectionDomain().getCodeSource().getLocation().getFile() + "../../src/test/resources/";
/*
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
*/        
        
        try {
		File fileDir = new File(locationPath + nomeFile);
			
		Writer out = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(fileDir), "UTF8"));
		out.append(contenuto);
		out.flush();
		out.close();
	        
	    } 
	   catch (UnsupportedEncodingException e) 
	   {
		System.out.println(e.getMessage());
	   } 
	   catch (IOException e) 
	   {
		System.out.println(e.getMessage());
	    }
	   catch (Exception e)
	   {
		System.out.println(e.getMessage());
	   } 
    }
    
}
