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

import it.eng.spagoLite.xmlbean.form.EndPage;
import it.eng.spagoLite.xmlbean.form.Step;
import it.eng.spagoLite.xmlbean.form.Wizard;
import it.eng.util.ClassUtil;

import java.io.IOException;
import java.io.Writer;

import org.apache.xmlbeans.XmlCursor;

public class WizardWriter {

    private Wizard wizard;

    public WizardWriter(Wizard wizard) {
        this.wizard = wizard;
    }

    public void write(Writer writer) throws IOException {

        writer.write("  public static class " + ClassUtil.getClassName(wizard.getName())
                + " extends Wizard {\n");
        writer.write("\n");
        writer.write("    public static String NAME = \"" + ClassUtil.getClassName(wizard.getName())
                + "\";\n");
        writer.write(
                "    public static String DESCRIPTION = \"" + wizard.getDescription() + "\";\n");

        writeConstants(writer);

        writer.write("\n");
        writer.write("    public " + ClassUtil.getClassName(wizard.getName()) + "() {\n");
        writer.write(
                "      super(null, NAME, DESCRIPTION, " + wizard.isSetHideStepCount() + ");\n");
        writeAdds(writer);
        writer.write("      goToFirstStep();\n");
        writer.write("    }\n");
        writer.write("\n");

        writeGets(writer);

        writer.write("  }\n");
    }

    private void writeConstants(Writer writer) throws IOException {
        writer.write("\n");

        try (XmlCursor cursor = wizard.newCursor()) {
            if (cursor.toFirstChild()) {
                int i = 0;
                do {
                    WizardElementWriter wizardElementWriter = null;
                    if (cursor.getObject() instanceof Step) {
                        wizardElementWriter = new StepWriter((Step) cursor.getObject(), i);

                    } else if (cursor.getObject() instanceof EndPage) {
                        wizardElementWriter = new EndPageWriter((EndPage) cursor.getObject());
                    }

                    if (wizardElementWriter != null) {
                        wizardElementWriter.writeConstant(writer);
                    }
                    i++;
                } while (cursor.toNextSibling());
            }
        }

        writer.write("\n");

        try (XmlCursor cursor = wizard.newCursor()) {
            if (cursor.toFirstChild()) {
                int i = 0;
                do {
                    WizardElementWriter wizardElementWriter = null;
                    if (cursor.getObject() instanceof Step) {
                        wizardElementWriter = new StepWriter((Step) cursor.getObject(), i);

                    } else if (cursor.getObject() instanceof EndPage) {
                        wizardElementWriter = new EndPageWriter((EndPage) cursor.getObject());
                    }

                    if (wizardElementWriter != null) {
                        wizardElementWriter.writeConstantFull(writer);
                    }
                    i++;
                } while (cursor.toNextSibling());
            }
        }
    }

    private void writeAdds(Writer writer) throws IOException {
        writer.write("\n");

        try (XmlCursor cursor = wizard.newCursor()) {
            if (cursor.toFirstChild()) {
                int i = 0;
                do {
                    WizardElementWriter wizardElementWriter = null;
                    if (cursor.getObject() instanceof Step) {
                        wizardElementWriter = new StepWriter((Step) cursor.getObject(), i);

                    } else if (cursor.getObject() instanceof EndPage) {
                        wizardElementWriter = new EndPageWriter((EndPage) cursor.getObject());
                    }

                    if (wizardElementWriter != null) {
                        wizardElementWriter.writeAdd(writer);
                    }
                    i++;
                } while (cursor.toNextSibling());
            }
        }
    }

    private void writeGets(Writer writer) throws IOException {
        writer.write("\n");

        try (XmlCursor cursor = wizard.newCursor()) {
            if (cursor.toFirstChild()) {
                int i = 0;
                do {
                    WizardElementWriter wizardElementWriter = null;
                    if (cursor.getObject() instanceof Step) {
                        wizardElementWriter = new StepWriter((Step) cursor.getObject(), i);

                    } else if (cursor.getObject() instanceof EndPage) {
                        wizardElementWriter = new EndPageWriter((EndPage) cursor.getObject());
                    }

                    if (wizardElementWriter != null) {
                        wizardElementWriter.writeGet(writer);
                    }
                    i++;
                } while (cursor.toNextSibling());
            }
        }
    }
}
