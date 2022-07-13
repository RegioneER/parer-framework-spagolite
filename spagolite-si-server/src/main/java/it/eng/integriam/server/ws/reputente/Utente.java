package it.eng.integriam.server.ws.reputente;

import java.util.Date;

public class Utente {

    private long idUserIam;
    private String cdFisc;
    private String cdPsw;
    private String cdSalt;
    private String dsEmail;
    private Date dtRegPsw;
    private Date dtScadPsw;
    private String flAttivo;
    private String flContrIp;
    private String flUserAdmin;
    private String nmCognomeUser;
    private String nmNomeUser;
    private String nmUserid;
    private String tipoUser;
    private String tipoAuth;
    private ListaOrganizAbil listaOrganizAbil;
    private ListaIndIp listaIndIp;

    public long getIdUserIam() {
        return idUserIam;
    }

    public void setIdUserIam(long idUserIam) {
        this.idUserIam = idUserIam;
    }

    public String getCdFisc() {
        return cdFisc;
    }

    public void setCdFisc(String cdFisc) {
        this.cdFisc = cdFisc;
    }

    public String getCdPsw() {
        return cdPsw;
    }

    public void setCdPsw(String cdPsw) {
        this.cdPsw = cdPsw;
    }

    public String getCdSalt() {
        return cdSalt;
    }

    public void setCdSalt(String cdSalt) {
        this.cdSalt = cdSalt;
    }

    public String getDsEmail() {
        return dsEmail;
    }

    public void setDsEmail(String dsEmail) {
        this.dsEmail = dsEmail;
    }

    public Date getDtRegPsw() {
        return dtRegPsw;
    }

    public void setDtRegPsw(Date dtRegPsw) {
        this.dtRegPsw = dtRegPsw;
    }

    public Date getDtScadPsw() {
        return dtScadPsw;
    }

    public void setDtScadPsw(Date dtScadPsw) {
        this.dtScadPsw = dtScadPsw;
    }

    public String getFlAttivo() {
        return flAttivo;
    }

    public void setFlAttivo(String flAttivo) {
        this.flAttivo = flAttivo;
    }

    public String getFlContrIp() {
        return flContrIp;
    }

    public void setFlContrIp(String flContrIp) {
        this.flContrIp = flContrIp;
    }

    public String getFlUserAdmin() {
        return flUserAdmin;
    }

    public void setFlUserAdmin(String flUserAdmin) {
        this.flUserAdmin = flUserAdmin;
    }

    public String getNmCognomeUser() {
        return nmCognomeUser;
    }

    public void setNmCognomeUser(String nmCognomeUser) {
        this.nmCognomeUser = nmCognomeUser;
    }

    public String getNmNomeUser() {
        return nmNomeUser;
    }

    public void setNmNomeUser(String nmNomeUser) {
        this.nmNomeUser = nmNomeUser;
    }

    public String getNmUserid() {
        return nmUserid;
    }

    public void setNmUserid(String nmUserid) {
        this.nmUserid = nmUserid;
    }

    public String getTipoUser() {
        return tipoUser;
    }

    public void setTipoUser(String tipoUser) {
        this.tipoUser = tipoUser;
    }

    public String getTipoAuth() {
        return this.tipoAuth;
    }

    public void setTipoAuth(String tipoAuth) {
        this.tipoAuth = tipoAuth;
    }

    public ListaOrganizAbil getListaOrganizAbil() {
        return listaOrganizAbil;
    }

    public void setListaOrganizAbil(ListaOrganizAbil listaOrganizAbil) {
        this.listaOrganizAbil = listaOrganizAbil;
    }

    public ListaIndIp getListaIndIp() {
        return listaIndIp;
    }

    public void setListaIndIp(ListaIndIp listaIndIp) {
        this.listaIndIp = listaIndIp;
    }
}
