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

package it.eng.parer.sacerlog.ejb.util;

import java.math.BigDecimal;

/**
 *
 * @author Iacolucci_M
 */
public class LogOggettoRuolo {
    private String nmTipoOggetto;
    private String tiRuoloPremis;
    private BigDecimal idOggetto;

    public String getNmTipoOggetto() {
        return nmTipoOggetto;
    }

    public void setNmTipoOggetto(String nmTipoOggetto) {
        this.nmTipoOggetto = nmTipoOggetto;
    }

    public String getTiRuoloPremis() {
        return tiRuoloPremis;
    }

    public void setTiRuoloPremis(String tiRuoloPremis) {
        this.tiRuoloPremis = tiRuoloPremis;
    }

    public BigDecimal getIdOggetto() {
        return idOggetto;
    }

    public void setIdOggetto(BigDecimal idOggetto) {
        this.idOggetto = idOggetto;
    }

    public LogOggettoRuolo(String nmTipoOggetto, BigDecimal idOggetto, String tiRuoloPremis) {
        this.nmTipoOggetto = nmTipoOggetto;
        this.tiRuoloPremis = tiRuoloPremis;
        this.idOggetto = idOggetto;
    }

    public LogOggettoRuolo(String nmTipoOggetto, BigDecimal idOggetto) {
        this.nmTipoOggetto = nmTipoOggetto;
        this.idOggetto = idOggetto;
    }

}
