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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the APL_V_LOG_TI_OGG database table.
 *
 */
@Entity
@Table(name = "APL_V_LOG_TI_OGG", schema = "SACER_IAM")
@NamedQueries({ @NamedQuery(name = "AplVLogTiOgg.findAll", query = "SELECT a FROM AplVLogTiOgg a"),
        @NamedQuery(name = "AplVLogTiOgg.findTipoOggettoByNome", query = "SELECT a FROM AplVLogTiOgg a WHERE a.nmApplic = :nmApplic AND a.nmTipoOggetto = :nmTipoOggetto"),
        @NamedQuery(name = "AplVLogTiOgg.findTipoOggettoById", query = "SELECT a FROM AplVLogTiOgg a WHERE a.idTipoOggetto = :idTipoOggetto"),
        @NamedQuery(name = "AplVLogTiOgg.findByAppName", query = "SELECT a FROM AplVLogTiOgg a WHERE a.nmApplic = :nmApplic ORDER BY a.nmTipoOggetto"),
        @NamedQuery(name = "AplVLogTiOgg.findByAppNameExcludingTipoOggetto", query = "SELECT a FROM AplVLogTiOgg a WHERE a.nmApplic = :nmApplic AND a.nmTipoOggetto != :nmTipoOggetto ORDER BY a.nmTipoOggetto") })
public class AplVLogTiOgg implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigDecimal idApplic;
    private BigDecimal idTipoOggetto;
    private BigDecimal idTipoOggettoPadre;
    private String nmApplic;
    private String nmTipoOggetto;

    public AplVLogTiOgg() {
    }

    @Column(name = "ID_APPLIC")
    public BigDecimal getIdApplic() {
        return this.idApplic;
    }

    public void setIdApplic(BigDecimal idApplic) {
        this.idApplic = idApplic;
    }

    @Id
    @Column(name = "ID_TIPO_OGGETTO")
    public BigDecimal getIdTipoOggetto() {
        return this.idTipoOggetto;
    }

    public void setIdTipoOggetto(BigDecimal idTipoOggetto) {
        this.idTipoOggetto = idTipoOggetto;
    }

    @Column(name = "ID_TIPO_OGGETTO_PADRE")
    public BigDecimal getIdTipoOggettoPadre() {
        return this.idTipoOggettoPadre;
    }

    public void setIdTipoOggettoPadre(BigDecimal idTipoOggettoPadre) {
        this.idTipoOggettoPadre = idTipoOggettoPadre;
    }

    @Column(name = "NM_APPLIC")
    public String getNmApplic() {
        return this.nmApplic;
    }

    public void setNmApplic(String nmApplic) {
        this.nmApplic = nmApplic;
    }

    @Column(name = "NM_TIPO_OGGETTO")
    public String getNmTipoOggetto() {
        return this.nmTipoOggetto;
    }

    public void setNmTipoOggetto(String nmTipoOggetto) {
        this.nmTipoOggetto = nmTipoOggetto;
    }

}
