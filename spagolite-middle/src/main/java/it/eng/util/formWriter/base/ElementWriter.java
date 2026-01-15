/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna <p/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version. <p/> This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. <p/> You should
 * have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
 */

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
        writer.write("    public static final String "
                + ClassUtil.getConstantName(getElement().getName()).toLowerCase() + " = \""
                + ClassUtil.getClassName(getElement().getName()) + "\";\n");
    }

    public void writeConstantFull(Writer writer) throws IOException {
        writer.write("    public static final String "
                + ClassUtil.getConstantName(getElement().getName()) + " = NAME + \".\" + "
                + ClassUtil.getConstantName(getElement().getName()).toLowerCase() + ";\n");
    }

    public void writeAdd(Writer writer) throws IOException {
        if (getElement() instanceof Button) {
            writer.write("      addComponent(new Button(this, " + getConstantName().toLowerCase()
                    + ", \"" + getElement().getDescription() + "\");\n");
        }
    }

    public void writeGet(Writer writer) throws IOException {
        String fieldType = "";
        if (getElement() instanceof Button) {
            fieldType = "Button";
            writer.write("    public " + fieldType + " get" + getClassName() + "() {\n");
            writer.write("      return (" + fieldType + ") getComponent("
                    + getConstantName().toLowerCase() + ");\n");
            writer.write("    }\n");
            writer.write("\n");
        }

    }

}
