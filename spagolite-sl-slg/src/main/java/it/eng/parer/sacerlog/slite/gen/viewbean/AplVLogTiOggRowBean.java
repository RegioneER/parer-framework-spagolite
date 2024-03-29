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

import java.math.BigDecimal;

import it.eng.parer.sacerlog.viewEntity.AplVLogTiOgg;
import it.eng.spagoLite.db.base.BaseRowInterface;
import it.eng.spagoLite.db.base.JEEBaseRowInterface;
import it.eng.spagoLite.db.base.row.BaseRow;
import it.eng.spagoLite.db.oracle.bean.column.TableDescriptor;

/**
 * RowBean per la tabella Apl_V_LOG_TI_OGG
 *
 */
public class AplVLogTiOggRowBean extends BaseRow implements BaseRowInterface, JEEBaseRowInterface {

    /*
     * @Generated( value = "it.eg.dbtool.db.oracle.beangen.Oracle4JPAClientBeanGen$ViewBeanWriter", comments =
     * "This class was generated by OraTool", date = "Thursday, 23 March 2017 16:02" )
     */

    public static AplVLogTiOggTableDescriptor TABLE_DESCRIPTOR = new AplVLogTiOggTableDescriptor();

    public AplVLogTiOggRowBean() {
        super();
    }

    public TableDescriptor getTableDescriptor() {
        return TABLE_DESCRIPTOR;
    }

    public BigDecimal getIdTipoOggetto() {
        return getBigDecimal("id_tipo_oggetto");
    }

    public void setIdTipoOggetto(BigDecimal id_tipo_oggetto) {
        setObject("id_tipo_oggetto", id_tipo_oggetto);
    }

    public BigDecimal getIdApplic() {
        return getBigDecimal("id_applic");
    }

    public void setIdApplic(BigDecimal id_applic) {
        setObject("id_applic", id_applic);
    }

    public String getNmApplic() {
        return getString("nm_applic");
    }

    public void setNmApplic(String nm_applic) {
        setObject("nm_applic", nm_applic);
    }

    public String getNmTipoOggetto() {
        return getString("nm_tipo_oggetto");
    }

    public void setNmTipoOggetto(String nm_tipo_oggetto) {
        setObject("nm_tipo_oggetto", nm_tipo_oggetto);
    }

    public BigDecimal getIdTipoOggettoPadre() {
        return getBigDecimal("id_tipo_oggetto_padre");
    }

    public void setIdTipoOggettoPadre(BigDecimal id_tipo_oggetto_padre) {
        setObject("id_tipo_oggetto_padre", id_tipo_oggetto_padre);
    }

    @Override
    public void entityToRowBean(Object obj) {
        AplVLogTiOgg entity = (AplVLogTiOgg) obj;
        this.setIdTipoOggetto(entity.getIdTipoOggetto());
        this.setIdApplic(entity.getIdApplic());
        this.setNmApplic(entity.getNmApplic());
        this.setNmTipoOggetto(entity.getNmTipoOggetto());
        this.setIdTipoOggettoPadre(entity.getIdTipoOggettoPadre());
    }

    @Override
    public AplVLogTiOgg rowBeanToEntity() {
        AplVLogTiOgg entity = new AplVLogTiOgg();
        entity.setIdTipoOggetto(this.getIdTipoOggetto());
        entity.setIdApplic(this.getIdApplic());
        entity.setNmApplic(this.getNmApplic());
        entity.setNmTipoOggetto(this.getNmTipoOggetto());
        entity.setIdTipoOggettoPadre(this.getIdTipoOggettoPadre());
        return entity;
    }

    // gestione della paginazione
    public void setRownum(Integer rownum) {
        setObject("rownum", rownum);
    }

    public Integer getRownum() {
        return Integer.parseInt(getObject("rownum").toString());
    }

    public void setRnum(Integer rnum) {
        setObject("rnum", rnum);
    }

    public Integer getRnum() {
        return Integer.parseInt(getObject("rnum").toString());
    }

    public void setNumrecords(Integer numRecords) {
        setObject("numrecords", numRecords);
    }

    public Integer getNumrecords() {
        return Integer.parseInt(getObject("numrecords").toString());
    }

}
