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
 * The persistent class for the APL_V_LOG_FOTO_TI_EVN_OGG database table.
 *
 */
@Entity
@Table(name = "APL_V_LOG_FOTO_TI_EVN_OGG", schema = "SACER_IAM")
@NamedQueries({
	@NamedQuery(name = "AplVLogFotoTiEvnOgg.findAll", query = "SELECT a FROM AplVLogFotoTiEvnOgg a"),
	@NamedQuery(name = "AplVLogFotoTiEvnOgg.findByEventoOggetto", query = "SELECT a FROM AplVLogFotoTiEvnOgg a WHERE a.idTipoEvento = :idTipoEvento AND a.idTipoOggetto = :idTipoOggetto") })
public class AplVLogFotoTiEvnOgg implements Serializable {

    private static final long serialVersionUID = 1L;
    private String blQueryTipoOggetto;
    private BigDecimal idQueryTipoOggetto;
    private BigDecimal idTipoEvento;
    private BigDecimal idTipoEventoOggetto;
    private BigDecimal idTipoOggetto;
    private String nmQueryTipoOggetto;
    private String tipoUsoQuery;

    public AplVLogFotoTiEvnOgg() {
    }

    @Lob
    @Column(name = "BL_QUERY_TIPO_OGGETTO")
    public String getBlQueryTipoOggetto() {
	return this.blQueryTipoOggetto;
    }

    public void setBlQueryTipoOggetto(String blQueryTipoOggetto) {
	this.blQueryTipoOggetto = blQueryTipoOggetto;
    }

    @Column(name = "ID_QUERY_TIPO_OGGETTO")
    public BigDecimal getIdQueryTipoOggetto() {
	return this.idQueryTipoOggetto;
    }

    public void setIdQueryTipoOggetto(BigDecimal idQueryTipoOggetto) {
	this.idQueryTipoOggetto = idQueryTipoOggetto;
    }

    @Column(name = "ID_TIPO_EVENTO")
    public BigDecimal getIdTipoEvento() {
	return this.idTipoEvento;
    }

    public void setIdTipoEvento(BigDecimal idTipoEvento) {
	this.idTipoEvento = idTipoEvento;
    }

    @Id
    @Column(name = "ID_TIPO_EVENTO_OGGETTO")
    public BigDecimal getIdTipoEventoOggetto() {
	return this.idTipoEventoOggetto;
    }

    public void setIdTipoEventoOggetto(BigDecimal idTipoEventoOggetto) {
	this.idTipoEventoOggetto = idTipoEventoOggetto;
    }

    @Column(name = "ID_TIPO_OGGETTO")
    public BigDecimal getIdTipoOggetto() {
	return this.idTipoOggetto;
    }

    public void setIdTipoOggetto(BigDecimal idTipoOggetto) {
	this.idTipoOggetto = idTipoOggetto;
    }

    @Column(name = "NM_QUERY_TIPO_OGGETTO")
    public String getNmQueryTipoOggetto() {
	return this.nmQueryTipoOggetto;
    }

    public void setNmQueryTipoOggetto(String nmQueryTipoOggetto) {
	this.nmQueryTipoOggetto = nmQueryTipoOggetto;
    }

    @Column(name = "TIPO_USO_QUERY")
    public String getTipoUsoQuery() {
	return this.tipoUsoQuery;
    }

    public void setTipoUsoQuery(String tipoUsoQuery) {
	this.tipoUsoQuery = tipoUsoQuery;
    }

}
