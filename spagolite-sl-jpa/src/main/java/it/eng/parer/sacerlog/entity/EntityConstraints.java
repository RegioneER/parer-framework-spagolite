/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.sacerlog.entity;

/**
 *
 * @author Iacolucci_M
 */
public class EntityConstraints {

    public static final String SEPARATORE_DS_KEY_OGGETTO = "~~";
    public static final String VALORE_NULL_DS_KEY_OGGETTO = "null";

    public enum TipoAzione {
        AZIONE_PAGINA, AZIONE_JOB
    }

    public enum TipoOrigineAgente {
        UTENTE, COMPONENTE_SW
    }

}
