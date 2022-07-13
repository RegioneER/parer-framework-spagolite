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
