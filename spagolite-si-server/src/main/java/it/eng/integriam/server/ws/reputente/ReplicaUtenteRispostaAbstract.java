package it.eng.integriam.server.ws.reputente;

import it.eng.integriam.server.ws.Costanti.EsitoServizio;

/**
 *
 * @author Gilioli_P
 */
public class ReplicaUtenteRispostaAbstract {

    private EsitoServizio cdEsito;
    private String cdErr;
    private String dsErr;

    public EsitoServizio getCdEsito() {
        return cdEsito;
    }

    public void setCdEsito(EsitoServizio cdEsito) {
        this.cdEsito = cdEsito;
    }

    public String getCdErr() {
        return cdErr;
    }

    public void setCdErr(String cdErr) {
        this.cdErr = cdErr;
    }

    public String getDsErr() {
        return dsErr;
    }

    public void setDsErr(String dsErr) {
        this.dsErr = dsErr;
    }

}
