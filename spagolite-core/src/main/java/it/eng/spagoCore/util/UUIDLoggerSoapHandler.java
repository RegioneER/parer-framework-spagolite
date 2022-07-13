/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.spagoCore.util;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 *
 * @author sinatti_s
 */
public class UUIDLoggerSoapHandler implements SOAPHandler<SOAPMessageContext> {

    @Override
    public void close(MessageContext context) {
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        // LOG UUID
        UUIDMdcLogUtil.genUuid();
        return true;
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

}
