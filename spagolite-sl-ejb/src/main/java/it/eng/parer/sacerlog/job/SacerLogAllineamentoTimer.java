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

package it.eng.parer.sacerlog.job;

import static it.eng.parer.sacerlog.job.SacerLogTimer.TIME_DURATION;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.parer.jboss.timer.common.CronSchedule;
import it.eng.parer.jboss.timer.common.JbossJobTimer;

/**
 *
 * @author Iacolucci_M
 *
 */
@Singleton
@LocalBean
@Lock(LockType.READ)
public class SacerLogAllineamentoTimer implements JbossJobTimer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Resource
    protected TimerService timerService;
    @EJB
    protected SacerLogJobHelper sacerLogJobHelper;
    @EJB
    private SacerLogJob sacerLogJob;

    public SacerLogAllineamentoTimer() {
	logger.debug(this.getClass().getName() + " creato");
    }

    @Override
    public String getJobName() {
	return Constants.NomiJob.ALLINEAMENTO_LOG.name();
    }

    protected boolean isActive(String nomeApplicazione) {
	boolean existTimer = false;
	// Si fa un controllo che il job per la stessa applicazione non sia già
	// presente.
	// Non è chiaro se questo controllo sia effettivamente necessario, ma male non
	// fa'.
	JobData jobDataDaConfrontare = new JobData(Constants.NomiJob.ALLINEAMENTO_LOG.name(),
		nomeApplicazione);
	for (Object obj : timerService.getTimers()) {
	    Timer timer = (Timer) obj;
	    Object oggJob = timer.getInfo();
	    if (oggJob instanceof JobData) {
		if (oggJob.equals(jobDataDaConfrontare)) {
		    existTimer = true;
		    break;
		}
	    }
	}
	return existTimer;
    }

    @Lock(LockType.WRITE)
    @Override
    public void startSingleAction(String nomeApplicazione) {
	/*
	 * Si fa un controllo che il job per la stessa applicazione non sia già presente. Non è
	 * chiaro se questo controllo sia effettivamente necessario, ma male non fa'.
	 */
	if (!isActive(nomeApplicazione)) {
	    timerService.createTimer(TIME_DURATION,
		    new JobData(Constants.NomiJob.ALLINEAMENTO_LOG.name(), nomeApplicazione));
	}
    }

    @Lock(LockType.WRITE)
    @Override
    public void startCronScheduled(CronSchedule sched, String nomeApplicazione) {
	ScheduleExpression tmpScheduleExpression;
	if (nomeApplicazione != null) {
	    if (!isActive(nomeApplicazione)) {
		logger.info("Schedulazione: Ore: " + sched.getHour());
		logger.info("Schedulazione: Minuti: " + sched.getMinute());
		logger.info("Schedulazione: DOW: " + sched.getDayOfWeek());
		logger.info("Schedulazione: Mese: " + sched.getMonth());
		logger.info("Schedulazione: DOM: " + sched.getDayOfMonth());
		tmpScheduleExpression = new ScheduleExpression();
		tmpScheduleExpression.hour(sched.getHour());
		tmpScheduleExpression.minute(sched.getMinute());
		tmpScheduleExpression.dayOfWeek(sched.getDayOfWeek());
		tmpScheduleExpression.month(sched.getMonth());
		tmpScheduleExpression.dayOfMonth(sched.getDayOfMonth());
		logger.info("lancio il timer {}_{}", Constants.NomiJob.ALLINEAMENTO_LOG.name(),
			nomeApplicazione);
		timerService.createCalendarTimer(tmpScheduleExpression, new TimerConfig(
			new JobData(Constants.NomiJob.ALLINEAMENTO_LOG.name(), nomeApplicazione),
			true));
	    }
	} else {
	    throw new RuntimeException(
		    "Errore nello startare l'allineamento dei log: FORNIRE NM_APPLIC.");
	}
    }

    @Lock(LockType.WRITE)
    @Override
    public void stop(String nomeApplicazione) {
	JobData jobDataDaConfrontare = new JobData(Constants.NomiJob.ALLINEAMENTO_LOG.name(),
		nomeApplicazione);
	for (Object obj : timerService.getTimers()) {
	    Timer timer = (Timer) obj;
	    Object scheduled = timer.getInfo();
	    if (scheduled.equals(jobDataDaConfrontare)) {
		timer.cancel();
		logger.info("fermo il timer {}_{}", Constants.NomiJob.ALLINEAMENTO_LOG.name(),
			nomeApplicazione);
	    }
	}
    }

    @Timeout
    public void doJob(Timer timer) throws Exception {
	Object oggJob = timer.getInfo();
	if (oggJob instanceof JobData) {
	    JobData jd = (JobData) oggJob;
	    logger.debug("{}_{} - Inizio schedulazione", Constants.NomiJob.ALLINEAMENTO_LOG.name(),
		    jd.getNomeApplicazione());
	    sacerLogJob.allineamentoLogByScript(jd.getNomeApplicazione());
	}
    }

    @Override
    public Date getNextElaboration(String nomeApplicazione) {
	JobData jd = new JobData(Constants.NomiJob.ALLINEAMENTO_LOG.name(), nomeApplicazione);
	for (Object obj : timerService.getTimers()) {
	    Timer timer = (Timer) obj;
	    Object scheduled = timer.getInfo();
	    if (scheduled instanceof JobData) {
		JobData jobDataScheduled = (JobData) scheduled;
		if (jobDataScheduled.equals(jd)) {
		    return timer.getNextTimeout();
		}
	    }
	}
	return null;
    }
}
