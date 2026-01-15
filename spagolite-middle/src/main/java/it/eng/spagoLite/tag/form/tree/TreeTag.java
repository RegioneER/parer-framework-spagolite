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

package it.eng.spagoLite.tag.form.tree;

import it.eng.spagoCore.error.EMFError;
import it.eng.spagoCore.util.JavaScript;
import it.eng.spagoLite.db.base.BaseRowInterface;
import it.eng.spagoLite.db.base.BaseTableInterface;
import it.eng.spagoLite.form.fields.impl.Button;
import it.eng.spagoLite.form.tree.Tree;
import it.eng.spagoLite.form.tree.TreeField;
import it.eng.spagoLite.tag.form.BaseFormTag;
import it.eng.spagoLite.xmlbean.form.TreeElement.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.jsp.JspException;
import org.apache.commons.lang3.StringUtils;

public class TreeTag extends BaseFormTag<Tree<TreeField<?>>> {

    private static final long serialVersionUID = 1L;

    private static final String SORT_PLUGIN = "\"sort\"";
    private static final String DND_PLUGIN = "\"dnd\"";
    private static final String CHECKBOX_PLUGIN = "\"checkbox\"";
    private static final String CONTEXTMENU_PLUGIN = "\"contextmenu\"";
    private static final String TYPES_PLUGIN = "\"types\"";

    private String additionalJsonParams;
    private String additionalPluginParams;

    private static final String beginLi = "<li ";
    private static final String endLi = "</li>";
    private static final String beginUl = "<ul>";
    private static final String endUl = "</ul>";

    @Override
    public int doStartTag() throws JspException {
        Tree<?> fields = (Tree<?>) getComponent();
        BaseTableInterface<?> table = fields.getTable();

        // if (table != null && table.size() > 0) {
        writeln("");
        writeln("<!-- Albero: " + getName() + " -->");

        StringBuilder body = new StringBuilder();

        drawBody(table, fields, body);
        writeln("<div class=\"newLine skipLine\"></div>\n");

        writeln(body);
        // }

        return SKIP_BODY;
    }

    private int drawBody(BaseTableInterface<?> table, Tree<?> fields, StringBuilder body)
            throws JspException {
        try {
            String styleClass = "tree";
            if (fields.isHidden()) {
                styleClass += " displayNone";
            }
            String treeId = "tree_" + JavaScript.stringToHTMLString(fields.getName());

            body.append(" <div id=\"" + treeId + "\" class=\"" + styleClass + "\">\n");
            String ulList = buildUlList(table, fields);
            /*
             * Nota: JStree 3.X.X modifica gestione delle <ul><li>
             */
            if (StringUtils.isNotBlank(ulList)) {
                // display: none; per evitare la transazione da DOM ad oggetto gestito dal
                // plugin jstree
                body.append("<ul style=\"display: none;\">" + ulList + "</ul>");
            }
            body.append(" </div>\n");

            body.append("<script type=\"text/javascript\">");

            String plugins = buildPluginsList(fields);

            if (fields.isEditable()) {
                if (!fields.isHideCreateNodeButton()) {
                    Button createButton = new Button(fields, fields.getName() + "_createBtn",
                            "Crea nodo", "", it.eng.spagoLite.xmlbean.form.Field.Type.STRING, null,
                            false, false, false, false, false, true);
                    String action = "button/" + getForm().getClass().getSimpleName() + "#"
                            + fields.getName() + "/" + createButton.getName();

                    if (createButton.isSecure() && isUserAuthorized(action)) {
                        writeln(getFieldButton(createButton));
                    } else if (!createButton.isSecure()) {
                        writeln(getFieldButton(createButton));
                    }
                    if (fields.isUseDefaultCreateNodeButton()) {
                        String name = JavaScript.stringToHTMLString(
                                StringUtils.uncapitalize(createButton.getName()));
                        body.append("\n$(\"#" + name + "\").click( function() {\n" + "if ( $(\"#"
                                + treeId + " li\").length > 0) {" + "$(\"#" + treeId
                                + "\").jstree(\"create\");" + "\n} else {" + "$(\"#" + treeId
                                + "\").jstree(\"create\", -1);" + "\n}" + "\n});");
                    }
                }

                if (!fields.isHideDeleteNodeButton()) {
                    Button deleteButton = new Button(fields, fields.getName() + "_deleteBtn",
                            "Elimina nodo", "", it.eng.spagoLite.xmlbean.form.Field.Type.STRING,
                            null, false, false, false, false, false, true);
                    String action = "button/" + getForm().getClass().getSimpleName() + "#"
                            + fields.getName() + "/" + deleteButton.getName();

                    if (deleteButton.isSecure() && isUserAuthorized(action)) {
                        writeln(getFieldButton(deleteButton));
                    } else if (!deleteButton.isSecure()) {
                        writeln(getFieldButton(deleteButton));
                    }
                    if (fields.isUseDefaultDeleteNodeButton()) {
                        String name = JavaScript.stringToHTMLString(
                                StringUtils.uncapitalize(deleteButton.getName()));
                        body.append("\n$(\"#" + name + "\").click( function() {\n" + "$(\"#"
                                + treeId + "\").jstree(\"remove\");\n" + " });");
                    }
                }

                if (!fields.isHideRenameNodeButton()) {
                    Button renameButton = new Button(fields, fields.getName() + "_renameBtn",
                            "Modifica nodo", "", it.eng.spagoLite.xmlbean.form.Field.Type.STRING,
                            null, false, false, false, false, false, true);
                    String action = "button/" + getForm().getClass().getSimpleName() + "#"
                            + fields.getName() + "/" + renameButton.getName();

                    if (renameButton.isSecure() && isUserAuthorized(action)) {
                        writeln(getFieldButton(renameButton));
                    } else if (!renameButton.isSecure()) {
                        writeln(getFieldButton(renameButton));
                    }
                    if (fields.isUseDefaultRenameNodeButton()) {
                        String name = JavaScript.stringToHTMLString(
                                StringUtils.uncapitalize(renameButton.getName()));
                        body.append("\n$(\"#" + name + "\").click( function() {\n" + "$(\"#"
                                + treeId + "\").jstree(\"rename\");\n" + " });");
                    }
                }
            }

            String types = buildTypes();
            String data = "";

            body.append("\n$(function() {" + "$(\"#" + treeId + "\").jstree({"
                    + (StringUtils.isNotBlank(getAdditionalJsonParams())
                            ? getAdditionalJsonParams() + ","
                            : "")
                    + plugins
                    + (StringUtils.isNotBlank(getAdditionalPluginParams())
                            ? "," + getAdditionalPluginParams()
                            : "")
                    + (fields.isColoredIcons() ? "," + types : "") + ",\"themes\" : {"
                    + "\"dots\" : false, \"icons\" : true}" + "})" + "});");

            body.append("\n</script>");
        } catch (EMFError e) {
            throw new JspException(e);
        }
        return 0;
    }

    private String getFieldButton(Button createButton) throws JspException {
        String className = " pulsante ";
        String submitName = JavaScript
                .stringToHTMLString(StringUtils.uncapitalize(createButton.getName()));
        String description = JavaScript.stringToHTMLString(createButton.getDescription());
        String disabled = "";
        return "<input type=\"button\" id=\"" + submitName + "\" value=\"" + description
                + "\" class=\"" + className + "\" " + disabled + " />\n";
    }

    private String buildTypes() {
        String types = "\"types\" : {" + "\"#\" : {" + "\"max_depth\" : -2,\"max_children\" : -2 "
                + "}," + "\"default\" : {" + "\"icon\" : \"" + CONTEXTPATH + "/img/jstree/Black.png"
                + "\"" + "}," + "\"black\" : {" + "\"icon\" : \"" + CONTEXTPATH
                + "/img/jstree/Black.png" + "\"" + "}," + "\"blue\" : {" + "\"icon\" : \""
                + CONTEXTPATH + "/img/jstree/Blue.png" + "\"" + "}," + "\"green\" : {"
                + "\"icon\" : " + "\"" + CONTEXTPATH + "/img/jstree/Green.png" + "\"" + "},"
                + "\"red\" : {" + "\"icon\" : \"" + CONTEXTPATH + "/img/jstree/Red.png" + "\""
                + "}," + "\"yellow\" : {" + "\"icon\" : \"" + CONTEXTPATH + "/img/jstree/Yellow.png"
                + "\"" + "}" + "}";
        return types;
    }

    private String buildPluginsList(Tree<?> fields) {
        List<String> plugins = new ArrayList<>();

        if (fields.isCheckable()) {
            plugins.add(CHECKBOX_PLUGIN);
        }

        if (fields.isActiveContextMenu()) {
            plugins.add(CONTEXTMENU_PLUGIN);
        }

        if (fields.isDraggable()) {
            plugins.add(DND_PLUGIN);
        }

        if (fields.isSorted()) {
            plugins.add(SORT_PLUGIN);
        }

        if (fields.isColoredIcons()) {
            plugins.add(TYPES_PLUGIN);
        }

        String joined = plugins.stream().map(x -> x).collect(Collectors.joining(" , "));
        return "\"plugins\" : [".concat(joined).concat("]");
    }

    private String buildUlList(BaseTableInterface<?> table, Tree<?> fields) throws EMFError {
        StringBuilder ulBuilder = new StringBuilder();
        int level = 0;
        List<String> parentList = new ArrayList<String>();
        parentList.add("ROOT");
        boolean ulChildrenCreated = false;

        if (table != null && table.size() > 0 && fields.getComponentList().size() > 0) {
            int counter = 0;
            for (BaseRowInterface row : table) {
                boolean hasParent = false;
                boolean closeUlChildren = false;

                String name = "";
                String id = "";
                String parent = "";
                String icon = "";

                for (TreeField<?> aafield : fields) {
                    TreeField<?> field = (TreeField<?>) aafield.clone();
                    String value = field.format(row);
                    icon = field.getIcon() != null ? field.getIcon() : icon;

                    if (field.getType().equals(Type.ID)) {
                        id = value;
                    } else if (field.getType().equals(Type.ID_PARENT)) {
                        if (StringUtils.isNotBlank(field.getValue())) {
                            hasParent = true;
                            if (!parent.equals(field.getValue())) {
                                if (!parentList.contains(field.getValue())) {
                                    level++;
                                    parentList.add(field.getValue());
                                    ulChildrenCreated = false;
                                } else {
                                    ulChildrenCreated = true;
                                    closeUlChildren = true;
                                }
                            }
                            parent = field.getValue();
                        } else {
                            parent = "ROOT";
                            if (level > 0) {
                                ulChildrenCreated = true;
                                closeUlChildren = true;
                            }
                        }
                    } else if (field.getType().equals(Type.NAME)) {
                        name = value;
                    }
                }

                if (closeUlChildren) {
                    level = closeChildrens(ulBuilder, parent, parentList, level);
                }
                if (hasParent) {
                    if (!ulChildrenCreated) {
                        ulBuilder.append(beginUl);
                        ulChildrenCreated = true;
                    }
                }

                ulBuilder.append(beginLi);
                if (StringUtils.isNotBlank(icon) && row.getString(icon) != null) {
                    Tree.IconColours iconColour = Tree.IconColours.valueOf(row.getString(icon));
                    String iconClass;
                    switch (iconColour) {
                    case BLACK:
                        iconClass = "black";
                        break;
                    case BLUE:
                        iconClass = "blue";
                        break;
                    case GREEN:
                        iconClass = "green";
                        break;
                    case RED:
                        iconClass = "red";
                        break;
                    case YELLOW:
                        iconClass = "yellow";
                        break;
                    default:
                        iconClass = null;
                        break;

                    }
                    if (StringUtils.isNotBlank(iconClass)) {
                        ulBuilder.append(" data-jstree='{\"type\" : \"" + iconClass + "\"}' ");

                    }
                }
                ulBuilder.append("tablePosition='" + counter + "' ");
                ulBuilder.append("id='" + id + "'>");
                ulBuilder.append("<a href='#'>");
                ulBuilder.append(name);
                ulBuilder.append("</a>");
                counter++;
            }
            if (level > 0) {
                level = closeChildrens(ulBuilder, "ROOT", parentList, level);
            } else {
                ulBuilder.append(endLi);
            }
        }

        return ulBuilder.toString();
    }

    private int closeChildrens(StringBuilder ulBuilder, String parent, List<String> parentList,
            int level) {
        if (parentList.get(parentList.size() - 1).equals(parent)) {
            ulBuilder.append(endLi);
        } else {
            ulBuilder.append(endLi);
            ulBuilder.append(endUl);
            level--;
            parentList.remove(parentList.size() - 1);
            closeChildrens(ulBuilder, parent, parentList, level);
        }
        return level;
    }

    public String getAdditionalJsonParams() {
        if (additionalJsonParams == null) {
            return "";
        } else {
            return additionalJsonParams;
        }
    }

    public void setAdditionalJsonParams(String additionalJsonParams) {
        this.additionalJsonParams = additionalJsonParams;
    }

    public String getAdditionalPluginParams() {
        return additionalPluginParams;
    }

    public void setAdditionalPluginParams(String additionalPluginParams) {
        this.additionalPluginParams = additionalPluginParams;
    }
}
