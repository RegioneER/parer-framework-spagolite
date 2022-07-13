package it.eng.integriam.server.ws.reputente;

public interface ReplicaUtenteInterface {

    public InserimentoUtenteRisposta inserimentoUtente(Utente utente);

    public CancellaUtenteRisposta cancellaUtente(Integer idUserIam);

    public ModificaUtenteRisposta modificaUtente(Utente utente);
}
