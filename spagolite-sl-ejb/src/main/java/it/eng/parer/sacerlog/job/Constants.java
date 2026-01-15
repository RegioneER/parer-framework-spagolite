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
