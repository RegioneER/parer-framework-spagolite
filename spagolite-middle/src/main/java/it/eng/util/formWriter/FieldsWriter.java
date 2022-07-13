package it.eng.util.formWriter;

import it.eng.spagoLite.xmlbean.form.Field;
import it.eng.spagoLite.xmlbean.form.Fields;
import it.eng.util.ClassUtil;

import java.io.IOException;
import java.io.Writer;

import org.apache.xmlbeans.XmlCursor;

public class FieldsWriter {
    private Fields fields;

    public FieldsWriter(Fields fields) {
        this.fields = fields;
    }

    public void write(Writer writer) throws IOException {
        writer.write(
                "  public static class " + ClassUtil.getClassName(fields.getName()) + " extends Fields<Field> {\n");
        writer.write("\n");
        writer.write("    public static String NAME = \"" + ClassUtil.getClassName(fields.getName()) + "\";\n");
        writer.write("    public static String DESCRIPTION = \"" + fields.getDescription() + "\";\n");

        writeConstants(writer);

        writer.write("\n");
        writer.write("    public " + ClassUtil.getClassName(fields.getName()) + "() {\n");
        writer.write("      super(null, NAME, DESCRIPTION);\n");

        writeAdds(writer);
        writer.write("    }\n");
        writer.write("\n");

        writeGets(writer);

        writer.write("  }\n");
    }

    private void writeConstants(Writer writer) throws IOException {
        writer.write("\n");

        XmlCursor cursor = fields.newCursor();
        cursor.toFirstChild();
        for (int i = 0;; i++) {
            FieldWriter fieldWriter = new FieldWriter((Field) cursor.getObject());
            fieldWriter.writeConstant(writer);

            if (!cursor.toNextSibling())
                break;
        }

        writer.write("\n");
        cursor = fields.newCursor();
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
        XmlCursor cursor = fields.newCursor();
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
        XmlCursor cursor = fields.newCursor();
        cursor.toFirstChild();
        for (int i = 0;; i++) {
            FieldWriter fieldWriter = new FieldWriter((Field) cursor.getObject());
            fieldWriter.writeGet(writer);

            if (!cursor.toNextSibling())
                break;
        }
    }

}
