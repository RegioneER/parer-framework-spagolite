package it.eng.parer.sacerlog.viewEntity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the LOG_V_DELTA_ASSERZIONI database table.
 *
 */
@Entity
@Table(name = "LOG_V_DELTA_ASSERZIONI", schema = "SACER_LOG")
@NamedQuery(name = "LogVDeltaAsserzioni.findAll", query = "SELECT l FROM LogVDeltaAsserzioni l")
public class LogVDeltaAsserzioni implements Serializable {

    private static final long serialVersionUID = 1L;
    private String blValoreNewCampo;
    private String blValoreOldCampo;
    private String dsChiaveTipoRecord;
    private String dsValoreNewCampo;
    private String dsValoreOldCampo;
    private BigDecimal idAsserzioneDeltaFoto;
    private BigDecimal idOggetto;
    private BigDecimal idOggettoEvento;
    private BigDecimal idRecord;
    private String idRecordPadre;
    private String labelCampo;
    private BigDecimal livello;
    private String nmTipoRecord;
    private String path;
    private String pathKey;
    private String tipoAsserzione;
    private String tipoValore;

    public LogVDeltaAsserzioni() {
    }

    @Lob
    @Column(name = "BL_VALORE_NEW_CAMPO")
    public String getBlValoreNewCampo() {
        return this.blValoreNewCampo;
    }

    public void setBlValoreNewCampo(String blValoreNewCampo) {
        this.blValoreNewCampo = blValoreNewCampo;
    }

    @Lob
    @Column(name = "BL_VALORE_OLD_CAMPO")
    public String getBlValoreOldCampo() {
        return this.blValoreOldCampo;
    }

    public void setBlValoreOldCampo(String blValoreOldCampo) {
        this.blValoreOldCampo = blValoreOldCampo;
    }

    @Column(name = "DS_CHIAVE_TIPO_RECORD")
    public String getDsChiaveTipoRecord() {
        return this.dsChiaveTipoRecord;
    }

    public void setDsChiaveTipoRecord(String dsChiaveTipoRecord) {
        this.dsChiaveTipoRecord = dsChiaveTipoRecord;
    }

    @Column(name = "DS_VALORE_NEW_CAMPO")
    public String getDsValoreNewCampo() {
        return this.dsValoreNewCampo;
    }

    public void setDsValoreNewCampo(String dsValoreNewCampo) {
        this.dsValoreNewCampo = dsValoreNewCampo;
    }

    @Column(name = "DS_VALORE_OLD_CAMPO")
    public String getDsValoreOldCampo() {
        return this.dsValoreOldCampo;
    }

    public void setDsValoreOldCampo(String dsValoreOldCampo) {
        this.dsValoreOldCampo = dsValoreOldCampo;
    }

    @Id
    @Column(name = "ID_ASSERZIONE_DELTA_FOTO")
    public BigDecimal getIdAsserzioneDeltaFoto() {
        return this.idAsserzioneDeltaFoto;
    }

    public void setIdAsserzioneDeltaFoto(BigDecimal idAsserzioneDeltaFoto) {
        this.idAsserzioneDeltaFoto = idAsserzioneDeltaFoto;
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

    @Column(name = "ID_RECORD")
    public BigDecimal getIdRecord() {
        return this.idRecord;
    }

    public void setIdRecord(BigDecimal idRecord) {
        this.idRecord = idRecord;
    }

    @Column(name = "ID_RECORD_PADRE")
    public String getIdRecordPadre() {
        return this.idRecordPadre;
    }

    public void setIdRecordPadre(String idRecordPadre) {
        this.idRecordPadre = idRecordPadre;
    }

    @Column(name = "LABEL_CAMPO")
    public String getLabelCampo() {
        return this.labelCampo;
    }

    public void setLabelCampo(String labelCampo) {
        this.labelCampo = labelCampo;
    }

    public BigDecimal getLivello() {
        return this.livello;
    }

    public void setLivello(BigDecimal livello) {
        this.livello = livello;
    }

    @Column(name = "NM_TIPO_RECORD")
    public String getNmTipoRecord() {
        return this.nmTipoRecord;
    }

    public void setNmTipoRecord(String nmTipoRecord) {
        this.nmTipoRecord = nmTipoRecord;
    }

    @Column(name = "\"PATH\"")
    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Column(name = "PATH_KEY")
    public String getPathKey() {
        return this.pathKey;
    }

    public void setPathKey(String pathKey) {
        this.pathKey = pathKey;
    }

    @Column(name = "TIPO_ASSERZIONE")
    public String getTipoAsserzione() {
        return this.tipoAsserzione;
    }

    public void setTipoAsserzione(String tipoAsserzione) {
        this.tipoAsserzione = tipoAsserzione;
    }

    @Column(name = "TIPO_VALORE")
    public String getTipoValore() {
        return this.tipoValore;
    }

    public void setTipoValore(String tipoValore) {
        this.tipoValore = tipoValore;
    }

}
