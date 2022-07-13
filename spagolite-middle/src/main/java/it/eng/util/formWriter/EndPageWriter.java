package it.eng.util.formWriter;

import it.eng.spagoLite.xmlbean.form.EndPage;
import it.eng.util.formWriter.base.ElementWriter;

import java.io.IOException;
import java.io.Writer;

public class EndPageWriter extends ElementWriter<EndPage> implements WizardElementWriter {

    public EndPageWriter(EndPage element) {
        super(element);
    }

    public void writeAdd(Writer writer) throws IOException {
        writer.write("      addComponent(new EndPage(this, " + getConstantName().toLowerCase() + ", \""
                + getElement().getDescription() + "\", " + getElement().getHideBar() + "));\n");
    }

    public void writeGet(Writer writer) throws IOException {
        writer.write("    public EndPage get" + getClassName() + "() {\n");
        writer.write("      return (EndPage) getComponent(" + getConstantName().toLowerCase() + ");\n");
        writer.write("    }\n");
        writer.write("\n");
    }

}
