package it.eng.util.formWriter;

import it.eng.spagoLite.xmlbean.form.Field;
import it.eng.spagoLite.xmlbean.form.NestedList;
import it.eng.util.ClassUtil;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.XmlCursor;

public class NestedListWriter {
    private NestedList list;

    public NestedListWriter(NestedList fields) {
        this.list = fields;
    }

    public void write(Writer writer) throws IOException {

        writer.write("  public static class " + ClassUtil.getClassName(list.getName())
                + " extends NestedList<SingleValueField<?>> {\n");
        writer.write("\n");
        writer.write("    public static String NAME = \"" + ClassUtil.getClassName(list.getName()) + "\";\n");
        writer.write("    public static String DESCRIPTION = \"" + list.getDescription() + "\";\n");

        writeConstants(writer);

        writer.write("\n");
        writer.write("    public " + ClassUtil.getClassName(list.getName()) + "() {\n");
        writer.write("      super(null, NAME, DESCRIPTION);\n");

        if (list.isSetPrimaryTableFields()) {
            writer.write("      this.setPrimaryTableFields(" + list.getPrimaryTableFields() + ");\n");
        }

        if (list.isSetSecondaryTableFields()) {
            writer.write("      this.setSecondaryTableFields(" + list.getSecondaryTableFields() + ");\n");
        }

        if (list.isSetMasterList()) {
            writer.write("      this.setMasterList(true);\n");
        }

        if (StringUtils.isNotBlank(list.getTitle())) {
            writer.write("      setTitle(\"" + list.getTitle() + "\");\n");
        }
        writeAdds(writer);
        writer.write("    }\n");
        writer.write("\n");

        writeGets(writer);

        writer.write("  }\n");
    }

    private void writeConstants(Writer writer) throws IOException {
        writer.write("\n");

        XmlCursor cursor = list.newCursor();
        cursor.toFirstChild();
        for (int i = 0;; i++) {
            FieldWriter fieldWriter = new FieldWriter((Field) cursor.getObject());
            fieldWriter.writeConstant(writer);

            if (!cursor.toNextSibling())
                break;
        }

        writer.write("\n");
        cursor = list.newCursor();
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
        XmlCursor cursor = list.newCursor();
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
        XmlCursor cursor = list.newCursor();
        cursor.toFirstChild();
        for (int i = 0;; i++) {
            FieldWriter fieldWriter = new FieldWriter((Field) cursor.getObject());
            fieldWriter.writeGet(writer);

            if (!cursor.toNextSibling())
                break;
        }
    }
}
