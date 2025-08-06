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

/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package it.eng.parer.sacerlog.ejb.helper;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.StringSubstitutor;

import it.eng.parer.sacerlog.common.SacerLogEjbType;
import it.eng.parer.sacerlog.exceptions.SacerLogRuntimeException;
import it.eng.parer.sacerlog.exceptions.SacerLogRuntimeException.SacerLogErrorCategory;
import it.eng.spagoCore.util.JpaUtils;

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

    /*
     * Dato un idOggetto ed un nome di funzione Oracle (NOME_SCHEMA.NOME_FUNZIONE) restituisce una
     * foto XML dell'entità a cui fa capo l'idOggetto passato.
     */
    public String exportFoto(BigDecimal idOggetto, String functionName) {
	Clob clob = null;
	String risultato = null;
	ResultSet rs = null;
	try (Connection con = JpaUtils.provideConnectionFrom(entityManager);
		PreparedStatement ps = con
			.prepareStatement("SELECT " + functionName + "(?) AS FOTO FROM DUAL")) {
	    ps.setBigDecimal(1, idOggetto);
	    rs = ps.executeQuery();
	    if (rs != null && rs.next()) {
		clob = rs.getClob("FOTO");
		risultato = clob == null ? null : getClobAsString(clob);
	    }
	} catch (Exception ex) {
	    throw SacerLogRuntimeException.builder().cause(ex)
		    .category(SacerLogErrorCategory.SQL_ERROR)
		    .message(
			    "Errore esportazione foto dalla funzione oracle {0}() per l'oggetto con ID {1,number,#}",
			    functionName, idOggetto)
		    .build();
	}
	return risultato;
    }

    /*
     * Data una foto ottenuta dalla funzione Oracle exportFoto e il nome della Procedura Oracle
     * (NOME_SCHEMA.NOME_PROCEDURA)effettua l'importazione dei dati su tutte le tabelle legate
     * all'entità fotografata nell'XML. Restituisce l'idOggetto dell'entità appena importata.
     */
    public long importFoto(String fotoXml, String procedureName) {
	long risultato = -1;
	String istruzione = "{call " + procedureName + "(?,?)}";
	try (Connection con = JpaUtils.provideConnectionFrom(entityManager);
		CallableStatement cs = con.prepareCall(istruzione);) {
	    cs.registerOutParameter(2, java.sql.Types.NUMERIC);
	    cs.setString(1, fotoXml);
	    cs.executeUpdate();
	    risultato = cs.getLong(2);
	} catch (Exception ex) {
	    throw SacerLogRuntimeException.builder().cause(ex)
		    .category(SacerLogErrorCategory.SQL_ERROR).message("Errore importazione Foto")
		    .build();
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
     * Prende in input una stringa contenente la Foto esportata dalla funzione exportFoto() ed una
     * mappa con nella chiave la variabile da sostituire all'interno dell'XML e la stringa
     * effettiva.
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String sostituisciTutto(String stringa, Map<String, String> mappa) {
	// Fà l'escape dei caratteri NON XML! Su tutta la mappa.
	for (Entry<String, String> entry : mappa.entrySet()) {
	    entry.setValue(StringEscapeUtils.escapeXml10(entry.getValue()));
	}
	return StringSubstitutor.replace(stringa, mappa, "$#${", "}$#$");
    }

}
