/**

    Copyright 2004, 2007 Engineering Ingegneria Informatica S.p.A.

    This file is part of Spago.

    Spago is free software; you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation; either version 2.1 of the License, or
    any later version.

    Spago is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Spago; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

 **/
package it.eng.spagoCore.error;

import java.io.Serializable;

/**
 * 
 * @author Enrico Grillini
 * 
 */
public class EMFError extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String INFORMATION = "INFORMATION";
    public static final String WARNING = "WARNING";
    public static final String ERROR = "ERROR";
    public static final String BLOCKING = "BLOCKING";

    private String severity;
    private String description;
    private Throwable throwable;

    public EMFError(String severity, String description, Throwable throwable) {
        this.severity = severity;
        this.description = description;
        this.throwable = throwable;
    }

    public EMFError(String severity, String description) {
        this(severity, description, null);
    }

    public EMFError(String severity, Throwable throwable) {
        this(severity, null, throwable);
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public boolean isOk(String serverity) {
        if (getSeverity() == null) {
            return true;
        }

        if (getSeverity().equalsIgnoreCase(serverity)) {
            return true;
        } else {
            return false;
        }
    }

}
