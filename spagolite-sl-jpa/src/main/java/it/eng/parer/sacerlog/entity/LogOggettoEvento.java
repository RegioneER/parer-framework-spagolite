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

package it.eng.parer.sacerlog.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the LOG_OGGETTO_EVENTO database table.
 *
 */
@Entity
@Table(schema = "SACER_LOG", name = "LOG_OGGETTO_EVENTO")
@NamedQueries({
	@NamedQuery(name = "LogOggettoEvento.findAll", query = "SELECT l FROM LogOggettoEvento l"),
	@NamedQuery(name = "LogOggettoEvento.deleteAll", query = "DELETE FROM LogOggettoEvento") })
public class LogOggettoEvento implements Serializable {

    private static final long serialVersionUID = 1L;
    private long idOggettoEvento;
    private String dsKeyOggetto;
    private BigDecimal idOggetto;
    private BigDecimal idTipoOggetto;
    private String tiRuoloOggettoEvento;
    private List<LogDeltaFoto> logDeltaFotos1;
    private List<LogDeltaFoto> logDeltaFotos2;
    private List<LogFotoOggettoEvento> logFotoOggettoEventos;
    private LogEvento logEvento;

    public LogOggettoEvento() {
    }

    @Id
    @SequenceGenerator(name = "LOG_OGGETTO_EVENTO_IDOGGETTOEVENTO_GENERATOR", schema = "SACER_LOG", sequenceName = "SLOG_OGGETTO_EVENTO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_OGGETTO_EVENTO_IDOGGETTOEVENTO_GENERATOR")
    @Column(name = "ID_OGGETTO_EVENTO")
    public long getIdOggettoEvento() {
	return this.idOggettoEvento;
    }

    public void setIdOggettoEvento(long idOggettoEvento) {
	this.idOggettoEvento = idOggettoEvento;
    }

    @Column(name = "DS_KEY_OGGETTO")
    public String getDsKeyOggetto() {
	return this.dsKeyOggetto;
    }

    public void setDsKeyOggetto(String dsKeyOggetto) {
	this.dsKeyOggetto = dsKeyOggetto;
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

    @Column(name = "TI_RUOLO_OGGETTO_EVENTO")
    public String getTiRuoloOggettoEvento() {
	return this.tiRuoloOggettoEvento;
    }

    public void setTiRuoloOggettoEvento(String tiRuoloOggettoEvento) {
	this.tiRuoloOggettoEvento = tiRuoloOggettoEvento;
    }

    // bi-directional many-to-one association to LogFotoOggettoEvento
    @OneToMany(mappedBy = "logOggettoEvento", cascade = CascadeType.PERSIST)
    public List<LogFotoOggettoEvento> getLogFotoOggettoEventos() {
	return this.logFotoOggettoEventos;
    }

    public void setLogFotoOggettoEventos(List<LogFotoOggettoEvento> logFotoOggettoEventos) {
	this.logFotoOggettoEventos = logFotoOggettoEventos;
    }

    public LogFotoOggettoEvento addLogFotoOggettoEvento(LogFotoOggettoEvento logFotoOggettoEvento) {
	getLogFotoOggettoEventos().add(logFotoOggettoEvento);
	logFotoOggettoEvento.setLogOggettoEvento(this);

	return logFotoOggettoEvento;
    }

    // bi-directional many-to-one association to LogDeltaFoto
    @OneToMany(mappedBy = "logOggettoEvento1", cascade = CascadeType.PERSIST)
    public List<LogDeltaFoto> getLogDeltaFotos1() {
	return this.logDeltaFotos1;
    }

    public void setLogDeltaFotos1(List<LogDeltaFoto> logDeltaFotos1) {
	this.logDeltaFotos1 = logDeltaFotos1;
    }

    public LogDeltaFoto addLogDeltaFotos1(LogDeltaFoto logDeltaFotos1) {
	getLogDeltaFotos1().add(logDeltaFotos1);
	logDeltaFotos1.setLogOggettoEvento1(this);

	return logDeltaFotos1;
    }

    public LogDeltaFoto removeLogDeltaFotos1(LogDeltaFoto logDeltaFotos1) {
	getLogDeltaFotos1().remove(logDeltaFotos1);
	logDeltaFotos1.setLogOggettoEvento1(null);

	return logDeltaFotos1;
    }

    // bi-directional many-to-one association to LogDeltaFoto
    @OneToMany(mappedBy = "logOggettoEvento2")
    public List<LogDeltaFoto> getLogDeltaFotos2() {
	return this.logDeltaFotos2;
    }

    public void setLogDeltaFotos2(List<LogDeltaFoto> logDeltaFotos2) {
	this.logDeltaFotos2 = logDeltaFotos2;
    }

    public LogDeltaFoto addLogDeltaFotos2(LogDeltaFoto logDeltaFotos2) {
	getLogDeltaFotos2().add(logDeltaFotos2);
	logDeltaFotos2.setLogOggettoEvento2(this);

	return logDeltaFotos2;
    }

    public LogDeltaFoto removeLogDeltaFotos2(LogDeltaFoto logDeltaFotos2) {
	getLogDeltaFotos2().remove(logDeltaFotos2);
	logDeltaFotos2.setLogOggettoEvento2(null);

	return logDeltaFotos2;
    }

    public LogFotoOggettoEvento removeLogFotoOggettoEvento(
	    LogFotoOggettoEvento logFotoOggettoEvento) {
	getLogFotoOggettoEventos().remove(logFotoOggettoEvento);
	logFotoOggettoEvento.setLogOggettoEvento(null);

	return logFotoOggettoEvento;
    }

    // bi-directional many-to-one association to LogEvento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EVENTO")
    public LogEvento getLogEvento() {
	return this.logEvento;
    }

    public void setLogEvento(LogEvento logEvento) {
	this.logEvento = logEvento;
    }

}
