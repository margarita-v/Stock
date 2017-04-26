import dialogs.DeleteManyFrame;
import dialogs.DialogFrame;
import dialogs.PriceFilterFrame;
import models.AbstractProduct;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MainFrame extends JFrame implements ActionListener {
    // Default components
    private JPanel rootPanel;
    private JLabel lblFilter;
    private JTable table;

    // Current table model
    private ProductTableModel tableModel;
    // Previous table model (need for clear filter)
    private ProductTableModel oldTableModel;
    // Table model which is a filter result
    private ProductTableModel filterResult;

    // Sorter for JTable
    private TableRowSorter<TableModel> sorter;

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
        JMenuItem resetSortItem = new JMenuItem(txtResetSort);
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
        resetSortItem.addActionListener(this);
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
        viewMenu.add(resetSortItem);

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

    private void createGui() {
        setContentPane(rootPanel);
        setTitle("Информация о товарах");
        setPreferredSize(new Dimension(700, 500));

        // Set font for all menu and menu items
        Font font = new Font("Arial", Font.PLAIN, 12);
        UIManager.put("Menu.font", font);
        UIManager.put("MenuItem.font", font);

        // Create numberFormatter for dialogs
        createNumberFormatter();
        // Create file chooser for open and save text files
        createFileChooser();
        // Create main menu bar
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

        // Create table and table model
        tableModel = new ProductTableModel();
        table = new JTable(tableModel);
        getContentPane().add(new JScrollPane(table));

        // Create sorter for table
        createSorter();

        // Render values on center
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Integer.class, centerRenderer);
        table.setDefaultRenderer(String.class, centerRenderer);

        // Edit product on double click
        editAction();
        // Delete product on delete pressed
        deleteAction();

        pack();
        setVisible(true);
    }

    private void createNumberFormatter() {
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
    }

    private void createFileChooser() {
        File workingDirectory = new File(System.getProperty("user.dir"));
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(workingDirectory);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
    }

    private void createSorter() {
        sorter = new TableRowSorter<>(tableModel);
        // Set comparator for last column, where weight is an optional field
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String value1, String value2) {
                boolean firstNull = Objects.equals(value1, "-"),
                        secondNull = Objects.equals(value2, "-");
                if (firstNull || secondNull) {
                    if (firstNull && !secondNull)
                        return 1;
                    if (!firstNull)
                        return -1;
                    return 0;
                }
                int firstValue = Integer.parseInt(value1);
                int secondValue = Integer.parseInt(value2);
                return firstValue < secondValue ? -1 : firstValue == secondValue ? 0 : 1;
            }
        };
        sorter.setComparator(6, comparator);
        table.setRowSorter(sorter);
    }

    private void editAction() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                if (e.getClickCount() == 2) {
                    Point point = e.getPoint();
                    int currentRow = table.rowAtPoint(point);
                    // get ID of chosen product
                    int selectedId = (Integer) table.getValueAt(currentRow, 0);
                    edit(selectedId, tableModel.getById(selectedId));
                }
            }
        });
    }

    private void deleteAction() {
        InputMap inputMap = table.getInputMap(JTable.WHEN_FOCUSED);
        ActionMap actionMap = table.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "DeleteRow");
        actionMap.put("DeleteRow", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogResult = JOptionPane.showConfirmDialog(rootPanel,
                        "Удалить выбранные товары?", "Подтверждение", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    // List of ids of chosen products
                    List<Integer> ids = new ArrayList<>();
                    for (int i : table.getSelectedRows())
                        ids.add((Integer) table.getValueAt(i, 0));
                    for (int id : ids)
                        tableModel.delete(id);
                    table.clearSelection();
                    updateTable("Товары были удалены.");
                }
            }
        });
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
            case txtResetSort:
                resetSort();
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
            if (tableModel.loadFromFile(file.getName()))
                loadInfo("Данные из файла были загружены.");
            else
                showErrorMessage("Файл содержит неверные данные.");
        }
    }

    private void openFromDatabase() {
        if (tableModel.loadFromDatabase())
            loadInfo("Данные из базы данных были загружены.");
    }

    private void saveToTextFile() {
        if (tableModel.size() > 0) {
            int res = fileChooser.showSaveDialog(null);
            if (res == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                // Check file extension
                String filename = file.getName();
                int i = filename.lastIndexOf('.');
                if (i > 0 && Objects.equals(filename.substring(i + 1), "txt")) {
                    tableModel.saveToFile(filename);
                    showMessage("Данные были сохранены в текстовый файл.");
                }
                else
                    showErrorMessage("Расширение файла должно быть .txt");
            }
        }
        else
            showMessage("Список товаров пуст.");
    }

    private void saveToDatabase() {
        if (tableModel.size() > 0) {
            tableModel.saveToDatabase();
            showMessage("Данные были сохранены в базу данных.");
        }
        else
            showMessage("Список товаров пуст.");
    }

    private void exit() {
        int dialogResult = JOptionPane.showConfirmDialog(this,
                "Вы уверены, что хотите выйти?", "Подтверждение", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            tableModel.clear();
            System.exit(0);
        }
    }

    // View menu
    private void simpleFilter(String message, Boolean priceMoreFilter) {
        if (tableModel.size() > 0) {
            String result = JOptionPane.showInputDialog(this, message,
                    "Фильтр по цене", JOptionPane.DEFAULT_OPTION);
            if (result != null) {
                try {
                    int price = Integer.parseInt(result);
                    if (price > 0) {
                        filterResult = priceMoreFilter ?
                                tableModel.filter(p -> p >= price) :
                                tableModel.filter(p -> p <= price);
                        applyFilter();
                        String filterMessage = "Применен фильтр: ";
                        filterMessage += priceMoreFilter ?
                                "цена больше " + price :
                                "цена меньше " + price;
                        lblFilter.setText(filterMessage);
                    }
                    else
                        throw new NumberFormatException();

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
        if (tableModel.size() > 0) {
            PriceFilterFrame dialog = new PriceFilterFrame(numberFormatter, "Введите ценовой диапазон");
            dialog.setVisible(true);
            int minPrice = dialog.getMinPrice(), maxPrice = dialog.getMaxPrice();
            if (minPrice > 0 && maxPrice > 0) {
                filterResult = tableModel.filter(price -> price >= minPrice && price <= maxPrice);
                applyFilter();
                lblFilter.setText("Применен фильтр: цена от " + minPrice + " до " + maxPrice);
            }
        }
        else
            showMessage("Список товаров пуст.");
    }

    private void applyFilter() {
        // if a filter result is not empty
        if (filterResult.size() > 0) {
            // save first table model, not every filter result
            if (oldTableModel == null)
                oldTableModel = tableModel;
            // set new model
            table.setModel(filterResult);
            tableModel = (ProductTableModel) table.getModel();
            // new sorter for new model
            sorter.setModel(tableModel);
            lblFilter.setVisible(true);
            showMessage("Фильтр применен.");
        }
        else
            showMessage("Ни один товар не удовлетворяет заданному фильтру.");
    }

    private void clearFilter() {
        // if a filter was applied
        if (oldTableModel != null) {
            // set first table model
            table.setModel(oldTableModel);
            sorter.setModel(oldTableModel);
            tableModel = (ProductTableModel) table.getModel();
            oldTableModel = null;
            lblFilter.setVisible(false);
            lblFilter.setText("");
        }
    }

    private void resetSort() {
        sorter.setSortKeys(null);
    }

    // Edit menu
    private void add() {
        DialogFrame dialog = new DialogFrame(numberFormatter, "Добавление товара", null);
        dialog.setVisible(true);
        AbstractProduct product = dialog.getProduct();
        if (product != null) {
            if (tableModel.add(product))
                updateTable("Товар был добавлен.");
            else
                showErrorMessage("Товар с данным ID уже существует.");
        }
    }

    private void edit() {
        if (tableModel.size() > 0) {
            String result = JOptionPane.showInputDialog(this,
                    "Введите ID товара, который требуется отредактировать.",
                    "Редактирование", JOptionPane.DEFAULT_OPTION);
            if (result != null) {
                try {
                    int id = Integer.parseInt(result);
                    AbstractProduct productForEdit = tableModel.getById(id);
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
        DialogFrame dialog = new DialogFrame(numberFormatter, "Редактирование товара", productForEdit);
        dialog.setVisible(true);
        AbstractProduct newProduct = dialog.getProduct();
        // if user didn't canceled dialog
        if (newProduct != null) {
            if (tableModel.edit(id, newProduct))
                updateTable("Товар был отредактирован.");
            else
                showErrorMessage("Товар с данным ID уже существует.");
        }
    }

    private void delete() {
        if (tableModel.size() > 0) {
            String result = JOptionPane.showInputDialog(this,
                    "Введите ID товара, который требуется удалить.",
                    "Удаление", JOptionPane.DEFAULT_OPTION);
            if (result != null) {
                try {
                    int id = Integer.parseInt(result);
                    if (!tableModel.delete(id))
                        showErrorMessage("Товар с данным ID не найден.");
                    else
                        updateTable("Товар был удален.");
                } catch (NumberFormatException e) {
                    showErrorMessage("Введено неверное значение ID.");
                }
            }
        }
        else
            showMessage("Список товаров пуст.");
    }

    private void deleteMany() {
        if (tableModel.size() > 0) {
            DeleteManyFrame dialog = new DeleteManyFrame(tableModel.getProductsIDs());
            dialog.setVisible(true);
            List<Integer> chosenItems = dialog.getChosenIDs();
            if (chosenItems.size() > 0) {
                for (Integer id : chosenItems) {
                    tableModel.delete(id);
                }
                updateTable("Выбранные товары были удалены.");
            }
        }
        else
            showMessage("Список товаров пуст.");
    }

    private void clear() {
        if (tableModel.size() > 0) {
            int dialogResult = JOptionPane.showConfirmDialog(this,
                    "Очистить список товаров?", "Подтверждение", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                tableModel.clear();
                updateTable("Товары были удалены.");
            }
        }
        else
            showMessage("Список товаров пуст.");
    }
    //endregion

    //region Helpful functions

    // Actions after loading data from text file or database
    private void loadInfo(String message) {
        clearFilter();
        resetSort();
        table.updateUI();
        showMessage(message);
    }

    // Actions after every updating data in product list
    private void updateTable(String message) {
        sorter.sort();
        table.updateUI();
        showMessage(message);
    }

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
    private static final String txtOpenTextFile     = "From text file";
    private static final String txtOpenFromDatabase = "From database";
    private static final String txtSaveToTextFile   = "To text file";
    private static final String txtSaveToDatabase   = "To database";
    private static final String txtExit             = "Exit";

    private static final String txtPriceMoreFilter  = "Price more than value";
    private static final String txtPriceLessFilter  = "Price less than value";
    private static final String txtPriceRangeFilter = "Set price range";
    private static final String txtClearFilter      = "ClearFilter";
    private static final String txtResetSort        = "Reset sort";

    private static final String txtAdd          = "Add";
    private static final String txtEdit         = "Edit";
    private static final String txtDelete       = "Delete";
    private static final String txtDeleteMany   = "Delete many";
    private static final String txtClear        = "Clear";
    //endregion
}