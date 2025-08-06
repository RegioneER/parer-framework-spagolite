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

package it.eng.integriam.server.ws;

import javax.ejb.EJB;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import it.eng.integriam.server.ws.reputente.CancellaUtenteRisposta;
import it.eng.integriam.server.ws.reputente.InserimentoUtenteRisposta;
import it.eng.integriam.server.ws.reputente.ModificaUtenteRisposta;
import it.eng.integriam.server.ws.reputente.ReplicaUtenteInterface;
import it.eng.integriam.server.ws.reputente.Utente;

@WebService(serviceName = "ReplicaUtente", targetNamespace = "http://ws.saceriam.eng.it/")
@HandlerChain(file = "/ws_handler.xml")
public class ReplicaUtente {

    // @Inject
    @EJB(beanName = "replicaUtenteEjb")
    private ReplicaUtenteInterface repUsr;

    @WebMethod(operationName = "inserimentoUtente")
    public InserimentoUtenteRisposta inserimentoUtente(@WebParam(name = "utente") Utente utente) {
	return repUsr.inserimentoUtente(utente);
    }

    @WebMethod(operationName = "cancellaUtente")
    public CancellaUtenteRisposta cancellaUtente(@WebParam(name = "idUserIam") Integer idUserIam) {
	return repUsr.cancellaUtente(idUserIam);
    }

    @WebMethod(operationName = "modificaUtente")
    public ModificaUtenteRisposta modificaUtente(@WebParam(name = "utente") Utente utente) {
	return repUsr.modificaUtente(utente);
    }
}
