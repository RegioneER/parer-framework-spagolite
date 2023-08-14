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

@WebService(serviceName = "ReplicaUtente")
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
