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

package it.eng.spagoLite.security;

import it.eng.spagoLite.security.menu.impl.Menu;
import it.eng.spagoLite.security.profile.Profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BaseUser<T extends Profile> implements IUser<T> {
    private static final long serialVersionUID = 1L;

    private Menu menu;

    private long idUtente = 0;
    private String username = null;
    private String cognome = "";
    private String nome = "";
    private T profile;
    private List<String> applicazioni = null;
    private boolean attivo;
    private Date scadenzaPwd;

    // ***** Aggiunti per la gestione dei diversi tipi di IDP
    private UserType userType = null;
    // ID di un eventiale IDP esterno (Es.: SPID (SpidID)
    private String externalId = null;
    private String email = null;

    public BaseUser(String codice, String descr) {
        this.menu = new Menu(codice, descr);
        this.profile = null;
        this.applicazioni = new ArrayList<String>();
    }

    @Override
    public Menu getMenu() {
        return menu;
    }

    @Override
    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getCognome() {
        return cognome;
    }

    @Override
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public T getProfile() {
        return profile;
    }

    @Override
    public void setProfile(T profile) {
        this.profile = profile;
    }

    @Override
    public long getIdUtente() {
        return idUtente;
    }

    @Override
    public void setIdUtente(long idUtente) {
        this.idUtente = idUtente;
    }

    @Override
    public boolean isAttivo() {

        return attivo;
    }

    @Override
    public void setAttivo(boolean attivo) {
        this.attivo = attivo;
    }

    @Override
    public Date getScadenzaPwd() {
        // TODO Auto-generated method stub
        return scadenzaPwd;
    }

    @Override
    public void setScadenzaPwd(Date scadenzaPwd) {
        this.scadenzaPwd = scadenzaPwd;
    }

    @Override
    public List<String> getApplicazioni() {
        // TODO Auto-generated method stub
        return applicazioni;
    }

    @Override
    public void setApplicazioni(List<String> applicazioni) {
        this.applicazioni = applicazioni;
    }

    @Override
    public Map<String, String> getOrganizzazioneMap() {
        // TODO Auto-generated method stub
        return null;
    }

    // ***** Aggiunti per la gestione dei diversi tipi di IDP
    @Override
    public UserType getUserType() {
        return userType;
    }

    @Override
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Override
    public String getExternalId() {
        return externalId;
    }

    @Override
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }
}
