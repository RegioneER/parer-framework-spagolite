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

package it.eng.parer.jboss.timer.common.entity;

import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(JobDefinition.class)
public abstract class JobDefinition_ {

    public static volatile SingularAttribute<JobDefinition, String> nmNodoAssegnato;
    public static volatile SingularAttribute<JobDefinition, String> tiStatoTimer;
    public static volatile SingularAttribute<JobDefinition, String> cdSchedHour;
    public static volatile SingularAttribute<JobDefinition, String> cdSchedMinute;
    public static volatile SingularAttribute<JobDefinition, String> cdSchedDayofmonth;
    public static volatile SingularAttribute<JobDefinition, String> cdSchedMonth;
    public static volatile SingularAttribute<JobDefinition, String> cdSchedDayofweek;
    public static volatile SingularAttribute<JobDefinition, String> flDataAccurata;
    public static volatile SingularAttribute<JobDefinition, Date> dtProssimaAttivazione;

    public static final String NM_NODO_ASSEGNATO = "nmNodoAssegnato";
    public static final String TI_STATO_TIMER = "tiStatoTimer";
    public static final String CD_SCHED_HOUR = "cdSchedHour";
    public static final String CD_SCHED_MINUTE = "cdSchedMinute";
    public static final String CD_SCHED_DAYOFMONTH = "cdSchedDayofmonth";
    public static final String CD_SCHED_MONTH = "cdSchedMonth";
    public static final String CD_SCHED_DAYOFWEEK = "cdSchedDayofweek";
    public static final String DT_PROSSIMA_ATTIVAZIONE = "dtProssimaAttivazione";
    public static final String FL_DATA_ACCURATA = "flDataAccurata";
}
