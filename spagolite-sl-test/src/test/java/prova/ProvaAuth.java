/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prova;

import it.eng.spagoLite.security.User;
import it.eng.spagoLite.security.auth.Authenticator;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Iacolucci_M Classe finta creata soltanto per darla come risorsa iniettabile a openejb perché una classe di
 *         spagolite-middle richiede questa risorsa!
 */
@Stateless
@LocalBean
public class ProvaAuth extends Authenticator {

    @Override
    protected String getAppName() {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }

    @Override
    public User recuperoAutorizzazioni(HttpSession hs) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }

}
