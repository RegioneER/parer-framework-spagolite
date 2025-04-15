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

import it.eng.integriam.server.ws.Costanti.EsitoServizio;

/**
 *
 * @author Gilioli_P
 */
public class ReplicaUtenteRispostaAbstract {

    private EsitoServizio cdEsito;
    private String cdErr;
    private String dsErr;

    public EsitoServizio getCdEsito() {
	return cdEsito;
    }

    public void setCdEsito(EsitoServizio cdEsito) {
	this.cdEsito = cdEsito;
    }

    public String getCdErr() {
	return cdErr;
    }

    public void setCdErr(String cdErr) {
	this.cdErr = cdErr;
    }

    public String getDsErr() {
	return dsErr;
    }

    public void setDsErr(String dsErr) {
	this.dsErr = dsErr;
    }

}
