/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna <p/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version. <p/> This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. <p/> You should
 * have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
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
 * The persistent class for the APL_V_LOG_TRIG_TI_EVN_OGG database table.
 *
 */
@Entity
@Table(name = "APL_V_LOG_TRIG_TI_EVN_OGG", schema = "SACER_IAM")
@NamedQueries({
	@NamedQuery(name = "AplVLogTrigTiEvnOgg.findAll", query = "SELECT a FROM AplVLogTrigTiEvnOgg a"),
	@NamedQuery(name = "AplVLogTrigTiEvnOgg.findTrigger", query = "SELECT a FROM AplVLogTrigTiEvnOgg a WHERE a.idTipoEventoOggetto = :idTipoEventoOggetto AND a.idQueryTipoOggettoTrig IS NOT NULL"),
	@NamedQuery(name = "AplVLogTrigTiEvnOgg.findTriggerBefore", query = "SELECT a FROM AplVLogTrigTiEvnOgg a WHERE a.idTipoEventoOggetto = :idTipoEventoOggetto AND a.idQueryTipoOggettoTrig IS NULL") })
public class AplVLogTrigTiEvnOgg implements Serializable {

    private static final long serialVersionUID = 1L;
    private String blQueryTipoOggettoSel;
    private String blQueryTipoOggettoTrig;
    private BigDecimal idQueryTipoOggettoSel;
    private BigDecimal idQueryTipoOggettoTrig;
    private BigDecimal idTipoEvento;
    private BigDecimal idTipoEventoOggetto;
    private BigDecimal idTipoEventoOggettoTrig;
    private BigDecimal idTipoEventoTrig;
    private BigDecimal idTipoOggetto;
    private BigDecimal idTipoOggettoTrig;
    private String nmQueryTipoOggettoSel;
    private String nmQueryTipoOggettoTrig;
    private String nmTipoEventoTrig;
    private String nmTipoOggettoTrig;
    private String tipoClasseEventoTrig;
    private String nmApplicTrig;

    public AplVLogTrigTiEvnOgg() {
    }

    @Lob
    @Column(name = "BL_QUERY_TIPO_OGGETTO_SEL")
    public String getBlQueryTipoOggettoSel() {
	return this.blQueryTipoOggettoSel;
    }

    public void setBlQueryTipoOggettoSel(String blQueryTipoOggettoSel) {
	this.blQueryTipoOggettoSel = blQueryTipoOggettoSel;
    }

    @Lob
    @Column(name = "BL_QUERY_TIPO_OGGETTO_TRIG")
    public String getBlQueryTipoOggettoTrig() {
	return this.blQueryTipoOggettoTrig;
    }

    public void setBlQueryTipoOggettoTrig(String blQueryTipoOggettoTrig) {
	this.blQueryTipoOggettoTrig = blQueryTipoOggettoTrig;
    }

    @Column(name = "ID_QUERY_TIPO_OGGETTO_SEL")
    public BigDecimal getIdQueryTipoOggettoSel() {
	return this.idQueryTipoOggettoSel;
    }

    public void setIdQueryTipoOggettoSel(BigDecimal idQueryTipoOggettoSel) {
	this.idQueryTipoOggettoSel = idQueryTipoOggettoSel;
    }

    @Column(name = "ID_QUERY_TIPO_OGGETTO_TRIG")
    public BigDecimal getIdQueryTipoOggettoTrig() {
	return this.idQueryTipoOggettoTrig;
    }

    public void setIdQueryTipoOggettoTrig(BigDecimal idQueryTipoOggettoTrig) {
	this.idQueryTipoOggettoTrig = idQueryTipoOggettoTrig;
    }

    @Column(name = "ID_TIPO_EVENTO")
    public BigDecimal getIdTipoEvento() {
	return this.idTipoEvento;
    }

    public void setIdTipoEvento(BigDecimal idTipoEvento) {
	this.idTipoEvento = idTipoEvento;
    }

    @Column(name = "ID_TIPO_EVENTO_OGGETTO")
    public BigDecimal getIdTipoEventoOggetto() {
	return this.idTipoEventoOggetto;
    }

    public void setIdTipoEventoOggetto(BigDecimal idTipoEventoOggetto) {
	this.idTipoEventoOggetto = idTipoEventoOggetto;
    }

    @Id
    @Column(name = "ID_TIPO_EVENTO_OGGETTO_TRIG")
    public BigDecimal getIdTipoEventoOggettoTrig() {
	return this.idTipoEventoOggettoTrig;
    }

    public void setIdTipoEventoOggettoTrig(BigDecimal idTipoEventoOggettoTrig) {
	this.idTipoEventoOggettoTrig = idTipoEventoOggettoTrig;
    }

    @Column(name = "ID_TIPO_EVENTO_TRIG")
    public BigDecimal getIdTipoEventoTrig() {
	return this.idTipoEventoTrig;
    }

    public void setIdTipoEventoTrig(BigDecimal idTipoEventoTrig) {
	this.idTipoEventoTrig = idTipoEventoTrig;
    }

    @Column(name = "ID_TIPO_OGGETTO")
    public BigDecimal getIdTipoOggetto() {
	return this.idTipoOggetto;
    }

    public void setIdTipoOggetto(BigDecimal idTipoOggetto) {
	this.idTipoOggetto = idTipoOggetto;
    }

    @Column(name = "ID_TIPO_OGGETTO_TRIG")
    public BigDecimal getIdTipoOggettoTrig() {
	return this.idTipoOggettoTrig;
    }

    public void setIdTipoOggettoTrig(BigDecimal idTipoOggettoTrig) {
	this.idTipoOggettoTrig = idTipoOggettoTrig;
    }

    @Column(name = "NM_QUERY_TIPO_OGGETTO_SEL")
    public String getNmQueryTipoOggettoSel() {
	return this.nmQueryTipoOggettoSel;
    }

    public void setNmQueryTipoOggettoSel(String nmQueryTipoOggettoSel) {
	this.nmQueryTipoOggettoSel = nmQueryTipoOggettoSel;
    }

    @Column(name = "NM_QUERY_TIPO_OGGETTO_TRIG")
    public String getNmQueryTipoOggettoTrig() {
	return this.nmQueryTipoOggettoTrig;
    }

    public void setNmQueryTipoOggettoTrig(String nmQueryTipoOggettoTrig) {
	this.nmQueryTipoOggettoTrig = nmQueryTipoOggettoTrig;
    }

    @Column(name = "NM_TIPO_EVENTO_TRIG")
    public String getNmTipoEventoTrig() {
	return this.nmTipoEventoTrig;
    }

    public void setNmTipoEventoTrig(String nmTipoEventoTrig) {
	this.nmTipoEventoTrig = nmTipoEventoTrig;
    }

    @Column(name = "NM_TIPO_OGGETTO_TRIG")
    public String getNmTipoOggettoTrig() {
	return this.nmTipoOggettoTrig;
    }

    public void setNmTipoOggettoTrig(String nmTipoOggettoTrig) {
	this.nmTipoOggettoTrig = nmTipoOggettoTrig;
    }

    @Column(name = "TIPO_CLASSE_EVENTO_TRIG")
    public String getTipoClasseEventoTrig() {
	return this.tipoClasseEventoTrig;
    }

    public void setTipoClasseEventoTrig(String tipoClasseEventoTrig) {
	this.tipoClasseEventoTrig = tipoClasseEventoTrig;
    }

    @Column(name = "NM_APPLIC_TRIG")
    public String getNmApplicTrig() {
	return this.nmApplicTrig;
    }

    public void setNmApplicTrig(String nmApplicTrig) {
	this.nmApplicTrig = nmApplicTrig;
    }

}
