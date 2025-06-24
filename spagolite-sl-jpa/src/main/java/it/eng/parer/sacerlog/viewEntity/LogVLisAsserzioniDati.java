/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package it.eng.parer.sacerlog.viewEntity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the LOG_V_LIS_ASSERZIONI_DATI database table.
 *
 */
@Entity
@Table(name = "LOG_V_LIS_ASSERZIONI_DATI", schema = "SACER_LOG")
@NamedQueries({ @NamedQuery(name = "LogVLisAsserzioniDati.findAll", query = "SELECT l FROM LogVLisAsserzioniDati l"),
        @NamedQuery(name = "LogVLisAsserzioniDati.findByIdOggettoEvento", query = "SELECT l FROM LogVLisAsserzioniDati l WHERE l.idOggettoEvento = :idOggettoEvento") })
public class LogVLisAsserzioniDati implements Serializable {

    private static final long serialVersionUID = 1L;
    private String blValoreNewCampo;
    private String blValoreOldCampo;
    private String dsChiaveTipoRecord;
    private String dsModifica;
    private String dsValoreNewCampo;
    private String dsValoreOldCampo;
    private BigDecimal idAsserzioneDeltaFoto;
    private BigDecimal idOggettoEvento;
    private BigDecimal idRecord;
    private String idRecordPadre;
    private String labelCampo;
    private String nmTipoRecord;
    private String path;
    private String pathKey;
    private String tipoModifica;
    private String tipoValore;

    public LogVLisAsserzioniDati() {
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

    @Column(name = "DS_MODIFICA")
    public String getDsModifica() {
        return this.dsModifica;
    }

    public void setDsModifica(String dsModifica) {
        this.dsModifica = dsModifica;
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

    @Column(name = "TIPO_MODIFICA")
    public String getTipoModifica() {
        return this.tipoModifica;
    }

    public void setTipoModifica(String tipoModifica) {
        this.tipoModifica = tipoModifica;
    }

    @Column(name = "TIPO_VALORE")
    public String getTipoValore() {
        return this.tipoValore;
    }

    public void setTipoValore(String tipoValore) {
        this.tipoValore = tipoValore;
    }

}
