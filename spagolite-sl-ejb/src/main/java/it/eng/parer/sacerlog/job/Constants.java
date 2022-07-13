package it.eng.parer.sacerlog.job;

import java.util.Calendar;
import java.util.Date;

public class Constants {

    public static final int PREFIX_INDEX_LENGTH = 100;

    public static final Date DATE_INITIAL_PARER;
    public static final Date DATE_ANNUL_INIT;

    static {
        Calendar cal = Calendar.getInstance();
        cal.set(2011, 12, 1);
        DATE_INITIAL_PARER = cal.getTime();

        // Init value of the field ARO_UNITA_DOC.DT_ANNUL
        cal.set(2444, 12, 31);
        DATE_ANNUL_INIT = cal.getTime();
    }

    // JOB CONTANTS
    public static final String DATE_FORMAT_JOB = "dd/MM/yyyy HH.mm.ss";
    public static final String FILE_JOB_PROPERTIES = "/sacerlog.properties";

    public enum JobEnum {
        SACER_LOG
    }

    public enum tiEvento {

        INIZIO_SCHEDULAZIONE, FINE_SCHEDULAZIONE, ERRORE;
    }

    public enum NomiJob {
        INIZIALIZZAZIONE_LOG, ALLINEAMENTO_LOG
    }

}
