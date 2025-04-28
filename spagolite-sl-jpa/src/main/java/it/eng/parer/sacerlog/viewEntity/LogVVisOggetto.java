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
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the LOG_V_VIS_OGGETTO database table.
 *
 */
@Entity
@Table(name = "LOG_V_VIS_OGGETTO", schema = "SACER_LOG")
@NamedQueries({
	@NamedQuery(name = "LogVVisOggetto.findAll", query = "SELECT l FROM LogVVisOggetto l"),
	@NamedQuery(name = "LogVVisOggetto.findByAppTipoOggettoId", query = "SELECT l FROM LogVVisOggetto l WHERE l.nmApplic = :nmApplic AND l.nmTipoOggetto = :nmTipoOggetto AND l.idOggetto = :idOggetto") })
public class LogVVisOggetto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String dsKeyOggetto;
    private Timestamp dtRegEvento;
    private BigDecimal idApplic;
    private BigDecimal idEvento;
    private BigDecimal idOggetto;
    private BigDecimal idOggettoEvento;
    private BigDecimal idTipoOggetto;
    private String nmAmbiente;
    private String nmApplic;
    private String nmEnte;
    private String nmOggetto;
    private String nmStruttura;
    private String nmTipoOggetto;
    private String nmVersatore;

    public LogVVisOggetto() {
    }

    @Column(name = "DS_KEY_OGGETTO")
    public String getDsKeyOggetto() {
	return this.dsKeyOggetto;
    }

    public void setDsKeyOggetto(String dsKeyOggetto) {
	this.dsKeyOggetto = dsKeyOggetto;
    }

    @Column(name = "DT_REG_EVENTO")
    public Timestamp getDtRegEvento() {
	return this.dtRegEvento;
    }

    public void setDtRegEvento(Timestamp dtRegEvento) {
	this.dtRegEvento = dtRegEvento;
    }

    @Column(name = "ID_APPLIC")
    public BigDecimal getIdApplic() {
	return this.idApplic;
    }

    public void setIdApplic(BigDecimal idApplic) {
	this.idApplic = idApplic;
    }

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

    @Id
    @Column(name = "ID_OGGETTO_EVENTO")
    public BigDecimal getIdOggettoEvento() {
	return this.idOggettoEvento;
    }

    public void setIdOggettoEvento(BigDecimal idOggettoEvento) {
	this.idOggettoEvento = idOggettoEvento;
    }

    @Column(name = "ID_TIPO_OGGETTO")
    public BigDecimal getIdTipoOggetto() {
	return this.idTipoOggetto;
    }

    public void setIdTipoOggetto(BigDecimal idTipoOggetto) {
	this.idTipoOggetto = idTipoOggetto;
    }

    @Column(name = "NM_AMBIENTE")
    public String getNmAmbiente() {
	return this.nmAmbiente;
    }

    public void setNmAmbiente(String nmAmbiente) {
	this.nmAmbiente = nmAmbiente;
    }

    @Column(name = "NM_APPLIC")
    public String getNmApplic() {
	return this.nmApplic;
    }

    public void setNmApplic(String nmApplic) {
	this.nmApplic = nmApplic;
    }

    @Column(name = "NM_ENTE")
    public String getNmEnte() {
	return this.nmEnte;
    }

    public void setNmEnte(String nmEnte) {
	this.nmEnte = nmEnte;
    }

    @Column(name = "NM_OGGETTO")
    public String getNmOggetto() {
	return this.nmOggetto;
    }

    public void setNmOggetto(String nmOggetto) {
	this.nmOggetto = nmOggetto;
    }

    @Column(name = "NM_STRUTTURA")
    public String getNmStruttura() {
	return this.nmStruttura;
    }

    public void setNmStruttura(String nmStruttura) {
	this.nmStruttura = nmStruttura;
    }

    @Column(name = "NM_TIPO_OGGETTO")
    public String getNmTipoOggetto() {
	return this.nmTipoOggetto;
    }

    public void setNmTipoOggetto(String nmTipoOggetto) {
	this.nmTipoOggetto = nmTipoOggetto;
    }

    @Column(name = "NM_VERSATORE")
    public String getNmVersatore() {
	return this.nmVersatore;
    }

    public void setNmVersatore(String nmVersatore) {
	this.nmVersatore = nmVersatore;
    }

}
