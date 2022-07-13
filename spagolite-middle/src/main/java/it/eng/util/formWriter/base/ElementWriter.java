package it.eng.util.formWriter.base;

import it.eng.spagoLite.form.fields.impl.Button;
import it.eng.spagoLite.xmlbean.form.Element;
import it.eng.util.ClassUtil;

import java.io.IOException;
import java.io.Writer;

public class ElementWriter<T extends Element> {
    private T element;

    public ElementWriter(T element) {
        this.element = element;
    }

    public T getElement() {
        return element;
    }

    public String getClassName() {
        return ClassUtil.getClassName(getElement().getName());
    }

    public String getConstantName() {
        return ClassUtil.getConstantName(getElement().getName());
    }

    public void writeConstant(Writer writer) throws IOException {
        writer.write("    public static final String " + ClassUtil.getConstantName(getElement().getName()).toLowerCase()
                + " = \"" + ClassUtil.getClassName(getElement().getName()) + "\";\n");
    }

    public void writeConstantFull(Writer writer) throws IOException {
        writer.write("    public static final String " + ClassUtil.getConstantName(getElement().getName())
                + " = NAME + \".\" + " + ClassUtil.getConstantName(getElement().getName()).toLowerCase() + ";\n");
    }

    public void writeAdd(Writer writer) throws IOException {
        if (getElement() instanceof Button) {
            writer.write("      addComponent(new Button(this, " + getConstantName().toLowerCase() + ", \""
                    + getElement().getDescription() + "\");\n");
        }
    }

    public void writeGet(Writer writer) throws IOException {
        String fieldType = "";
        if (getElement() instanceof Button) {
            fieldType = "Button";
            writer.write("    public " + fieldType + " get" + getClassName() + "() {\n");
            writer.write("      return (" + fieldType + ") getComponent(" + getConstantName().toLowerCase() + ");\n");
            writer.write("    }\n");
            writer.write("\n");
        }

    }

}
