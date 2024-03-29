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

import it.eng.parer.sacerlog.viewEntity.LogVUsrAbilOrganiz;
import it.eng.parer.sacerlog.viewEntity.LogVUsrAbilOrganizId;
import it.eng.spagoLite.db.base.BaseRowInterface;
import it.eng.spagoLite.db.base.JEEBaseRowInterface;
import it.eng.spagoLite.db.base.row.BaseRow;
import it.eng.spagoLite.db.oracle.bean.column.TableDescriptor;

/**
 * RowBean per la tabella Usr_V_Abil_Organiz
 *
 */
public class LogVUsrAbilOrganizRowBean extends BaseRow implements BaseRowInterface, JEEBaseRowInterface {

    /*
     * @Generated( value = "it.eg.dbtool.db.oracle.beangen.Oracle4JPAClientBeanGen$ViewBeanWriter", comments =
     * "This class was generated by OraTool", date = "Friday, 24 March 2017 14:00" )
     */

    public static LogVUsrAbilOrganizTableDescriptor TABLE_DESCRIPTOR = new LogVUsrAbilOrganizTableDescriptor();

    public LogVUsrAbilOrganizRowBean() {
        super();
    }

    public TableDescriptor getTableDescriptor() {
        return TABLE_DESCRIPTOR;
    }

    public BigDecimal getIdUserIam() {
        return getBigDecimal("id_user_iam");
    }

    public void setIdUserIam(BigDecimal id_user_iam) {
        setObject("id_user_iam", id_user_iam);
    }

    public String getFlAttivo() {
        return getString("fl_attivo");
    }

    public void setFlAttivo(String fl_attivo) {
        setObject("fl_attivo", fl_attivo);
    }

    public BigDecimal getIdUsoUserApplic() {
        return getBigDecimal("id_uso_user_applic");
    }

    public void setIdUsoUserApplic(BigDecimal id_uso_user_applic) {
        setObject("id_uso_user_applic", id_uso_user_applic);
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

    public BigDecimal getIdDichAbilOrganiz() {
        return getBigDecimal("id_dich_abil_organiz");
    }

    public void setIdDichAbilOrganiz(BigDecimal id_dich_abil_organiz) {
        setObject("id_dich_abil_organiz", id_dich_abil_organiz);
    }

    public BigDecimal getIdOrganizIam() {
        return getBigDecimal("id_organiz_iam");
    }

    public void setIdOrganizIam(BigDecimal id_organiz_iam) {
        setObject("id_organiz_iam", id_organiz_iam);
    }

    public BigDecimal getIdOrganizApplic() {
        return getBigDecimal("id_organiz_applic");
    }

    public void setIdOrganizApplic(BigDecimal id_organiz_applic) {
        setObject("id_organiz_applic", id_organiz_applic);
    }

    public BigDecimal getIdTipoOrganiz() {
        return getBigDecimal("id_tipo_organiz");
    }

    public void setIdTipoOrganiz(BigDecimal id_tipo_organiz) {
        setObject("id_tipo_organiz", id_tipo_organiz);
    }

    public String getNmTipoOrganiz() {
        return getString("nm_tipo_organiz");
    }

    public void setNmTipoOrganiz(String nm_tipo_organiz) {
        setObject("nm_tipo_organiz", nm_tipo_organiz);
    }

    public String getNmOrganiz() {
        return getString("nm_organiz");
    }

    public void setNmOrganiz(String nm_organiz) {
        setObject("nm_organiz", nm_organiz);
    }

    public String getDsOrganiz() {
        return getString("ds_organiz");
    }

    public void setDsOrganiz(String ds_organiz) {
        setObject("ds_organiz", ds_organiz);
    }

    public BigDecimal getIdOrganizIamPadre() {
        return getBigDecimal("id_organiz_iam_padre");
    }

    public void setIdOrganizIamPadre(BigDecimal id_organiz_iam_padre) {
        setObject("id_organiz_iam_padre", id_organiz_iam_padre);
    }

    public String getDlCompositoOrganiz() {
        return getString("dl_composito_organiz");
    }

    public void setDlCompositoOrganiz(String dl_composito_organiz) {
        setObject("dl_composito_organiz", dl_composito_organiz);
    }

    public String getDlPathIdOrganizIam() {
        return getString("dl_path_id_organiz_iam");
    }

    public void setDlPathIdOrganizIam(String dl_path_id_organiz_iam) {
        setObject("dl_path_id_organiz_iam", dl_path_id_organiz_iam);
    }

    @Override
    public void entityToRowBean(Object obj) {
        LogVUsrAbilOrganiz entity = (LogVUsrAbilOrganiz) obj;
        this.setIdUserIam(entity.getLogVUsrAbilOrganizId().getIdUserIam());
        this.setFlAttivo(entity.getFlAttivo());
        this.setIdUsoUserApplic(entity.getIdUsoUserApplic());
        this.setIdApplic(entity.getIdApplic());
        this.setNmApplic(entity.getNmApplic());
        this.setIdDichAbilOrganiz(entity.getIdDichAbilOrganiz());
        this.setIdOrganizIam(entity.getLogVUsrAbilOrganizId().getIdOrganizIam());
        this.setIdOrganizApplic(entity.getIdOrganizApplic());
        this.setIdTipoOrganiz(entity.getIdTipoOrganiz());
        this.setNmTipoOrganiz(entity.getNmTipoOrganiz());
        this.setNmOrganiz(entity.getNmOrganiz());
        this.setDsOrganiz(entity.getDsOrganiz());
        this.setIdOrganizIamPadre(entity.getIdOrganizIamPadre());
        this.setDlCompositoOrganiz(entity.getDlCompositoOrganiz());
        this.setDlPathIdOrganizIam(entity.getDlPathIdOrganizIam());
    }

    @Override
    public LogVUsrAbilOrganiz rowBeanToEntity() {
        LogVUsrAbilOrganiz entity = new LogVUsrAbilOrganiz();
        entity.setLogVUsrAbilOrganizId(new LogVUsrAbilOrganizId());
        entity.getLogVUsrAbilOrganizId().setIdUserIam(this.getIdUserIam());
        entity.setFlAttivo(this.getFlAttivo());
        entity.setIdUsoUserApplic(this.getIdUsoUserApplic());
        entity.setIdApplic(this.getIdApplic());
        entity.setNmApplic(this.getNmApplic());
        entity.setIdDichAbilOrganiz(this.getIdDichAbilOrganiz());
        entity.getLogVUsrAbilOrganizId().setIdOrganizIam(this.getIdOrganizIam());
        entity.setIdOrganizApplic(this.getIdOrganizApplic());
        entity.setIdTipoOrganiz(this.getIdTipoOrganiz());
        entity.setNmTipoOrganiz(this.getNmTipoOrganiz());
        entity.setNmOrganiz(this.getNmOrganiz());
        entity.setDsOrganiz(this.getDsOrganiz());
        entity.setIdOrganizIamPadre(this.getIdOrganizIamPadre());
        entity.setDlCompositoOrganiz(this.getDlCompositoOrganiz());
        entity.setDlPathIdOrganizIam(this.getDlPathIdOrganizIam());
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
