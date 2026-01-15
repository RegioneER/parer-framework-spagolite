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
 * The persistent class for the APL_V_LOG_CHIAVE_TI_OGG database table.
 *
 */
@Entity
@Table(name = "APL_V_LOG_CHIAVE_TI_OGG", schema = "SACER_IAM")
@NamedQueries({
        @NamedQuery(name = "AplVLogChiaveTiOgg.findAll", query = "SELECT a FROM AplVLogChiaveTiOgg a"),
        @NamedQuery(name = "AplVLogChiaveTiOgg.findByIdTipoOggetto", query = "SELECT a FROM AplVLogChiaveTiOgg a WHERE a.idTipoOggetto = :idTipoOggetto") })
public class AplVLogChiaveTiOgg implements Serializable {

    private static final long serialVersionUID = 1L;
    private String blQueryTipoOggetto;
    private BigDecimal idQueryTipoOggetto;
    private BigDecimal idTipoOggetto;
    private BigDecimal idTipoOggettoChiave;
    private String nmQueryTipoOggetto;
    private String nmTipoOggettoChiave;

    public AplVLogChiaveTiOgg() {
    }

    @Lob
    @Column(name = "BL_QUERY_TIPO_OGGETTO")
    public String getBlQueryTipoOggetto() {
        return this.blQueryTipoOggetto;
    }

    public void setBlQueryTipoOggetto(String blQueryTipoOggetto) {
        this.blQueryTipoOggetto = blQueryTipoOggetto;
    }

    @Id
    @Column(name = "ID_QUERY_TIPO_OGGETTO")
    public BigDecimal getIdQueryTipoOggetto() {
        return this.idQueryTipoOggetto;
    }

    public void setIdQueryTipoOggetto(BigDecimal idQueryTipoOggetto) {
        this.idQueryTipoOggetto = idQueryTipoOggetto;
    }

    @Column(name = "ID_TIPO_OGGETTO")
    public BigDecimal getIdTipoOggetto() {
        return this.idTipoOggetto;
    }

    public void setIdTipoOggetto(BigDecimal idTipoOggetto) {
        this.idTipoOggetto = idTipoOggetto;
    }

    @Column(name = "ID_TIPO_OGGETTO_CHIAVE")
    public BigDecimal getIdTipoOggettoChiave() {
        return this.idTipoOggettoChiave;
    }

    public void setIdTipoOggettoChiave(BigDecimal idTipoOggettoChiave) {
        this.idTipoOggettoChiave = idTipoOggettoChiave;
    }

    @Column(name = "NM_QUERY_TIPO_OGGETTO")
    public String getNmQueryTipoOggetto() {
        return this.nmQueryTipoOggetto;
    }

    public void setNmQueryTipoOggetto(String nmQueryTipoOggetto) {
        this.nmQueryTipoOggetto = nmQueryTipoOggetto;
    }

    @Column(name = "NM_TIPO_OGGETTO_CHIAVE")
    public String getNmTipoOggettoChiave() {
        return this.nmTipoOggettoChiave;
    }

    public void setNmTipoOggettoChiave(String nmTipoOggettoChiave) {
        this.nmTipoOggettoChiave = nmTipoOggettoChiave;
    }

}
