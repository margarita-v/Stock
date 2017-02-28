import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel rootPanel;

    private void createGui() {

        JMenuBar menuBar = new JMenuBar();
        Font font = new Font("Arial", Font.PLAIN, 12);

        // Main items of menu bar
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(font);
        JMenu editMenu = new JMenu("Edit");
        editMenu.setFont(font);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        // Configure file menu
        JMenuItem newItem = new JMenuItem("New");
        newItem.setFont(font);

        JMenu openMenu = new JMenu("Open");
        openMenu.setFont(font);
        JMenuItem txtFileItem = new JMenuItem("From text file");
        txtFileItem.setFont(font);
        JMenuItem dbItem = new JMenuItem("From database");
        dbItem.setFont(font);

        openMenu.add(txtFileItem);
        openMenu.add(dbItem);

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setFont(font);

        JMenu saveAsMenu = new JMenu("Save As");
        saveAsMenu.setFont(font);
        JMenuItem saveAsTxtItem = new JMenuItem("As text file");
        saveAsTxtItem.setFont(font);
        JMenuItem saveAsDbItem = new JMenuItem("As database");
        saveAsDbItem.setFont(font);

        saveAsMenu.add(saveAsTxtItem);
        saveAsMenu.add(saveAsDbItem);

        JMenuItem closeItem = new JMenuItem("Close");
        JMenuItem exitItem = new JMenuItem("Exit");
        closeItem.setFont(font);
        exitItem.setFont(font);

        fileMenu.add(newItem);
        fileMenu.add(openMenu);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsMenu);
        fileMenu.add(closeItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Configure edit menu
        JMenuItem addItem = new JMenuItem("Add");
        JMenuItem editItem = new JMenuItem("Edit");
        JMenuItem deleteItem = new JMenuItem("Delete");
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
        setPreferredSize(new Dimension(400, 300));
        setJMenuBar(menuBar);
        pack();
    }

    private MainFrame() {
        createGui();
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
