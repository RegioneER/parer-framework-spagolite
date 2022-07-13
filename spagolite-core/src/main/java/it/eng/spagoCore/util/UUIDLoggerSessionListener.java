/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.spagoCore.util;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author sinatti_s
 */
public class UUIDLoggerSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent hse) {
        // LOG UUID
        UUIDMdcLogUtil.genUuid();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        // metodo deliberatamente vuoto.
        // non ho alcun interesse ad intercettare questo evento, per ora.
    }

}
