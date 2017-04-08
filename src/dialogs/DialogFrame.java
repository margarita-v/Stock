package dialogs;

import models.AbstractProduct;
import models.Book;
import models.Clothes;
import models.Food;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.event.*;
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
    private JTextField textFieldString;
    private JLabel lblString;
    private JLabel lblInt;
    private JFormattedTextField ftfInt;

    // AbstractProduct which this dialog returns as a result
    private AbstractProduct product;

    private AbstractProduct productForEdit;

    public DialogFrame(NumberFormatter numberFormatter, String title, AbstractProduct productForEdit) {
        setContentPane(contentPane);
        setModal(true);
        setTitle(title);
        getRootPane().setDefaultButton(buttonOK);

        DefaultFormatterFactory formatter = new DefaultFormatterFactory(numberFormatter);
        ftfId.setFormatterFactory(formatter);
        ftfPrice.setFormatterFactory(formatter);
        ftfCount.setFormatterFactory(formatter);

        // Pre-filling product's fields if this dialog is using for edit
        this.productForEdit = productForEdit;
        if (productForEdit != null) {
            ftfId.setValue(productForEdit.getId());
            textFieldName.setText(productForEdit.getName());
            ftfPrice.setValue(productForEdit.getPrice());
            ftfCount.setValue(productForEdit.getQuantity());

            if (productForEdit instanceof Food) {
                lblInt.setText("Вес");
                lblInt.setVisible(true);
                ftfInt.setVisible(true);
                ftfInt.setFormatterFactory(formatter);
                ftfInt.setValue(((Food) productForEdit).getWeight());
            }
            else {
                lblString.setVisible(true);
                textFieldString.setVisible(true);

                if (productForEdit instanceof Book) {
                    lblString.setText("Жанр");
                    textFieldString.setText(((Book) productForEdit).getGenre());
                }
                else {
                    lblString.setText("Цвет");
                    textFieldString.setText(((Clothes) productForEdit).getColor());
                }
            }
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
        String stringField = textFieldString.getText();
        Object intField = ftfInt.getValue();

        if (!Objects.equals(idObj, null) && !Objects.equals(name, "")
                && !Objects.equals(countObj, null) && !Objects.equals(priceObj, null)) {
            try {
                int id = (int) idObj;
                int price = (int) priceObj;
                int count = (int) countObj;

                if (!Objects.equals(stringField, "")){
                    if (productForEdit instanceof Book)
                        product = new Book(id, name, price, count, stringField);
                    else if (productForEdit instanceof Clothes)
                        product = new Clothes(id, name, price, count, stringField);
                }
                else if (intField != null && productForEdit instanceof Food)
                    product = new Food(id, name, price, count, (int) intField);

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

    public AbstractProduct getProduct() {
        return product;
    }
}
