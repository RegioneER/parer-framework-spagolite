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
 * The persistent class for the LOG_V_LOG_AGENTE database table.
 *
 */
@Entity
@Table(name = "LOG_V_LOG_AGENTE", schema = "SACER_IAM")
@NamedQueries({ @NamedQuery(name = "LogVLogAgente.findAll", query = "SELECT l FROM LogVLogAgente l"),
        @NamedQuery(name = "LogVLogAgente.getByNomeAgente", query = "SELECT l FROM LogVLogAgente l WHERE l.nmAgente = :nomeAgente"),
        @NamedQuery(name = "LogVLogAgente.getByIdAgente", query = "SELECT l FROM LogVLogAgente l WHERE l.idAgente = :idAgente"),
        @NamedQuery(name = "LogVLogAgente.getByNomeCompSoftware", query = "SELECT l FROM LogVLogAgente l WHERE l.nmNomeUserCompSw = :nmNomeUserCompSw") })
public class LogVLogAgente implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigDecimal idAgente;
    private BigDecimal idUserCompSw;
    private String nmAgente;
    private String nmCognomeUserApplic;
    private String nmNomeUserCompSw;
    private String tipoAgentePremis;
    private String tipoOrigineAgente;

    public LogVLogAgente() {
    }

    @Id
    @Column(name = "ID_AGENTE")
    public BigDecimal getIdAgente() {
        return this.idAgente;
    }

    public void setIdAgente(BigDecimal idAgente) {
        this.idAgente = idAgente;
    }

    @Column(name = "ID_USER_COMP_SW")
    public BigDecimal getIdUserCompSw() {
        return this.idUserCompSw;
    }

    public void setIdUserCompSw(BigDecimal idUserCompSw) {
        this.idUserCompSw = idUserCompSw;
    }

    @Column(name = "NM_AGENTE")
    public String getNmAgente() {
        return this.nmAgente;
    }

    public void setNmAgente(String nmAgente) {
        this.nmAgente = nmAgente;
    }

    @Column(name = "NM_COGNOME_USER_APPLIC")
    public String getNmCognomeUserApplic() {
        return this.nmCognomeUserApplic;
    }

    public void setNmCognomeUserApplic(String nmCognomeUserApplic) {
        this.nmCognomeUserApplic = nmCognomeUserApplic;
    }

    @Column(name = "NM_NOME_USER_COMP_SW")
    public String getNmNomeUserCompSw() {
        return this.nmNomeUserCompSw;
    }

    public void setNmNomeUserCompSw(String nmNomeUserCompSw) {
        this.nmNomeUserCompSw = nmNomeUserCompSw;
    }

    @Column(name = "TIPO_AGENTE_PREMIS")
    public String getTipoAgentePremis() {
        return this.tipoAgentePremis;
    }

    public void setTipoAgentePremis(String tipoAgentePremis) {
        this.tipoAgentePremis = tipoAgentePremis;
    }

    @Column(name = "TIPO_ORIGINE_AGENTE")
    public String getTipoOrigineAgente() {
        return this.tipoOrigineAgente;
    }

    public void setTipoOrigineAgente(String tipoOrigineAgente) {
        this.tipoOrigineAgente = tipoOrigineAgente;
    }

}
