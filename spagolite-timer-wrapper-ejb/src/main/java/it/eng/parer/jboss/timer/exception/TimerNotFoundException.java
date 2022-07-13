package it.eng.parer.jboss.timer.exception;

/**
 * Eccezione <strong>Checked</strong> relativa ai timer configurati. Utilizzata per gestire le discrepanze tra i timer
 * configurati sull'applicazione e quelli definit su db.
 *
 * @author Snidero_L
 */
public class TimerNotFoundException extends Exception {

    private static final long serialVersionUID = 6302071168054022872L;
    private final String timerName;

    public TimerNotFoundException(String timerName) {
        this.timerName = timerName;
    }

    @Override
    public String getMessage() {
        return "Timer " + timerName + " non correttamente configurato.";
    }

}
