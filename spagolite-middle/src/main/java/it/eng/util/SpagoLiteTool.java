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

package it.eng.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import it.eng.spagoLite.xmlbean.form.FormDocument;
import it.eng.util.actionWriter.ActionWriter;
import it.eng.util.constantWriter.ConstantWriter;
import it.eng.util.formWriter.FormWriter;
import org.apache.xmlbeans.XmlException;

public class SpagoLiteTool {

    private static final String srcPath = "./src";
    private static final String formPath = "./forms";
    private static final String jspPath = "./web/jsp";
    private static final String confPath = "./web/WEB-INF/conf/spago";

    private String actionPath;
    private String actionRerPath;

    private String genPackage;
    private String actionPackage;
    private String formPackage;

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

    public SpagoLiteTool(String actionPath, String actionRerPath, String genPackage,
	    String actionPackage, String formPackage) {
	this.actionPath = actionPath;
	this.actionRerPath = actionRerPath;

	this.genPackage = genPackage;
	this.actionPackage = actionPackage;
	this.formPackage = formPackage;
    }

    private void writeAction(FileWriter fileWriter, String path) {
	List<File> actionFiles = FileSystemUtil.getFileList(path, "java",
		FileSystemUtil.EXLUDE_PATH);
	for (File file : actionFiles) {
	    String fullClassName = file.getPath()
		    .substring(new File(srcPath).getPath().length() + 1);
	    fullClassName = fullClassName.substring(0, fullClassName.length() - ".java".length());
	    fullClassName = fullClassName.replace("\\", ".");

	    String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);

	    try {
		Class.forName(fullClassName).newInstance();
		fileWriter.write(" <action name=\"" + ClassUtil.getConstantName(className)
			+ "\" class=\"" + fullClassName + "\" scope=\"REQUEST\" />\n");
	    } catch (Throwable e) {
	    }
	}
    }

    private void writePublisher(List<File> jspFiles) throws IOException {

	FileWriter fileWriter = new FileWriter(confPath + "/publishers.xml");
	fileWriter.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
	fileWriter.write(
		"<publishers xmlns=\"http://www.spagoLite.eng.it/spagoLite/publishers\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema\">\n");
	for (File file : jspFiles) {
	    String name = file.getName().substring(0, file.getName().length() - ".jsp".length());
	    fileWriter.write(
		    " <publisher name=\"" + ClassUtil.getConstantName(name) + "_PUBLISHER\">\n");
	    fileWriter.write("  <rendering channel=\"HTTP\" type=\"JSP\" mode=\"FORWARD\">\n");
	    fileWriter.write("   <resources>\n");
	    fileWriter.write("    <item prog=\"0\" resource=\""
		    + file.getPath().substring(5).replace("\\", "/") + "\" />\n");
	    fileWriter.write("   </resources>\n");
	    fileWriter.write("  </rendering>\n");
	    fileWriter.write(" </publisher>\n");
	}

	fileWriter.write("</publishers>\n");
	fileWriter.close();
    }

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
		String actionFileName = ClassUtil.getActionFileName(srcPath, getActionPackage(),
			file);

		String formClassName = ClassUtil.getFormClassName(file);
		String formFileName = ClassUtil.getFormFileName(srcPath, getFormPackage(), file);

		// Action
		FileWriter actionFileWriter = new FileWriter(actionFileName);
		ActionWriter actionWriter = new ActionWriter(getActionPackage(), actionClassName,
			getFormPackage(), formClassName, formDocument.getForm(), null);
		actionWriter.write(actionFileWriter);
		actionFileWriter.close();

		// Form
		FileWriter formFileWriter = new FileWriter(formFileName);
		FormWriter formWriter = new FormWriter(getFormPackage(), formClassName,
			formDocument.getForm());
		formWriter.write(formFileWriter);
		formFileWriter.close();

	    }

	    // File di configurazioni
	    List<File> actionFiles = FileSystemUtil.getFileList(getActionPath(), "java",
		    FileSystemUtil.EXLUDE_PATH);
	    List<File> jspFiles = FileSystemUtil.getFileList(jspPath, "jsp",
		    FileSystemUtil.EXLUDE_PATH);

	    // Write actions.xml
	    System.out.println("Write actions.xml");
	    FileWriter fileWriter = new FileWriter(confPath + "/actions.xml");
	    fileWriter.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
	    fileWriter.write(
		    "<actions xmlns=\"http://www.spagoLite.eng.it/spagoLite/action\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema\">\n");
	    fileWriter.write(
		    " <action name=\"REDIRECT_ACTION\" class=\"it.eng.spagoLite.actions.RedirectAction\" scope=\"REQUEST\" />\n");
	    writeAction(fileWriter, getActionPath());
	    writeAction(fileWriter, getActionRerPath());
	    fileWriter.write("</actions>\n");
	    fileWriter.close();

	    // Get Jsp
	    System.out.println("Write publisher.xml");
	    writePublisher(jspFiles);

	    FileWriter writer = new FileWriter(
		    ClassUtil.getConstantFileName(srcPath, getGenPackage()));
	    ConstantWriter constantWriter = new ConstantWriter(getGenPackage(), actionPath,
		    actionFiles, jspFiles);
	    constantWriter.write(writer);
	    writer.close();

	} catch (IOException | XmlException e) {
	    e.printStackTrace();
	}

    }

    public static String getGeneratedAnnotation(String className) {
	StringBuilder writer = new StringBuilder();
	writer.append("  @Generated(\n");
	writer.append("    value = \"").append(className).append("\",\n");
	writer.append("    comments = \"Questa classe e' stata generata dal SipsTool\"\n");
	writer.append("  )\n");
	return writer.toString();
    }
}
