package it.eng.util.formWriter;

import it.eng.spagoLite.xmlbean.form.TreeElement;
import it.eng.spagoLite.xmlbean.form.TreeElement.Type;
import it.eng.util.formWriter.base.ElementWriter;

import java.io.IOException;
import java.io.Writer;

public class TreeElementWriter extends ElementWriter<TreeElement> {

    public TreeElementWriter(TreeElement element) {
        super(element);
    }

    private String getDataType() {
        String dataType = "<String>";
        if (getElement().getType().equals(Type.ID)) {
            dataType = "<BigDecimal>";
        } else if (getElement().getType().equals(Type.ID_PARENT)) {
            dataType = "<BigDecimal>";
        } else if (getElement().getType().equals(Type.NAME)) {
            dataType = "<String>";
        }

        return dataType;
    }

    public void writeAdd(Writer writer) throws IOException {
        String description = getElement().getDescription();
        String alias = getElement().getAlias() == null ? "" : getElement().getAlias();
        String type = getElement().getType() != null
                ? "it.eng.spagoLite.xmlbean.form.TreeElement.Type." + getElement().getType() : "null";
        String icon = getElement().getIcon() != null ? "\"" + getElement().getIcon() + "\"" : "null";

        // public TreeField(Component parent, String name, String description,
        // String alias, Type.Enum type, boolean hidden)
        writer.write("      addComponent(new TreeField" + getDataType() + "(this, " + getConstantName().toLowerCase()
                + ", \"" + description + "\", \"" + alias + "\", " + type + ", " + icon + "));\n");
    }

    public void writeGet(Writer writer) throws IOException {
        String fieldType = "TreeField" + getDataType();

        writer.write("    public " + fieldType + " get" + getClassName() + "() {\n");
        writer.write("      return (" + fieldType + ") getComponent(" + getConstantName().toLowerCase() + ");\n");
        writer.write("    }\n");
        writer.write("\n");
    }

}
