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

package it.eng.util.constantWriter;

import it.eng.util.ClassUtil;
import it.eng.util.SpagoLiteTool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class ConstantWriter {
    private String packageName;
    private String actionPath;
    private List<File> actionList;
    private List<File> jspList;

    public ConstantWriter(String packageName, String actionPath, List<File> actionList, List<File> jspList) {
        this.actionPath = actionPath;
        this.packageName = packageName;
        this.actionList = actionList;
        this.jspList = jspList;
    }

    public String getPackageName() {
        return packageName;
    }

    public List<File> getActionList() {
        return actionList;
    }

    public List<File> getJspList() {
        return jspList;
    }

    public void write(Writer writer) throws IOException {
        writer.write("package " + getPackageName() + ";\n");
        writer.write("\n");
        writer.write("import javax.annotation.Generated;\n");
        writer.write("\n");
        writer.write("public class Application {\n");
        writer.write("\n");
        writer.write(SpagoLiteTool.getGeneratedAnnotation(this.getClass().getName()));
        writer.write("\n");
        writer.write("  public static class Actions {\n");
        writer.write("\n");
        for (File file : getActionList()) {
            String name = file.getName().substring(0, file.getName().length() - ".java".length());
            writer.write("    public static final String " + ClassUtil.getConstantName(name) + " = \""
                    + ClassUtil.getConstantName(name) + "\";\n");
        }
        writer.write("  }\n");
        writer.write("\n");
        writer.write("  public static class Publisher {\n");
        writer.write("\n");

        for (File file : getJspList()) {
            String name = file.getName().substring(0, file.getName().length() - ".jsp".length());
            writer.write("    public static final String " + ClassUtil.getConstantName(name) + " = \""
                    + ClassUtil.getConstantName(name) + "_PUBLISHER\";\n");
        }

        writer.write("  }\n");
        writer.write("}\n");
    }

    public void write4Spring(Writer writer, String jspBase) throws IOException {
        writer.write("package " + getPackageName() + ";\n");
        writer.write("\n");
        writer.write("import javax.annotation.Generated;\n");
        writer.write("\n");
        writer.write("public class Application {\n");
        writer.write("\n");
        writer.write(SpagoLiteTool.getGeneratedAnnotation(this.getClass().getName()));
        writer.write("\n");
        writer.write("  public static class Actions {\n");
        writer.write("\n");
        for (File file : getActionList()) {
            String name = file.getName().substring(0, file.getName().length() - "Action.java".length());
            String context = file.getParent().replaceAll("\\\\", "/").replaceFirst(actionPath, "");
            context = context.length() > 0 ? (context.substring(1) + "/") : "";
            writer.write("    public static final String " + ClassUtil.getConstantName(name) + " = \"" + context + name
                    + ".html" + "\";\n");
        }
        writer.write("  }\n");
        writer.write("\n");
        writer.write("  public static class Publisher {\n");
        writer.write("\n");

        for (File file : getJspList()) {
            if (file.getName().endsWith(".jsp")) {
                String name = file.getName().substring(0, file.getName().length() - ".jsp".length());
                String jspName = file.getPath().substring(jspBase.length()).replace("\\", "/");
                jspName = jspName.substring(0, jspName.length() - ".jsp".length());
                writer.write("    public static final String " + ClassUtil.getConstantName(name) + " = \"" + jspName
                        + "\";\n");
            }
        }

        writer.write("  }\n");
        writer.write("}\n");
    }

}
