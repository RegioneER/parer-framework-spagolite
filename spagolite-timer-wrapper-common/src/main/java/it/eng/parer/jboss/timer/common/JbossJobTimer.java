package it.eng.parer.jboss.timer.common;

import java.util.Date;

import javax.ejb.Timer;

/**
 * Ogni timer del parer dovrà implementare questa interfaccia.
 *
 * @author Snidero_L
 */
public interface JbossJobTimer {

    /**
     * Restituisce la data del prossimo timeout.
     *
     * @return data della prossima elaborazione
     * 
     * @param applicationName
     *            nome dell'applicazione. Necessario per SacerLog
     */
    Date getNextElaboration(String applicationName);

    /**
     * Nome del job.
     *
     * @return the name of the job
     */
    String getJobName();

    /**
     * Esegue la schedulazione del job.
     *
     * @param sched
     *            espressione di schedulazione
     * @param applicationName
     *            nome dell'applicazione. Necessario per SacerLog
     */
    void startCronScheduled(CronSchedule sched, String applicationName);

    /**
     * Esegue il job per un'esecuzione singola.
     *
     * @param applicationName
     *            nome dell'applicazione. Necessario per SacerLog
     */
    void startSingleAction(String applicationName);

    /**
     * Cancella l'esecuzione del job.
     *
     * @param applicationName
     *            nome dell'applicazione. Necessario per SacerLog
     */
    void stop(String applicationName);

    /**
     * Metodo che esegue il job (marcato con @Timeout).
     *
     * @param timer
     *            firma standard del metodo di timeout
     * 
     * @throws java.lang.Exception
     *             eccezione generica
     */
    void doJob(Timer timer) throws Exception;

}
