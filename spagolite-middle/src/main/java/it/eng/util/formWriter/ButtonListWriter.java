package it.eng.util.formWriter;

import it.eng.spagoLite.xmlbean.form.ButtonList;
import it.eng.spagoLite.xmlbean.form.Field;
import it.eng.util.ClassUtil;

import java.io.IOException;
import java.io.Writer;

import org.apache.xmlbeans.XmlCursor;

public class ButtonListWriter {
    private ButtonList buttonList;

    public ButtonListWriter(ButtonList buttonList) {
        this.buttonList = buttonList;
    }

    public void write(Writer writer) throws IOException {

        writer.write(
                "  public static class " + ClassUtil.getClassName(buttonList.getName()) + " extends ButtonList {\n");
        writer.write("\n");
        writer.write("    public static String NAME = \"" + ClassUtil.getClassName(buttonList.getName()) + "\";\n");
        writer.write("    public static String DESCRIPTION = \"" + buttonList.getDescription() + "\";\n");

        writeConstants(writer);

        writer.write("\n");
        writer.write("    public " + ClassUtil.getClassName(buttonList.getName()) + "() {\n");
        writer.write(
                "      super(null, NAME, DESCRIPTION, " + (buttonList.getDisableAll() ? "true" : "false") + ");\n");
        writeAdds(writer);
        writer.write("    }\n");
        writer.write("\n");

        writeGets(writer);

        writer.write("  }\n");
    }

    private void writeConstants(Writer writer) throws IOException {
        writer.write("\n");

        XmlCursor cursor = buttonList.newCursor();
        cursor.toFirstChild();
        for (int i = 0;; i++) {
            FieldWriter fieldWriter = new FieldWriter((Field) cursor.getObject());
            fieldWriter.writeConstant(writer);

            if (!cursor.toNextSibling())
                break;
        }

        writer.write("\n");
        cursor = buttonList.newCursor();
        cursor.toFirstChild();
        for (int i = 0;; i++) {
            FieldWriter fieldWriter = new FieldWriter((Field) cursor.getObject());
            fieldWriter.writeConstantFull(writer);

            if (!cursor.toNextSibling())
                break;
        }
    }

    private void writeAdds(Writer writer) throws IOException {
        writer.write("\n");
        XmlCursor cursor = buttonList.newCursor();
        cursor.toFirstChild();
        for (int i = 0;; i++) {
            FieldWriter fieldWriter = new FieldWriter((Field) cursor.getObject());
            fieldWriter.writeAdd(writer);

            if (!cursor.toNextSibling())
                break;
        }
    }

    private void writeGets(Writer writer) throws IOException {
        writer.write("\n");
        XmlCursor cursor = buttonList.newCursor();
        cursor.toFirstChild();
        for (int i = 0;; i++) {
            FieldWriter fieldWriter = new FieldWriter((Field) cursor.getObject());
            fieldWriter.writeGet(writer);

            if (!cursor.toNextSibling())
                break;
        }
    }
}
