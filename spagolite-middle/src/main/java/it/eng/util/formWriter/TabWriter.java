package it.eng.util.formWriter;

import it.eng.spagoLite.xmlbean.form.Tab;
import it.eng.spagoLite.xmlbean.form.TabElement;
import it.eng.util.ClassUtil;

import java.io.IOException;
import java.io.Writer;

import org.apache.xmlbeans.XmlCursor;

public class TabWriter {
    private Tab Tab;

    public TabWriter(Tab Tab) {
        this.Tab = Tab;
    }

    public void write(Writer writer) throws IOException {

        writer.write("  public static class " + ClassUtil.getClassName(Tab.getName()) + " extends Tab {\n");
        writer.write("\n");
        writer.write("    public static String NAME = \"" + ClassUtil.getClassName(Tab.getName()) + "\";\n");
        writer.write("    public static String DESCRIPTION = \"" + Tab.getDescription() + "\";\n");

        writeConstants(writer);

        writer.write("\n");
        writer.write("    public " + ClassUtil.getClassName(Tab.getName()) + "() {\n");
        writer.write("      super(null, NAME, DESCRIPTION);\n");

        writeAdds(writer);
        writer.write("    }\n");
        writer.write("\n");

        writeGets(writer);

        writer.write("  }\n");
    }

    private void writeConstants(Writer writer) throws IOException {
        writer.write("\n");

        XmlCursor cursor = Tab.newCursor();
        cursor.toFirstChild();
        for (int i = 0;; i++) {
            TabElementWriter TabElementWriter = new TabElementWriter((TabElement) cursor.getObject());
            TabElementWriter.writeConstant(writer);

            if (!cursor.toNextSibling())
                break;
        }

        writer.write("\n");
        cursor = Tab.newCursor();
        cursor.toFirstChild();
        for (int i = 0;; i++) {
            TabElementWriter TabElementWriter = new TabElementWriter((TabElement) cursor.getObject());
            TabElementWriter.writeConstantFull(writer);

            if (!cursor.toNextSibling())
                break;
        }
    }

    private void writeAdds(Writer writer) throws IOException {
        writer.write("\n");
        XmlCursor cursor = Tab.newCursor();
        cursor.toFirstChild();
        for (int i = 0;; i++) {
            TabElementWriter TabElementWriter = new TabElementWriter((TabElement) cursor.getObject());
            TabElementWriter.writeAdd(writer);

            if (!cursor.toNextSibling())
                break;
        }
    }

    private void writeGets(Writer writer) throws IOException {
        writer.write("\n");
        XmlCursor cursor = Tab.newCursor();
        cursor.toFirstChild();
        for (int i = 0;; i++) {
            TabElementWriter TabElementWriter = new TabElementWriter((TabElement) cursor.getObject());
            TabElementWriter.writeGet(writer);

            if (!cursor.toNextSibling())
                break;
        }
    }
}
