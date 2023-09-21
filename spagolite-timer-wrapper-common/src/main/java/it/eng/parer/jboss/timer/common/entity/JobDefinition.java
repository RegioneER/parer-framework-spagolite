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

package it.eng.parer.jboss.timer.common.entity;

import it.eng.parer.jboss.timer.common.JobTable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Implementazione di una parte dell'entity necessaria a mappare la tabella dei job. I metodi rispettano (parzialmente)
 * il contratto definito da {@link JobTable}
 * 
 * @author Snidero_L
 */
@MappedSuperclass
public class JobDefinition {

    private String nmNodoAssegnato;
    private String tiStatoTimer;
    private String cdSchedHour;
    private String cdSchedMinute;
    private String cdSchedDayofmonth;
    private String cdSchedMonth;
    private String cdSchedDayofweek;
    private Date dtProssimaAttivazione;
    private String flDataAccurata;

    public JobDefinition() {
    }

    @Column(name = "NM_NODO_ASSEGNATO")
    public String getNmNodoAssegnato() {
        return nmNodoAssegnato;
    }

    public void setNmNodoAssegnato(String nmNodoAssegnato) {
        this.nmNodoAssegnato = nmNodoAssegnato;
    }

    @Column(name = "TI_STATO_TIMER")
    public String getTiStatoTimer() {
        return tiStatoTimer;
    }

    public void setTiStatoTimer(String tiStatoTimer) {
        this.tiStatoTimer = tiStatoTimer;
    }

    @Column(name = "CD_SCHED_HOUR")
    public String getCdSchedHour() {
        return cdSchedHour;
    }

    public void setCdSchedHour(String cdSchedHour) {
        this.cdSchedHour = cdSchedHour;
    }

    @Column(name = "CD_SCHED_MINUTE")
    public String getCdSchedMinute() {
        return cdSchedMinute;
    }

    public void setCdSchedMinute(String cdSchedMinute) {
        this.cdSchedMinute = cdSchedMinute;
    }

    @Column(name = "CD_SCHED_DAYOFMONTH")
    public String getCdSchedDayofmonth() {
        return cdSchedDayofmonth;
    }

    public void setCdSchedDayofmonth(String cdSchedDayofmonth) {
        this.cdSchedDayofmonth = cdSchedDayofmonth;
    }

    @Column(name = "CD_SCHED_MONTH")
    public String getCdSchedMonth() {
        return cdSchedMonth;
    }

    public void setCdSchedMonth(String cdSchedMonth) {
        this.cdSchedMonth = cdSchedMonth;
    }

    @Column(name = "CD_SCHED_DAYOFWEEK")
    public String getCdSchedDayofweek() {
        return cdSchedDayofweek;
    }

    public void setCdSchedDayofweek(String cdSchedDayofweek) {
        this.cdSchedDayofweek = cdSchedDayofweek;
    }

    @Column(name = "DT_PROSSIMA_ATTIVAZIONE")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDtProssimaAttivazione() {
        return dtProssimaAttivazione;
    }

    public void setDtProssimaAttivazione(Date dtProssimaAttivazione) {
        this.dtProssimaAttivazione = dtProssimaAttivazione;
    }

    @Column(name = "FL_DATA_ACCURATA", columnDefinition = "char")
    public String getFlDataAccurata() {
        return flDataAccurata;
    }

    public void setFlDataAccurata(String flDataAccurata) {
        this.flDataAccurata = flDataAccurata;
    }
}
