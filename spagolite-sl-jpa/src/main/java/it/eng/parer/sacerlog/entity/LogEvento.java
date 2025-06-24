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

package it.eng.parer.sacerlog.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the LOG_EVENTO database table.
 *
 */
@Entity
@Table(schema = "SACER_LOG", name = "LOG_EVENTO")
@NamedQueries({ @NamedQuery(name = "LogEvento.findAll", query = "SELECT l FROM LogEvento l"),
        @NamedQuery(name = "LogEvento.deleteAll", query = "DELETE FROM LogEvento") })
public class LogEvento implements Serializable {

    private static final long serialVersionUID = 1L;
    private long idEvento;
    private Calendar dtRegEvento;
    private java.math.BigDecimal idApplic;
    private java.math.BigDecimal idTipoEvento;
    private String nmAzione;
    private String nmGeneratoreAzione;
    private String tipoAzione;
    private java.math.BigDecimal idTransazione;
    private String dsMotivoScript;
    private List<LogAgenteEvento> logAgenteEventos;
    private List<LogChiaveAccessoEvento> logChiaveAccessoEventos;
    private List<LogOggettoEvento> logOggettoEventos;

    public LogEvento() {
    }

    @Id
    @SequenceGenerator(name = "LOG_EVENTO_IDEVENTO_GENERATOR", schema = "SACER_LOG", sequenceName = "SLOG_EVENTO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_EVENTO_IDEVENTO_GENERATOR")
    @Column(name = "ID_EVENTO")
    public long getIdEvento() {
        return this.idEvento;
    }

    public void setIdEvento(long idEvento) {
        this.idEvento = idEvento;
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

    @Column(name = "ID_APPLIC")
    public java.math.BigDecimal getIdApplic() {
        return this.idApplic;
    }

    public void setIdApplic(java.math.BigDecimal idApplic) {
        this.idApplic = idApplic;
    }

    @Column(name = "ID_TIPO_EVENTO")
    public java.math.BigDecimal getIdTipoEvento() {
        return this.idTipoEvento;
    }

    public void setIdTipoEvento(java.math.BigDecimal idTipoEvento) {
        this.idTipoEvento = idTipoEvento;
    }

    @Column(name = "NM_AZIONE")
    public String getNmAzione() {
        return this.nmAzione;
    }

    public void setNmAzione(String nmAzione) {
        this.nmAzione = nmAzione;
    }

    @Column(name = "NM_GENERATORE_AZIONE")
    public String getNmGeneratoreAzione() {
        return this.nmGeneratoreAzione;
    }

    public void setNmGeneratoreAzione(String nmGeneratoreAzione) {
        this.nmGeneratoreAzione = nmGeneratoreAzione;
    }

    @Column(name = "TIPO_AZIONE")
    public String getTipoAzione() {
        return this.tipoAzione;
    }

    public void setTipoAzione(String tipoAzione) {
        this.tipoAzione = tipoAzione;
    }

    @Column(name = "ID_TRANSAZIONE")
    public java.math.BigDecimal getIdTransazione() {
        return this.idTransazione;
    }

    public void setIdTransazione(java.math.BigDecimal idTransazione) {
        this.idTransazione = idTransazione;
    }

    @Column(name = "DS_MOTIVO_SCRIPT")
    public String getDsMotivoScript() {
        return this.dsMotivoScript;
    }

    public void setDsMotivoScript(String dsMotivoScript) {
        this.dsMotivoScript = dsMotivoScript;
    }

    // bi-directional many-to-one association to LogAgenteEvento
    @OneToMany(mappedBy = "logEvento", cascade = CascadeType.PERSIST)
    public List<LogAgenteEvento> getLogAgenteEventos() {
        return this.logAgenteEventos;
    }

    public void setLogAgenteEventos(List<LogAgenteEvento> logAgenteEventos) {
        this.logAgenteEventos = logAgenteEventos;
    }

    public LogAgenteEvento addLogAgenteEvento(LogAgenteEvento logAgenteEvento) {
        getLogAgenteEventos().add(logAgenteEvento);
        logAgenteEvento.setLogEvento(this);

        return logAgenteEvento;
    }

    public LogAgenteEvento removeLogAgenteEvento(LogAgenteEvento logAgenteEvento) {
        getLogAgenteEventos().remove(logAgenteEvento);
        logAgenteEvento.setLogEvento(null);

        return logAgenteEvento;
    }

    // bi-directional many-to-one association to LogChiaveAccessoEvento
    @OneToMany(mappedBy = "logEvento", cascade = CascadeType.PERSIST)
    public List<LogChiaveAccessoEvento> getLogChiaveAccessoEventos() {
        return this.logChiaveAccessoEventos;
    }

    public void setLogChiaveAccessoEventos(List<LogChiaveAccessoEvento> logChiaveAccessoEventos) {
        this.logChiaveAccessoEventos = logChiaveAccessoEventos;
    }

    public LogChiaveAccessoEvento addLogChiaveAccessoEvento(LogChiaveAccessoEvento logChiaveAccessoEvento) {
        getLogChiaveAccessoEventos().add(logChiaveAccessoEvento);
        logChiaveAccessoEvento.setLogEvento(this);

        return logChiaveAccessoEvento;
    }

    public LogChiaveAccessoEvento removeLogChiaveAccessoEvento(LogChiaveAccessoEvento logChiaveAccessoEvento) {
        getLogChiaveAccessoEventos().remove(logChiaveAccessoEvento);
        logChiaveAccessoEvento.setLogEvento(null);

        return logChiaveAccessoEvento;
    }

    // bi-directional many-to-one association to LogOggettoEvento
    @OneToMany(mappedBy = "logEvento", cascade = CascadeType.PERSIST)
    public List<LogOggettoEvento> getLogOggettoEventos() {
        return this.logOggettoEventos;
    }

    public void setLogOggettoEventos(List<LogOggettoEvento> logOggettoEventos) {
        this.logOggettoEventos = logOggettoEventos;
    }

    public LogOggettoEvento addLogOggettoEvento(LogOggettoEvento logOggettoEvento) {
        getLogOggettoEventos().add(logOggettoEvento);
        logOggettoEvento.setLogEvento(this);

        return logOggettoEvento;
    }

    public LogOggettoEvento removeLogOggettoEvento(LogOggettoEvento logOggettoEvento) {
        getLogOggettoEventos().remove(logOggettoEvento);
        logOggettoEvento.setLogEvento(null);

        return logOggettoEvento;
    }

}
