/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna
 * <p/>
 * This program is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package it.eng.util.formWriter;

import it.eng.spagoLite.xmlbean.form.Field;
import it.eng.spagoLite.xmlbean.form.List;
import it.eng.util.ClassUtil;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.XmlCursor;

public class ListWriter {
    private List list;

    public ListWriter(List fields) {
        this.list = fields;
    }

    public void write(Writer writer) throws IOException {

        writer.write("  public static class " + ClassUtil.getClassName(list.getName())
                + " extends List<SingleValueField<?>> {\n");
        writer.write("\n");
        writer.write("    public static String NAME = \"" + ClassUtil.getClassName(list.getName()) + "\";\n");
        writer.write("    public static String DESCRIPTION = \"" + list.getDescription() + "\";\n");

        writeConstants(writer);

        writer.write("\n");
        writer.write("    public " + ClassUtil.getClassName(list.getName()) + "() {\n");
        String visibility = list.getVisibilityProperty() == null ? ", null"
                : " , \"" + list.getVisibilityProperty() + "\"";
        String filterValidRecords = list.isSetFilterValidRecords() ? String.valueOf(list.getFilterValidRecords())
                : "null";
        String goToPageNavigation = list.isSetGoToPageNavigation() ? String.valueOf(list.getGoToPageNavigation())
                : "null";
        String excelFileName = list.isSetExcelFileName() ? String.valueOf(list.getExcelFileName()) : "null";
        writer.write("      super(null, NAME, DESCRIPTION,Status.view,DESCRIPTION,null,false,"
                + list.isSetHideDetailButton() + "," + list.isSetHideUpdateButton() + "," + list.isSetHideDeleteButton()
                + "," + list.isSetHideInsertButton() + "," + list.isSetEditable() + visibility + ","
                + filterValidRecords + "," + excelFileName + "," + goToPageNavigation + ");\n");

        // writer.write(" super(null, NAME, DESCRIPTION);\n");
        if (list.isSetEditable()) {
            writer.write("      this.setEditable(true);\n");
        }
        if (list.isSetMasterList()) {
            writer.write("      this.setMasterList(true);\n");
        }
        if (list.isSetReadOnly()) {
            writer.write("      this.setReadOnly(true);\n");
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
