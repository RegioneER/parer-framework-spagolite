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
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the LOG_V_VIS_EVENTO_OGGETTO database table.
 *
 */
@Entity
@Table(name = "LOG_V_VIS_EVENTO_OGGETTO", schema = "SACER_LOG")
@NamedQuery(name = "LogVVisEventoOggetto.findAll", query = "SELECT l FROM LogVVisEventoOggetto l")
public class LogVVisEventoOggetto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String dsAzione;
    private String dsGeneratoreAzione;
    private Timestamp dtRegEvento;
    private BigDecimal idAgente;
    private BigDecimal idAgenteEvento;
    private BigDecimal idEvento;
    private BigDecimal idOggettoEvento;
    private BigDecimal idTipoEvento;
    private BigDecimal idTipoOggetto;
    private String nmAgente;
    private String nmAmbiente;
    private String nmEnte;
    private String nmOggetto;
    private String nmStruttura;
    private String nmTipoEvento;
    private String nmTipoOggetto;
    private String tiRuoloAgenteEvento;
    private String tiRuoloOggettoEvento;
    private String tipoClasseEvento;
    private String tipoOrigineAgente;
    private String tipoOrigineEvento;
    private BigDecimal idTransazione;

    public LogVVisEventoOggetto() {
    }

    @Column(name = "DS_AZIONE")
    public String getDsAzione() {
        return this.dsAzione;
    }

    public void setDsAzione(String dsAzione) {
        this.dsAzione = dsAzione;
    }

    @Column(name = "DS_GENERATORE_AZIONE")
    public String getDsGeneratoreAzione() {
        return this.dsGeneratoreAzione;
    }

    public void setDsGeneratoreAzione(String dsGeneratoreAzione) {
        this.dsGeneratoreAzione = dsGeneratoreAzione;
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

    @Column(name = "ID_AGENTE_EVENTO")
    public BigDecimal getIdAgenteEvento() {
        return this.idAgenteEvento;
    }

    public void setIdAgenteEvento(BigDecimal idAgenteEvento) {
        this.idAgenteEvento = idAgenteEvento;
    }

    @Column(name = "ID_EVENTO")
    public BigDecimal getIdEvento() {
        return this.idEvento;
    }

    public void setIdEvento(BigDecimal idEvento) {
        this.idEvento = idEvento;
    }

    @Id
    @Column(name = "ID_OGGETTO_EVENTO")
    public BigDecimal getIdOggettoEvento() {
        return this.idOggettoEvento;
    }

    public void setIdOggettoEvento(BigDecimal idOggettoEvento) {
        this.idOggettoEvento = idOggettoEvento;
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

    @Column(name = "NM_AMBIENTE")
    public String getNmAmbiente() {
        return this.nmAmbiente;
    }

    public void setNmAmbiente(String nmAmbiente) {
        this.nmAmbiente = nmAmbiente;
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

    @Column(name = "TIPO_CLASSE_EVENTO")
    public String getTipoClasseEvento() {
        return this.tipoClasseEvento;
    }

    public void setTipoClasseEvento(String tipoClasseEvento) {
        this.tipoClasseEvento = tipoClasseEvento;
    }

    @Column(name = "TIPO_ORIGINE_AGENTE")
    public String getTipoOrigineAgente() {
        return this.tipoOrigineAgente;
    }

    public void setTipoOrigineAgente(String tipoOrigineAgente) {
        this.tipoOrigineAgente = tipoOrigineAgente;
    }

    @Column(name = "TIPO_ORIGINE_EVENTO")
    public String getTipoOrigineEvento() {
        return this.tipoOrigineEvento;
    }

    public void setTipoOrigineEvento(String tipoOrigineEvento) {
        this.tipoOrigineEvento = tipoOrigineEvento;
    }

    @Column(name = "ID_TRANSAZIONE")
    public BigDecimal getIdTransazione() {
        return this.idTransazione;
    }

    public void setIdTransazione(BigDecimal idTransazione) {
        this.idTransazione = idTransazione;
    }

}
