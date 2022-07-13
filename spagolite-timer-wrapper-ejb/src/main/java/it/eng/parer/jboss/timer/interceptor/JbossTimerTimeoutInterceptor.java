package it.eng.parer.jboss.timer.interceptor;

import it.eng.parer.jboss.timer.common.JbossJobTimer;
import it.eng.parer.jboss.timer.service.JbossTimerEjb;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Timer;
import javax.interceptor.AroundTimeout;
import javax.interceptor.InvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Intercettore del metodo di timeout. Intercettore sul metodo "core" del timer. Si occupa di impostare la data di
 * prossima attivazione <strong>prima</strong> che venga eseguito il corpo del job.
 *
 * @author Snidero_L
 */
public class JbossTimerTimeoutInterceptor {

    private final Logger log = LoggerFactory.getLogger(JbossTimerTimeoutInterceptor.class);

    @EJB
    protected JbossTimerEjb service;

    /**
     * Intercettore per il metodo del timer annotato con @Timeout. L'esecuzione di questo metodo avviene solo nel nodo
     * previsto dalla configurazione. Tale requisito è garantito dall'esecuzione precedente dell'interceptor
     * {@link JbossTimerNodeInterceptor}.
     *
     * @param inv
     *            invocation context del metodo doJob
     * 
     * @return di solito i metodi intercettati sono void
     * 
     * @throws Exception
     *             eccezione del metodo intercettato
     */
    @AroundTimeout
    public Object timeout(InvocationContext inv) throws Exception {
        final String logPrefix = "[" + service.getApplicationName() + " Jboss Timer Interceptor timeout] -";
        if (service.isStandalone()) {
            log.debug(String.format("%s Modalità standalone. Procedo con l'interceptor successivo.", logPrefix));
            return inv.proceed();
        }
        JbossJobTimer target = (JbossJobTimer) inv.getTarget();

        Timer timer = (Timer) inv.getTimer();

        String jobName = target.getJobName();
        Date nextElaboration = timer.isCalendarTimer() ? target.getNextElaboration(service.getApplicationName()) : null;
        service.setDataProssimaAttivazione(jobName, nextElaboration);
        log.debug(String.format("%s Timeout del job %s ", logPrefix, jobName));
        return inv.proceed();
    }

}
