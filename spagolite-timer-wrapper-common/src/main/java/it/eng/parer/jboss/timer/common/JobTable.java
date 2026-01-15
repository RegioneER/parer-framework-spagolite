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

package it.eng.parer.jboss.timer.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Contratto che l'entity che rappresenta la tabella dei job deve ottemperare.
 *
 * @author Snidero_L
 */
public interface JobTable extends Serializable {

    /**
     * Stato del timer.
     */
    public enum STATO_TIMER {
        INATTIVO, ATTIVO, ESECUZIONE_SINGOLA
    };

    /**
     * Valore positivo del flag.
     */
    public static final String ACCURATO = "1";

    /**
     * Valore negativo del flag.
     */
    public static final String NON_ACCURATO = "0";

    /**
     * Primary key della tabella - getter.
     *
     * @return id job
     */
    Long getIdJob();

    /**
     * Primary key della tabella - setter.
     *
     * @param idJob id job
     */
    void setIdJob(Long idJob);

    /**
     * Nome job - getter. Chiave alternativa della tabella. Deve essere unico.
     *
     * @return nome del job
     */
    String getNmJob();

    /**
     * Nome job - setter.
     *
     * @param nmJob nome del job
     */
    void setNmJob(String nmJob);

    /**
     * Descrizione job - getter.
     *
     * @return descrizione del job
     */
    String getDsJob();

    /**
     * Descrizione job - setter.
     *
     * @param dsJob descrizione del job
     */
    void setDsJob(String dsJob);

    /**
     * Nome ambito - getter.
     *
     * @return nome dell'ambito
     */
    String getNmAmbito();

    /**
     * Nome ambito - setter.
     *
     * @param nmAmbito nome dell'ambito
     */
    void setNmAmbito(String nmAmbito);

    /**
     * Numero ordine esecuzione- getter.
     *
     * @return numero ordine
     */
    BigDecimal getNiOrdExec();

    /**
     * Numero ordine esecuzione - setter.
     *
     * @param niOrdExec numero ordine
     */
    void setNiOrdExec(BigDecimal niOrdExec);

    /**
     * Tipologia di schedulazione job (utilizzato per ZABBIX) - getter.
     *
     * @return tipologia di schedulazione
     */
    String getTiSchedJob();

    /**
     * Tipologia di schedulazione job (utilizzato per ZABBIX) - setter.
     *
     * @param tiSchedJob tipologia di schedulazione
     */
    void setTiSchedJob(String tiSchedJob);

    /**
     * Scopo del job - getter . Alcuni esempi:
     * <ul>
     * <li>VERSAMENTI</li>
     * <li>RECUPERO_VERS</li>
     * <li>INTEGRAZIONE_IAM</li>
     * </ul>
     *
     * @return codice scopo del job
     */
    String getTiScopoJob();

    /**
     * Scopo del job - setter.
     *
     * @param tiScopoJob codice scopo del job
     */
    void setTiScopoJob(String tiScopoJob);

    /**
     * Nodo del cluster assegnato per il job - getter.
     *
     * @return nodo assegnato
     */
    String getNmNodoAssegnato();

    /**
     * Nodo del cluster assegnato per il job - setter.
     *
     * @param nmNodoAssegnato nodo assegnato
     */
    void setNmNodoAssegnato(String nmNodoAssegnato);

    /**
     * Stato del timer - getter. Il timer può assumere uno stato tra quelli definiti in
     * {@link STATO_TIMER}
     *
     * @return stato del timer
     */
    String getTiStatoTimer();

    /**
     * Stato del timer - setter.
     *
     * @param tiStatoTimer stato del timer
     */
    void setTiStatoTimer(String tiStatoTimer);

    /**
     * Schedulazione: ora - getter.
     *
     * @return ora
     */
    String getCdSchedHour();

    /**
     * Schedulazione: ora - setter.
     *
     * @param cdSchedHour ora
     */
    void setCdSchedHour(String cdSchedHour);

    /**
     * Schedulazione: minuto - getter.
     *
     * @return minuto
     */
    String getCdSchedMinute();

    /**
     * Schedulazione: minuto - setter.
     *
     * @param cdSchedMinute minuto
     */
    void setCdSchedMinute(String cdSchedMinute);

    /**
     * Schedulazione: giorno del mese - getter.
     *
     * @return giorno del mese
     */
    String getCdSchedDayofmonth();

    /**
     * Schedulazione: giorno del mese - setter.
     *
     * @param cdSchedDayofmonth giorno del mese
     */
    void setCdSchedDayofmonth(String cdSchedDayofmonth);

    /**
     * Schedulazione: mese - getter.
     *
     * @return mese
     */
    String getCdSchedMonth();

    /**
     * Schedulazione: mese - setter.
     *
     * @param cdSchedMonth mese
     */
    void setCdSchedMonth(String cdSchedMonth);

    /**
     * Schedulazione: giorno della settimana - getter.
     *
     * @return giorno della settimana
     */
    String getCdSchedDayofweek();

    /**
     * Schedulazione: giorno della settimana - setter.
     *
     * @param cdSchedDayofweek giorno della settimana
     */
    void setCdSchedDayofweek(String cdSchedDayofweek);

    /**
     * Data di prossima attivazione - getter. La data di prossima attivazione viene impostata dallo
     * strato web (in maniera non accurata) oppure dal timer stesso.
     *
     * @return data di prossima attivazione
     */
    Date getDtProssimaAttivazione();

    /**
     * Data di prossima attivazione - setter.
     *
     * @param dtProssimaAttivazione data di prossima attivazione
     */
    void setDtProssimaAttivazione(Date dtProssimaAttivazione);

    /**
     * Flag di accuratezza della data - getter. Uno tra {@link #ACCURATO} e {@link #NON_ACCURATO}.
     *
     * @return flag di accuratezza
     */
    String getFlDataAccurata();

    /**
     * Flag di accuratezza della data - setter.
     *
     * @param flDataAccurata flag di accuratezza
     */
    void setFlDataAccurata(String flDataAccurata);
}
