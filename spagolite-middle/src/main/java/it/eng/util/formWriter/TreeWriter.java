package it.eng.util.formWriter;

import it.eng.spagoLite.xmlbean.form.Tree;
import it.eng.spagoLite.xmlbean.form.TreeElement;
import it.eng.util.ClassUtil;
import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.StringUtils;

public class TreeWriter {

    private Tree tree;

    public TreeWriter(Tree fields) {
        this.tree = fields;
    }

    public void write(Writer writer) throws IOException {

        writer.write(
                "  public static class " + ClassUtil.getClassName(tree.getName()) + " extends Tree<TreeField<?>> {\n");
        writer.write("\n");
        writer.write("    public static String NAME = \"" + ClassUtil.getClassName(tree.getName()) + "\";\n");
        writer.write("    public static String DESCRIPTION = \"" + tree.getDescription() + "\";\n");

        writeConstants(writer);

        writer.write("\n");
        writer.write("    public " + ClassUtil.getClassName(tree.getName()) + "() {\n");
        /*
         * Tree(Component parent, String name, String description, String title, BaseTableInterface<?> table, boolean
         * hidden, boolean hideCreateNodeButton, boolean hideDeleteNodeButton, boolean hideRenameNodeButton, boolean
         * checkable, boolean editable, boolean draggable, boolean sorted, boolean coloredIcons)
         */
        writer.write("      super(null, NAME, DESCRIPTION,DESCRIPTION,null," + tree.getHidden() + ","
                + tree.getHideCreateNodeButton() + "," + tree.getUseDefaultCreateNodeButton() + ","
                + tree.getHideDeleteNodeButton() + "," + tree.getUseDefaultDeleteNodeButton() + ","
                + tree.getHideRenameNodeButton() + "," + tree.getUseDefaultRenameNodeButton() + ","
                + tree.getCheckable() + "," + tree.getEditable() + "," + tree.getDraggable() + "," + tree.getSorted()
                + "," + tree.getColoredIcons() + "," + tree.getActiveContextMenu() + "," + "\"" + tree.getDataType()
                + "\"" + ");\n");

        if (StringUtils.isNotBlank(tree.getTitle())) {
            writer.write("      setTitle(\"" + tree.getTitle() + "\");\n");
        }
        writeAdds(writer);
        writer.write("    }\n");
        writer.write("\n");

        writeGets(writer);

        writer.write("  }\n");
    }

    private void writeConstants(Writer writer) throws IOException {
        writer.write("\n");

        if (tree.getTreeElementArray() != null && tree.getTreeElementArray().length > 0) {
            for (TreeElement element : tree.getTreeElementArray()) {
                TreeElementWriter treeElementWriter = new TreeElementWriter(element);
                treeElementWriter.writeConstant(writer);
            }
            writer.write("\n");
            for (TreeElement element : tree.getTreeElementArray()) {
                TreeElementWriter treeElementWriter = new TreeElementWriter(element);
                treeElementWriter.writeConstantFull(writer);
            }
        }
    }

    private void writeAdds(Writer writer) throws IOException {
        writer.write("\n");
        if (tree.getTreeElementArray() != null && tree.getTreeElementArray().length > 0) {
            for (TreeElement element : tree.getTreeElementArray()) {
                TreeElementWriter treeElementWriter = new TreeElementWriter(element);
                treeElementWriter.writeAdd(writer);
            }
        }
    }

    private void writeGets(Writer writer) throws IOException {
        writer.write("\n");
        if (tree.getTreeElementArray() != null && tree.getTreeElementArray().length > 0) {
            for (TreeElement element : tree.getTreeElementArray()) {
                TreeElementWriter fieldWriter = new TreeElementWriter(element);
                fieldWriter.writeGet(writer);
            }
        }
    }
}
