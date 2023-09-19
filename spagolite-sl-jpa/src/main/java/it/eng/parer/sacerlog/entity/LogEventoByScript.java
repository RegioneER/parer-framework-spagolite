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
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the LOG_EVENTO_BY_SCRIPT database table.
 *
 */
@Entity
@Table(schema = "SACER_LOG", name = "LOG_EVENTO_BY_SCRIPT")
@NamedQueries({
        @NamedQuery(name = "LogEventoByScript.findByTipoOggettoId", query = "SELECT l FROM LogEventoByScript l WHERE l.idTipoOggetto = :idTipoOggetto AND l.idOggetto = :idOggetto"),
        @NamedQuery(name = "LogEventoByScript.deleteAll", query = "DELETE FROM LogEventoByScript")
/*
 * ,
 * 
 * @NamedQuery(name = "LogEventoByScript.findDistinctLogEventoScriptByIdApplic", query =
 * "SELECT DISTINCT l.idTipoOggetto, l.idOggetto FROM LogEventoByScript l WHERE l.idApplic = :idApplic")
 */
})
public class LogEventoByScript implements Serializable {

    private static final long serialVersionUID = 1L;
    private long idEventoByScript;
    private Timestamp dtRegEvento;
    private BigDecimal idAgente;
    private BigDecimal idAzioneCompSw;
    private BigDecimal idOggetto;
    private BigDecimal idTipoOggetto;
    private String tiRuoloAgenteEvento;
    private String tiRuoloOggettoEvento;
    private BigDecimal idApplic;
    private String dsMotivoScript;

    public LogEventoByScript() {
    }

    @Id
    @SequenceGenerator(name = "LOG_EVENTO_BY_SCRIPT_IDEVENTOBYSCRIPT_GENERATOR", schema = "SACER_LOG", sequenceName = "SLOG_EVENTO_BY_SCRIPT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_EVENTO_BY_SCRIPT_IDEVENTOBYSCRIPT_GENERATOR")
    @Column(name = "ID_EVENTO_BY_SCRIPT")
    public long getIdEventoByScript() {
        return this.idEventoByScript;
    }

    public void setIdEventoByScript(long idEventoByScript) {
        this.idEventoByScript = idEventoByScript;
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

    @Column(name = "ID_AZIONE_COMP_SW")
    public BigDecimal getIdAzioneCompSw() {
        return this.idAzioneCompSw;
    }

    public void setIdAzioneCompSw(BigDecimal idAzioneCompSw) {
        this.idAzioneCompSw = idAzioneCompSw;
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

    @Column(name = "ID_APPLIC")
    public BigDecimal getIdApplic() {
        return this.idApplic;
    }

    public void setIdApplic(BigDecimal idApplic) {
        this.idApplic = idApplic;
    }

    @Column(name = "DS_MOTIVO_SCRIPT")
    public String getDsMotivoScript() {
        return this.dsMotivoScript;
    }

    public void setDsMotivoScript(String dsMotivoScript) {
        this.dsMotivoScript = dsMotivoScript;
    }

}
