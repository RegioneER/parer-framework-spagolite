package it.eng.parer.jboss.timer.helper;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import it.eng.parer.jboss.timer.common.CronSchedule;
import it.eng.parer.jboss.timer.common.JbossJobTimer;
import it.eng.parer.jboss.timer.common.JobTable;
import it.eng.parer.jboss.timer.common.JobTable.STATO_TIMER;
import it.eng.parer.jboss.timer.exception.TimerNotFoundException;

/**
 * Helper. Questa è l'unica classe che deve essere esplicitamente implementata nello strato ejb delle applicazioni che
 * utilizzano i timer.
 *
 * @author Snidero_L
 */
@Local
public interface JbossTimerHelper {

    /**
     * Operazione di cambio nodo per il job. Al momento non è utilizzata. Potrebbe essere utile se si deciderà di
     * cambiare nodo tramite una funzione applicativa.
     *
     * @param jobName
     *            nome del job (solitamente una costante)
     * @param nodeName
     *            nome del nodo.
     * 
     * @throws TimerNotFoundException
     *             nel caso in cui il job non esista
     */
    void changeNode(String jobName, String nodeName) throws TimerNotFoundException;

    /**
     * Nome dell'applicazione chiamante. Si è rivelato necessario aggiungere questo metodo per ottemperare ai contratti
     * dei job di sacerlog.
     *
     * @return il nome dell'applicazione chiamante.
     */
    String getApplicationName();

    /**
     * Entity del job. E' stata creata un'interfaccia per disaccoppiare le tabelle specifiche. In futuro ne metteremo
     * una in uno schema separato?
     *
     * @param jobName
     *            nome del job
     * 
     * @return entity della tabella che memorizza lo stato dei job
     * 
     * @throws TimerNotFoundException
     *             nel caso in cui il job non esista
     */
    JobTable getJob(String jobName) throws TimerNotFoundException;

    /**
     * Lista dei job. Lista delle entity che rappresentano i job. Il sistema agisce solo sui job il cui stato
     * {@link JobTable#getTiStatoTimer() } sia diverso da null.
     *
     * @return lista di job
     */
    List<JobTable> getJobs();

    /**
     * Lista dei job configurati sul nodo <strong>nodeName</strong>. Non restituisce null.
     *
     * @param nodeName
     *            nome del nodo
     * 
     * @return lista di job
     */
    List<JobTable> getJobs(String nodeName);

    /**
     * Flag di accuratezza della data.
     *
     * @param jobName
     *            nome del job
     * 
     * @return true se il timer lo ha impostato. false altrimenti
     * 
     * @throws TimerNotFoundException
     *             nel caso in cui il job non esista
     */
    boolean isDataAccurata(String jobName) throws TimerNotFoundException;

    /**
     * Reimposta il record del job. Si suggerisce una transazione nuova.
     *
     * @param jobName
     *            nome del job
     * 
     * @throws TimerNotFoundException
     *             nel caso in cui il job non esista
     */
    void resetStatus(String jobName) throws TimerNotFoundException;

    /**
     * Imposta la data di nuova attivazione. Utilizzato nel caso di "start". Anche qui si suggerisce di creare una nuova
     * transazione. Nota: è lecito che la nuova data sia null.
     *
     * @param jobName
     *            nome del job
     * @param newDate
     *            nuova data
     * 
     * @throws TimerNotFoundException
     *             nel caso in cui il job non esista
     */
    void setDataProssimaAttivazione(String jobName, Date newDate) throws TimerNotFoundException;

    /**
     * Imposta il job a {@link STATO_TIMER#ESECUZIONE_SINGOLA}
     *
     * @param jobName
     *            nome del job
     * 
     * @throws TimerNotFoundException
     *             nel caso in cui il job non esista
     */
    void setJobAttivo(String jobName) throws TimerNotFoundException;

    /**
     * Imposta il job a {@link STATO_TIMER#ESECUZIONE_SINGOLA}
     *
     * @param jobName
     *            nome del job
     * 
     * @throws TimerNotFoundException
     *             nel caso in cui il job non esista
     */
    void setJobEsecuzioneSingola(String jobName) throws TimerNotFoundException;

    /**
     * Imposta il job a {@link STATO_TIMER#ESECUZIONE_SINGOLA}. Permette di specificare una data diversa. Verrà
     * considerata attendibile.
     *
     * @param jobName
     *            nome del job
     * @param newDate
     *            nuova data
     * 
     * @throws TimerNotFoundException
     *             nel caso in cui il job non esista
     */
    void setJobEsecuzioneSingola(String jobName, Date newDate) throws TimerNotFoundException;

    /**
     * Imposta il job a {@link STATO_TIMER#INATTIVO}
     *
     * @param jobName
     *            nome del job
     * 
     * @throws TimerNotFoundException
     *             nel caso in cui il job non esista
     */
    void setJobInattivo(String jobName) throws TimerNotFoundException;

    /**
     * Ottiene l'istanza del job a seconda del nome. Non è passato come costante perché ci possono essere dei job non
     * appartenenti a questo modulo.
     *
     * @param jobName
     *            nome del job
     * 
     * @return istanza del timer
     * 
     * @throws TimerNotFoundException
     *             nel caso in cui il job non esista
     */
    JbossJobTimer getTimer(String jobName) throws TimerNotFoundException;

    /**
     * Aggiorna lo stato del job
     *
     * @param jobName
     *            nome del job
     * @param stato
     *            nuovo stato
     * 
     * @throws TimerNotFoundException
     *             nel caso in cui il job non esista
     */
    void aggiornaStato(String jobName, String stato) throws TimerNotFoundException;

    /**
     * Ottiene la data di prossima attivazione del job.
     *
     * @param jobName
     *            nome del job
     * 
     * @return data di prossima attivazione oppure null
     * 
     * @throws TimerNotFoundException
     *             nel caso in cui il job non esista
     */
    Date getDataProssimaAttivazione(String jobName) throws TimerNotFoundException;

    /**
     * Ottieni la lista dei timer configurati per l'applicazione.
     *
     * @return lista di nomi di timer
     */
    Set<String> getApplicationTimerNames();

    /**
     * Ottieni la schedulazione del job.
     *
     * @param jobName
     *            nome del job
     * 
     * @return l'oggetto cronschedule
     * 
     * @throws TimerNotFoundException
     *             nel caso in cui il job non esista
     */
    public CronSchedule getSchedule(String jobName) throws TimerNotFoundException;

}
