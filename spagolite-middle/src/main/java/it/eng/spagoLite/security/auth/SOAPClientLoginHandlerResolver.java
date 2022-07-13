package it.eng.spagoLite.security.auth;

import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

public class SOAPClientLoginHandlerResolver implements HandlerResolver {

    @Override
    public List<Handler> getHandlerChain(PortInfo portInfo) {
        List<Handler> handlerChain = new ArrayList<Handler>();
        SOAPClientLoginHandler hh = new SOAPClientLoginHandler();
        handlerChain.add(hh);
        return handlerChain;
    }
}