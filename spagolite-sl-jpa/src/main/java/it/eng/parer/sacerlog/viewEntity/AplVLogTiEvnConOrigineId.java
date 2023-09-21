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
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AplVLogTiEvnConOrigineId implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigDecimal idApplic;
    private BigDecimal idAzionePaginaCompSw;
    private BigDecimal idPaginaCompSw;
    private BigDecimal idTipoEvento;

    public AplVLogTiEvnConOrigineId() {
    }

    @Column(name = "ID_APPLIC")
    public BigDecimal getIdApplic() {
        return this.idApplic;
    }

    public void setIdApplic(BigDecimal idApplic) {
        this.idApplic = idApplic;
    }

    @Column(name = "ID_AZIONE_PAGINA_COMP_SW")
    public BigDecimal getIdAzionePaginaCompSw() {
        return this.idAzionePaginaCompSw;
    }

    public void setIdAzionePaginaCompSw(BigDecimal idAzionePaginaCompSw) {
        this.idAzionePaginaCompSw = idAzionePaginaCompSw;
    }

    @Column(name = "ID_PAGINA_COMP_SW")
    public BigDecimal getIdPaginaCompSw() {
        return this.idPaginaCompSw;
    }

    public void setIdPaginaCompSw(BigDecimal idPaginaCompSw) {
        this.idPaginaCompSw = idPaginaCompSw;
    }

    @Column(name = "ID_TIPO_EVENTO")
    public BigDecimal getIdTipoEvento() {
        return this.idTipoEvento;
    }

    public void setIdTipoEvento(BigDecimal idTipoEvento) {
        this.idTipoEvento = idTipoEvento;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.idApplic);
        hash = 59 * hash + Objects.hashCode(this.idAzionePaginaCompSw);
        hash = 59 * hash + Objects.hashCode(this.idPaginaCompSw);
        hash = 59 * hash + Objects.hashCode(this.idTipoEvento);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AplVLogTiEvnConOrigineId other = (AplVLogTiEvnConOrigineId) obj;
        if (!Objects.equals(this.idApplic, other.idApplic)) {
            return false;
        }
        if (!Objects.equals(this.idAzionePaginaCompSw, other.idAzionePaginaCompSw)) {
            return false;
        }
        if (!Objects.equals(this.idPaginaCompSw, other.idPaginaCompSw)) {
            return false;
        }
        if (!Objects.equals(this.idTipoEvento, other.idTipoEvento)) {
            return false;
        }
        return true;
    }

}
