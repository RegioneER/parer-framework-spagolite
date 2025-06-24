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

package it.eng.parer.sacerlog.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the LOG_EVENTO_LOGIN_USER database table.
 *
 */
@Entity
@Table(schema = "SACER_LOG", name = "LOG_EVENTO_LOGIN_USER")
@NamedQuery(name = "LogEventoLoginUser.findAll", query = "SELECT l FROM LogEventoLoginUser l")
public class LogEventoLoginUser implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idEventoLoginUser;
    private String cdIndIpClient;
    private String cdIndServer;
    private String dsEvento;
    private Date dtEvento;
    private String nmUserid;
    private String tipoEvento;
    private String nmNomeUser;
    private String nmCognomeUser;
    private String cdFiscUser;
    private String dsEmailUser;
    private String cdIDEsterno;

    @Id
    @SequenceGenerator(name = "LOG_EVENTO_LOGIN_USER_IDEVENTOLOGINUSER_GENERATOR", schema = "SACER_LOG", sequenceName = "SLOG_EVENTO_LOGIN_USER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_EVENTO_LOGIN_USER_IDEVENTOLOGINUSER_GENERATOR")
    @Column(name = "ID_EVENTO_LOGIN_USER")
    public Long getIdEventoLoginUser() {
        return this.idEventoLoginUser;
    }

    public void setIdEventoLoginUser(Long idEventoLoginUser) {
        this.idEventoLoginUser = idEventoLoginUser;
    }

    @Column(name = "CD_IND_IP_CLIENT")
    public String getCdIndIpClient() {
        return this.cdIndIpClient;
    }

    public void setCdIndIpClient(String cdIndIpClient) {
        this.cdIndIpClient = cdIndIpClient;
    }

    @Column(name = "CD_IND_SERVER")
    public String getCdIndServer() {
        return this.cdIndServer;
    }

    public void setCdIndServer(String cdIndServer) {
        this.cdIndServer = cdIndServer;
    }

    @Column(name = "DS_EVENTO")
    public String getDsEvento() {
        return this.dsEvento;
    }

    public void setDsEvento(String dsEvento) {
        this.dsEvento = dsEvento;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DT_EVENTO")
    public Date getDtEvento() {
        return this.dtEvento;
    }

    public void setDtEvento(Date dtEvento) {
        this.dtEvento = dtEvento;
    }

    @Column(name = "NM_USERID")
    public String getNmUserid() {
        return this.nmUserid;
    }

    public void setNmUserid(String nmUserid) {
        this.nmUserid = nmUserid;
    }

    @Column(name = "TIPO_EVENTO")
    public String getTipoEvento() {
        return this.tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    @Column(name = "NM_NOME_USER")
    public String getNmNomeUser() {
        return nmNomeUser;
    }

    public void setNmNomeUser(String nmNomeUser) {
        this.nmNomeUser = nmNomeUser;
    }

    @Column(name = "NM_COGNOME_USER")
    public String getNmCognomeUser() {
        return nmCognomeUser;
    }

    public void setNmCognomeUser(String nmCognomeUser) {
        this.nmCognomeUser = nmCognomeUser;
    }

    @Column(name = "CD_FISC_USER")
    public String getCdFiscUser() {
        return cdFiscUser;
    }

    public void setCdFiscUser(String cdFiscUser) {
        this.cdFiscUser = cdFiscUser;
    }

    @Column(name = "DS_EMAIL_USER")
    public String getDsEmailUser() {
        return dsEmailUser;
    }

    public void setDsEmailUser(String dsEmailUser) {
        this.dsEmailUser = dsEmailUser;
    }

    @Column(name = "CD_ID_ESTERNO")
    public String getCdIDEsterno() {
        return cdIDEsterno;
    }

    public void setCdIDEsterno(String cdIDEsterno) {
        this.cdIDEsterno = cdIDEsterno;
    }

}
