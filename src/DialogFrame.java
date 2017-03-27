import task.Product;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.Objects;

public class DialogFrame extends JDialog {
    // Default widgets
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    // Components for user's input
    private JFormattedTextField ftfId;
    private JTextField textFieldName;
    private JFormattedTextField ftfPrice;
    private JFormattedTextField ftfCount;
    private JTextField textFieldDescription;

    // Product which this dialog returns as a result
    private Product product;

    DialogFrame(String title, Product productForEdit) {
        setContentPane(contentPane);
        setModal(true);
        setTitle(title);
        getRootPane().setDefaultButton(buttonOK);

        NumberFormat integerFormat = NumberFormat.getIntegerInstance();
        NumberFormatter numberFormatter = new NumberFormatter(integerFormat);
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setMinimum(1);
        numberFormatter.setMaximum(Integer.MAX_VALUE);
        numberFormatter.setAllowsInvalid(false);

        DefaultFormatterFactory formatter = new DefaultFormatterFactory(numberFormatter);
        ftfId.setFormatterFactory(formatter);
        ftfPrice.setFormatterFactory(formatter);
        ftfCount.setFormatterFactory(formatter);

        // Pre-filling product's fields if this dialog is using for edit
        if (productForEdit != null) {
            ftfId.setText(Integer.toString(productForEdit.getId()));
            textFieldName.setText(productForEdit.getName());
            ftfPrice.setText(Integer.toString(productForEdit.getPrice()));
            ftfCount.setText(Integer.toString(productForEdit.getQuantity()));
            textFieldDescription.setText(productForEdit.getDescription());
        }

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
    }

    private void onOK() {
        String idStr = ftfId.getText();
        String name = textFieldName.getText();
        String countStr = ftfCount.getText();
        String priceStr = ftfPrice.getText();

        if (!Objects.equals(idStr, "") && !Objects.equals(name, "")
                && !Objects.equals(countStr, "") && !Objects.equals(priceStr, "")) {
            try {
                int id = Integer.parseInt(idStr);
                int price = Integer.parseInt(priceStr);
                int count = Integer.parseInt(countStr);
                
                product = new Product(id, name, price, count, textFieldDescription.getText());
                dispose();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Введены неверные значения!");
            }
        }
        else
            JOptionPane.showMessageDialog(this, "Заполните все поля!");
    }

    private void onCancel() {
        dispose();
    }

    Product getProduct() {
        return product;
    }
}
