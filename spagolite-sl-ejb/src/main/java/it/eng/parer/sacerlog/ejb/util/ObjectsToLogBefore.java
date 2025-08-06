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
package it.eng.parer.sacerlog.ejb.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Iacolucci_M
 */
public class ObjectsToLogBefore {

    private String nomeApplicazione;
    private String tipoOggetto;
    private String tipoEvento;
    private String classeEvento;
    private List<BigDecimal> idOggetto = new ArrayList();

    public ObjectsToLogBefore(String nomeApplicazione, String tipoOggetto, String tipoEvento,
	    String classeEvento) {
	this.nomeApplicazione = nomeApplicazione;
	this.tipoOggetto = tipoOggetto;
	this.tipoEvento = tipoEvento;
	this.classeEvento = classeEvento;
    }

    public String getNomeApplicazione() {
	return nomeApplicazione;
    }

    public void setNomeApplicazione(String nomeApplicazione) {
	this.nomeApplicazione = nomeApplicazione;
    }

    public String getTipoOggetto() {
	return tipoOggetto;
    }

    public void setTipoOggetto(String tipoOggetto) {
	this.tipoOggetto = tipoOggetto;
    }

    public String getTipoEvento() {
	return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
	this.tipoEvento = tipoEvento;
    }

    public String getClasseEvento() {
	return classeEvento;
    }

    public void setClasseEvento(String classeEvento) {
	this.classeEvento = classeEvento;
    }

    public List<BigDecimal> getIdOggetto() {
	return idOggetto;
    }

    public void setIdOggetto(List<BigDecimal> idOggetto) {
	this.idOggetto = idOggetto;
    }

    /*
     * Filra gli oggetti aventi come classe evento "CANCELLAZIONE"
     */
    public static List<ObjectsToLogBefore> filterObjectsForDeletion(
	    List<ObjectsToLogBefore> sourceList) {
	ArrayList<ObjectsToLogBefore> destList = null;
	if (sourceList != null) {
	    destList = new ArrayList();
	    for (ObjectsToLogBefore source : sourceList) {
		if (source.getClasseEvento()
			.equals(PremisEnums.TipoClasseEvento.CANCELLAZIONE.name())) {
		    destList.add(source);
		}
	    }
	}
	return destList;
    }

    /*
     * Filra gli oggetti aventi come classe evento "MODIFICA"
     */
    public static List<ObjectsToLogBefore> filterObjectsForModifying(
	    List<ObjectsToLogBefore> sourceList) {
	ArrayList<ObjectsToLogBefore> destList = null;
	if (sourceList != null) {
	    destList = new ArrayList();
	    for (ObjectsToLogBefore source : sourceList) {
		if (source.getClasseEvento().equals(PremisEnums.TipoClasseEvento.MODIFICA.name())) {
		    destList.add(source);
		}
	    }
	}
	return destList;
    }

}
