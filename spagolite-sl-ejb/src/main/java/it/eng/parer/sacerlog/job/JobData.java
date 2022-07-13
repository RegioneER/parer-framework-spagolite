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
