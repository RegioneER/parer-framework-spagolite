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

package it.eng.integriam.server.ws.reputente;

/**
 *
 * @author Gilioli_P
 */
public class TipoDatoAbil {

    private String nmClasseTipoDato;
    private Integer idTipoDatoApplic;

    public String getNmClasseTipoDato() {
	return nmClasseTipoDato;
    }

    public void setNmClasseTipoDato(String nmClasseTipoDato) {
	this.nmClasseTipoDato = nmClasseTipoDato;
    }

    public Integer getIdTipoDatoApplic() {
	return idTipoDatoApplic;
    }

    public void setIdTipoDatoApplic(Integer idTipoDatoApplic) {
	this.idTipoDatoApplic = idTipoDatoApplic;
    }
}
