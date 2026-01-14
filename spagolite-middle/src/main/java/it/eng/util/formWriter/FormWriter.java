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

package it.eng.util.formWriter;

import java.io.IOException;
import java.io.Writer;

import org.apache.xmlbeans.XmlCursor;

import it.eng.spagoLite.xmlbean.form.ButtonList;
import it.eng.spagoLite.xmlbean.form.Element;
import it.eng.spagoLite.xmlbean.form.Fields;
import it.eng.spagoLite.xmlbean.form.Form;
import it.eng.spagoLite.xmlbean.form.List;
import it.eng.spagoLite.xmlbean.form.NestedList;
import it.eng.spagoLite.xmlbean.form.Section;
import it.eng.spagoLite.xmlbean.form.Tab;
import it.eng.spagoLite.xmlbean.form.Tree;
import it.eng.spagoLite.xmlbean.form.Wizard;
import it.eng.util.ClassUtil;
import it.eng.util.SpagoLiteTool;

public class FormWriter {
    private String packageName;
    private String className;
    private Form form;
    private final static String SEPARATOR = "/";

    public FormWriter(String packageName, String className, Form form) {
        this.packageName = packageName;
        this.className = className;
        this.form = form;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public Form getForm() {
        return form;
    }

    public void write(Writer writer) throws IOException {
        writer.write("package " + getPackageName() + ";\n");
        writer.write("\n");
        writer.write("import javax.annotation.Generated;\n");
        writer.write("import it.eng.spagoLite.form.base.BaseElement;\n");
        writer.write("import it.eng.spagoLite.form.base.BaseForm;\n");
        writer.write("import it.eng.spagoLite.form.fields.Field;\n");
        writer.write("import it.eng.spagoLite.form.fields.Fields;\n");
        writer.write("import it.eng.spagoLite.form.fields.SingleValueField;\n");
        writer.write("import it.eng.spagoLite.form.list.List;\n");
        writer.write("import it.eng.spagoLite.form.list.NestedList;\n");
        writer.write("import it.eng.spagoLite.form.fields.impl.ComboBox;\n");
        writer.write("import it.eng.spagoLite.form.fields.impl.Input;\n");
        writer.write("import it.eng.spagoLite.form.fields.impl.MultiSelect;\n");
        writer.write("import it.eng.spagoLite.form.fields.impl.MultiSemaphore;\n");
        writer.write("import it.eng.spagoLite.form.fields.impl.TextArea;\n");
        writer.write("import it.eng.spagoLite.form.fields.impl.CheckBox;\n");
        writer.write("import it.eng.spagoLite.form.fields.impl.Button;\n");
        writer.write("import it.eng.spagoLite.form.fields.impl.Link;\n");
        writer.write("import it.eng.spagoLite.form.fields.impl.Radio;\n");
        writer.write("import it.eng.spagoLite.form.fields.impl.Semaphore;\n");
        writer.write("import it.eng.spagoLite.form.buttonList.ButtonList;\n");
        writer.write("import it.eng.spagoLite.form.fields.Section;\n");
        writer.write("import it.eng.spagoLite.form.tab.Tab;\n");
        writer.write("import it.eng.spagoLite.form.tab.TabElement;\n");
        writer.write("import it.eng.spagoLite.form.tree.Tree;\n");
        writer.write("import it.eng.spagoLite.form.tree.TreeField;\n");
        writer.write("import it.eng.spagoLite.form.wizard.Step;\n");
        writer.write("import it.eng.spagoLite.form.wizard.EndPage;\n");
        writer.write("import it.eng.spagoLite.form.wizard.Wizard;\n");
        writer.write("import it.eng.spagoLite.xmlbean.form.Field.Type;\n");
        writer.write("import it.eng.spagoLite.xmlbean.form.Input.ForceCase;\n");
        writer.write("import it.eng.spagoLite.xmlbean.form.Input.ForceTrim;\n");

        writer.write("\n");
        writer.write("import java.util.ArrayList;\n");
        writer.write("import java.math.BigDecimal;\n");
        writer.write("import java.sql.Timestamp;\n");

        writer.write("\n");
        writer.write("public class " + getClassName() + " extends BaseForm {\n");
        writer.write("\n");
        writer.write(
                "  public static String DESCRIPTION = \"" + getForm().getDescription() + "\";\n");
        writer.write("\n");
        writer.write(SpagoLiteTool.getGeneratedAnnotation(this.getClass().getName()));
        writer.write("\n");
        writer.write("  public " + getClassName() + " () {\n");
        writer.write("    super(DESCRIPTION);\n");
        writeAdds(writer);
        writer.write("  }\n");
        writer.write("\n");
        writeGets(writer);

        // Genero i fields
        Fields[] fieldsArray = getForm().getFieldsArray();
        for (int i = 0; i < fieldsArray.length; i++) {
            FieldsWriter fieldsWriter = new FieldsWriter(fieldsArray[i]);
            fieldsWriter.write(writer);
        }

        // Genero le liste
        List[] listArray = getForm().getListArray();
        for (int i = 0; i < listArray.length; i++) {
            ListWriter listWriter = new ListWriter(listArray[i]);
            listWriter.write(writer);
        }

        // Genero le liste
        NestedList[] nestedlistArray = getForm().getNestedListArray();
        for (int i = 0; i < nestedlistArray.length; i++) {
            NestedListWriter listWriter = new NestedListWriter(nestedlistArray[i]);
            listWriter.write(writer);
        }

        // Genero i tab
        Tab[] tabArray = getForm().getTabArray();
        for (int i = 0; i < tabArray.length; i++) {
            TabWriter tabWriter = new TabWriter(tabArray[i]);
            tabWriter.write(writer);
        }

        // Genero i wizard
        Wizard[] wizardArray = getForm().getWizardArray();
        for (int i = 0; i < wizardArray.length; i++) {
            WizardWriter wizardWriter = new WizardWriter(wizardArray[i]);
            wizardWriter.write(writer);
        }

        // Genero le section
        Section[] sectionArray = getForm().getSectionArray();
        for (int i = 0; i < sectionArray.length; i++) {
            SectionWriter sectionWriter = new SectionWriter(sectionArray[i]);
            sectionWriter.write(writer);
        }
        // Genero i buttonList
        ButtonList[] buttonListArray = getForm().getButtonListArray();
        for (int i = 0; i < buttonListArray.length; i++) {
            ButtonListWriter buttonListWriter = new ButtonListWriter(buttonListArray[i]);
            buttonListWriter.write(writer);
        }

        // Genero i tree
        Tree[] treeArray = getForm().getTreeArray();
        for (int i = 0; i < treeArray.length; i++) {
            TreeWriter treeWriter = new TreeWriter(treeArray[i]);
            treeWriter.write(writer);
        }

        writer.write("}\n");
    }

    private void writeAdds(Writer writer) throws IOException {
        try (XmlCursor cursor = getForm().newCursor()) {
            if (!cursor.toFirstChild()) {
                return;
            }
            do {
                Object obj = cursor.getObject();
                if (obj instanceof Element) {
                    Element field = (Element) obj;
                    String name = ClassUtil.getClassName(field.getName());
                    writer.write("    addComponent (new " + name + "());\n");
                }
            } while (cursor.toNextSibling());
        }
    }

    private void writeGets(Writer writer) throws IOException {
        try (XmlCursor cursor = getForm().newCursor()) {
            if (!cursor.toFirstChild()) {
                return;
            }
            do {
                Object obj = cursor.getObject();
                if (obj instanceof Element) {
                    Element field = (Element) obj;
                    String name = ClassUtil.getClassName(field.getName());

                    writer.write("  public " + name + " get" + name + " () {\n");
                    writer.write("    return (" + name + ") getComponent(" + name + ".NAME);\n");
                    writer.write("  }\n");
                    writer.write("\n");
                }
            } while (cursor.toNextSibling());
        }
    }

}
