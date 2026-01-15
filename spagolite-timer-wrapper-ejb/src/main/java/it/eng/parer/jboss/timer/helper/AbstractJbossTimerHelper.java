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

package it.eng.parer.jboss.timer.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import it.eng.parer.jboss.timer.common.CronSchedule;
import it.eng.parer.jboss.timer.common.JobTable;
import it.eng.parer.jboss.timer.exception.TimerNotFoundException;

/**
 * Implementazione parziale di {@link JbossTimerHelper}. Metodi da definire nell'helper specifico
 * dell'applicazione. In questo modo è necessario implementare effettivamente solamente 3 metodi.
 * <strong>Nota bene:</strong> E' necessario che l'helper dell'applicazione abbia come <em>name</em>
 * <strong>timerHelper</strong> e, nonostante estenda questa interfaccia, <em>implementi
 * esplicitamente l'interfaccia</em>.
 *
 * @author Snidero_L
 */
public abstract class AbstractJbossTimerHelper implements JbossTimerHelper {

    @Override
    public void setJobAttivo(String jobName) throws TimerNotFoundException {
        JobTable job = getJob(jobName);
        job.setTiStatoTimer(JobTable.STATO_TIMER.ATTIVO.name());
        job.setDtProssimaAttivazione(new Date());
        job.setFlDataAccurata(JobTable.NON_ACCURATO);
    }

    @Override
    public void setJobInattivo(String jobName) throws TimerNotFoundException {
        JobTable job = getJob(jobName);
        job.setTiStatoTimer(JobTable.STATO_TIMER.INATTIVO.name());
        job.setDtProssimaAttivazione(null);
        job.setFlDataAccurata(null);
    }

    @Override
    public void setJobEsecuzioneSingola(String jobName) throws TimerNotFoundException {
        JobTable job = getJob(jobName);
        job.setTiStatoTimer(JobTable.STATO_TIMER.ESECUZIONE_SINGOLA.name());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 1);

        job.setDtProssimaAttivazione(cal.getTime());
        job.setFlDataAccurata(JobTable.ACCURATO);
    }

    @Override
    public void setJobEsecuzioneSingola(String jobName, Date newDate)
            throws TimerNotFoundException {
        JobTable job = getJob(jobName);
        job.setTiStatoTimer(JobTable.STATO_TIMER.ESECUZIONE_SINGOLA.name());
        job.setDtProssimaAttivazione(newDate);
        job.setFlDataAccurata(JobTable.ACCURATO);
    }

    @Override
    public void changeNode(String jobName, String nodeName) throws TimerNotFoundException {
        JobTable job = getJob(jobName);
        if (nodeName != null) {
            job.setNmNodoAssegnato(nodeName);
        }
    }

    @Override
    public boolean isDataAccurata(String jobName) throws TimerNotFoundException {
        JobTable job = getJob(jobName);
        return job.getFlDataAccurata() != null && job.getFlDataAccurata().equals(JobTable.ACCURATO);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void resetStatus(String jobName) throws TimerNotFoundException {
        JobTable job = getJob(jobName);
        job.setTiStatoTimer(null);
        job.setDtProssimaAttivazione(null);
        job.setFlDataAccurata(null);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void setDataProssimaAttivazione(String jobName, Date newDate)
            throws TimerNotFoundException {
        JobTable job = getJob(jobName);
        job.setDtProssimaAttivazione(newDate);
        if (newDate != null) {
            job.setFlDataAccurata(JobTable.ACCURATO);
        } else {
            job.setFlDataAccurata(null);
        }
    }

    @Override
    public void aggiornaStato(String jobName, String stato) throws TimerNotFoundException {
        JobTable job = getJob(jobName);
        job.setTiStatoTimer(stato);
    }

    @Override
    public Date getDataProssimaAttivazione(String jobName) throws TimerNotFoundException {
        JobTable job = getJob(jobName);
        return job.getDtProssimaAttivazione();
    }

    @Override
    public List<JobTable> getJobs(String nodeName) {
        List<JobTable> jobs = new ArrayList<>();
        List<JobTable> jobList = getJobs();
        if (jobList != null) {
            for (JobTable job : jobList) {
                if (job.getNmNodoAssegnato() != null
                        && job.getNmNodoAssegnato().equalsIgnoreCase(nodeName)) {
                    jobs.add(job);
                }
            }
        }
        return jobs;
    }

    @Override
    public CronSchedule getSchedule(String jobName) throws TimerNotFoundException {
        JobTable job = getJob(jobName);
        CronSchedule sched = new CronSchedule();
        sched.setMinute(job.getCdSchedMinute());
        sched.setHour(job.getCdSchedHour());
        sched.setDayOfWeek(job.getCdSchedDayofweek());
        sched.setMonth(job.getCdSchedMonth());
        sched.setDayOfMonth(job.getCdSchedDayofmonth());
        return sched;
    }

}
