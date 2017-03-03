import task.Product;
import task.ProductList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainFrame extends JFrame implements ActionListener {

    private JPanel rootPanel;
    private JTable table;
    private JMenuItem dbItem;

    private ProductList productList;

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        Font font = new Font("Arial", Font.PLAIN, 12);

        // Main items of menu bar
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(font);
        JMenu editMenu = new JMenu("Edit");
        editMenu.setEnabled(true);
        editMenu.setFont(font);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        // Configure file menu
        JMenu openMenu = new JMenu("Open");
        openMenu.setFont(font);
        // Open from text file
        JMenuItem txtFileItem = new JMenuItem("From text file");
        txtFileItem.setFont(font);
        txtFileItem.addActionListener(this);
        // Open from database
        dbItem = new JMenuItem("From database");
        dbItem.setFont(font);
        dbItem.addActionListener(this);
        dbItem.setEnabled(false);

        openMenu.add(txtFileItem);
        openMenu.add(dbItem);

        JMenu saveMenu = new JMenu("Save");
        saveMenu.setFont(font);
        // Save as text file
        JMenuItem saveToTxtItem = new JMenuItem("To text file");
        saveToTxtItem.setFont(font);
        saveToTxtItem.addActionListener(this);
        // Save as database
        JMenuItem saveToDbItem = new JMenuItem("To database");
        saveToDbItem.setFont(font);
        saveToDbItem.addActionListener(this);

        saveMenu.add(saveToTxtItem);
        saveMenu.add(saveToDbItem);

        // Exit item
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(this);
        exitItem.setFont(font);

        fileMenu.add(openMenu);
        fileMenu.add(saveMenu);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Configure edit menu
        JMenuItem addItem = new JMenuItem("Add");
        JMenuItem editItem = new JMenuItem("Edit");
        JMenuItem deleteItem = new JMenuItem("Delete");
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
        setTitle("Информация о товарах");
        createMenuBar();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(600, 500));

        productList = new ProductList();
        ProductTableModel tableModel = new ProductTableModel(productList);
        table = new JTable(tableModel);

        getContentPane().add(new JScrollPane(table));
        pack();
        setVisible(true);
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
            // Open
            case "From text file":
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
                int res = fileChooser.showOpenDialog(null);
                if (res == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    if (productList.loadFromFile(file.getName())) {
                        table.updateUI();
                        JOptionPane.showMessageDialog(this, "Данные из файла были загружены.");
                    }
                    else
                        JOptionPane.showMessageDialog(this,
                                "Файл содержит неверные данные.",
                                "Ошибка", JOptionPane.WARNING_MESSAGE);
                }
                break;
            case "From database":
                if (productList.loadFromDatabase()) {
                    table.updateUI();
                    JOptionPane.showMessageDialog(this, "Данные из базы данных были загружены.");
                }
                break;
            // Save
            case "To text file":
                fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
                res = fileChooser.showSaveDialog(null);
                if (res == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    productList.saveToFile(file.getName());
                    JOptionPane.showMessageDialog(this, "Данные были сохранены в файл.");
                }
                break;
            case "To database":
                productList.saveToDatabase();
                JOptionPane.showMessageDialog(this, "Данные были сохранены в базу данных.");
                dbItem.setEnabled(true);
                break;
            //region Edit menu
            // Add product
            case "Add":
                DialogFrame dialog = new DialogFrame("Добавление товара");
                dialog.setVisible(true);
                Product product = dialog.getProduct();
                if (product != null) {
                    if (productList.add(product)) {
                        table.updateUI();
                        JOptionPane.showMessageDialog(this, "Товар был добавлен.");
                    }
                    else
                        JOptionPane.showMessageDialog(this,
                                "Товар с данным ID уже существует.",
                                "Ошибка", JOptionPane.WARNING_MESSAGE);
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
                                    "Товар с данным ID не найден.",
                                    "Ошибка", JOptionPane.WARNING_MESSAGE);
                        else {
                            dialog = new DialogFrame("Редактирование товара");
                            dialog.setVisible(true);
                            if (productList.edit(id, dialog.getProduct())) {
                                table.updateUI();
                                JOptionPane.showMessageDialog(this, "Товар был отредактирован.");
                            }
                            else
                                JOptionPane.showMessageDialog(this,
                                        "Товар с данным ID уже существует.",
                                        "Ошибка", JOptionPane.WARNING_MESSAGE);
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
                            JOptionPane.showMessageDialog(this,
                                    "Товар с данным ID не найден.",
                                    "Ошибка", JOptionPane.WARNING_MESSAGE);
                        else {
                            table.updateUI();
                            JOptionPane.showMessageDialog(this, "Товар был удален.");
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
                if (dialogResult == JOptionPane.YES_OPTION) {
                    System.exit(0);
                    productList.clear();
                }
            default:
                break;
        }
    }
}

