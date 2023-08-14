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
 * Intercettore principale, si occupa di controllare che il job venga eseguito nel nodo corretto.
 *
 * @author Snidero_L
 */
public class JbossTimerNodeInterceptor {

    private final Logger log = LoggerFactory.getLogger(JbossTimerNodeInterceptor.class);

    @EJB
    protected JbossTimerEjb service;

    /**
     * L'invocazione dell'intercettore avviene in ogni nodo. E' il primo intercettore della catena. Solo il timer sul
     * nodo corretto verrà eseguito.
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
    public Object executeOnJbossNode(InvocationContext inv) throws Exception {
        final String logPrefix = "[" + service.getApplicationName() + " Jboss Timer Interceptor executeOnJbossNode] -";
        if (service.isStandalone()) {
            log.debug(String.format("%s Modalità standalone. Procedo con l'interceptor successivo.", logPrefix));
            return inv.proceed();
        }
        Object result = null;

        JbossJobTimer target = (JbossJobTimer) inv.getTarget();
        String jobName = target.getJobName();

        String nodeName = service.getCurrentNode();
        JobTable job = service.getJob(jobName);

        if (job != null && nodeName != null && nodeName.equals(job.getNmNodoAssegnato())
                && job.getTiStatoTimer() != null) {
            log.info(String.format("%s Procedo all'esecuzione del job %s nel nodo %s con lo stato %s", logPrefix,
                    jobName, nodeName, job.getTiStatoTimer()));
            result = inv.proceed();
        } else {
            String applicNodeName = job != null ? job.getNmNodoAssegnato() : "undefined";
            log.debug(String.format(
                    "%s Il job %s non può essere eseguito nel nodo %s. E' stato configurato per essere eseguito sul nodo %s ",
                    logPrefix, jobName, nodeName, applicNodeName));
        }
        return result;
    }

}
