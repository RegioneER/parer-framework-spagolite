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

package it.eng.util.actionWriter;

import it.eng.spagoLite.xmlbean.form.Button;
import it.eng.spagoLite.xmlbean.form.ButtonList;
import it.eng.spagoLite.xmlbean.form.Element;
import it.eng.spagoLite.xmlbean.form.Field;
import it.eng.spagoLite.xmlbean.form.Fields;
import it.eng.spagoLite.xmlbean.form.Form;
import it.eng.spagoLite.xmlbean.form.Link;
import it.eng.spagoLite.xmlbean.form.List;
import it.eng.spagoLite.xmlbean.form.NestedList;
import it.eng.spagoLite.xmlbean.form.Step;
import it.eng.spagoLite.xmlbean.form.Tab;
import it.eng.spagoLite.xmlbean.form.TabElement;
import it.eng.spagoLite.xmlbean.form.Wizard;
import it.eng.util.SpagoLiteTool;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.text.WordUtils;

public class ActionWriter {

    private String actionPackageName;
    private String actionClassName;
    private String formPackageName;
    private String formClassName;
    private Form form;
    private String userPackage;

    public String getActionPackageName() {
        return actionPackageName;
    }

    public String getActionClassName() {
        return actionClassName;
    }

    public String getFormPackageName() {
        return formPackageName;
    }

    public String getFormClassName() {
        return formClassName;
    }

    public Form getForm() {
        return form;
    }

    public ActionWriter(String actionPackageName, String actionClassName, String formPackageName, String formClassName,
            Form form, String userPackage) {
        this.actionPackageName = actionPackageName;
        this.actionClassName = actionClassName;
        this.formPackageName = formPackageName;
        this.formClassName = formClassName;
        this.form = form;
        this.userPackage = userPackage;
    }

    public void write(Writer writer) throws IOException {
        writer.write("package " + getActionPackageName() + ";\n");
        writer.write("\n");

        writer.write("import " + getFormPackageName() + "." + getFormClassName() + ";\n");
        writer.write("import javax.annotation.Generated;\n");
        writer.write("import it.eng.spagoCore.error.EMFError;\n");
        writer.write("import it.eng.spagoLite.actions.form.FormAction;\n");
        writer.write("import it.eng.spagoLite.actions.form.ListAction;\n");
        writer.write("import it.eng.spagoLite.db.base.sorting.SortingRule;\n");
        writer.write("import it.eng.spagoLite.form.fields.SingleValueField;\n");
        writer.write("import it.eng.spagoLite.form.fields.Section;\n");
        writer.write("import it.eng.spagoLite.form.fields.Field;\n");
        writer.write("import it.eng.spagoLite.form.fields.Fields;\n");
        writer.write("import it.eng.spagoLite.form.list.List;\n");
        writer.write("import it.eng.spagoLite.form.list.NestedList;\n");
        writer.write("import org.codehaus.jettison.json.JSONObject;\n");
        writer.write("import it.eng.spagoLite.form.wizard.Step;\n");
        writer.write("import it.eng.spagoLite.form.wizard.EndPage;\n");
        writer.write("import it.eng.spagoLite.form.wizard.WizardElement;\n");
        writer.write("import it.eng.spagoLite.form.wizard.Wizard.WizardNavigation;\n");
        writer.write("import it.eng.spagoLite.form.wizard.Wizard;\n");
        writer.write("import it.eng.spagoLite.security.Secure;\n");
        writer.write(
                "import "
                        + (userPackage != null ? userPackage
                                : getActionPackageName().subSequence(0, getActionPackageName().lastIndexOf(".")))
                        + ".User;\n");

        writer.write("\n");
        if (getForm().getListArray().length == 0) {
            writer.write("public abstract class " + getActionClassName() + " extends FormAction<" + getFormClassName()
                    + ", User> {\n");
        } else {
            writer.write("public abstract class " + getActionClassName() + " extends ListAction<" + getFormClassName()
                    + ", User> {\n");
        }
        writer.write("\n");
        writer.write("  private static final long serialVersionUID = 1L;\n");
        writer.write("\n");
        writer.write(SpagoLiteTool.getGeneratedAnnotation(this.getClass().getName()));
        writer.write("\n");
        writer.write("  @Override\n");
        writer.write("  public " + getFormClassName() + " newForm() {\n");
        writer.write("    return new " + getFormClassName() + "();\n");
        writer.write("  }\n");
        // writer.write("\n");
        // writer.write(" protected abstract void forwardTo(String fieldPublisher);\n");
        writer.write("\n");
        writer.write("  public abstract void initOnClick () throws EMFError;\n");
        writer.write("\n");

        // Genero i tab
        Tab[] tabArray = getForm().getTabArray();
        for (int i = 0; i < tabArray.length; i++) {
            TabElement tabElementArray[] = tabArray[i].getTabElementArray();

            for (int j = 0; j < tabElementArray.length; j++) {
                writer.write(
                        "  public abstract void tab" + tabElementArray[j].getName() + "OnClick () throws EMFError;\n");
                writer.write("\n");
            }
        }

        writer.write("\n");
        // Genero gli stub per i campi dipendenti
        for (Fields fields : getForm().getFieldsArray()) {
            writeFields(fields, writer);
        }

        for (List list : getForm().getListArray()) {
            writeList(list, writer);
        }

        for (NestedList nestedList : getForm().getNestedListArray()) {
            writeNestedList(nestedList, writer);
        }

        for (Fields fields : getForm().getFieldsArray()) {
            // Genero gli stub per le operazioni dei pulsanti
            for (Element button : fields.getButtonArray()) {
                writer.write("  //Operazione: \"" + button.getName() + "\"\n");
                if (((Button) button).getSecure()) {
                    writer.write("  @Secure(action=\"button/" + formClassName + "#" + fields.getName() + "/"
                            + button.getName() + "\")\n");
                }
                writer.write("  public abstract void " + button.getName() + "() throws EMFError;\n");
                writer.write("\n");
            }
            // Genero gli stub per le operazioni dei link
            for (Link link : fields.getLinkArray()) {
                if (!link.getIsTargetList()) {
                    writer.write("  //Operazione: \"" + link.getTarget() + "\"\n");
                    writer.write("  @Secure(action=\"detail/" + formClassName + "#" + fields.getName() + "/"
                            + link.getTarget() + "\")\n");
                    writer.write("  public abstract void " + link.getTarget() + "() throws EMFError;\n");
                    writer.write("\n");
                }
            }
        }
        // Genero gli stub per le operazioni dei pulsanti presenti sulle liste.I pulsanti agiscono SOLO sulla riga
        // corrente (1 operazione contemporanea sulla lista)
        if (getForm().getListArray() != null) {
            for (List list : getForm().getListArray()) {
                for (Element button : list.getButtonArray()) {
                    writer.write("  //Operazione: \"" + button.getName() + "\"\n");
                    writer.write("  public abstract void " + button.getName() + "() throws EMFError;\n");
                    writer.write("\n");
                }
            }
        }

        writeFieldsOperations(writer);
        writeListsOperations(writer);
        writeNestedListsOperations(writer);
        // Genero gli stub per le operazioni dei pulsanti presenti sulle ButtonList.
        if (getForm().getButtonListArray() != null) {
            for (ButtonList list : getForm().getButtonListArray()) {
                writer.write("  /*\n");
                writer.write("   * Pulsantiera " + list.getName() + " \n");
                writer.write("   */\n");
                for (Button button : list.getButtonArray()) {
                    writer.write("  // Operazione: \"" + button.getName() + "\"\n");
                    if (button.getSecure()) {
                        writer.write("  @Secure(action=\"button/" + formClassName + "#" + list.getName() + "/"
                                + button.getName() + "\")\n");
                    }
                    writer.write("  public abstract void " + button.getName() + "() throws Throwable;\n");
                    writer.write("\n");
                }

            }
        }

        writer.write("\n");

        // Genero gli stub per i wizard
        writeWizard(writer);

        writer.write("}\n");
    }

    private void writeFields(Fields fields, Writer writer) throws IOException {
        String fqn = fields.getName();

        if (fields.getCheckBoxArray() != null) {
            for (Field field : fields.getCheckBoxArray()) {
                if (field.getTrigger()) {
                    writer.write(getTriggerFunction(fqn, field.getName()));
                }
            }
        }
        if (fields.getComboBoxArray() != null) {
            for (Field field : fields.getComboBoxArray()) {
                if (field.getTrigger()) {
                    writer.write(getTriggerFunction(fqn, field.getName()));
                }
            }
        }
        if (fields.getInputArray() != null) {
            for (Field field : fields.getInputArray()) {
                if (field.getTrigger()) {
                    writer.write(getTriggerFunction(fqn, field.getName()));
                }
            }
        }
        if (fields.getMultiSelectArray() != null) {
            for (Field field : fields.getMultiSelectArray()) {
                if (field.getTrigger()) {
                    writer.write(getTriggerFunction(fqn, field.getName()));
                }
            }
        }
        if (fields.getButtonArray() != null) {
            for (Field field : fields.getButtonArray()) {
                if (field.getTrigger()) {
                    writer.write(getTriggerFunction(fqn, field.getName()));
                }
            }
        }
        if (fields.getRadioArray() != null) {
            for (Field field : fields.getRadioArray()) {
                if (field.getTrigger()) {
                    writer.write(getTriggerFunction(fqn, field.getName()));
                }
            }
        }
    }

    private String getTriggerFunction(String fqn, String FieldName) {
        String fqFieldName = fqn + WordUtils.capitalize(FieldName);
        StringBuilder writer = new StringBuilder("");
        writer.append("  //per interazioni non Ajax \n");
        writer.append("  public abstract JSONObject trigger" + fqFieldName + "OnTrigger () throws EMFError;\n");
        writer.append("  \n");
        writer.append("  public void trigger" + fqFieldName + "OnTriggerNoAjax () throws EMFError {\n");
        writer.append("    trigger" + fqFieldName + "OnTrigger(); \n");
        writer.append("    forwardToPublisher(getLastPublisher()); \n");
        writer.append("  } \n");
        writer.append("  \n");
        writer.append("  public void trigger" + fqFieldName + "OnTriggerAjax () throws EMFError {\n");
        writer.append("    JSONObject jsonObject = trigger" + fqFieldName + "OnTrigger(); \n");
        writer.append("    redirectToAjax(jsonObject); \n");
        writer.append("  }\n");
        return writer.toString();
    }

    private void writeList(List list, Writer writer) throws IOException {
        String fqn = list.getName();
        if (list.getCheckBoxArray() != null) {
            for (Field field : list.getCheckBoxArray()) {
                if (field.getTrigger()) {
                    writer.write(getTriggerFunction(fqn, field.getName()));
                }
            }
        }
        if (list.getComboBoxArray() != null) {
            for (Field field : list.getComboBoxArray()) {
                if (field.getTrigger()) {
                    writer.write(getTriggerFunction(fqn, field.getName()));
                }
            }
        }
        if (list.getInputArray() != null) {
            for (Field field : list.getInputArray()) {
                if (field.getTrigger()) {
                    writer.write(getTriggerFunction(fqn, field.getName()));
                }
            }
        }

    }

    private void writeListsOperations(Writer writer) throws IOException {
        if (getForm().getListArray() != null && getForm().getListArray().length > 0) {

            String[] listOperations = new String[] { "update", "select", "delete", "postLazyLoad",
                    "filterInactiveRecords", "goToPageNavigation" };
            for (String operation : listOperations) {
                writeListOperationManager(operation, writer);
            }
            writer.write("\n");
            for (List list : getForm().getListArray()) {
                writer.write("  // Operazioni disponibili per la lista " + list.getName() + "\n");
                for (String operation : listOperations) {
                    writer.write("  public void " + operation + list.getName() + "() throws EMFError {\n");
                    writer.write("  }\n");
                    writer.write("\n");
                }
            }
        }
    }

    private void writeListOperationManager(String operation, Writer writer) throws IOException {
        writer.write("\n");
        writer.write("  /**\n");
        writer.write("   * Manager per le operazioni di tipo \"" + operation + "\"\n");
        writer.write("   * \n");
        writer.write("   * @param list lista operazioni\n");
        writer.write("   * @throws EMFError in caso di errore\n");
        writer.write("   */\n");
        writer.write("  public void " + operation + "(List<?> list) throws EMFError {\n");
        writer.write("   ");
        for (List list : getForm().getListArray()) {

            writer.write(" if (getForm().get" + list.getName() + "().equals(list)) {\n");
            writer.write("      " + operation + list.getName() + "();\n");
            writer.write("    } else");
        }
        writer.write(" {\n");
        writer.write("      return;\n");
        writer.write("    }\n");
        writer.write("  }\n");
    }

    private void writeFieldsOperations(Writer writer) throws IOException {
        if (getForm().getFieldsArray() != null && getForm().getFieldsArray().length > 0) {

            String[] fieldOperations = new String[] { "update", "select", "delete" };
            for (String operation : fieldOperations) {
                writeFieldOperationManager(operation, writer);
            }
            writer.write("\n");
            for (Fields fields : getForm().getFieldsArray()) {
                writer.write("  // Operazioni disponibili per la lista " + fields.getName() + "\n");
                for (String operation : fieldOperations) {
                    writer.write("  public void " + operation + fields.getName() + "() throws EMFError {\n");
                    writer.write("  }\n");
                    writer.write("\n");
                }
            }
        }
    }

    private void writeFieldOperationManager(String operation, Writer writer) throws IOException {
        writer.write("\n");
        writer.write("  /**\n");
        writer.write("   * Manager per le operazioni di tipo \"" + operation + "\"\n");
        writer.write("   * \n");
        writer.write("   * @param field nome campo\n");
        writer.write("   * @throws EMFError in caso di errore\n");
        writer.write("   */\n");
        writer.write("  public void " + operation + "(Fields<Field> field) throws EMFError {\n");
        writer.write("   ");
        for (Fields field : getForm().getFieldsArray()) {

            writer.write(" if (getForm().get" + field.getName() + "().equals(field)) {\n");
            writer.write("      " + operation + field.getName() + "();\n");
            writer.write("    } else");
        }
        writer.write(" {\n");
        writer.write("      return;\n");
        writer.write("    }\n");
        writer.write("  }\n");
    }

    private void writeNestedList(NestedList nestedList, Writer writer) throws IOException {
        String fqn = nestedList.getName();
        if (nestedList.getCheckBoxArray() != null) {
            for (Field field : nestedList.getCheckBoxArray()) {
                if (field.getTrigger()) {
                    writer.write(getTriggerFunction(fqn, field.getName()));
                }
            }
        }
        if (nestedList.getComboBoxArray() != null) {
            for (Field field : nestedList.getComboBoxArray()) {
                if (field.getTrigger()) {
                    writer.write(getTriggerFunction(fqn, field.getName()));
                }
            }
        }
        if (nestedList.getInputArray() != null) {
            for (Field field : nestedList.getInputArray()) {
                if (field.getTrigger()) {
                    writer.write(getTriggerFunction(fqn, field.getName()));
                }
            }
        }

    }

    private void writeNestedListsOperations(Writer writer) throws IOException {
        if (getForm().getNestedListArray() != null && getForm().getNestedListArray().length > 0) {

            String[] listOperations = new String[] { "update", "select", "delete" };
            for (String operation : listOperations) {
                writeNestedListOperationManager(operation, writer);
            }
            writer.write("\n");
            for (NestedList nestedList : getForm().getNestedListArray()) {
                writer.write("  // Operazioni disponibili per la lista " + nestedList.getName() + "\n");
                for (String operation : listOperations) {
                    writer.write("  public void " + operation + nestedList.getName() + "() throws EMFError {\n");
                    writer.write("  }\n");
                    writer.write("\n");
                }
            }
        }
    }

    private void writeNestedListOperationManager(String operation, Writer writer) throws IOException {
        writer.write("\n");
        writer.write("  /**\n");
        writer.write("   * Manager per le operazioni di tipo \"" + operation + "\"\n");
        writer.write("   * \n");
        writer.write("   * @param nestedList lista operazioni\n");
        writer.write("   * @throws EMFError in caso di errore\n");
        writer.write("   */\n");
        writer.write("  public void " + operation + "(NestedList<?> nestedList) throws EMFError {\n");
        writer.write("   ");
        for (NestedList nestedList : getForm().getNestedListArray()) {

            writer.write(" if (getForm().get" + nestedList.getName() + "().equals(nestedList)) {\n");
            writer.write("      " + operation + nestedList.getName() + "();\n");
            writer.write("    } else");
        }
        writer.write(" {\n");
        writer.write("      return;\n");
        writer.write("    }\n");
        writer.write("  }\n");
    }

    private void writeWizard(Writer writer) throws IOException {
        if (getForm().getWizardArray() != null && getForm().getWizardArray().length > 0) {

            // wizard On Enter
            for (Wizard wizard : getForm().getWizardArray()) {
                String baseWizardMethod = WordUtils.uncapitalize(wizard.getName());
                writer.write("  private void " + baseWizardMethod + "OnEnter(Wizard wizard) throws EMFError {\n");
                for (Step step : wizard.getStepArray()) {
                    String wizardName = "get" + WordUtils.capitalize(wizard.getName()) + "()";
                    String stepGetterName = "get" + step.getName() + "()";
                    String baseStepMethod = baseWizardMethod + WordUtils.capitalize(step.getName());

                    writer.write("    if (getForm()." + wizardName + "." + stepGetterName
                            + " ==  wizard.getCurrentElement()) {\n");
                    writer.write("      " + baseStepMethod + "OnEnter();\n");
                    writer.write("      return;\n");
                    writer.write("    }\n");
                }
                writer.write("  }\n");
                writer.write("\n");
            }

            // wizard On Exit
            for (Wizard wizard : getForm().getWizardArray()) {
                String baseWizardMethod = WordUtils.uncapitalize(wizard.getName());
                writer.write("  private boolean " + baseWizardMethod + "OnExit(Wizard wizard) throws EMFError {\n");
                for (Step step : wizard.getStepArray()) {
                    String wizardName = "get" + WordUtils.capitalize(wizard.getName()) + "()";
                    String stepGetterName = "get" + step.getName() + "()";
                    String baseStepMethod = baseWizardMethod + WordUtils.capitalize(step.getName());

                    writer.write("    if (getForm()." + wizardName + "." + stepGetterName
                            + " ==  wizard.getCurrentElement()) {\n");
                    writer.write("      return " + baseStepMethod + "OnExit();\n");
                    writer.write("    }\n");
                }
                writer.write("    return false;\n");
                writer.write("  }\n");
                writer.write("\n");
            }

            writer.write("  /**\n");
            writer.write("   * Operazione sui wizard\n");
            writer.write("   * \n");
            writer.write("   * @param step nome step\n");
            writer.write("   * @param wizardNavigation nome wizard\n");
            writer.write("   * @throws EMFError in caso di errore\n");
            writer.write("   */\n");
            writer.write(
                    "  public void wizard(Wizard wizard, WizardElement element, Wizard.WizardNavigation wizardNavigation) throws EMFError {\n");
            for (Wizard wizard : getForm().getWizardArray()) {
                String wizardGetterName = "get" + wizard.getName() + "()";
                writer.write("    if (getForm()." + wizardGetterName + ".equals(wizard)) {\n");
                writer.write("      forwardToPublisher(getDefault" + wizard.getName() + "Publisher());\n");
                writer.write("    }\n");
            }

            for (Wizard wizard : getForm().getWizardArray()) {
                String baseWizardMethod = WordUtils.uncapitalize(wizard.getName());
                String wizardGetterName = "get" + wizard.getName() + "()";

                writer.write("    if (getForm()." + wizardGetterName
                        + ".equals(wizard) && WizardNavigation.Cancel.equals(wizardNavigation)) {\n");
                writer.write("      " + baseWizardMethod + "OnCancel();\n");
                writer.write("    } else if (getForm()." + wizardGetterName
                        + ".equals(wizard) && WizardNavigation.Save.equals(wizardNavigation)) {\n");
                writer.write("      if (" + baseWizardMethod + "OnExit(wizard) && " + baseWizardMethod
                        + "OnSave() && wizard.hasEndPage() ) {\n");
                writer.write("        wizard.goToEndPage();\n");
                writer.write("      }\n");
                writer.write("    } else if (getForm()." + wizardGetterName
                        + ".equals(wizard) && WizardNavigation.Next.equals(wizardNavigation)) {\n");
                writer.write("      if (" + baseWizardMethod + "OnExit(wizard))  {\n");
                writer.write("        wizard.goToNextStep();\n");
                writer.write("        " + baseWizardMethod + "OnEnter(wizard);\n");
                writer.write("      }\n");
                writer.write("    } else if (getForm()." + wizardGetterName
                        + ".equals(wizard) && WizardNavigation.Prev.equals(wizardNavigation)) {\n");
                writer.write("        wizard.goToPrevStep();\n");
                writer.write("        " + baseWizardMethod + "OnEnter(wizard);\n");
                writer.write("    }\n");
            }
            writer.write("  }\n");
            writer.write("\n");

            // Abstract Method
            for (Wizard wizard : getForm().getWizardArray()) {
                String baseWizardMethod = WordUtils.uncapitalize(wizard.getName());

                writer.write("  public abstract boolean " + baseWizardMethod + "OnSave() throws EMFError;\n");
                writer.write("\n");
                writer.write("  public abstract void " + baseWizardMethod + "OnCancel() throws EMFError;\n");
                writer.write("\n");
                writer.write(
                        "  public abstract String getDefault" + wizard.getName() + "Publisher() throws EMFError;\n");
                writer.write("\n");

                // Step
                for (Step step : wizard.getStepArray()) {
                    String baseStepMethod = baseWizardMethod + WordUtils.capitalize(step.getName());

                    writer.write("  /**\n");
                    writer.write("    OnEnter: " + step.getDescription() + "\n");
                    writer.write("  */\n");
                    writer.write("  public abstract void " + baseStepMethod + "OnEnter() throws EMFError;\n");
                    writer.write("\n");
                    writer.write("  /**\n");
                    writer.write("    OnExit: " + step.getDescription() + "\n");
                    writer.write("  */\n");
                    writer.write("  public abstract boolean " + baseStepMethod + "OnExit() throws EMFError;\n");
                    writer.write("\n");
                }
            }

        } else {
            writer.write(
                    "  public final void wizard(Wizard wizard, WizardElement element, Wizard.WizardNavigation wizardNavigation) throws EMFError {\n");
            writer.write("  }\n");

        }
    }

}
