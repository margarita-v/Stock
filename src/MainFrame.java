import dialogs.DeleteManyFrame;
import dialogs.DialogFrame;
import dialogs.PriceFilterFrame;
import models.AbstractProduct;
import task.ProductList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

public class MainFrame extends JFrame implements ActionListener {
    // Default components
    private JPanel rootPanel;
    private JTable table;
    private JPopupMenu popupMenu;

    // Main product list
    private ProductList productList;
    // AbstractProduct list which will be created after filter apply
    private ProductList filterResult;
    // ID of selected product in a table
    private int selectedId;

    // Table model for JTable based on main product list
    private ProductTableModel tableModel;

    private NumberFormatter numberFormatter;
    private JFileChooser fileChooser;

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Main items of menu bar
        JMenu fileMenu = new JMenu("File");
        JMenu viewMenu = new JMenu("View");
        JMenu editMenu = new JMenu("Edit");

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(editMenu);

        // Configure file menu
        JMenu openMenu = new JMenu("Open");
        JMenuItem txtFileItem = new JMenuItem(txtOpenTextFile);
        JMenuItem dbItem = new JMenuItem(txtOpenFromDatabase);
        setShortcut(txtFileItem, 'O');
        txtFileItem.addActionListener(this);
        dbItem.addActionListener(this);

        openMenu.add(txtFileItem);
        openMenu.add(dbItem);

        JMenu saveMenu = new JMenu("Save");
        JMenuItem saveToTxtItem = new JMenuItem(txtSaveToTextFile);
        JMenuItem saveToDbItem = new JMenuItem(txtSaveToDatabase);
        setShortcut(saveToTxtItem, 'S');
        saveToTxtItem.addActionListener(this);
        saveToDbItem.addActionListener(this);

        saveMenu.add(saveToTxtItem);
        saveMenu.add(saveToDbItem);

        // Exit item
        JMenuItem exitItem = new JMenuItem(txtExit);
        exitItem.addActionListener(this);

        fileMenu.add(openMenu);
        fileMenu.add(saveMenu);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Configure view menu
        JMenu filterMenu = new JMenu("Filter");
        // Items of filter menu
        JMenuItem moreFilterItem = new JMenuItem(txtPriceMoreFilter);
        JMenuItem lessFilterItem = new JMenuItem(txtPriceLessFilter);
        JMenuItem rangeFilterItem = new JMenuItem(txtPriceRangeFilter);
        JMenuItem clearFilterItem = new JMenuItem(txtClearFilter);
        // Set action listener
        moreFilterItem.addActionListener(this);
        lessFilterItem.addActionListener(this);
        rangeFilterItem.addActionListener(this);
        clearFilterItem.addActionListener(this);
        // Set shortcuts
        setShortcut(moreFilterItem, 'M');
        setShortcut(lessFilterItem, 'L');
        setShortcut(rangeFilterItem, 'R');

        filterMenu.add(moreFilterItem);
        filterMenu.add(lessFilterItem);
        filterMenu.add(rangeFilterItem);
        filterMenu.addSeparator();
        filterMenu.add(clearFilterItem);
        viewMenu.add(filterMenu);

        // Configure edit menu
        JMenuItem addItem = new JMenuItem(txtAdd);
        JMenuItem editItem = new JMenuItem(txtEdit);
        JMenuItem deleteItem = new JMenuItem(txtDelete);
        JMenuItem deleteManyItem = new JMenuItem(txtDeleteMany);
        JMenuItem clearItem = new JMenuItem(txtClear);
        // Set action listener
        addItem.addActionListener(this);
        editItem.addActionListener(this);
        deleteItem.addActionListener(this);
        deleteManyItem.addActionListener(this);
        clearItem.addActionListener(this);
        // Set shortcuts
        setShortcut(addItem, 'N');
        setShortcut(editItem, 'E');
        setShortcut(deleteItem, 'D');
        setShortcut(deleteManyItem, '-');

        editMenu.add(addItem);
        editMenu.add(editItem);
        editMenu.add(deleteItem);
        editMenu.add(deleteManyItem);
        editMenu.addSeparator();
        editMenu.add(clearItem);

        setJMenuBar(menuBar);
    }

    private void createPopupMenu() {
        popupMenu = new JPopupMenu();
        JMenuItem popupEditItem = new JMenuItem(txtEditPopup);
        JMenuItem popupDeleteItem = new JMenuItem(txtDeletePopup);
        popupEditItem.addActionListener(this);
        popupDeleteItem.addActionListener(this);

        popupMenu.add(popupEditItem);
        popupMenu.add(popupDeleteItem);
    }

    private void createGui() {
        setContentPane(rootPanel);
        setTitle("Информация о товарах");
        setPreferredSize(new Dimension(600, 500));

        // Set font for all menu and menu items
        Font font = new Font("Arial", Font.PLAIN, 12);
        UIManager.put("Menu.font", font);
        UIManager.put("MenuItem.font", font);

        // Create numberFormatter for dialogs
        NumberFormat integerFormat = NumberFormat.getIntegerInstance();
        numberFormatter = new NumberFormatter(integerFormat) {
            @Override
            public Object stringToValue(String string) throws ParseException {
                if (string == null || string.length() == 0)
                    return null;
                return super.stringToValue(string);
            }
        };
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setMinimum(1);
        numberFormatter.setMaximum(Integer.MAX_VALUE);
        numberFormatter.setAllowsInvalid(false);

        // Create file chooser for open and save text files
        File workingDirectory = new File(System.getProperty("user.dir"));
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(workingDirectory);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));

        createMenuBar();

        // call exit() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        // call exit() on ESCAPE
        rootPanel.registerKeyboardAction(e -> exit(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        productList = new ProductList();
        tableModel = new ProductTableModel(productList);
        table = new JTable(tableModel);
        getContentPane().add(new JScrollPane(table));

        /*
        TableRowSorter sorter = new TableRowSorter(tableModel);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        table.setRowSorter(sorter);
        */

        // Render integer values on center
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Integer.class, centerRenderer);

        createPopupMenu();
        table.setComponentPopupMenu(popupMenu);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                Point point = mouseEvent.getPoint();
                int currentRow = table.rowAtPoint(point);
                // get ID of chosen product
                selectedId = (Integer) table.getValueAt(currentRow, 0);
            }
        });
        pack();
        setVisible(true);
    }

    private MainFrame() {
        createGui();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(MainFrame::new);
    }

    // User selection of menu item
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {
            // File menu
            case txtOpenTextFile:
                openFromTextFile();
                break;
            case txtOpenFromDatabase:
                openFromDatabase();
                break;
            case txtSaveToTextFile:
                saveToTextFile();
                break;
            case txtSaveToDatabase:
                saveToDatabase();
                break;
            case txtExit:
                exit();
                break;
            // View menu
            case txtPriceMoreFilter:
                priceMoreFilter();
                break;
            case txtPriceLessFilter:
                priceLessFilter();
                break;
            case txtPriceRangeFilter:
                priceRangeFilter();
                break;
            case txtClearFilter:
                clearFilter();
                break;
            // Edit menu
            case txtAdd:
                add();
                break;
            case txtEdit:
                edit();
                break;
            case txtDelete:
                delete();
                break;
            case txtDeleteMany:
                deleteMany();
                break;
            case txtClear:
                clear();
                break;
            // Popup menu
            case txtEditPopup:
                edit(selectedId, productList.getById(selectedId));
                break;
            case txtDeletePopup:
                productList.delete(selectedId);
                table.updateUI();
                showMessage("Товар был удален");
                break;
            default:
                break;
        }
    }

    //region Menu management
    // File menu
    private void openFromTextFile() {
        int res = fileChooser.showOpenDialog(null);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (productList.loadFromFile(file.getName())) {
                table.setAutoCreateRowSorter(true);
                table.updateUI();
                showMessage("Данные из файла были загружены.");
            }
            else
                showErrorMessage("Файл содержит неверные данные.");
        }
    }

    private void openFromDatabase() {
        if (productList.loadFromDatabase()) {
            table.setAutoCreateRowSorter(true);
            table.updateUI();
            showMessage("Данные из базы данных были загружены");
        }
    }

    private void saveToTextFile() {
        int res = fileChooser.showSaveDialog(null);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            productList.saveToFile(file.getName());
            showMessage("Данные были сохранены в текстовый файл.");
        }
    }

    private void saveToDatabase() {
        productList.saveToDatabase();
        showMessage("Данные были сохранены в базу данных.");
    }

    private void exit() {
        int dialogResult = JOptionPane.showConfirmDialog(this,
                "Вы уверены, что хотите выйти?", "Подтверждение", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            productList.clear();
            System.exit(0);
        }
    }

    // View menu
    private void simpleFilter(String message, Boolean priceMoreFilter) {
        if (productList.size() > 0) {
            String result = JOptionPane.showInputDialog(this, message,
                    "Фильтр по цене", JOptionPane.DEFAULT_OPTION);
            if (result != null) {
                try {
                    int price = Integer.parseInt(result);
                    if (priceMoreFilter)
                        filterResult = productList.filter(p -> p >= price);
                    else
                        filterResult = productList.filter(p -> p <= price);

                    if (filterResult.size() > 0) {
                        table.setModel(new ProductTableModel(filterResult));
                        showMessage("Фильтр применен.");
                    }
                    else
                        showMessage("Ни один товар не удовлетворяет заданному фильтру.");

                } catch (NumberFormatException e) {
                    showErrorMessage("Введено неверное значение цены.");
                }
            }
        }
        else
            showMessage("Список товаров пуст.");
    }

    private void priceMoreFilter() {
        simpleFilter("Введите минимальное значение цены.", true);
    }

    private void priceLessFilter() {
        simpleFilter("Введите максимальное значение цены.", false);
    }

    private void priceRangeFilter() {
        if (productList.size() > 0) {
            PriceFilterFrame dialog = new PriceFilterFrame(numberFormatter, "Введите ценовой диапазон");
            dialog.setVisible(true);
            int minPrice = dialog.getMinPrice(), maxPrice = dialog.getMaxPrice();
            if (minPrice > 0 && maxPrice > 0) {
                filterResult = productList.filter(price -> price >= minPrice && price <= maxPrice);
                if (filterResult.size() > 0) {
                    table.setModel(new ProductTableModel(filterResult));
                    showMessage("Фильтр применен.");
                }
                else
                    showMessage("Ни один товар не удовлетворяет заданному фильтру.");
            }
        }
        else
            showMessage("Список товаров пуст.");
    }

    private void clearFilter() {
        table.setModel(tableModel);
    }

    // Edit menu
    private void add() {
        DialogFrame dialog = new DialogFrame(numberFormatter, "Добавление товара");
        dialog.setVisible(true);
        AbstractProduct product = dialog.getProduct();
        if (product != null) {
            if (productList.add(product)) {
                table.setAutoCreateRowSorter(true);
                table.updateUI();
                showMessage("Товар был добавлен");
            }
            else
                showErrorMessage("Товар с данным ID уже существует.");
        }
    }

    private void edit() {
        if (productList.size() > 0) {
            String result = JOptionPane.showInputDialog(this,
                    "Введите ID товара, который требуется отредактировать.",
                    "Редактирование", JOptionPane.DEFAULT_OPTION);
            if (result != null) {
                try {
                    int id = Integer.parseInt(result);
                    AbstractProduct productForEdit = productList.getById(id);
                    if (productForEdit == null)
                        showErrorMessage("Товар с данным ID не найден.");
                    else
                        edit(id, productForEdit);

                } catch (NumberFormatException e) {
                    showErrorMessage("Введено неверное значение ID.");
                }
            }
        }
        else
            showMessage("Список товаров пуст.");
    }

    private void edit(int id, AbstractProduct productForEdit) {
        DialogFrame dialog = new DialogFrame(numberFormatter, "Редактирование товара");
        dialog.setVisible(true);
        AbstractProduct newProduct = dialog.getProduct();
        // if user didn't canceled dialog
        if (newProduct != null) {
            if (productList.edit(id, newProduct)) {
                table.updateUI();
                showMessage("Товар был отредактирован.");
                // ID of selected product was changed
                selectedId = newProduct.getId();
            } else
                showErrorMessage("Товар с данным ID уже существует.");
        }
    }

    private void delete() {
        if (productList.size() > 0) {
            String result = JOptionPane.showInputDialog(this,
                    "Введите ID товара, который требуется удалить.",
                    "Удаление", JOptionPane.DEFAULT_OPTION);
            if (result != null) {
                try {
                    int id = Integer.parseInt(result);
                    if (!productList.delete(id))
                        showErrorMessage("Товар с данным ID не найден.");
                    else {
                        table.setAutoCreateRowSorter(true);
                        table.updateUI();
                        showMessage("Товар был удален.");
                    }
                } catch (NumberFormatException e) {
                    showErrorMessage("Введено неверное значение ID.");
                }
            }
        }
        else
            showMessage("Список товаров пуст.");
    }

    private void deleteMany() {
        if (productList.size() > 0) {
            DeleteManyFrame dialog = new DeleteManyFrame(productList.getProductsIDs());
            dialog.setVisible(true);
            List<Integer> chosenItems = dialog.getChosenIDs();
            if (chosenItems.size() > 0) {
                for (Integer id : chosenItems) {
                    productList.delete(id);
                }
                table.setAutoCreateRowSorter(true);
                table.updateUI();
                showMessage("Выбранные товары были удалены.");
            }
        }
        else
            showMessage("Список товаров пуст.");
    }

    private void clear() {
        if (productList.size() > 0) {
            int dialogResult = JOptionPane.showConfirmDialog(this,
                    "Очистить список товаров?", "Подтверждение", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                productList.clear();
                table.updateUI();
                showMessage("Товары были удалены.");
            }
        }
        else
            showMessage("Список товаров пуст.");
    }
    //endregion

    //region Helpful functions
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message,
                "Ошибка", JOptionPane.WARNING_MESSAGE);
    }

    private static void setShortcut(JMenuItem menuItem, char symbol) {
        menuItem.setAccelerator(KeyStroke.getKeyStroke(symbol,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }
    //endregion

    //region Menu item's names
    private static final String txtOpenTextFile = "From text file";
    private static final String txtOpenFromDatabase = "From database";
    private static final String txtSaveToTextFile = "To text file";
    private static final String txtSaveToDatabase = "To database";
    private static final String txtExit = "Exit";

    private static final String txtPriceMoreFilter = "Price more than value";
    private static final String txtPriceLessFilter = "Price less than value";
    private static final String txtPriceRangeFilter = "Set price range";
    private static final String txtClearFilter = "ClearFilter";

    private static final String txtAdd = "Add";
    private static final String txtEdit = "Edit";
    private static final String txtDelete = "Delete";
    private static final String txtDeleteMany = "Delete many";
    private static final String txtClear = "Clear";

    private static final String txtEditPopup = "Edit product";
    private static final String txtDeletePopup = "Delete product";
    //endregion
}