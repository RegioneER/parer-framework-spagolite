package it.eng.parer.sacerlog.viewEntity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;

/**
 * The persistent class for the LOG_V_LIS_EVENTO_OGGETTO database table.
 *
 */
@Entity
@Table(name = "LOG_V_LIS_EVENTO_OGGETTO", schema = "SACER_LOG")
@NamedQueries({ @NamedQuery(name = "LogVLisEventoOggetto.findAll", query = "SELECT l FROM LogVLisEventoOggetto l"),
        @NamedQuery(name = "LogVLisEventoOggetto.findEventsByAppTipoOggNameId", query = "SELECT l FROM LogVLisEventoOggetto l WHERE l.nmApplic = :nmApplic AND l.nmTipoOggetto = :nmTipoOggetto AND l.idOggetto = :idOggetto ORDER BY l.dtRegEvento DESC") })
public class LogVLisEventoOggetto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String dsAzione;
    private String dsGeneratoreAzione;
    private Timestamp dtRegEvento;
    private BigDecimal idAgente;
    private BigDecimal idAgenteEvento;
    private BigDecimal idApplic;
    private BigDecimal idEvento;
    private BigDecimal idOggetto;
    private BigDecimal idOggettoEvento;
    private BigDecimal idTipoEvento;
    private BigDecimal idTipoOggetto;
    private String nmAgente;
    private String nmApplic;
    private String nmTipoEvento;
    private String nmTipoOggetto;
    private String tiRuoloAgenteEvento;
    private String tipoClasseEvento;
    private String tipoOrigineAgente;
    private String tipoOrigineEvento;
    private BigDecimal idTransazione;
    private String dsMotivoScript;

    public LogVLisEventoOggetto() {
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

    @Column(name = "ID_APPLIC")
    public BigDecimal getIdApplic() {
        return this.idApplic;
    }

    public void setIdApplic(BigDecimal idApplic) {
        this.idApplic = idApplic;
    }

    @Id
    @Column(name = "ID_EVENTO")
    public BigDecimal getIdEvento() {
        return this.idEvento;
    }

    public void setIdEvento(BigDecimal idEvento) {
        this.idEvento = idEvento;
    }

    @Column(name = "ID_OGGETTO")
    public BigDecimal getIdOggetto() {
        return this.idOggetto;
    }

    public void setIdOggetto(BigDecimal idOggetto) {
        this.idOggetto = idOggetto;
    }

    @Column(name = "ID_OGGETTO_EVENTO")
    public BigDecimal getIdOggettoEvento() {
        return this.idOggettoEvento;
    }

    public void setIdOggettoEvento(BigDecimal idOggettoEvento) {
        this.idOggettoEvento = idOggettoEvento;
    }

    @Column(name = "ID_TIPO_EVENTO")
    public BigDecimal getIdTipoEvento() {
        return this.idTipoEvento;
    }

    public void setIdTipoEvento(BigDecimal idTipoEvento) {
        this.idTipoEvento = idTipoEvento;
    }

    @Column(name = "ID_TIPO_OGGETTO")
    public BigDecimal getIdTipoOggetto() {
        return this.idTipoOggetto;
    }

    public void setIdTipoOggetto(BigDecimal idTipoOggetto) {
        this.idTipoOggetto = idTipoOggetto;
    }

    @Column(name = "NM_AGENTE")
    public String getNmAgente() {
        return this.nmAgente;
    }

    public void setNmAgente(String nmAgente) {
        this.nmAgente = nmAgente;
    }

    @Column(name = "NM_APPLIC")
    public String getNmApplic() {
        return this.nmApplic;
    }

    public void setNmApplic(String nmApplic) {
        this.nmApplic = nmApplic;
    }

    @Column(name = "NM_TIPO_EVENTO")
    public String getNmTipoEvento() {
        return this.nmTipoEvento;
    }

    public void setNmTipoEvento(String nmTipoEvento) {
        this.nmTipoEvento = nmTipoEvento;
    }

    @Column(name = "NM_TIPO_OGGETTO")
    public String getNmTipoOggetto() {
        return this.nmTipoOggetto;
    }

    public void setNmTipoOggetto(String nmTipoOggetto) {
        this.nmTipoOggetto = nmTipoOggetto;
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

    @Column(name = "ID_TRANSAZIONE")
    public BigDecimal getIdTransazione() {
        return this.idTransazione;
    }

    public void setIdTransazione(BigDecimal idTransazione) {
        this.idTransazione = idTransazione;
    }

    @Column(name = "DS_MOTIVO_SCRIPT")
    public String getDsMotivoScript() {
        return this.dsMotivoScript;
    }

    public void setDsMotivoScript(String dsMotivoScript) {
        this.dsMotivoScript = dsMotivoScript;
    }

}
