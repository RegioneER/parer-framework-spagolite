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

package it.eng.util;

import java.io.File;

public class ClassUtil {

    public static final String getActionClassName(File xmlFile) {
        return xmlFile.getName().substring(0, xmlFile.getName().indexOf(".")) + "AbstractAction";
    }

    public static final String getActionFileName(String srcPath, String packageName, File xmlFile) {
        String path = srcPath + "/" + packageName.replace(".", "/");
        String fileName = getActionClassName(xmlFile) + ".java";
        new File(path).mkdirs();
        return path + "/" + fileName;
    }

    public static final String getFormClassName(File xmlFile) {
        return xmlFile.getName().substring(0, xmlFile.getName().indexOf(".")) + "Form";
    }

    public static final String getFormFileName(String srcPath, String packageName, File xmlFile) {
        String path = srcPath + "/" + packageName.replace(".", "/");
        String fileName = getFormClassName(xmlFile) + ".java";
        new File(path).mkdirs();
        return path + "/" + fileName;
    }

    public static final String getConstantFileName(String srcPath, String packageName) {
        String path = srcPath + "/" + packageName.replace(".", "/");
        String fileName = "Application.java";
        new File(path).mkdirs();
        return path + "/" + fileName;
    }

    public static final boolean isUpperCase(char ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    public static final boolean isLowerCase(char ch) {
        return !isUpperCase(ch);
    }

    public static final String getConstantName(String testo) {
        String constant = "";
        for (int i = 0; i < testo.length(); i++) {
            if (i == 0) {
                constant += testo.charAt(i);
            } else if (isUpperCase(testo.charAt(i)) && !isUpperCase(testo.charAt(i - 1))) {
                constant += "_" + testo.charAt(i);
            } else {
                constant += testo.charAt(i);
            }
        }

        return constant.toUpperCase();
    }

    public static final String getClassName(String testo) {
        return testo.substring(0, 1).toUpperCase() + testo.substring(1);
    }
}
