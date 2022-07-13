package it.eng.parer.jboss.timer.interceptor;

import it.eng.parer.jboss.timer.common.JbossJobTimer;
import it.eng.parer.jboss.timer.common.JobTable;
import it.eng.parer.jboss.timer.service.JbossTimerEjb;
import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * Reset dello stato su tabella. E' il secondo intercettore della catena. Viene eseguito dopo il job che filtra il
     * nodo.
     *
     * @param inv
     *            invocation context
     * 
     * @return di solito i metodi intercettati sono void
     * 
     * @throws Exception
     *             eccezione del metodo intercettato
     */
    @AroundInvoke
    public Object resetStatus(InvocationContext inv) throws Exception {
        final String logPrefix = "[" + service.getApplicationName() + " Jboss Timer Interceptor resetStatus] -";
        if (service.isStandalone()) {
            log.debug(String.format("%s Modalità standalone. Procedo con l'interceptor successivo.", logPrefix));
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
