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
