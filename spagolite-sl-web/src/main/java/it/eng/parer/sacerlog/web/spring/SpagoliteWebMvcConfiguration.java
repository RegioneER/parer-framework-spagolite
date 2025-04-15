package it.eng.parer.sacerlog.web.spring;

import it.eng.parer.sacerlog.web.action.GestioneLogEventiAction;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author S257421
 *
 *         Classe Padre per la WebConfiguration di Spring che configura a livello di framework le
 *         Action del logging
 */
public class SpagoliteWebMvcConfiguration {

    /* Gestione Eventi */
    @Bean(value = "/GestioneLogEventi.html")
    GestioneLogEventiAction gestioneLogEventiAction() {
	return new GestioneLogEventiAction();
    }

    @Bean(value = "/DettaglioEventoOggetto.html")
    GestioneLogEventiAction gestioneLogEventiActionDettaglio() {
	return new GestioneLogEventiAction();
    }

    @Bean(value = "/DettaglioValori/.html")
    GestioneLogEventiAction gestioneLogEventiActionDettagliValori() {
	return new GestioneLogEventiAction();
    }

    @Bean(value = "/DettaglioXml.html")
    GestioneLogEventiAction gestioneLogEventiActionDettaglioXml() {
	return new GestioneLogEventiAction();
    }

    /* ricerca Eventi */
    @Bean(value = "/RicercaEventi.html")
    GestioneLogEventiAction gestioneLogEventiActionRicercaEventi() {
	return new GestioneLogEventiAction();
    }

}
