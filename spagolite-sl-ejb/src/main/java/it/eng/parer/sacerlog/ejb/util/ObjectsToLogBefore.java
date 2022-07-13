/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

    public ObjectsToLogBefore(String nomeApplicazione, String tipoOggetto, String tipoEvento, String classeEvento) {
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
    public static List<ObjectsToLogBefore> filterObjectsForDeletion(List<ObjectsToLogBefore> sourceList) {
        ArrayList<ObjectsToLogBefore> destList = null;
        if (sourceList != null) {
            destList = new ArrayList();
            for (ObjectsToLogBefore source : sourceList) {
                if (source.getClasseEvento().equals(PremisEnums.TipoClasseEvento.CANCELLAZIONE.name())) {
                    destList.add(source);
                }
            }
        }
        return destList;
    }

    /*
     * Filra gli oggetti aventi come classe evento "MODIFICA"
     */
    public static List<ObjectsToLogBefore> filterObjectsForModifying(List<ObjectsToLogBefore> sourceList) {
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
