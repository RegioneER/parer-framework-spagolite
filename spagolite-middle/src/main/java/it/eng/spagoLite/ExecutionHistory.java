/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna <p/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version. <p/> This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. <p/> You should
 * have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
 */

package it.eng.spagoLite;

import java.io.Serializable;

import it.eng.spagoLite.form.Form;

public class ExecutionHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private Form form;
    private boolean isAction;
    // true if last execution is a forward, false a redirect
    private boolean isForward;
    // se l'action è pubblicata
    private boolean isActionPublished = true;
    // nome del publisher a cui la back deve rimandare in caso di
    // forwardBackToAction
    private String publisherName;
    // eventuali parametri da mandare al publisher quando si torna in back (ad
    // esempio il parametro table)
    private String backParameter = "";

    public ExecutionHistory(String name, Form form, boolean isAction, boolean isForward) {
	super();
	this.name = name;
	this.form = form;
	this.isAction = isAction;
	this.isForward = isForward;
    }

    public Form getForm() {
	return form;
    }

    public void setForm(Form form) {
	this.form = form;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public boolean isAction() {
	return isAction;
    }

    public void setAction(boolean isAction) {
	this.isAction = isAction;
    }

    public boolean isForward() {
	return isForward;
    }

    public void setForward(boolean isForward) {
	this.isForward = isForward;
    }

    public boolean isActionPublished() {
	return isActionPublished;
    }

    public void setActionPublished(boolean isActionPublished) {
	this.isActionPublished = isActionPublished;
    }

    public String getPublisherName() {
	return publisherName;
    }

    public void setPublisherName(String publisherName) {
	this.publisherName = publisherName;
    }

    public String getBackParameter() {
	return backParameter;
    }

    public void setBackParameter(String backParameter) {
	this.backParameter = backParameter;
    }

}
