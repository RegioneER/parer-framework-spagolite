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

package it.eng.parer.jboss.timer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.parer.jboss.timer.common.CronSchedule;
import it.eng.parer.jboss.timer.common.JbossJobTimer;
import it.eng.parer.jboss.timer.common.JobTable;
import it.eng.parer.jboss.timer.service.JbossTimerEjb;

/**
 * Timer che esegue il pollig dalla tabella di configurazione. Sì, il nome della classe è una citazione.
 *
 * @author Snidero_L
 */
@Singleton
@Startup
@DependsOn("timerHelper")
public class MasterOfTimers {

    private final Logger log = LoggerFactory.getLogger(MasterOfTimers.class);

    private static final String MASTER_INFO = MasterOfTimers.class.getSimpleName();

    @EJB
    private JbossTimerEjb service;

    @EJB
    private MasterOfTimers me;

    /**
     * Lista dei timer presenti nell'applicazione.
     */
    private Set<String> timerDeployed;

    private String currentNode;

    private String logPrefix;

    @Resource
    private TimerService timerService;

    /**
     * Inizializzo il gestore dei timer.
     */
    @PostConstruct
    private void init() {
        currentNode = service.getCurrentNode();
        logPrefix = "[" + service.getApplicationName() + " Jboss Timer Master] -";

        log.info(String.format(
                "%s Questo job viene eseguito a deploy time su ogni nodo dell'applicazione. Questo per esempio è il nodo %s",
                logPrefix, currentNode));
        timerDeployed = service.getApplicationTimerNames();

        // Effettuo la query solo se sono in ambiente domain.
        if (!service.isStandalone()) {
            // pulisco lo stato di tutti i job del nodo su cui sono sono in esecuzione. Se il database è condiviso può
            // creare problemi.
            for (String jobName : getJobsOnNode()) {
                log.info(String.format("%s Reset del job con nome %s sul nodo %s.", logPrefix, jobName, currentNode));
                service.resetStatus(jobName);
            }
        }
        // Sono riuscito ad accedere all'helper dell'applicazione deployata. Accendo il timer.
        startMaster();
    }

    /**
     * Ottieni la lista dei nomi dei job configurati sul nodo. La lista risultante comprende solo i job presenti
     * nell'applicazione e configurati su DB.
     *
     * @return Lista di nomi dei job.
     */
    private Set<String> getJobsOnNode() {
        Set<String> jobConfigured = new HashSet<>();
        List<JobTable> jobs = service.getJobs(currentNode);
        if (jobs.isEmpty()) {
            log.warn(String.format("%s Sul nodo %s non è stato configurato alcun job", logPrefix, currentNode));
        }
        for (JobTable job : jobs) {
            if (timerDeployed.contains(job.getNmJob())) {
                jobConfigured.add(job.getNmJob());
            }
        }
        return jobConfigured;
    }

    /**
     * Attivatore del job. Inizializzazione programmatica del timer.
     *
     */
    private void startMaster() {
        ScheduleExpression expr = new ScheduleExpression();
        expr.hour("*");
        expr.minute("*/1");
        StringBuilder sb = new StringBuilder();
        sb.append(MASTER_INFO);
        sb.append(": Ore: ").append(expr.getHour());
        sb.append(", Minuti: ").append(expr.getMinute());
        sb.append(", DOW: ").append(expr.getDayOfWeek());
        sb.append(", Mese: ").append(expr.getMonth());
        sb.append(", DOM: ").append(expr.getDayOfMonth());

        timerService.createCalendarTimer(expr, new TimerConfig(MASTER_INFO, false));

        log.info(String.format("%s Schedulazione timer %s", logPrefix, sb.toString()));
    }

    /**
     * Metodo che ogni minuto legge lo stato dei job. In modalità standalone la lista dei job sul nodo risulterà sempre
     * vuota.
     */
    @Timeout
    public void polling() {
        if (service.isStandalone()) {
            log.debug(String.format(
                    "%s Esecuzione in ambiente standalone. Non accedo alla banca dati per ottenere la lista dei job.",
                    logPrefix));
            return;
        }

        List<JobTable> jobs = service.getJobs(currentNode);
        for (JobTable job : jobs) {
            try {
                JbossJobTimer timer = service.getTimer(job.getNmJob());
                me.execute(timer, job);
            } catch (Exception e) {
                log.error(String.format("%s Errore generico nell'esecuzione del job %s .", logPrefix, job.getNmJob()),
                        e);
            }
        }
    }

    /**
     * Eseguo su chi estende {@link JbossJobTimer} il metodo indicato dallo stato del job. Marcato con REQUIRES_NEW per
     * evitare di invalidare la transazione chiamante in caso di errore.
     *
     * @param timer
     *            timer concreto che estende {@link JbossJobTimer}
     * @param job
     *            definizione del job
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void execute(JbossJobTimer timer, JobTable job) {
        if (timer == null) {
            log.warn(String.format("%s Il timer con nome %s non esiste nell'applicazione %s", logPrefix, job.getNmJob(),
                    service.getApplicationName()));
            return;
        }
        // Stato timer == null
        if (job.getTiStatoTimer() == null) {
            return;
        }
        // Timer non configurato sull'applicazione
        if (!timerDeployed.contains(job.getNmJob())) {
            log.warn(String.format("%s Il timer con nome %s non è stato configurato ", logPrefix, job.getNmJob()));
            return;
        }
        // Valutazione operazione
        JobTable.STATO_TIMER operation;
        try {
            operation = JobTable.STATO_TIMER.valueOf(job.getTiStatoTimer());
        } catch (IllegalArgumentException e) {
            log.warn(String.format("%s L'operazione \"%s\" non è supportata", logPrefix, job.getTiStatoTimer()));
            return;
        }

        log.debug(String.format("%s Esecuzione dell'operazione %s sul timer %s ", logPrefix, operation.name(),
                job.getNmJob()));

        if (operation == JobTable.STATO_TIMER.INATTIVO) {

            timer.stop(service.getApplicationName());
            return;
        }
        if (operation == JobTable.STATO_TIMER.ATTIVO) {

            CronSchedule schedule = new CronSchedule();
            schedule.setHour(job.getCdSchedHour());
            schedule.setMinute(job.getCdSchedMinute());
            schedule.setDayOfMonth(job.getCdSchedDayofmonth());
            schedule.setMonth(job.getCdSchedMonth());
            schedule.setDayOfWeek(job.getCdSchedDayofweek());

            timer.startCronScheduled(schedule, service.getApplicationName());
            return;
        }
        if (operation == JobTable.STATO_TIMER.ESECUZIONE_SINGOLA) {

            timer.startSingleAction(service.getApplicationName());
        }
    }

    @PreDestroy
    public void shutdown() {
        log.info(String.format("%s Shutdown del Master Timer. Sul nodo %s non sarà più possibile eseguire job ",
                logPrefix, currentNode));
    }

}
