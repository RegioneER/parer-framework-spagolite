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

package it.eng.util.formWriter;

import it.eng.spagoLite.xmlbean.form.Step;
import it.eng.util.formWriter.base.ElementWriter;

import java.io.IOException;
import java.io.Writer;

public class StepWriter extends ElementWriter<Step> implements WizardElementWriter {

    // serve ad inizializzare il primo step come current
    private int position;

    public StepWriter(Step element, int position) {
        super(element);
        this.position = position;
    }

    public void writeAdd(Writer writer) throws IOException {
        writer.write("      addComponent(new Step(this, " + getConstantName().toLowerCase() + ", \""
                + getElement().getDescription() + "\", " + getElement().isSetSummary() + ", "
                + ((position == 0) ? true : getElement().getCurrent()) + ", "
                + getElement().isSetHidden() + "));\n");
    }

    public void writeGet(Writer writer) throws IOException {
        writer.write("    public Step get" + getClassName() + "() {\n");
        writer.write(
                "      return (Step) getComponent(" + getConstantName().toLowerCase() + ");\n");
        writer.write("    }\n");
        writer.write("\n");
    }

}
