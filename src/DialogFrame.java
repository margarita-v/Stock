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
            ftfId.setValue(productForEdit.getId());
            textFieldName.setText(productForEdit.getName());
            ftfPrice.setValue(productForEdit.getPrice());
            ftfCount.setValue(productForEdit.getQuantity());
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
        Object idObj = ftfId.getValue();
        String name = textFieldName.getText();
        Object countObj = ftfCount.getValue();
        Object priceObj = ftfPrice.getValue();

        if (!Objects.equals(idObj, null) && !Objects.equals(name, "")
                && !Objects.equals(countObj, null) && !Objects.equals(priceObj, null)) {
            try {
                int id = (int) idObj;
                int price = (int) priceObj;
                int count = (int) countObj;

                product = new Product(id, name, price, count, textFieldDescription.getText());
                dispose();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Введены неверные значения!",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
        }
        else
            JOptionPane.showMessageDialog(this,
                    "Заполните все поля!",
                    "Ошибка", JOptionPane.WARNING_MESSAGE);
    }

    private void onCancel() {
        dispose();
    }

    Product getProduct() {
        return product;
    }
}
