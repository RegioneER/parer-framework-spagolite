package it.eng.parer.sacerlog.viewEntity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The persistent class for the APL_V_LOG_INIT database table.
 *
 */
@Entity
@Table(name = "APL_V_LOG_INIT", schema = "SACER_IAM")
@NamedQueries({ @NamedQuery(name = "AplVLogInit.findAll", query = "SELECT a FROM AplVLogInit a"),
        @NamedQuery(name = "AplVLogInit.findByAppName", query = "SELECT a FROM AplVLogInit a WHERE a.nmApplic = :nmApplic") })
public class AplVLogInit implements Serializable {

    private static final long serialVersionUID = 1L;
    private String blQueryTipoOggetto;
    private BigDecimal idAgente;
    private BigDecimal idApplic;
    private BigDecimal idAzioneCompSw;
    private BigDecimal idCompSw;
    private BigDecimal idQueryTipoOggetto;
    private BigDecimal idTipoEvento;
    private BigDecimal idTipoOggetto;
    private String nmAgente;
    private String nmApplic;
    private String nmAzioneCompSw;
    private String nmCompSw;
    private String nmQueryTipoOggetto;
    private String nmTipoEvento;
    private String nmTipoOggetto;

    public AplVLogInit() {
    }

    @Lob
    @Column(name = "BL_QUERY_TIPO_OGGETTO")
    public String getBlQueryTipoOggetto() {
        return this.blQueryTipoOggetto;
    }

    public void setBlQueryTipoOggetto(String blQueryTipoOggetto) {
        this.blQueryTipoOggetto = blQueryTipoOggetto;
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

    @Column(name = "ID_COMP_SW")
    public BigDecimal getIdCompSw() {
        return this.idCompSw;
    }

    public void setIdCompSw(BigDecimal idCompSw) {
        this.idCompSw = idCompSw;
    }

    @Id
    @Column(name = "ID_QUERY_TIPO_OGGETTO")
    public BigDecimal getIdQueryTipoOggetto() {
        return this.idQueryTipoOggetto;
    }

    public void setIdQueryTipoOggetto(BigDecimal idQueryTipoOggetto) {
        this.idQueryTipoOggetto = idQueryTipoOggetto;
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

    @Column(name = "NM_AZIONE_COMP_SW")
    public String getNmAzioneCompSw() {
        return this.nmAzioneCompSw;
    }

    public void setNmAzioneCompSw(String nmAzioneCompSw) {
        this.nmAzioneCompSw = nmAzioneCompSw;
    }

    @Column(name = "NM_COMP_SW")
    public String getNmCompSw() {
        return this.nmCompSw;
    }

    public void setNmCompSw(String nmCompSw) {
        this.nmCompSw = nmCompSw;
    }

    @Column(name = "NM_QUERY_TIPO_OGGETTO")
    public String getNmQueryTipoOggetto() {
        return this.nmQueryTipoOggetto;
    }

    public void setNmQueryTipoOggetto(String nmQueryTipoOggetto) {
        this.nmQueryTipoOggetto = nmQueryTipoOggetto;
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

}
