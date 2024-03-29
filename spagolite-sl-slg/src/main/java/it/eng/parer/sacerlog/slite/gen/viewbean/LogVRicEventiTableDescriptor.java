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

package it.eng.parer.sacerlog.slite.gen.viewbean;

import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.Map;

import it.eng.spagoLite.db.oracle.bean.column.ColumnDescriptor;
import it.eng.spagoLite.db.oracle.bean.column.TableDescriptor;

/**
 * @author Sloth
 *
 *         Bean per la tabella Log_V_Ric_Eventi
 *
 */
public class LogVRicEventiTableDescriptor extends TableDescriptor {

    /*
     * @Generated( value = "it.eg.dbtool.db.oracle.beangen.Oracle4JPAClientBeanGen$ViewBeanWriter", comments =
     * "This class was generated by OraTool", date = "Monday, 27 March 2017 14:47" )
     */

    public static final String SELECT = "Select * from Log_V_Ric_Eventi /**/";
    public static final String TABLE_NAME = "Log_V_Ric_Eventi";
    public static final String COL_ID_APPLIC = "id_applic";
    public static final String COL_NM_APPLIC = "nm_applic";
    public static final String COL_ID_TIPO_EVENTO = "id_tipo_evento";
    public static final String COL_NM_TIPO_EVENTO = "nm_tipo_evento";
    public static final String COL_TIPO_CLASSE_EVENTO = "tipo_classe_evento";
    public static final String COL_ID_TIPO_OGGETTO = "id_tipo_oggetto";
    public static final String COL_NM_TIPO_OGGETTO = "nm_tipo_oggetto";
    public static final String COL_ID_EVENTO = "id_evento";
    public static final String COL_TIPO_AZIONE = "tipo_azione";
    public static final String COL_NM_GENERATORE_AZIONE = "nm_generatore_azione";
    public static final String COL_NM_AZIONE = "nm_azione";
    public static final String COL_ID_TRANSAZIONE = "id_transazione";
    public static final String COL_DT_REG_EVENTO = "dt_reg_evento";
    public static final String COL_ID_OGGETTO_EVENTO = "id_oggetto_evento";
    public static final String COL_DS_KEY_OGGETTO = "ds_key_oggetto";
    public static final String COL_ID_OGGETTO = "id_oggetto";
    public static final String COL_ID_ORGANIZ_APPLIC = "id_organiz_applic";
    public static final String COL_NM_AMBIENTE = "nm_ambiente";
    public static final String COL_NM_ENTE = "nm_ente";
    public static final String COL_NM_STRUTTURA = "nm_struttura";
    public static final String COL_NM_VERSATORE = "nm_versatore";
    public static final String COL_NM_OGGETTO = "nm_oggetto";
    public static final String COL_ID_AGENTE_EVENTO = "id_agente_evento";
    public static final String COL_TI_RUOLO_AGENTE_EVENTO = "ti_ruolo_agente_evento";
    public static final String COL_ID_AGENTE = "id_agente";
    public static final String COL_NM_AGENTE = "nm_agente";
    public static final String COL_TIPO_ORIGINE_AGENTE = "tipo_origine_agente";

    private static Map<String, ColumnDescriptor> map = new LinkedHashMap<String, ColumnDescriptor>();

    static {
        map.put(COL_ID_APPLIC, new ColumnDescriptor(COL_ID_APPLIC, Types.DECIMAL, 22, true));
        map.put(COL_NM_APPLIC, new ColumnDescriptor(COL_NM_APPLIC, Types.VARCHAR, 100, true));
        map.put(COL_ID_TIPO_EVENTO, new ColumnDescriptor(COL_ID_TIPO_EVENTO, Types.DECIMAL, 22, true));
        map.put(COL_NM_TIPO_EVENTO, new ColumnDescriptor(COL_NM_TIPO_EVENTO, Types.VARCHAR, 100, true));
        map.put(COL_TIPO_CLASSE_EVENTO, new ColumnDescriptor(COL_TIPO_CLASSE_EVENTO, Types.VARCHAR, 30, true));
        map.put(COL_ID_TIPO_OGGETTO, new ColumnDescriptor(COL_ID_TIPO_OGGETTO, Types.DECIMAL, 22, true));
        map.put(COL_NM_TIPO_OGGETTO, new ColumnDescriptor(COL_NM_TIPO_OGGETTO, Types.VARCHAR, 100, true));
        map.put(COL_ID_EVENTO, new ColumnDescriptor(COL_ID_EVENTO, Types.DECIMAL, 22, true));
        map.put(COL_TIPO_AZIONE, new ColumnDescriptor(COL_TIPO_AZIONE, Types.VARCHAR, 20, true));
        map.put(COL_NM_GENERATORE_AZIONE, new ColumnDescriptor(COL_NM_GENERATORE_AZIONE, Types.VARCHAR, 100, true));
        map.put(COL_NM_AZIONE, new ColumnDescriptor(COL_NM_AZIONE, Types.VARCHAR, 100, true));
        map.put(COL_ID_TRANSAZIONE, new ColumnDescriptor(COL_ID_TRANSAZIONE, Types.DECIMAL, 22, true));
        map.put(COL_DT_REG_EVENTO, new ColumnDescriptor(COL_DT_REG_EVENTO, Types.TIMESTAMP, 11, true));
        map.put(COL_ID_OGGETTO_EVENTO, new ColumnDescriptor(COL_ID_OGGETTO_EVENTO, Types.DECIMAL, 22, true));
        map.put(COL_DS_KEY_OGGETTO, new ColumnDescriptor(COL_DS_KEY_OGGETTO, Types.VARCHAR, 254, true));
        map.put(COL_ID_OGGETTO, new ColumnDescriptor(COL_ID_OGGETTO, Types.DECIMAL, 22, true));
        map.put(COL_ID_ORGANIZ_APPLIC, new ColumnDescriptor(COL_ID_ORGANIZ_APPLIC, Types.DECIMAL, 22, true));
        map.put(COL_NM_AMBIENTE, new ColumnDescriptor(COL_NM_AMBIENTE, Types.VARCHAR, 254, true));
        map.put(COL_NM_ENTE, new ColumnDescriptor(COL_NM_ENTE, Types.VARCHAR, 254, true));
        map.put(COL_NM_STRUTTURA, new ColumnDescriptor(COL_NM_STRUTTURA, Types.VARCHAR, 254, true));
        map.put(COL_NM_VERSATORE, new ColumnDescriptor(COL_NM_VERSATORE, Types.VARCHAR, 254, true));
        map.put(COL_NM_OGGETTO, new ColumnDescriptor(COL_NM_OGGETTO, Types.VARCHAR, 254, true));
        map.put(COL_ID_AGENTE_EVENTO, new ColumnDescriptor(COL_ID_AGENTE_EVENTO, Types.DECIMAL, 22, true));
        map.put(COL_TI_RUOLO_AGENTE_EVENTO, new ColumnDescriptor(COL_TI_RUOLO_AGENTE_EVENTO, Types.VARCHAR, 30, true));
        map.put(COL_ID_AGENTE, new ColumnDescriptor(COL_ID_AGENTE, Types.DECIMAL, 22, true));
        map.put(COL_NM_AGENTE, new ColumnDescriptor(COL_NM_AGENTE, Types.VARCHAR, 100, true));
        map.put(COL_TIPO_ORIGINE_AGENTE, new ColumnDescriptor(COL_TIPO_ORIGINE_AGENTE, Types.VARCHAR, 30, true));
    }

    public Map<String, ColumnDescriptor> getColumnMap() {
        return map;
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getStatement() {
        return SELECT;
    }

}
