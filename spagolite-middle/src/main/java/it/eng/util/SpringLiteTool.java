package it.eng.util;

import it.eng.spagoLite.xmlbean.form.FormDocument;
import it.eng.util.actionWriter.ActionWriter;
import it.eng.util.constantWriter.ConstantWriter;
import it.eng.util.constantWriter.ScriptWriter;
import it.eng.util.formWriter.FormWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class SpringLiteTool {

    private String formPath = "./forms";
    private String confPath = "./web/WEB-INF/conf/spago";

    private String srcPath = "./src";
    private String jspPath = "./web/jsp";

    private String actionPath;
    private String actionRerPath;

    private String genPackage;
    private String actionPackage;
    private String formPackage;

    private String userPackage;

    public SpringLiteTool(String actionPath, String actionRerPath, String genPackage, String actionPackage,
            String formPackage) {
        this.actionPath = actionPath;
        this.actionRerPath = actionRerPath;

        this.genPackage = genPackage;
        this.actionPackage = actionPackage;
        this.formPackage = formPackage;
    }

    // private void writeAction(FileWriter fileWriter, String path) {
    // List<File> actionFiles = FileSystemUtil.getFileList(path, "java",
    // FileSystemUtil.EXLUDE_PATH);
    // for (File file : actionFiles) {
    // String fullClassName = file.getPath().substring(
    // new File(srcPath).getPath().length() + 1);
    // fullClassName = fullClassName.substring(0, fullClassName.length()
    // - ".java".length());
    // fullClassName = fullClassName.replace("\\", ".");
    //
    // String className = fullClassName.substring(fullClassName
    // .lastIndexOf(".") + 1);
    //
    // try {
    // Class.forName(fullClassName).newInstance();
    // fileWriter.write(" <action name=\""
    // + ClassUtil.getConstantName(className) + "\" class=\""
    // + fullClassName + "\" scope=\"REQUEST\" />\n");
    // } catch (Throwable e) {
    // }
    // }
    // }
    //
    // private void writePublisher(List<File> jspFiles) throws IOException {
    //
    // FileWriter fileWriter = new FileWriter(confPath + "/publishers.xml");
    // fileWriter.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
    // fileWriter
    // .write("<publishers xmlns=\"http://www.spagoLite.eng.it/spagoLite/publishers\"
    // xmlns:xsi=\"http://www.w3.org/2001/XMLSchema\">\n");
    // for (File file : jspFiles) {
    // String name = file.getName().substring(0,
    // file.getName().length() - ".jsp".length());
    // fileWriter.write(" <publisher name=\""
    // + ClassUtil.getConstantName(name) + "_PUBLISHER\">\n");
    // fileWriter
    // .write(" <rendering channel=\"HTTP\" type=\"JSP\" mode=\"FORWARD\">\n");
    // fileWriter.write(" <resources>\n");
    // fileWriter.write(" <item prog=\"0\" resource=\""
    // + file.getPath().substring(5).replace("\\", "/")
    // + "\" />\n");
    // fileWriter.write(" </resources>\n");
    // fileWriter.write(" </rendering>\n");
    // fileWriter.write(" </publisher>\n");
    // }
    //
    // fileWriter.write("</publishers>\n");
    // fileWriter.close();
    // }

    public void run() {
        try {
            List<File> files = null;

            // Form Gen
            System.out.println("Parse form");

            files = FileSystemUtil.getFileList(formPath, "xml", FileSystemUtil.EXLUDE_PATH);
            for (File file : files) {
                System.out.println("   " + file.getName() + " --> it.eng.sample.gen.form.out.out("
                        + ClassUtil.getFormClassName(file) + ".java:10)");
                FormDocument formDocument = FormDocument.Factory.parse(file);

                String actionClassName = ClassUtil.getActionClassName(file);
                String actionFileName = ClassUtil.getActionFileName(srcPath, getActionPackage(), file);

                String formClassName = ClassUtil.getFormClassName(file);
                String formFileName = ClassUtil.getFormFileName(srcPath, getFormPackage(), file);

                // Action
                FileWriter actionFileWriter = new FileWriter(actionFileName);
                ActionWriter actionWriter = new ActionWriter(getActionPackage(), actionClassName, getFormPackage(),
                        formClassName, formDocument.getForm(), userPackage);
                actionWriter.write(actionFileWriter);
                actionFileWriter.close();

                // Form
                FileWriter formFileWriter = new FileWriter(formFileName);
                FormWriter formWriter = new FormWriter(getFormPackage(), formClassName, formDocument.getForm());
                formWriter.write(formFileWriter);
                formFileWriter.close();

            }

            // File di configurazioni
            // List<File> actionFiles = new Vector<File>();
            List<File> actionFiles = FileSystemUtil.getFileList(getActionPath(), "java", FileSystemUtil.EXLUDE_PATH);
            List<File> jspFiles = FileSystemUtil.getFileList(jspPath, "jsp", FileSystemUtil.EXLUDE_PATH);
            //
            // // Write actions.xml
            // System.out.println("Write actions.xml");
            // FileWriter fileWriter = new FileWriter(confPath +
            // "/actions.xml");
            // fileWriter.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
            // fileWriter.write("<actions xmlns=\"http://www.spagoLite.eng.it/spagoLite/action\"
            // xmlns:xsi=\"http://www.w3.org/2001/XMLSchema\">\n");
            // fileWriter.write(" <action name=\"REDIRECT_ACTION\" class=\"it.eng.spagoLite.actions.RedirectAction\"
            // scope=\"REQUEST\" />\n");
            // writeAction(fileWriter, getActionPath());
            // writeAction(fileWriter, getActionRerPath());
            // fileWriter.write("</actions>\n");
            // fileWriter.close();
            //
            // // Get Jsp
            // System.out.println("Write publisher.xml");
            // writePublisher(jspFiles);

            // Scrivo il file APPLICATION.JAVA

            FileWriter writer = new FileWriter(ClassUtil.getConstantFileName(srcPath, getGenPackage()));
            ConstantWriter constantWriter = new ConstantWriter(getGenPackage(), actionPath, actionFiles, jspFiles);
            // constantWriter.write(writer);
            constantWriter.write4Spring(writer, jspPath);
            writer.close();

            // Scrivo il file con gli script del DB per la profilatura

            writer = new FileWriter(srcPath + "/" + getGenPackage().replace(".", "/") + "/DBScript");
            // constantWriter = new ConstantWriter(getGenPackage(), actionPath,
            // actionFiles, jspFiles);
            // constantWriter.write(writer);
            ScriptWriter.writePages(writer, jspPath, jspFiles);

            writer.write("\n## Azioni\n\n");

            ScriptWriter.writeActions(writer, jspFiles, files);
            // }
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }

    public static String getGeneratedAnnotation(String className) {
        StringBuilder writer = new StringBuilder();
        // FIXME Elimina la riga sottostante
        writer.append("  /*\n");
        writer.append("  @Generated(\n");
        writer.append("    value = \"" + className + "\",\n");
        writer.append("    comments = \"Questa classe e' stata generata dal SipsTool\",\n");
        writer.append("    date = \""
                + new SimpleDateFormat("EEEE, d MMMM yyyy HH:mm", Locale.ITALIAN).format(new Date()) + "\"\n");
        writer.append("  )\n");
        // FIXME Elimina la riga sottostante
        writer.append(" */\n");
        return writer.toString();
    }

    public String getFormPath() {
        return formPath;
    }

    public void setFormPath(String formPath) {
        this.formPath = formPath;
    }

    public String getConfPath() {
        return confPath;
    }

    public void setConfPath(String confPath) {
        this.confPath = confPath;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public String getJspPath() {
        return jspPath;
    }

    public void setJspPath(String jspPath) {
        this.jspPath = jspPath;
    }

    public String getGenPackage() {
        return genPackage;
    }

    public String getActionPackage() {
        return actionPackage;
    }

    public String getActionPath() {
        return actionPath;
    }

    public String getActionRerPath() {
        return actionRerPath;
    }

    public String getFormPackage() {
        return formPackage;
    }

    public String getUserPackage() {
        return userPackage;
    }

    public void setUserPackage(String userPackage) {
        this.userPackage = userPackage;
    }

}
