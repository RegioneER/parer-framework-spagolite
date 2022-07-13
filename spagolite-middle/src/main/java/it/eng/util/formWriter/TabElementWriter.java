package it.eng.util.formWriter;

import it.eng.spagoLite.xmlbean.form.TabElement;
import it.eng.util.formWriter.base.ElementWriter;

import java.io.IOException;
import java.io.Writer;

public class TabElementWriter extends ElementWriter<TabElement> {

    public TabElementWriter(TabElement element) {
        super(element);
    }

    public void writeAdd(Writer writer) throws IOException {
        String iconUrlStr = getElement().getIconUrl() == null ? "null" : "\"" + getElement().getIconUrl() + "\"";

        writer.write("      addComponent(new TabElement(this, " + getConstantName().toLowerCase() + ", \""
                + getElement().getDescription() + "\", " + getElement().getCurrent() + ", " + getElement().getHidden()
                + ", " + getElement().getDisabled() + ", " + iconUrlStr + "));\n");
    }

    public void writeGet(Writer writer) throws IOException {
        writer.write("    public TabElement get" + getClassName() + "() {\n");
        writer.write("      return (TabElement) getComponent(" + getConstantName().toLowerCase() + ");\n");
        writer.write("    }\n");
        writer.write("\n");

    }

}
