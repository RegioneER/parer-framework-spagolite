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
import java.util.Calendar;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the LOG_CHIAVE_ACCESSO_EVENTO database table.
 *
 */
@Entity
@Table(schema = "SACER_LOG", name = "LOG_CHIAVE_ACCESSO_EVENTO")
@NamedQueries({
	@NamedQuery(name = "LogChiaveAccessoEvento.findAll", query = "SELECT l FROM LogChiaveAccessoEvento l"),
	@NamedQuery(name = "LogChiaveAccessoEvento.deleteAll", query = "DELETE FROM LogChiaveAccessoEvento") })
public class LogChiaveAccessoEvento implements Serializable {

    private static final long serialVersionUID = 1L;
    private long idChiaveAccessoEvento;
    private BigDecimal idApplic;
    private BigDecimal idOggetto;
    private BigDecimal idTipoOggetto;
    private Calendar dtRegEvento;
    private LogEvento logEvento;

    public LogChiaveAccessoEvento() {
    }

    @Id
    @SequenceGenerator(name = "LOG_CHIAVE_ACCESSO_EVENTO_IDCHIAVEACCESSOEVENTO_GENERATOR", schema = "SACER_LOG", sequenceName = "SLOG_CHIAVE_ACCESSO_EVENTO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_CHIAVE_ACCESSO_EVENTO_IDCHIAVEACCESSOEVENTO_GENERATOR")
    @Column(name = "ID_CHIAVE_ACCESSO_EVENTO")
    public long getIdChiaveAccessoEvento() {
	return this.idChiaveAccessoEvento;
    }

    public void setIdChiaveAccessoEvento(long idChiaveAccessoEvento) {
	this.idChiaveAccessoEvento = idChiaveAccessoEvento;
    }

    @Column(name = "ID_APPLIC")
    public BigDecimal getIdApplic() {
	return this.idApplic;
    }

    public void setIdApplic(BigDecimal idApplic) {
	this.idApplic = idApplic;
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

    // @org.eclipse.persistence.annotations.Convert("ORACLE_DATE")
    @Column(name = "DT_REG_EVENTO")
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar getDtRegEvento() {
	return this.dtRegEvento;
    }

    public void setDtRegEvento(Calendar dtRegEvento) {
	this.dtRegEvento = dtRegEvento;
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
