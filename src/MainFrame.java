import task.Product;
import task.ProductList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements ActionListener {

    private JPanel rootPanel;
    private JTable table;
    private JMenuBar menuBar;
    private JMenu fileMenu, editMenu, openMenu, saveAsMenu;
    private JMenuItem newItem, txtFileItem, dbItem,
            saveItem, saveAsTxtItem, saveAsDbItem, exitItem,
            addItem, editItem, deleteItem;
    private ProductTableModel tableModel;

    private ProductList productList;
    private DialogFrame dialog;

    private void createMenuBar() {
        menuBar = new JMenuBar();
        Font font = new Font("Arial", Font.PLAIN, 12);

        // Main items of menu bar
        fileMenu = new JMenu("File");
        fileMenu.setFont(font);
        editMenu = new JMenu("Edit");
        editMenu.setEnabled(true);
        editMenu.setFont(font);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        // Configure file menu
        newItem = new JMenuItem("New");
        newItem.setFont(font);
        newItem.addActionListener(this);

        openMenu = new JMenu("Open");
        openMenu.setFont(font);
        // Open from text file
        txtFileItem = new JMenuItem("From text file");
        txtFileItem.setFont(font);
        txtFileItem.addActionListener(this);
        // Open from database
        dbItem = new JMenuItem("From database");
        dbItem.setFont(font);
        dbItem.addActionListener(this);

        openMenu.add(txtFileItem);
        openMenu.add(dbItem);

        saveItem = new JMenuItem("Save");
        saveItem.setFont(font);
        saveItem.setEnabled(false);
        saveItem.addActionListener(this);

        saveAsMenu = new JMenu("Save As");
        saveAsMenu.setFont(font);
        saveAsMenu.setEnabled(false);
        // Save as text file
        saveAsTxtItem = new JMenuItem("As text file");
        saveAsTxtItem.setFont(font);
        saveAsTxtItem.addActionListener(this);
        // Save as database
        saveAsDbItem = new JMenuItem("As database");
        saveAsDbItem.setFont(font);
        saveAsDbItem.addActionListener(this);

        saveAsMenu.add(saveAsTxtItem);
        saveAsMenu.add(saveAsDbItem);

        // Exit item
        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(this);
        exitItem.setFont(font);

        fileMenu.add(newItem);
        fileMenu.add(openMenu);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsMenu);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Configure edit menu
        addItem = new JMenuItem("Add");
        editItem = new JMenuItem("Edit");
        deleteItem = new JMenuItem("Delete");
        // Set action listener
        addItem.addActionListener(this);
        editItem.addActionListener(this);
        deleteItem.addActionListener(this);
        // Set font
        addItem.setFont(font);
        editItem.setFont(font);
        deleteItem.setFont(font);

        editMenu.add(addItem);
        editMenu.add(editItem);
        editMenu.add(deleteItem);

        setJMenuBar(menuBar);
    }

    private void createGui() {
        setContentPane(rootPanel);
        setVisible(true);
        setTitle("Информация о товарах");
        createMenuBar();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(600, 500));

        productList = new ProductList();
        tableModel = new ProductTableModel(productList);
        table = new JTable(tableModel);

        getContentPane().add(new JScrollPane(table));
        pack();
    }

    private MainFrame() {
        createGui();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(MainFrame::new);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {
            case "New":
                Product p = new Product(5, "jhjh", 34, 4, "fghjk");
                productList.add(p);
                table.updateUI();
                break;
            // Open
            case "From text file":
                break;
            case "From database":
                break;
            case "Save":
                break;
            // Save as
            case "As text file":
                break;
            case "As database":
                break;
            //region Edit menu
            // Add product
            case "Add":
                dialog = new DialogFrame("Добавление товара");
                dialog.setVisible(true);
                Product product = dialog.getProduct();
                if (product != null) {
                    productList.add(product);
                    table.updateUI();
                }
                break;
            // Edit product
            case "Edit":
                String result = JOptionPane.showInputDialog(this,
                        "Введите ID товара, который требуется отредактировать.");
                if (result != null) {
                    try {
                        int id = Integer.parseInt(result);
                        if (!productList.find(id))
                            JOptionPane.showMessageDialog(this,
                                    "Товар с данным ID не найден.");
                        else {
                            dialog = new DialogFrame("Редактирование товара");
                            dialog.setVisible(true);

                            table.updateUI();
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this,
                                "Введено неверное значение ID.",
                                "Ошибка",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
            // Delete product
            case "Delete":
                result = JOptionPane.showInputDialog(this,
                        "Введите ID товара, который требуется удалить.");
                if (result != null) {
                    try {
                        int id = Integer.parseInt(result);
                        if (!productList.delete(id))
                            JOptionPane.showMessageDialog(this, "Товар с данным ID не найден.");
                        else {
                            JOptionPane.showMessageDialog(this, "Товар был удален.");
                            table.updateUI();
                        }
                    } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this,
                            "Введено неверное значение ID.",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
            //endregion
            case "Exit":
                int dialogResult = JOptionPane.showConfirmDialog(this,
                        "Вы уверены, что хотите выйти?", "Подтверждение", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION)
                    System.exit(0);
            default:
                break;
        }
    }
}

