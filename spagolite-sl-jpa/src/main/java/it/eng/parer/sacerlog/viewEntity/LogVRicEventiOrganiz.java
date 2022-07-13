package it.eng.parer.sacerlog.viewEntity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * The persistent class for the LOG_V_RIC_EVENTI_ORGANIZ database table.
 *
 */
@Entity
@Table(name = "LOG_V_RIC_EVENTI_ORGANIZ", schema = "SACER_LOG")
@NamedQueries({ @NamedQuery(name = "LogVRicEventiOrganiz.findAll", query = "SELECT l FROM LogVRicEventiOrganiz l"),
        @NamedQuery(name = "LogVRicEventiOrganiz.findByIdEvento", query = "SELECT l FROM LogVRicEventiOrganiz l WHERE l.idEvento = :idEvento"),
        @NamedQuery(name = "LogVRicEventiOrganiz.findByIdTransazioneExcludingIdEvento", query = "SELECT l FROM LogVRicEventiOrganiz l WHERE l.idTransazione = :idTransazione AND l.idEvento <> :idEvento") })
public class LogVRicEventiOrganiz implements ILogVRicEventi {

    private static final long serialVersionUID = 1L;
    private String dsKeyOggetto;
    private Timestamp dtRegEvento;
    private BigDecimal idAgente;
    private BigDecimal idAgenteEvento;
    private BigDecimal idApplic;
    private BigDecimal idEvento;
    private BigDecimal idOggetto;
    private BigDecimal idOggettoEvento;
    private BigDecimal idOrganizApplic;
    private BigDecimal idTipoEvento;
    private BigDecimal idTipoOggetto;
    private BigDecimal idTipoOggettoPadre;
    private BigDecimal idTransazione;
    private String nmAgente;
    private String nmAmbiente;
    private String nmVersatore;
    private String nmApplic;
    private String nmAzione;
    private String nmEnte;
    private String nmGeneratoreAzione;
    private String nmOggetto;
    private String nmStruttura;
    private String nmTipoEvento;
    private String nmTipoOggetto;
    private String nmTipoOggettoPadre;
    private String tiRuoloAgenteEvento;
    private String tipoAzione;
    private String tipoClasseEvento;
    private String tipoOrigineAgente;
    private String dsMotivoScript;

    public LogVRicEventiOrganiz() {
    }

    @Column(name = "DS_KEY_OGGETTO")
    @Override
    public String getDsKeyOggetto() {
        return this.dsKeyOggetto;
    }

    @Override
    public void setDsKeyOggetto(String dsKeyOggetto) {
        this.dsKeyOggetto = dsKeyOggetto;
    }

    @Column(name = "DT_REG_EVENTO")
    @Override
    public Timestamp getDtRegEvento() {
        return this.dtRegEvento;
    }

    @Override
    public void setDtRegEvento(Timestamp dtRegEvento) {
        this.dtRegEvento = dtRegEvento;
    }

    @Column(name = "ID_AGENTE")
    @Override
    public BigDecimal getIdAgente() {
        return this.idAgente;
    }

    @Override
    public void setIdAgente(BigDecimal idAgente) {
        this.idAgente = idAgente;
    }

    @Id
    @Column(name = "ID_AGENTE_EVENTO")
    @Override
    public BigDecimal getIdAgenteEvento() {
        return this.idAgenteEvento;
    }

    @Override
    public void setIdAgenteEvento(BigDecimal idAgenteEvento) {
        this.idAgenteEvento = idAgenteEvento;
    }

    @Column(name = "ID_APPLIC")
    @Override
    public BigDecimal getIdApplic() {
        return this.idApplic;
    }

    @Override
    public void setIdApplic(BigDecimal idApplic) {
        this.idApplic = idApplic;
    }

    @Column(name = "ID_EVENTO")
    @Override
    public BigDecimal getIdEvento() {
        return this.idEvento;
    }

    @Override
    public void setIdEvento(BigDecimal idEvento) {
        this.idEvento = idEvento;
    }

    @Column(name = "ID_OGGETTO")
    @Override
    public BigDecimal getIdOggetto() {
        return this.idOggetto;
    }

    @Override
    public void setIdOggetto(BigDecimal idOggetto) {
        this.idOggetto = idOggetto;
    }

    @Id
    @Column(name = "ID_OGGETTO_EVENTO")
    @Override
    public BigDecimal getIdOggettoEvento() {
        return this.idOggettoEvento;
    }

    @Override
    public void setIdOggettoEvento(BigDecimal idOggettoEvento) {
        this.idOggettoEvento = idOggettoEvento;
    }

    @Column(name = "ID_TIPO_EVENTO")
    @Override
    public BigDecimal getIdTipoEvento() {
        return this.idTipoEvento;
    }

    @Override
    public void setIdTipoEvento(BigDecimal idTipoEvento) {
        this.idTipoEvento = idTipoEvento;
    }

    @Column(name = "ID_TIPO_OGGETTO")
    @Override
    public BigDecimal getIdTipoOggetto() {
        return this.idTipoOggetto;
    }

    @Override
    public void setIdTipoOggetto(BigDecimal idTipoOggetto) {
        this.idTipoOggetto = idTipoOggetto;
    }

    @Column(name = "ID_TIPO_OGGETTO_PADRE")
    @Override
    public BigDecimal getIdTipoOggettoPadre() {
        return this.idTipoOggettoPadre;
    }

    @Override
    public void setIdTipoOggettoPadre(BigDecimal idTipoOggettoPadre) {
        this.idTipoOggettoPadre = idTipoOggettoPadre;
    }

    @Column(name = "ID_TRANSAZIONE")
    @Override
    public BigDecimal getIdTransazione() {
        return this.idTransazione;
    }

    @Override
    public void setIdTransazione(BigDecimal idTransazione) {
        this.idTransazione = idTransazione;
    }

    @Column(name = "NM_AGENTE")
    @Override
    public String getNmAgente() {
        return this.nmAgente;
    }

    @Override
    public void setNmAgente(String nmAgente) {
        this.nmAgente = nmAgente;
    }

    @Column(name = "NM_AMBIENTE")
    @Override
    public String getNmAmbiente() {
        return this.nmAmbiente;
    }

    @Override
    public void setNmAmbiente(String nmAmbiente) {
        this.nmAmbiente = nmAmbiente;
    }

    @Column(name = "NM_APPLIC")
    @Override
    public String getNmApplic() {
        return this.nmApplic;
    }

    @Override
    public void setNmApplic(String nmApplic) {
        this.nmApplic = nmApplic;
    }

    @Column(name = "NM_AZIONE")
    @Override
    public String getNmAzione() {
        return this.nmAzione;
    }

    @Override
    public void setNmAzione(String nmAzione) {
        this.nmAzione = nmAzione;
    }

    @Column(name = "NM_ENTE")
    @Override
    public String getNmEnte() {
        return this.nmEnte;
    }

    @Override
    public void setNmEnte(String nmEnte) {
        this.nmEnte = nmEnte;
    }

    @Column(name = "NM_GENERATORE_AZIONE")
    @Override
    public String getNmGeneratoreAzione() {
        return this.nmGeneratoreAzione;
    }

    @Override
    public void setNmGeneratoreAzione(String nmGeneratoreAzione) {
        this.nmGeneratoreAzione = nmGeneratoreAzione;
    }

    @Column(name = "NM_OGGETTO")
    @Override
    public String getNmOggetto() {
        return this.nmOggetto;
    }

    @Override
    public void setNmOggetto(String nmOggetto) {
        this.nmOggetto = nmOggetto;
    }

    @Column(name = "NM_STRUTTURA")
    @Override
    public String getNmStruttura() {
        return this.nmStruttura;
    }

    @Override
    public void setNmStruttura(String nmStruttura) {
        this.nmStruttura = nmStruttura;
    }

    @Column(name = "NM_TIPO_EVENTO")
    @Override
    public String getNmTipoEvento() {
        return this.nmTipoEvento;
    }

    @Override
    public void setNmTipoEvento(String nmTipoEvento) {
        this.nmTipoEvento = nmTipoEvento;
    }

    @Column(name = "NM_TIPO_OGGETTO")
    @Override
    public String getNmTipoOggetto() {
        return this.nmTipoOggetto;
    }

    @Override
    public void setNmTipoOggetto(String nmTipoOggetto) {
        this.nmTipoOggetto = nmTipoOggetto;
    }

    @Column(name = "NM_TIPO_OGGETTO_PADRE")
    @Override
    public String getNmTipoOggettoPadre() {
        return this.nmTipoOggettoPadre;
    }

    @Override
    public void setNmTipoOggettoPadre(String nmTipoOggettoPadre) {
        this.nmTipoOggettoPadre = nmTipoOggettoPadre;
    }

    @Column(name = "TI_RUOLO_AGENTE_EVENTO")
    @Override
    public String getTiRuoloAgenteEvento() {
        return this.tiRuoloAgenteEvento;
    }

    @Override
    public void setTiRuoloAgenteEvento(String tiRuoloAgenteEvento) {
        this.tiRuoloAgenteEvento = tiRuoloAgenteEvento;
    }

    @Column(name = "TIPO_AZIONE")
    @Override
    public String getTipoAzione() {
        return this.tipoAzione;
    }

    @Override
    public void setTipoAzione(String tipoAzione) {
        this.tipoAzione = tipoAzione;
    }

    @Column(name = "TIPO_CLASSE_EVENTO")
    @Override
    public String getTipoClasseEvento() {
        return this.tipoClasseEvento;
    }

    @Override
    public void setTipoClasseEvento(String tipoClasseEvento) {
        this.tipoClasseEvento = tipoClasseEvento;
    }

    @Column(name = "TIPO_ORIGINE_AGENTE")
    @Override
    public String getTipoOrigineAgente() {
        return this.tipoOrigineAgente;
    }

    @Override
    public void setTipoOrigineAgente(String tipoOrigineAgente) {
        this.tipoOrigineAgente = tipoOrigineAgente;
    }

    @Column(name = "NM_VERSATORE")
    @Override
    public String getNmVersatore() {
        return this.nmVersatore;
    }

    @Override
    public void setNmVersatore(String nmVersatore) {
        this.nmVersatore = nmVersatore;
    }

    @Column(name = "ID_ORGANIZ_APPLIC")
    @Override
    public BigDecimal getIdOrganizApplic() {
        return this.idOrganizApplic;
    }

    @Override
    public void setIdOrganizApplic(BigDecimal idOrganizApplic) {
        this.idOrganizApplic = idOrganizApplic;
    }

    @Column(name = "DS_MOTIVO_SCRIPT")
    @Override
    public String getDsMotivoScript() {
        return this.dsMotivoScript;
    }

    @Override
    public void setDsMotivoScript(String dsMotivoScript) {
        this.dsMotivoScript = dsMotivoScript;
    }

}
