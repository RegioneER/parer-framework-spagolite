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

package it.eng.parer.jboss.timer.interceptor;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.parer.jboss.timer.common.JbossJobTimer;
import it.eng.parer.jboss.timer.common.JobTable;
import it.eng.parer.jboss.timer.service.JbossTimerEjb;

/**
 * Secondo intercettore della catena. Si occupa di reimpostare lo stato del job.
 *
 * @author Snidero_L
 */
public class JbossTimerResetStatusInterceptor {

    private final Logger log = LoggerFactory.getLogger(JbossTimerResetStatusInterceptor.class);

    @EJB
    protected JbossTimerEjb service;

    /**
     * Reset dello stato su tabella. E' il secondo intercettore della catena. Viene eseguito dopo il
     * job che filtra il nodo.
     *
     * @param inv invocation context
     *
     * @return di solito i metodi intercettati sono void
     *
     * @throws Exception eccezione del metodo intercettato
     */
    @AroundInvoke
    public Object resetStatus(InvocationContext inv) throws Exception {
        final String logPrefix = "[" + service.getApplicationName()
                + " Jboss Timer Interceptor resetStatus] -";
        if (service.isStandalone()) {
            log.debug(String.format("%s Modalità standalone. Procedo con l'interceptor successivo.",
                    logPrefix));
            return inv.proceed();
        }
        JbossJobTimer target = (JbossJobTimer) inv.getTarget();
        String jobName = target.getJobName();

        JobTable job = service.getJob(jobName);

        log.debug(String.format("%s Reset dello stato per il job %s", logPrefix, jobName));
        service.resetStatus(job.getNmJob());
        return inv.proceed();
    }

}
