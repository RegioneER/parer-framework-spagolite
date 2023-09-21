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

package it.eng.parer.jboss.timer.interceptor;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.parer.jboss.timer.common.JbossJobTimer;
import it.eng.parer.jboss.timer.service.JbossTimerEjb;

/**
 * Intercettore per l'operazione di start. Viene invocato dopo l'intercettore principale
 * {@link JbossTimerNodeInterceptor} e si occupa di impostare la data di prossima esecuzione ottenuta dal timer.
 *
 * @author Snidero_L
 */
public class JbossTimerStartInterceptor {

    private final Logger log = LoggerFactory.getLogger(JbossTimerStartInterceptor.class);

    @EJB
    protected JbossTimerEjb service;

    /**
     * Intercettore del metodo start programmatico del timer. L'esecuzione di questo metodo avviene solo nel nodo
     * previsto dalla configurazione. Tale requisito è garantito dall'esecuzione dell'interceptor
     * {@link JbossTimerNodeInterceptor}. Imposta la data di prossima attivazione <strong>dopo aver schedulato il
     * job</strong> (invocazione del metodo startScheduled).
     *
     * @param inv
     *            invocation context del metodo doJob
     * 
     * @return di solito i metodi intercettati sono void
     * 
     * @throws Exception
     *             eccezione del metodo intercettato
     */
    @AroundInvoke
    public Object start(InvocationContext inv) throws Exception {
        final String logPrefix = "[" + service.getApplicationName() + " Jboss Timer Interceptor start] -";
        if (service.isStandalone()) {
            log.debug(String.format("%s Modalità standalone. Procedo con l'interceptor successivo.", logPrefix));
            return inv.proceed();
        }
        JbossJobTimer target = (JbossJobTimer) inv.getTarget();
        String jobName = target.getJobName();
        log.debug(String.format("%s Start del job %s ", logPrefix, jobName));
        Object result = inv.proceed();
        service.setDataProssimaAttivazione(jobName, target.getNextElaboration(service.getApplicationName()));
        return result;
    }
}
