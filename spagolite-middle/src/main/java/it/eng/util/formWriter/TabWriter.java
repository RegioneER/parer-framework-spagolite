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

	writer.write("  public static class " + ClassUtil.getClassName(Tab.getName())
		+ " extends Tab {\n");
	writer.write("\n");
	writer.write("    public static String NAME = \"" + ClassUtil.getClassName(Tab.getName())
		+ "\";\n");
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

	try (XmlCursor cursor = Tab.newCursor()) {
	    if (cursor.toFirstChild()) {
		do {
		    TabElementWriter TabElementWriter = new TabElementWriter(
			    (TabElement) cursor.getObject());
		    TabElementWriter.writeConstant(writer);
		} while (cursor.toNextSibling());
	    }
	}

	writer.write("\n");

	try (XmlCursor cursor = Tab.newCursor()) {
	    if (cursor.toFirstChild()) {
		do {
		    TabElementWriter TabElementWriter = new TabElementWriter(
			    (TabElement) cursor.getObject());
		    TabElementWriter.writeConstantFull(writer);
		} while (cursor.toNextSibling());
	    }
	}
    }

    private void writeAdds(Writer writer) throws IOException {
	writer.write("\n");
	try (XmlCursor cursor = Tab.newCursor()) {
	    if (cursor.toFirstChild()) {
		do {
		    TabElementWriter TabElementWriter = new TabElementWriter(
			    (TabElement) cursor.getObject());
		    TabElementWriter.writeAdd(writer);
		} while (cursor.toNextSibling());
	    }
	}
    }

    private void writeGets(Writer writer) throws IOException {
	writer.write("\n");
	try (XmlCursor cursor = Tab.newCursor()) {
	    if (cursor.toFirstChild()) {
		do {
		    TabElementWriter TabElementWriter = new TabElementWriter(
			    (TabElement) cursor.getObject());
		    TabElementWriter.writeGet(writer);
		} while (cursor.toNextSibling());
	    }
	}
    }
}
