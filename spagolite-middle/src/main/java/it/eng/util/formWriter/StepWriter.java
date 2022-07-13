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
                + ((position == 0) ? true : getElement().getCurrent()) + ", " + getElement().isSetHidden() + "));\n");
    }

    public void writeGet(Writer writer) throws IOException {
        writer.write("    public Step get" + getClassName() + "() {\n");
        writer.write("      return (Step) getComponent(" + getConstantName().toLowerCase() + ");\n");
        writer.write("    }\n");
        writer.write("\n");
    }

}
