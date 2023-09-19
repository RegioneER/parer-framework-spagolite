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
