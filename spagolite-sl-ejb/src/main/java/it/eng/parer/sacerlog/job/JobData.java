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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.sacerlog.job;

import java.io.Serializable;

/**
 *
 * @author Iacolucci_M
 */
public class JobData implements Serializable {

    private String nomeApplicazione = null;
    private String nomeJob = null;

    public JobData(String nomeJob, String nomeApplicazione) {
        this.nomeJob = nomeJob;
        this.nomeApplicazione = nomeApplicazione;
    }

    public String getNomeApplicazione() {
        return nomeApplicazione;
    }

    public void setNomeApplicazione(String nomeApplicazione) {
        this.nomeApplicazione = nomeApplicazione;
    }

    public String getNomeJob() {
        return nomeJob;
    }

    public void setNomeJob(String nomeJob) {
        this.nomeJob = nomeJob;
    }

    @Override
    public String toString() {
        return "JobData{" + "nomeApplicazione=" + nomeApplicazione + ", nomeJob=" + nomeJob + '}';
    }

    /*
     * Il test di uguaglianza viene fatto considerando SOLO "nomeApplicazione" e "nomeJob". l'id applicazione viene
     * lasciato volutamente fuori.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JobData other = (JobData) obj;
        if ((this.nomeApplicazione == null) ? (other.nomeApplicazione != null)
                : !this.nomeApplicazione.equals(other.nomeApplicazione)) {
            return false;
        }
        if ((this.nomeJob == null) ? (other.nomeJob != null) : !this.nomeJob.equals(other.nomeJob)) {
            return false;
        }
        return true;
    }

}
