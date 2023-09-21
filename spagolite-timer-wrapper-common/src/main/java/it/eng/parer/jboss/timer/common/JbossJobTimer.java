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
