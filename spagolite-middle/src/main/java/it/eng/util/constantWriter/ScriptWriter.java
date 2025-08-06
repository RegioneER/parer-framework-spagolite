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

package it.eng.util.constantWriter;

import it.eng.spagoLite.xmlbean.form.Button;
import it.eng.spagoLite.xmlbean.form.ButtonList;
import it.eng.spagoLite.xmlbean.form.Fields;
import it.eng.spagoLite.xmlbean.form.Form;
import it.eng.spagoLite.xmlbean.form.FormDocument;
import it.eng.spagoLite.xmlbean.form.Link;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.map.CaseInsensitiveMap;

import it.eng.spagoLite.xmlbean.form.List;
import it.eng.util.ClassUtil;

public class ScriptWriter {

    private final static String SEPARATOR = "/";
    private final static Pattern list = Pattern
	    .compile("\\<slf:list .*?name\\=\"\\<%\\=\\s?(.+?)\\.NAME\\s?%\\>\""); // <slf:list
										   // .+name="<%=(.+)%>"

    private final static Pattern selectlist = Pattern.compile(
	    "\\<slf:selectList .*?name\\=\"\\<%\\=\\s?(.+?)\\.NAME\\s?%\\>\".*?(addList\\=\"(.+?)\")?\\s*/>");
    private final static Pattern navBar = Pattern.compile(
	    "\\<slf:listNavBarDetail .*?name\\=\"\\<%\\=\\s?(.+?)\\.NAME\\s?%\\>.*?(hideOperationButton\\=\"(.+?)\")?\\s*/>");
    private final static Pattern fieldBar = Pattern.compile(
	    "\\<slf:fieldBarDetailTag .*?name\\=\"\\<%\\=\\s?(.+?)\\.NAME\\s?%\\>\"(.*?((hideOperationButton)|(hideDeleteButton)|(hideInsertButton)|(hideDetailButton)|(hideUpdateButton)).*?)*/>");
    private final static Pattern buttonList = Pattern
	    .compile("\\<slf:buttonList .*?name\\=\"\\<%\\=\\s?(.+?)\\.NAME\\s?%\\>\"");
    private final static Pattern lblField = Pattern
	    .compile("\\<slf:lblField .*?name\\=\"\\<%\\=\\s?(.+?)%\\>\"");
    // {0} &nome_azione {1} &pagina

    private final static String insert = "insert into APL_AZIONE_PAGINA \n"
	    + "(id_azione_pagina, id_pagina_web, nm_azione_pagina, ds_azione_pagina) \n"
	    + "select sapl_azione_pagina.nextval, \n"
	    + " pag.id_pagina_web, '{0}', '&descr_azione' \n"
	    + "from APL_APPLIC apl join APL_PAGINA_WEB pag on (pag.id_applic = apl.id_applic and pag.nm_pagina_web = '{1}') \n"
	    + " where nm_applic = 'SACER' and not exists ( \n"
	    + "select * from APL_AZIONE_PAGINA azio \n"
	    + "where azio.id_pagina_web = pag.id_pagina_web \n"
	    + "and azio.nm_azione_pagina = '{0}'); \n";

    public static void writePages(Writer writer, String jspBase, java.util.List<File> jspList)
	    throws IOException {

	writer.write("## Lista delle pagine\n\n");
	for (File file : jspList) {
	    if (file.getName().endsWith(".jsp")) {
		String jspName = file.getPath().substring(jspBase.length()).replace("\\", "/");
		jspName = jspName.substring(0, jspName.length() - ".jsp".length());
		writer.write(jspName + "\n");
	    }
	}

    }

    public static void writeActions(Writer writer, java.util.List<File> jspList,
	    java.util.List<File> files) throws Exception {
	StringBuilder sb = new StringBuilder();
	// Map<String, String> actions = new HashMap<String, String>();
	Map<String, String> actions = new CaseInsensitiveMap();
	Map<String, String> actionsNavBar = new CaseInsensitiveMap();
	for (File file : files) {
	    FormDocument formDoc = FormDocument.Factory.parse(file);
	    String className = ClassUtil.getFormClassName(file);
	    Form form = formDoc.getForm();

	    // writer.write("## Azioni nella form "+className+"\n\n");
	    List[] listArray = form.getListArray();
	    // processo le liste
	    for (int i = 0; i < listArray.length; i++) {
		sb = new StringBuilder();
		if (!listArray[i].getHideUpdateButton())
		    sb.append("detail" + SEPARATOR + className + "#" + listArray[i].getName()
			    + SEPARATOR + "edit|");
		if (!listArray[i].getHideDeleteButton())
		    sb.append("detail" + SEPARATOR + className + "#" + listArray[i].getName()
			    + SEPARATOR + "delete|");
		if (!listArray[i].getHideDetailButton())
		    sb.append("detail" + SEPARATOR + className + "#" + listArray[i].getName()
			    + SEPARATOR + "view|");
		if (!listArray[i].getHideInsertButton())
		    sb.append("detail" + SEPARATOR + className + "#" + listArray[i].getName()
			    + SEPARATOR + "insert|");
		for (Link link : listArray[i].getLinkArray()) {
		    if (link.getIsTargetList()) {
			sb.append("detail" + SEPARATOR + className + "#" + link.getTarget()
				+ SEPARATOR + "view|");
		    } else {
			sb.append("detail" + SEPARATOR + className + "#" + listArray[i].getName()
				+ SEPARATOR + link.getTarget() + "|");
		    }
		}
		if (actions.get(className + "." + listArray[i].getName()) != null)
		    throw new Exception();
		if (!sb.toString().isEmpty()) {
		    actions.put(className + "." + listArray[i].getName(), sb.toString());
		    // writer.write(sb.toString());
		}
	    }

	    // stesso giro delle liste per le barre di navigazione
	    for (int i = 0; i < listArray.length; i++) {
		sb = new StringBuilder();
		if (!listArray[i].getHideUpdateButton())
		    sb.append("toolbar" + SEPARATOR + "edit|");
		if (!listArray[i].getHideDeleteButton())
		    sb.append("toolbar" + SEPARATOR + "delete|");
		if (!listArray[i].getHideInsertButton())
		    sb.append("toolbar" + SEPARATOR + "insert|");
		for (Link link : listArray[i].getLinkArray()) {
		    if (link.getIsTargetList()) {
			sb.append("toolbar" + SEPARATOR + link.getName() + "|");
		    }
		}
		if (actionsNavBar.get(className + "." + listArray[i].getName()) != null)
		    throw new Exception();
		if (!sb.toString().isEmpty()) {
		    actionsNavBar.put(className + "." + listArray[i].getName(), sb.toString());
		}
	    }

	    // processo la lista di bottoni
	    ButtonList[] buttonListArray = form.getButtonListArray();
	    for (int i = 0; i < buttonListArray.length; i++) {
		String s = "";
		String s1 = "";
		for (Button b : buttonListArray[i].getButtonArray()) {
		    if (b.getSecure()) {
			s += "button" + SEPARATOR + className + "#" + buttonListArray[i].getName()
				+ SEPARATOR + b.getName() + "|";
			s1 = "button" + SEPARATOR + className + "#" + buttonListArray[i].getName()
				+ SEPARATOR + b.getName();
			if (actions.get(className + "." + buttonListArray[i].getName() + "."
				+ b.getName().toUpperCase()) != null)
			    throw new Exception();
			actions.put(className + "." + buttonListArray[i].getName() + "."
				+ ClassUtil.getConstantName(b.getName()).toUpperCase(), s1);
		    }
		    // s += "operation"+SEPARATOR+className.replace("Form", "Action")+SEPARATOR+
		    // b.getName()+"|";
		}
		if (actions.get(className + "." + buttonListArray[i].getName()) != null)
		    throw new Exception();
		actions.put(className + "." + buttonListArray[i].getName(), s);

		// writer.write(s);
	    }

	    // processo i bottoni singoli
	    Fields[] fieldsArray = form.getFieldsArray();
	    for (int i = 0; i < fieldsArray.length; i++) {
		for (Button b : fieldsArray[i].getButtonArray()) {
		    String s = "";
		    if (b.getSecure()) {
			s = "button" + SEPARATOR + className + "#" + fieldsArray[i].getName()
				+ SEPARATOR + b.getName();
		    }
		    // String s = "operation"+SEPARATOR+className.replace("Form",
		    // "Action")+SEPARATOR+ b.getName();
		    if (actions.get(className + "." + fieldsArray[i].getName() + "."
			    + b.getName().toUpperCase()) != null)
			throw new Exception();
		    actions.put(className + "." + fieldsArray[i].getName() + "."
			    + ClassUtil.getConstantName(b.getName()).toUpperCase(), s);
		    // writer.write(s);
		}
	    }
	    // writer.write("\n");
	}
	for (File file : jspList) {
	    sb = new StringBuilder();
	    FileInputStream fstream = new FileInputStream(file);
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String strLine;
	    while ((strLine = br.readLine()) != null) {
		sb.append(strLine);
	    }

	    br.close();
	    String jsp = sb.toString();
	    // Liste
	    Matcher matcher = list.matcher(jsp);
	    while (matcher.find()) {
		// System.out.println("DEBUG LIST: '" + matcher.group(1)+"'");
		String match = actions.get(matcher.group(1));
		if (match != null) {

		    for (String st : match.split("\\|")) {
			System.out.println("LIST - " + file.getName() + " - " + st);
			writer.write(insert.replaceAll("\\{0\\}", st).replaceAll("\\{1\\}",
				"/" + file.getName().substring(0, file.getName().length() - 4))
				+ "\n");
		    }
		}
	    }

	    // select List TESTARE IL GROUP 2, MA ANCHE l'1!
	    matcher = selectlist.matcher(jsp);
	    while (matcher.find()) {
		// System.out.println("DEBUG SELECTLIST: '" + matcher.group(1)+"'");
		String match = actions.get(matcher.group(1));
		if (match != null) {

		    for (String st : match.split("\\|")) {
			System.out.println("SELECTLIST - " + file.getName() + " - " + st);
			writer.write(insert.replaceAll("\\{0\\}", st).replaceAll("\\{1\\}",
				"/" + file.getName().substring(0, file.getName().length() - 4))
				+ "\n");
		    }
		}
		if (matcher.group(2) != null) {
		    // System.out.println("DEBUG - SELECTLIST - "+matcher.group(2));
		    String st = "detail" + SEPARATOR + matcher.group(1).replace(".", "#")
			    + SEPARATOR
			    + ("true".equalsIgnoreCase(matcher.group(3)) ? "add" : "remove");
		    System.out.println("SELECTLIST - " + file.getName() + " - " + st);

		    writer.write(insert.replaceAll("\\{0\\}", st).replaceAll("\\{1\\}",
			    "/" + file.getName().substring(0, file.getName().length() - 4)) + "\n");
		}

	    }

	    // nav Bar
	    matcher = navBar.matcher(jsp);
	    while (matcher.find()) {
		// System.out.println("DEBUG NAVBAR: '" + matcher.group(1)+"'");
		String match = actionsNavBar.get(matcher.group(1));
		if (match != null && matcher.group(2) == null) {

		    match = actionsNavBar.get(matcher.group(1));
		    for (String st : match.split("\\|")) {
			if (!st.endsWith("view")) {
			    System.out.println("NAVBAR - " + file.getName() + " - " + st);
			    writer.write(insert.replaceAll("\\{0\\}", st).replaceAll("\\{1\\}",
				    "/" + file.getName().substring(0, file.getName().length() - 4))
				    + "\n");
			}
			// if(matcher.group(2)!=null)
			// System.out.println("NAVBAR - "+file.getName() + " -
			// dettaglio"+SEPARATOR+matcher.group(1).replace(".", "#") + SEPARATOR+
			// matcher.group(2));
		    }
		}
	    }

	    // field Bar
	    matcher = fieldBar.matcher(jsp);
	    while (matcher.find()) {
		sb = new StringBuilder();
		String barActions = matcher.group(0);
		if (!barActions.contains("hideOperationButton=\"true\"")) {
		    if (!barActions.contains("hideUpdateButton=\"true\""))
			sb.append("toolbar" + SEPARATOR + "edit|");
		    if (!barActions.contains("hideDeleteButton=\"true\""))
			sb.append("toolbar" + SEPARATOR + "delete|");
		    // if(!barActions.contains("hideDetailButton"))
		    // sb.append("dettaglio"+SEPARATOR+matcher.group(1).replace(".",
		    // "#")+SEPARATOR+"dettaglio|");
		    if (!barActions.contains("hideInsertButton=\"true\""))
			sb.append("toolbar" + SEPARATOR + "insert|");
		}
		if (!sb.toString().isEmpty()) {

		    for (String st : sb.toString().split("\\|")) {
			System.out.println("FIELDBAR - " + file.getName() + " - " + st);
			writer.write(insert.replaceAll("\\{0\\}", st).replaceAll("\\{1\\}",
				"/" + file.getName().substring(0, file.getName().length() - 4))
				+ "\n");
		    }
		}

	    }

	    // button list
	    matcher = buttonList.matcher(jsp);

	    while (matcher.find()) {
		// System.out.println("DEBUG FIELDBAR: '" + matcher.group(1)+"'");
		String match = actions.get(matcher.group(1));
		if (match != null) {
		    for (String st : match.split("\\|")) {
			System.out.println("BUTTONLIST - " + file.getName() + " - " + st);
			writer.write(insert.replaceAll("\\{0\\}", st).replaceAll("\\{1\\}",
				"/" + file.getName().substring(0, file.getName().length() - 4))
				+ "\n");
		    }
		}

	    }

	    // lblfield
	    matcher = lblField.matcher(jsp);

	    while (matcher.find()) {
		// System.out.println("DEBUG - BUTTON - "+matcher.group(1));
		if (actions.get(matcher.group(1)) != null) {
		    System.out.println(
			    "BUTTON - " + file.getName() + " - " + actions.get(matcher.group(1)));
		    String st = actions.get(matcher.group(1));
		    writer.write(insert.replaceAll("\\{0\\}", st).replaceAll("\\{1\\}",
			    "/" + file.getName().substring(0, file.getName().length() - 4)) + "\n");
		}

	    }

	}

    }

}
