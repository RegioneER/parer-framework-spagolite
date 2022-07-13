package it.eng.parer.sacerlog.viewEntity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The persistent class for the LOG_V_LOG_AGENTE database table.
 *
 */
@Entity
@Table(name = "LOG_V_LOG_AGENTE", schema = "SACER_IAM")
@NamedQueries({ @NamedQuery(name = "LogVLogAgente.findAll", query = "SELECT l FROM LogVLogAgente l"),
        @NamedQuery(name = "LogVLogAgente.getByNomeAgente", query = "SELECT l FROM LogVLogAgente l WHERE l.nmAgente = :nomeAgente"),
        @NamedQuery(name = "LogVLogAgente.getByIdAgente", query = "SELECT l FROM LogVLogAgente l WHERE l.idAgente = :idAgente"),
        @NamedQuery(name = "LogVLogAgente.getByNomeCompSoftware", query = "SELECT l FROM LogVLogAgente l WHERE l.nmNomeUserCompSw = :nmNomeUserCompSw") })
public class LogVLogAgente implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigDecimal idAgente;
    private BigDecimal idUserCompSw;
    private String nmAgente;
    private String nmCognomeUserApplic;
    private String nmNomeUserCompSw;
    private String tipoAgentePremis;
    private String tipoOrigineAgente;

    public LogVLogAgente() {
    }

    @Id
    @Column(name = "ID_AGENTE")
    public BigDecimal getIdAgente() {
        return this.idAgente;
    }

    public void setIdAgente(BigDecimal idAgente) {
        this.idAgente = idAgente;
    }

    @Column(name = "ID_USER_COMP_SW")
    public BigDecimal getIdUserCompSw() {
        return this.idUserCompSw;
    }

    public void setIdUserCompSw(BigDecimal idUserCompSw) {
        this.idUserCompSw = idUserCompSw;
    }

    @Column(name = "NM_AGENTE")
    public String getNmAgente() {
        return this.nmAgente;
    }

    public void setNmAgente(String nmAgente) {
        this.nmAgente = nmAgente;
    }

    @Column(name = "NM_COGNOME_USER_APPLIC")
    public String getNmCognomeUserApplic() {
        return this.nmCognomeUserApplic;
    }

    public void setNmCognomeUserApplic(String nmCognomeUserApplic) {
        this.nmCognomeUserApplic = nmCognomeUserApplic;
    }

    @Column(name = "NM_NOME_USER_COMP_SW")
    public String getNmNomeUserCompSw() {
        return this.nmNomeUserCompSw;
    }

    public void setNmNomeUserCompSw(String nmNomeUserCompSw) {
        this.nmNomeUserCompSw = nmNomeUserCompSw;
    }

    @Column(name = "TIPO_AGENTE_PREMIS")
    public String getTipoAgentePremis() {
        return this.tipoAgentePremis;
    }

    public void setTipoAgentePremis(String tipoAgentePremis) {
        this.tipoAgentePremis = tipoAgentePremis;
    }

    @Column(name = "TIPO_ORIGINE_AGENTE")
    public String getTipoOrigineAgente() {
        return this.tipoOrigineAgente;
    }

    public void setTipoOrigineAgente(String tipoOrigineAgente) {
        this.tipoOrigineAgente = tipoOrigineAgente;
    }

}
