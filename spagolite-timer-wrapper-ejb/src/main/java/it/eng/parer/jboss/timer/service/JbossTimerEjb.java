package it.eng.parer.jboss.timer.service;

import it.eng.parer.jboss.timer.common.CronSchedule;
import it.eng.parer.jboss.timer.common.JbossJobTimer;
import it.eng.parer.jboss.timer.common.JobTable;
import it.eng.parer.jboss.timer.common.JobTable.STATO_TIMER;
import it.eng.parer.jboss.timer.exception.TimerNotFoundException;
import it.eng.parer.jboss.timer.helper.JbossTimerHelper;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.LocalBean;
import javax.ejb.NoMoreTimeoutsException;
import javax.ejb.Stateless;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service Layer. Qui sono presenti i metodi "a grana grossa" che dialogano con la tabella di gestione dei job. Questa
 * classe è pensata per essere robusta e gestire tutte le eccezioni lanciate dall'helper. Il comportamento dei metodi di
 * questa classe, come negli interceptor, è diverso a seconda che l'applicazione sia in domain mode oppure in standalone
 * mode.
 *
 * @author Snidero_L
 */
@Stateless
@LocalBean
public class JbossTimerEjb {

    private final Logger log = LoggerFactory.getLogger(JbossTimerEjb.class);

    @EJB(beanName = "timerHelper")
    // @Inject
    private JbossTimerHelper helper;

    /**
     * Stabilisce se siamo in standalone o domain mode.
     *
     * @return true se siamo in standalone mode.
     */
    public boolean isStandalone() {
        boolean standalone = true;
        // Domain mode o standalone mode?
        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            ObjectName query = new ObjectName("jboss.as:core-service=server-environment");
            String attribute = (String) server.getAttribute(query, "launchType");
            if (attribute != null && attribute.equalsIgnoreCase("DOMAIN")) {
                standalone = false;
            }
            log.debug(String.format("%s Operazione eseguita in ambiente %s", logPrefix(), attribute));
        } catch (Exception e) {
            log.warn(String.format("%s Errore durante l'individuazione del launchType dell'AS (STANDALONE o DOMAIN)",
                    logPrefix()), e);
        }
        return standalone;
    }

    /**
     * Imposta, sulla tabella deputata, lo stato "INATTIVO". Entro un minuto questa informazione verrà letta dal Master
     * Timer.
     *
     * @param jobName
     *            nome del job
     */
    public void stop(String jobName) {
        try {
            if (isStandalone()) {
                JbossJobTimer timer = helper.getTimer(jobName);
                timer.stop(helper.getApplicationName());
            } else {
                helper.setJobInattivo(jobName);
            }
        } catch (TimerNotFoundException ex) {
            log.warn(String.format("%s %s", logPrefix(), ex.getMessage()));
        }
    }

    /**
     * Imposta, sulla tabella deputata, lo stato "ATTIVO". Entro un minuto questa informazione verrà letta dal Master
     * Timer.
     *
     * @param jobName
     *            nome del job
     * @param nodeName
     *            nome del nodo. Viene preso in considerazione solo se non nullo.
     */
    public void start(String jobName, String nodeName) {
        try {
            if (isStandalone()) {
                JbossJobTimer timer = helper.getTimer(jobName);
                CronSchedule schedule = helper.getSchedule(jobName);
                timer.startCronScheduled(schedule, helper.getApplicationName());
            } else {
                helper.setJobAttivo(jobName);
                if (nodeName != null) {
                    helper.changeNode(jobName, nodeName);
                }
            }
        } catch (TimerNotFoundException ex) {
            log.warn(String.format("%s %s", logPrefix(), ex.getMessage()));
        }
    }

    /**
     * Imposta, sulla tabella deputata, lo stato "ESECUZIONE SINGOLA". Entro un minuto questa informazione verrà letta
     * dal Master Timer.
     *
     * @param jobName
     *            nome del job
     * @param nodeName
     *            nome del nodo. Viene preso in considerazione solo se non nullo.
     */
    public void esecuzioneSingola(String jobName, String nodeName) {
        try {
            if (isStandalone()) {
                JbossJobTimer timer = helper.getTimer(jobName);
                timer.startSingleAction(helper.getApplicationName());
            } else {
                helper.setJobEsecuzioneSingola(jobName);
                if (nodeName != null) {
                    helper.changeNode(jobName, nodeName);
                }
            }
        } catch (TimerNotFoundException ex) {
            log.warn(String.format("%s %s", logPrefix(), ex.getMessage()));
        }
    }

    /**
     * Data di prossima attivazione del job. Risulta diversa da null solo nel caso in cui si sia premuto il pulsante
     * start.
     *
     * @param jobName
     *            nome del job
     * 
     * @return null o la data della prossima attivazioen del job
     */
    public Date getDataProssimaAttivazione(String jobName) {
        Date result = null;
        try {
            if (isStandalone()) {
                JbossJobTimer timer = helper.getTimer(jobName);
                result = timer.getNextElaboration(helper.getApplicationName());
            } else {
                result = helper.getDataProssimaAttivazione(jobName);
            }
        } catch (EJBException e) {
            if (e.getCause() != null && e.getCause() instanceof NoMoreTimeoutsException) {
                log.warn(String.format("%s Errore nel timer %s (modalità standalone)", logPrefix(), e.getMessage()));
            } else {
                log.warn(String.format("%s Errore generico %s (modalità standalone)", logPrefix(), e.getMessage()));
            }

        } catch (TimerNotFoundException ex) {
            log.warn(String.format("%s %s", logPrefix(), ex.getMessage()));
        }
        return result;
    }

    /**
     * La data presente in tabella è calcolata dal job oppure "a mano" ?
     *
     *
     * @param jobName
     *            nome del job
     * 
     * @return true se la risposta è sì, false altrimenti
     */
    public boolean isDataProssimaAttivazioneAccurata(String jobName) {
        if (isStandalone()) {
            return true;
        }

        boolean result = false;
        try {
            result = helper.isDataAccurata(jobName);
        } catch (TimerNotFoundException ex) {
            log.warn(String.format("%s %s", logPrefix(), ex.getMessage()));
        }
        return result;
    }

    /**
     * Ottieni la lista dei timer configurati per l'applicazione.
     *
     * @return lista di nomi di timer
     */
    public Set<String> getApplicationTimerNames() {
        return helper.getApplicationTimerNames();
    }

    /**
     * Imposta lo stato del job a {@link STATO_TIMER}
     *
     * @param jobName
     *            nome del job
     * @param stato
     *            stato del timer
     */
    public void setStatoJob(String jobName, STATO_TIMER stato) {
        try {
            if (stato == STATO_TIMER.ATTIVO) {
                helper.setJobAttivo(jobName);
                return;
            }
            if (stato == STATO_TIMER.ESECUZIONE_SINGOLA) {
                helper.setJobEsecuzioneSingola(jobName);
                return;
            }
            if (stato == STATO_TIMER.INATTIVO) {
                helper.setJobInattivo(jobName);
            }
        } catch (TimerNotFoundException ex) {
            log.warn(String.format("%s %s", logPrefix(), ex.getMessage()));
        }
    }

    /**
     * Lista dei job. Lista delle entity che rappresentano i job. Il sistema agisce solo sui job il cui stato
     * {@link JobTable#getTiStatoTimer() } sia diverso da null.
     *
     * @return lista di job
     */
    public List<JobTable> getJobs() {
        return helper.getJobs();
    }

    /**
     * Lista dei job assegnati al nodo. Non resituisce null.
     *
     * @param nodoAssegnato
     *            definito su banca dati
     * 
     * @return lista di job
     */
    public List<JobTable> getJobs(String nodoAssegnato) {
        List<JobTable> jobs = helper.getJobs(nodoAssegnato);
        if (jobs == null) {
            return new ArrayList<>();
        }
        return jobs;
    }

    /**
     * Ottiene l'istanza del job a seconda del nome. Non è passato come costante perché ci possono essere dei job non
     * appartenenti a questo modulo. Se il parametro jobName non corrisponde ad alcun timer viene restituito
     * <strong>null</strong>.
     *
     * @param jobName
     *            nome del job
     * 
     * @return istanza del timer
     */
    public JbossJobTimer getTimer(String jobName) {
        JbossJobTimer timer = null;
        try {
            timer = helper.getTimer(jobName);
        } catch (TimerNotFoundException ex) {
            log.warn(String.format("%s %s", logPrefix(), ex.getMessage()));
        }
        return timer;
    }

    /**
     * Nome dell'applicazione chiamante. Si è rivelato necessario aggiungere questo metodo per ottemperare ai contratti
     * dei job di sacerlog.
     *
     * @return il nome dell'applicazione chiamante.
     */
    public String getApplicationName() {
        return helper.getApplicationName();
    }

    /**
     * Entity del job.
     *
     * @param jobName
     *            nome del job
     * 
     * @return entity oppure null
     */
    public JobTable getJob(String jobName) {
        JobTable job = null;
        try {
            job = helper.getJob(jobName);
        } catch (TimerNotFoundException ex) {
            log.warn(String.format("%s %s", logPrefix(), ex.getMessage()));
        }
        return job;
    }

    /**
     * Ottieni il nome del nodo corrente.
     *
     * @return nome del nodo corrente oppure "undefined".
     */
    public String getCurrentNode() {
        String nodeName = System.getProperty("jboss.node.name");
        if (nodeName == null || nodeName.isEmpty()) {
            nodeName = "undefined";
        }
        return nodeName;
    }

    /**
     * Reimposta lo stato del job.
     *
     * @param jobName
     *            nome del job
     */
    public void resetStatus(String jobName) {
        try {
            helper.resetStatus(jobName);
        } catch (TimerNotFoundException ex) {
            log.warn(String.format("%s %s", logPrefix(), ex.getMessage()));
        }
    }

    /**
     * Imposta la data di prossima attivazione.
     *
     * @param jobName
     *            nome del job
     * @param newDate
     *            nuova data
     */
    public void setDataProssimaAttivazione(String jobName, Date newDate) {
        try {
            helper.setDataProssimaAttivazione(jobName, newDate);
        } catch (TimerNotFoundException ex) {
            log.warn(String.format("%s %s", logPrefix(), ex.getMessage()));
        }
    }

    /**
     * Determina se ricade nel minuto tra l'esecuzione diel timer o meno.
     *
     * @param jobName
     *            nome del job
     *
     * @return true se l'operazione non è stata ancora eseguita
     */
    public boolean isEsecuzioneInCorso(String jobName) {
        // In standalone mode non è necessario aspettare il prossimo timeout
        if (isStandalone()) {
            return false;
        }
        JobTable job = getJob(jobName);
        return (job != null && job.getTiStatoTimer() != null);
    }

    /**
     * Prefisso dei log. Il prefisso contiene l'applicaizione nella quale sto utilizzando il modulo.
     *
     * @return [NOME APPLICAZIONE Jboss Timer Ejb] -
     */
    private String logPrefix() {
        return "[" + helper.getApplicationName() + " Jboss Timer Ejb] -";
    }

}
