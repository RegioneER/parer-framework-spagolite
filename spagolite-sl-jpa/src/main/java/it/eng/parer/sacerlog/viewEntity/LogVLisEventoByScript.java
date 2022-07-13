package it.eng.parer.sacerlog.viewEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.*;

/**
 * The persistent class for the LOG_V_LIS_EVENTO_BY_SCRIPT database table.
 *
 */
@Entity
@Table(name = "SACER_LOG.LOG_V_LIS_EVENTO_BY_SCRIPT")
@NamedQueries({
        @NamedQuery(name = "LogVLisEventoByScript.findByTipoOggettoId", query = "SELECT l FROM LogVLisEventoByScript l WHERE l.idTipoOggetto = :idTipoOggetto AND l.idOggetto = :idOggetto"),
        @NamedQuery(name = "LogVLisEventoByScript.findDistinctByNmApplic", query = "SELECT DISTINCT l.idTipoOggetto, l.idOggetto, l.nmTipoOggetto FROM LogVLisEventoByScript l WHERE l.nmApplic = :nmApplic") })
public class LogVLisEventoByScript implements Serializable {

    private static final long serialVersionUID = 1L;
    private Timestamp dtRegEvento;
    private BigDecimal idAgente;
    private BigDecimal idApplic;
    private BigDecimal idAzioneCompSw;
    private BigDecimal idEventoByScript;
    private BigDecimal idOggetto;
    private BigDecimal idTipoOggetto;
    private String nmApplic;
    private String nmTipoOggetto;
    private String tiRuoloAgenteEvento;
    private String tiRuoloOggettoEvento;

    public LogVLisEventoByScript() {
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

    @Column(name = "ID_APPLIC")
    public BigDecimal getIdApplic() {
        return this.idApplic;
    }

    public void setIdApplic(BigDecimal idApplic) {
        this.idApplic = idApplic;
    }

    @Column(name = "ID_AZIONE_COMP_SW")
    public BigDecimal getIdAzioneCompSw() {
        return this.idAzioneCompSw;
    }

    public void setIdAzioneCompSw(BigDecimal idAzioneCompSw) {
        this.idAzioneCompSw = idAzioneCompSw;
    }

    @Id
    @Column(name = "ID_EVENTO_BY_SCRIPT")
    public BigDecimal getIdEventoByScript() {
        return this.idEventoByScript;
    }

    public void setIdEventoByScript(BigDecimal idEventoByScript) {
        this.idEventoByScript = idEventoByScript;
    }

    @Column(name = "ID_OGGETTO")
    public BigDecimal getIdOggetto() {
        return this.idOggetto;
    }

    public void setIdOggetto(BigDecimal idOggetto) {
        this.idOggetto = idOggetto;
    }

    @Column(name = "ID_TIPO_OGGETTO")
    public BigDecimal getIdTipoOggetto() {
        return this.idTipoOggetto;
    }

    public void setIdTipoOggetto(BigDecimal idTipoOggetto) {
        this.idTipoOggetto = idTipoOggetto;
    }

    @Column(name = "NM_APPLIC")
    public String getNmApplic() {
        return this.nmApplic;
    }

    public void setNmApplic(String nmApplic) {
        this.nmApplic = nmApplic;
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

    @Column(name = "TI_RUOLO_OGGETTO_EVENTO")
    public String getTiRuoloOggettoEvento() {
        return this.tiRuoloOggettoEvento;
    }

    public void setTiRuoloOggettoEvento(String tiRuoloOggettoEvento) {
        this.tiRuoloOggettoEvento = tiRuoloOggettoEvento;
    }

}
