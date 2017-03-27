import task.Product;
import task.ProductList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class MainFrame extends JFrame implements ActionListener {

    private JPanel rootPanel;
    private JTable table;
    private Font font;
    private JPopupMenu popupMenu;

    private ProductList productList;
    private ProductList filterResult;
    private int selectedId;

    private ProductTableModel tableModel;

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        font = new Font("Arial", Font.PLAIN, 12);

        // Main items of menu bar
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(font);
        JMenu viewMenu = new JMenu("View");
        viewMenu.setFont(font);
        JMenu editMenu = new JMenu("Edit");
        editMenu.setFont(font);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(editMenu);

        // Configure file menu
        JMenu openMenu = new JMenu("Open");
        openMenu.setFont(font);
        // Open from text file
        JMenuItem txtFileItem = new JMenuItem(StaticMembers.txtOpenTextFile);
        txtFileItem.setFont(font);
        txtFileItem.addActionListener(this);
        StaticMembers.setShortcut(txtFileItem, 'O');
        // Open from database
        JMenuItem dbItem = new JMenuItem(StaticMembers.txtOpenFromDatabase);
        dbItem.setFont(font);
        dbItem.addActionListener(this);

        openMenu.add(txtFileItem);
        openMenu.add(dbItem);

        JMenu saveMenu = new JMenu("Save");
        saveMenu.setFont(font);
        // Save as text file
        JMenuItem saveToTxtItem = new JMenuItem(StaticMembers.txtSaveToTextFile);
        saveToTxtItem.setFont(font);
        saveToTxtItem.addActionListener(this);
        StaticMembers.setShortcut(saveToTxtItem, 'S');
        // Save as database
        JMenuItem saveToDbItem = new JMenuItem(StaticMembers.txtSaveToDatabase);
        saveToDbItem.setFont(font);
        saveToDbItem.addActionListener(this);

        saveMenu.add(saveToTxtItem);
        saveMenu.add(saveToDbItem);

        // Exit item
        JMenuItem exitItem = new JMenuItem(StaticMembers.txtExit);
        exitItem.addActionListener(this);
        exitItem.setFont(font);

        fileMenu.add(openMenu);
        fileMenu.add(saveMenu);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Configure view menu
        JMenu filterMenu = new JMenu("Filter");
        filterMenu.setFont(font);
        // Items of filter menu
        JMenuItem moreFilterItem = new JMenuItem(StaticMembers.txtPriceMoreFilter);
        JMenuItem lessFilterItem = new JMenuItem(StaticMembers.txtPriceLessFilter);
        JMenuItem rangeFilterItem = new JMenuItem(StaticMembers.txtPriceRangeFilter);
        JMenuItem clearFilterItem = new JMenuItem(StaticMembers.txtClearFilter);
        // Set action listener
        moreFilterItem.addActionListener(this);
        lessFilterItem.addActionListener(this);
        rangeFilterItem.addActionListener(this);
        clearFilterItem.addActionListener(this);
        // Set shortcuts
        StaticMembers.setShortcut(moreFilterItem, 'M');
        StaticMembers.setShortcut(lessFilterItem, 'L');
        StaticMembers.setShortcut(rangeFilterItem, 'R');
        // Set font
        moreFilterItem.setFont(font);
        lessFilterItem.setFont(font);
        rangeFilterItem.setFont(font);
        clearFilterItem.setFont(font);

        filterMenu.add(moreFilterItem);
        filterMenu.add(lessFilterItem);
        filterMenu.add(rangeFilterItem);
        filterMenu.addSeparator();
        filterMenu.add(clearFilterItem);
        viewMenu.add(filterMenu);

        // Configure edit menu
        JMenuItem addItem = new JMenuItem(StaticMembers.txtAdd);
        JMenuItem editItem = new JMenuItem(StaticMembers.txtEdit);
        JMenuItem deleteItem = new JMenuItem(StaticMembers.txtDelete);
        JMenuItem deleteManyItem = new JMenuItem(StaticMembers.txtDeleteMany);
        JMenuItem clearItem = new JMenuItem(StaticMembers.txtClear);
        // Set action listener
        addItem.addActionListener(this);
        editItem.addActionListener(this);
        deleteItem.addActionListener(this);
        deleteManyItem.addActionListener(this);
        clearItem.addActionListener(this);
        // Set shortcuts
        StaticMembers.setShortcut(addItem, 'N');
        StaticMembers.setShortcut(editItem, 'E');
        StaticMembers.setShortcut(deleteItem, 'D');
        StaticMembers.setShortcut(deleteManyItem, '-');
        // Set font
        addItem.setFont(font);
        editItem.setFont(font);
        deleteItem.setFont(font);
        deleteManyItem.setFont(font);
        clearItem.setFont(font);

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
        JMenuItem popupEditItem = new JMenuItem(StaticMembers.txtEditPopup);
        popupEditItem.setFont(font);
        popupEditItem.addActionListener(this);
        JMenuItem popupDeleteItem = new JMenuItem(StaticMembers.txtDeletePopup);
        popupDeleteItem.setFont(font);
        popupDeleteItem.addActionListener(this);

        popupMenu.add(popupEditItem);
        popupMenu.add(popupDeleteItem);
    }

    private void createGui() {
        setContentPane(rootPanel);
        setTitle("Информация о товарах");
        createMenuBar();
        setPreferredSize(new Dimension(600, 500));

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

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message,
                "Ошибка", JOptionPane.WARNING_MESSAGE);
    }

    // User selection of menu item
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {
            // File menu
            case StaticMembers.txtOpenTextFile:
                openFromTextFile();
                break;
            case StaticMembers.txtOpenFromDatabase:
                openFromDatabase();
                break;
            case StaticMembers.txtSaveToTextFile:
                saveToTextFile();
                break;
            case StaticMembers.txtSaveToDatabase:
                saveToDatabase();
                break;
            case StaticMembers.txtExit:
                exit();
                break;
            // View menu
            case StaticMembers.txtPriceMoreFilter:
                priceMoreFilter();
                break;
            case StaticMembers.txtPriceLessFilter:
                priceLessFilter();
                break;
            case StaticMembers.txtPriceRangeFilter:
                priceRangeFilter();
                break;
            case StaticMembers.txtClearFilter:
                clearFilter();
                break;
            // Edit menu
            case StaticMembers.txtAdd:
                add();
                break;
            case StaticMembers.txtEdit:
                edit();
                break;
            case StaticMembers.txtDelete:
                delete();
                break;
            case StaticMembers.txtDeleteMany:
                deleteMany();
                break;
            case StaticMembers.txtClear:
                clear();
                break;
            // Popup menu
            case StaticMembers.txtEditPopup:
                edit(selectedId, productList.getById(selectedId));
                break;
            case StaticMembers.txtDeletePopup:
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
        File workingDirectory = new File(System.getProperty("user.dir"));
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(workingDirectory);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
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
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
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
    private void priceMoreFilter() {
        if (productList.size() > 0) {
            String result = JOptionPane.showInputDialog(this,
                    "Введите минимальное значение цены.",
                    "Фильтр по цене", JOptionPane.DEFAULT_OPTION);
            if (result != null) {
                try {
                    int price = Integer.parseInt(result);
                    filterResult = productList.filter(p -> p >= price);
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

    private void priceLessFilter() {
        if (productList.size() > 0) {
            String result = JOptionPane.showInputDialog(this,
                    "Введите максимальное значение цены.",
                    "Фильтр по цене", JOptionPane.DEFAULT_OPTION);
            if (result != null) {
                try {
                    int price = Integer.parseInt(result);
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

    private void priceRangeFilter() {
        if (productList.size() > 0) {
            PriceFilterFrame dialog = new PriceFilterFrame("Введите ценовой диапазон");
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
        DialogFrame dialog = new DialogFrame("Добавление товара", null);
        dialog.setVisible(true);
        Product product = dialog.getProduct();
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
                    Product productForEdit = productList.getById(id);
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

    private void edit(int id, Product productForEdit) {
        DialogFrame dialog = new DialogFrame("Редактирование товара", productForEdit);
        dialog.setVisible(true);
        Product newProduct = dialog.getProduct();
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
}

