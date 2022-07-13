/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.spagoLite.actions.application;

/**
 *
 * @author Iacolucci_M
 * 
 *         Interfaccia che ogni applicazione che usa SpagoLite dovrebbe implementare per popolare l'oggetto
 *         ApplicationBaseProperties da qualsiasi fonte, tipicamente un DB
 */
public interface IApplicationBasePropertiesSevice {

    public static final String CONST_HELP_RICERCA_DIPS = "HELP_RICERCA_DIPS";
    public static final String CONST_HELP_PAGINA = "HELP_PAGINA";

    public ApplicationBaseProperties getApplicationBaseProperties();

}
