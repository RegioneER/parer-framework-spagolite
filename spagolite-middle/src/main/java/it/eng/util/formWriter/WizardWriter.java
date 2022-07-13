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

        writer.write("  public static class " + ClassUtil.getClassName(wizard.getName()) + " extends Wizard {\n");
        writer.write("\n");
        writer.write("    public static String NAME = \"" + ClassUtil.getClassName(wizard.getName()) + "\";\n");
        writer.write("    public static String DESCRIPTION = \"" + wizard.getDescription() + "\";\n");

        writeConstants(writer);

        writer.write("\n");
        writer.write("    public " + ClassUtil.getClassName(wizard.getName()) + "() {\n");
        writer.write("      super(null, NAME, DESCRIPTION, " + wizard.isSetHideStepCount() + ");\n");
        writeAdds(writer);
        writer.write("      goToFirstStep();\n");
        writer.write("    }\n");
        writer.write("\n");

        writeGets(writer);

        writer.write("  }\n");
    }

    private void writeConstants(Writer writer) throws IOException {
        writer.write("\n");

        XmlCursor cursor = wizard.newCursor();
        cursor.toFirstChild();
        for (int i = 0;; i++) {
            WizardElementWriter wizardElementWriter = null;
            if (cursor.getObject() instanceof Step) {
                wizardElementWriter = new StepWriter((Step) cursor.getObject(), i);

            } else if (cursor.getObject() instanceof EndPage) {
                wizardElementWriter = new EndPageWriter((EndPage) cursor.getObject());
            }
            wizardElementWriter.writeConstant(writer);

            if (!cursor.toNextSibling()) {
                break;
            }
        }

        writer.write("\n");
        cursor = wizard.newCursor();
        cursor.toFirstChild();
        for (int i = 0;; i++) {
            WizardElementWriter wizardElementWriter = null;
            if (cursor.getObject() instanceof Step) {
                wizardElementWriter = new StepWriter((Step) cursor.getObject(), i);

            } else if (cursor.getObject() instanceof EndPage) {
                wizardElementWriter = new EndPageWriter((EndPage) cursor.getObject());
            }

            wizardElementWriter.writeConstantFull(writer);

            if (!cursor.toNextSibling()) {
                break;
            }
        }
    }

    private void writeAdds(Writer writer) throws IOException {
        writer.write("\n");
        XmlCursor cursor = wizard.newCursor();
        cursor.toFirstChild();
        for (int i = 0;; i++) {
            WizardElementWriter wizardElementWriter = null;
            if (cursor.getObject() instanceof Step) {
                wizardElementWriter = new StepWriter((Step) cursor.getObject(), i);

            } else if (cursor.getObject() instanceof EndPage) {
                wizardElementWriter = new EndPageWriter((EndPage) cursor.getObject());
            }
            wizardElementWriter.writeAdd(writer);

            if (!cursor.toNextSibling()) {
                break;
            }
        }
    }

    private void writeGets(Writer writer) throws IOException {
        writer.write("\n");
        XmlCursor cursor = wizard.newCursor();
        cursor.toFirstChild();
        for (int i = 0;; i++) {
            WizardElementWriter wizardElementWriter = null;
            if (cursor.getObject() instanceof Step) {
                wizardElementWriter = new StepWriter((Step) cursor.getObject(), i);

            } else if (cursor.getObject() instanceof EndPage) {
                wizardElementWriter = new EndPageWriter((EndPage) cursor.getObject());
            }
            wizardElementWriter.writeGet(writer);

            if (!cursor.toNextSibling()) {
                break;
            }
        }
    }
}
