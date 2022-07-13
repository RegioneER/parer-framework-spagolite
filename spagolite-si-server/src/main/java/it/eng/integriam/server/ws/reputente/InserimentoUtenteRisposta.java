package it.eng.integriam.server.ws.reputente;

/**
 *
 * @author Gilioli_P
 */
public class InserimentoUtenteRisposta extends ReplicaUtenteRispostaAbstract {

    private Utente utente;

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
}
