/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.sacerlog.ejb.helper;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import it.eng.parer.sacerlog.common.SacerLogEjbType;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.text.StrSubstitutor;

/**
 *
 * @author Iacolucci_M
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@SacerLogEjbType
public class ExportImportFotoHelper {

    @PersistenceContext
    private EntityManager entityManager;

    private static Logger log = LoggerFactory.getLogger(ExportImportFotoHelper.class);

    /*
     * Dato un idOggetto ed un nome di funzione Oracle (NOME_SCHEMA.NOME_FUNZIONE) restituisce una foto XML dell'entità
     * a cui fa capo l'idOggetto passato.
     */
    public String exportFoto(BigDecimal idOggetto, String functionName) {
        Clob clob = null;
        String risultato = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            Connection con = entityManager.unwrap(Connection.class);
            ps = con.prepareStatement("SELECT " + functionName + "(?) AS FOTO FROM DUAL");
            ps.setBigDecimal(1, idOggetto);
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                clob = rs.getClob("FOTO");
                risultato = clob == null ? null : getClobAsString(clob);
            }
        } catch (Exception ex) {
            log.error("Errore esportazione foto dalla funzione oracle " + functionName + "() per l'oggetto con ID [{}]",
                    idOggetto, ex);
            throw new RuntimeException("Errore esportazione foto dalla funzione oracle " + functionName + "()", ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    if (ps != null) {
                        ps.close();
                    }
                }
            } catch (Exception ex) {
                throw new RuntimeException("Errore esportazione foto dalla funzione oracle " + functionName + "()", ex);
            }
        }
        return risultato;
    }

    /*
     * Data una foto ottenuta dalla funzione Oracle exportFoto e il nome della Procedura Oracle
     * (NOME_SCHEMA.NOME_PROCEDURA)effettua l'importazione dei dati su tutte le tabelle legate all'entità fotografata
     * nell'XML. Restituisce l'idOggetto dell'entità appena importata.
     */
    public long importFoto(String fotoXml, String procedureName) {
        long risultato = -1;
        CallableStatement cs = null;
        String istruzione = "{call " + procedureName + "(?,?)}";
        try {
            Connection con = entityManager.unwrap(Connection.class);
            cs = con.prepareCall(istruzione);
            cs.registerOutParameter(2, java.sql.Types.NUMERIC);
            cs.setString(1, fotoXml);
            cs.executeUpdate();
            risultato = cs.getLong(2);
        } catch (Exception ex) {
            log.error("Errore importazione Foto", ex);
            throw new RuntimeException("Errore importazione Foto", ex);
        } finally {
            if (cs != null) {
                try {
                    cs.close();
                } catch (SQLException ex) {
                    log.debug("Errore importazione Foto", ex);
                    throw new RuntimeException("Errore importazione Foto", ex);
                }
            }
        }
        return risultato;
    }

    private String getClobAsString(Clob clob) throws SQLException, IOException {
        StringBuilder sb = new StringBuilder((int) clob.length());
        Reader r = clob.getCharacterStream();
        char[] cbuf = new char[2048];
        int n;
        while ((n = r.read(cbuf, 0, cbuf.length)) != -1) {
            sb.append(cbuf, 0, n);
        }
        return sb.toString();
    }

    /*
     * private Clob getStringAsClob(Connection con, String str) throws SQLException { Clob c = con.createClob();
     * c.setString(1, str); return c; }
     */

    /*
     * Prende in input una stringa contenente la Foto esportata dalla funzione exportFoto() ed una mappa con nella
     * chiave la variabile da sostituire all'interno dell'XML e la stringa effettiva.
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String sostituisciTutto(String stringa, HashMap<String, String> mappa) {
        // Fà l'escape dei caratteri NON XML! Su tutta la mappa.
        for (Entry<String, String> entry : mappa.entrySet()) {
            entry.setValue(StringEscapeUtils.escapeXml(entry.getValue()));
        }
        String str = StrSubstitutor.replace(stringa, mappa, "$#${", "}$#$");
        return str;
    }

}
