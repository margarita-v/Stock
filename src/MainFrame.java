import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements ActionListener {

    private JPanel rootPanel;
    private JMenuBar menuBar;
    private JMenu fileMenu, editMenu, openMenu, saveAsMenu;
    private JMenuItem newItem, txtFileItem, dbItem,
            saveItem, saveAsTxtItem, saveAsDbItem,
            closeItem, exitItem,
            addItem, editItem, deleteItem;
    private JTable table;

    private void createGui() {

        menuBar = new JMenuBar();
        Font font = new Font("Arial", Font.PLAIN, 12);

        // Main items of menu bar
        fileMenu = new JMenu("File");
        fileMenu.setFont(font);
        editMenu = new JMenu("Edit");
        editMenu.setEnabled(false);
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

        // Close item
        closeItem = new JMenuItem("Close");
        closeItem.addActionListener(this);
        closeItem.setEnabled(false);
        closeItem.setFont(font);

        // Exit item
        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(this);
        exitItem.setFont(font);

        fileMenu.add(newItem);
        fileMenu.add(openMenu);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsMenu);
        fileMenu.add(closeItem);
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

        // Configure main frame
        setContentPane(rootPanel);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(500, 400));
        setJMenuBar(menuBar);
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
            case "Close":
                break;
            // Edit menu
            case "Add":
                break;
            case "Edit":
                break;
            case "Delete":
                break;
            case "Exit":
                System.exit(0);
            default:
                break;
        }
    }
}

