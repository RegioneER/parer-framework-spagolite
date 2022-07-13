/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.spagoLite.security.exception;

import it.eng.spagoLite.security.exception.AuthWSException.CodiceErrore;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Gilioli_P
 */
public class SOAPFaultInfo implements Serializable {

    @XmlElement(name = "faultcode")
    private CodiceErrore faultCode;
    @XmlElement(name = "faulstring")
    private String faultString;

    public SOAPFaultInfo() {
    }

    public SOAPFaultInfo(CodiceErrore faultCode, String faultString) {
        this.faultCode = faultCode;
        this.faultString = faultString;
    }

    public CodiceErrore getFaultCode() {
        return faultCode;
    }

    public void setFaultCode(CodiceErrore faultCode) {
        this.faultCode = faultCode;
    }

    public String getFaultString() {
        return faultString;
    }

    public void setFaultString(String faultString) {
        this.faultString = faultString;
    }

}
