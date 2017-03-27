import javax.swing.*;
import java.awt.*;

class StaticMembers {
    static final String txtOpenTextFile = "From text file";
    static final String txtOpenFromDatabase = "From database";
    static final String txtSaveToTextFile = "To text file";
    static final String txtSaveToDatabase = "To database";
    static final String txtExit = "Exit";

    static final String txtPriceMoreFilter = "Price more than value";
    static final String txtPriceLessFilter = "Price less than value";
    static final String txtPriceRangeFilter = "Set price range";
    static final String txtClearFilter = "ClearFilter";

    static final String txtAdd = "Add";
    static final String txtEdit = "Edit";
    static final String txtDelete = "Delete";
    static final String txtDeleteMany = "Delete many";
    static final String txtClear = "Clear";

    static final String txtEditPopup = "Edit product";
    static final String txtDeletePopup = "Delete product";

    static void setShortcut(JMenuItem menuItem, char symbol) {
        menuItem.setAccelerator(KeyStroke.getKeyStroke(symbol,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }
}
