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

import it.eng.spagoLite.xmlbean.form.Section;
import it.eng.util.ClassUtil;
import it.eng.util.formWriter.base.ElementWriter;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang3.StringUtils;

public class SectionWriter extends ElementWriter<Section> {
    private Section section;

    public SectionWriter(Section section) {
        super(section);

    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    private void writeAdds(Writer writer) throws IOException {
        // writer.write(" addComponent(new Section(this, " + getConstantName().toLowerCase() + ", \"" +
        // getElement().getDescription() + "\", " + getElement().getLegend() + ", " + getElement().getHidden() + ", " +
        // getElement().getBorderHidden() + ", "+ getElement().getShowButton() + ",
        // "+getElement().getLoadOpened()+"));\n");
        String legend = StringUtils.isBlank(getElement().getLegend()) ? "null" : "\"" + getElement().getLegend() + "\"";
        String hidden = getElement().getHidden() ? "true" : "false";
        String borderHidden = getElement().getBorderHidden() ? "true" : "false";
        String showButton = getElement().getShowButton() ? "true" : "false";
        String loadOpened = getElement().getLoadOpened() ? "true" : "false";

        writer.write("      setLegend(" + legend + ");\n");
        writer.write("      setHidden(" + hidden + ");\n");
        writer.write("      setBorderHidden(" + borderHidden + ");\n");
        writer.write("      setShowButton(" + showButton + ");\n");
        writer.write("      setLoadOpened(" + loadOpened + ");\n");
        if (getElement().getHidden()) {
            writer.write("      setViewMode();\n");
        } else {
            writer.write("      setEditMode();\n");
        }
    }

    private void writeGets(Writer writer) throws IOException {
        writer.write("    public Section get" + getClassName() + "() {\n");
        writer.write("      return (Section) getComponent(" + getConstantName().toLowerCase() + ");\n");
        writer.write("    }\n");
        writer.write("\n");

    }

    public void write(Writer writer) throws IOException {
        writer.write(
                "  public static class " + ClassUtil.getClassName(getElement().getName()) + " extends Section {\n");
        writer.write("\n");
        writer.write("    public static String NAME = \"" + ClassUtil.getClassName(getElement().getName()) + "\";\n");
        writer.write("    public static String DESCRIPTION = \"" + getElement().getDescription() + "\";\n");

        writer.write("\n");
        writer.write("    public " + ClassUtil.getClassName(getElement().getName()) + "() {\n");
        writer.write("      super(null, NAME, DESCRIPTION);\n");

        writeAdds(writer);
        writer.write("    }\n");
        writer.write("\n");

        // writeGets (writer);

        writer.write("  }\n");
    }

}
