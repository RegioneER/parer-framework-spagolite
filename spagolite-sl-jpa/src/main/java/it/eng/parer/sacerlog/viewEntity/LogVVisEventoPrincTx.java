package it.eng.parer.sacerlog.viewEntity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * The persistent class for the LOG_V_VIS_EVENTO_PRINC_TX database table.
 *
 */
@Entity
@Table(name = "LOG_V_VIS_EVENTO_PRINC_TX", schema = "SACER_LOG")
@NamedQuery(name = "LogVVisEventoPrincTx.findTxById", query = "SELECT a FROM LogVVisEventoPrincTx a WHERE a.idTransazione = :idTransazione ")
public class LogVVisEventoPrincTx implements Serializable {

    private static final long serialVersionUID = 1L;
    private String dsAzione;
    private String dsGeneratoreAzione;
    private Timestamp dtRegEvento;
    private BigDecimal idAgente;
    private BigDecimal idAgenteEvento;
    private BigDecimal idEvento;
    private BigDecimal idTipoEvento;
    private BigDecimal idTransazione;
    private String nmAgente;
    private String nmTipoEvento;
    private String tiRuoloAgenteEvento;
    private String tipoClasseEvento;
    private String tipoOrigineAgente;
    private String tipoOrigineEvento;
    private String dsMotivoScript;

    public LogVVisEventoPrincTx() {
    }

    @Column(name = "DS_AZIONE")
    public String getDsAzione() {
        return this.dsAzione;
    }

    public void setDsAzione(String dsAzione) {
        this.dsAzione = dsAzione;
    }

    @Column(name = "DS_GENERATORE_AZIONE")
    public String getDsGeneratoreAzione() {
        return this.dsGeneratoreAzione;
    }

    public void setDsGeneratoreAzione(String dsGeneratoreAzione) {
        this.dsGeneratoreAzione = dsGeneratoreAzione;
    }

    @Column(name = "DT_REG_EVENTO")
    public Timestamp getDtRegEvento() {
        return this.dtRegEvento;
    }

    public void setDtRegEvento(Timestamp dtRegEvento) {
        this.dtRegEvento = dtRegEvento;
    }

    @Column(name = "ID_AGENTE")
    public BigDecimal getIdAgente() {
        return this.idAgente;
    }

    public void setIdAgente(BigDecimal idAgente) {
        this.idAgente = idAgente;
    }

    @Column(name = "ID_AGENTE_EVENTO")
    public BigDecimal getIdAgenteEvento() {
        return this.idAgenteEvento;
    }

    public void setIdAgenteEvento(BigDecimal idAgenteEvento) {
        this.idAgenteEvento = idAgenteEvento;
    }

    @Column(name = "ID_EVENTO")
    public BigDecimal getIdEvento() {
        return this.idEvento;
    }

    public void setIdEvento(BigDecimal idEvento) {
        this.idEvento = idEvento;
    }

    @Column(name = "ID_TIPO_EVENTO")
    public BigDecimal getIdTipoEvento() {
        return this.idTipoEvento;
    }

    public void setIdTipoEvento(BigDecimal idTipoEvento) {
        this.idTipoEvento = idTipoEvento;
    }

    @Id
    @Column(name = "ID_TRANSAZIONE")
    public BigDecimal getIdTransazione() {
        return this.idTransazione;
    }

    public void setIdTransazione(BigDecimal idTransazione) {
        this.idTransazione = idTransazione;
    }

    @Column(name = "NM_AGENTE")
    public String getNmAgente() {
        return this.nmAgente;
    }

    public void setNmAgente(String nmAgente) {
        this.nmAgente = nmAgente;
    }

    @Column(name = "NM_TIPO_EVENTO")
    public String getNmTipoEvento() {
        return this.nmTipoEvento;
    }

    public void setNmTipoEvento(String nmTipoEvento) {
        this.nmTipoEvento = nmTipoEvento;
    }

    @Column(name = "TI_RUOLO_AGENTE_EVENTO")
    public String getTiRuoloAgenteEvento() {
        return this.tiRuoloAgenteEvento;
    }

    public void setTiRuoloAgenteEvento(String tiRuoloAgenteEvento) {
        this.tiRuoloAgenteEvento = tiRuoloAgenteEvento;
    }

    @Column(name = "TIPO_CLASSE_EVENTO")
    public String getTipoClasseEvento() {
        return this.tipoClasseEvento;
    }

    public void setTipoClasseEvento(String tipoClasseEvento) {
        this.tipoClasseEvento = tipoClasseEvento;
    }

    @Column(name = "TIPO_ORIGINE_AGENTE")
    public String getTipoOrigineAgente() {
        return this.tipoOrigineAgente;
    }

    public void setTipoOrigineAgente(String tipoOrigineAgente) {
        this.tipoOrigineAgente = tipoOrigineAgente;
    }

    @Column(name = "TIPO_ORIGINE_EVENTO")
    public String getTipoOrigineEvento() {
        return this.tipoOrigineEvento;
    }

    public void setTipoOrigineEvento(String tipoOrigineEvento) {
        this.tipoOrigineEvento = tipoOrigineEvento;
    }

    @Column(name = "DS_MOTIVO_SCRIPT")
    public String getDsMotivoScript() {
        return this.dsMotivoScript;
    }

    public void setDsMotivoScript(String dsMotivoScript) {
        this.dsMotivoScript = dsMotivoScript;
    }

}
