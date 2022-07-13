package it.eng.integriam.server.ws.reputente;

public class CancellaUtenteRisposta extends ReplicaUtenteRispostaAbstract {

    private Integer idUserIam;

    public Integer getIdUserIam() {
        return idUserIam;
    }

    public void setIdUserIam(Integer idUserIam) {
        this.idUserIam = idUserIam;
    }
}
