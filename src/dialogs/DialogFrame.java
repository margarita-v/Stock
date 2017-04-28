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

public class DialogFrame extends JDialog implements ActionListener {
    //region Frame components
    // Default widgets
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    // Components for user's input
    private JFormattedTextField ftfId;
    private JTextField textFieldName;
    private JFormattedTextField ftfPrice;
    private JFormattedTextField ftfCount;

    private JRadioButton rbBook;
    private JRadioButton rbClothes;
    private JRadioButton rbFood;

    private JTextField textFieldBook;
    private JTextField textFieldClothes;
    private JFormattedTextField ftfFood;

    private JLabel lblBook;
    private JLabel lblClothes;
    private JLabel lblFood;
    private JPanel panelForAdd;
    private JTextField textFieldString;
    private JFormattedTextField ftfInt;
    private JLabel lblString;
    private JLabel lblInt;
    //endregion

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
        ftfFood.setFormatterFactory(formatter);
        ftfInt.setFormatterFactory(formatter);

        // Configure radio group
        rbBook.addActionListener(this);
        rbClothes.addActionListener(this);
        rbFood.addActionListener(this);
        ButtonGroup group = new ButtonGroup();
        group.add(rbBook);
        group.add(rbClothes);
        group.add(rbFood);

        // Pre-filling product's fields if this dialog is using for edit
        this.productForEdit = productForEdit;
        fillForEdit();

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

        String genre = productForEdit != null ? textFieldString.getText() : textFieldBook.getText();
        String color = productForEdit != null ? textFieldString.getText() : textFieldClothes.getText();
        Object weightObj = productForEdit != null ? ftfInt.getValue() : ftfFood.getValue();

        if (!Objects.equals(idObj, null) && !Objects.equals(name, "")
                && !Objects.equals(countObj, null) && !Objects.equals(priceObj, null)
                && (!Objects.equals(genre, "") || !Objects.equals(color, "")
                || !Objects.equals(weightObj, null))) {
            try {
                int id = (int) idObj;
                int price = (int) priceObj;
                int count = (int) countObj;

                boolean usedForEdit = productForEdit != null;

                if (usedForEdit && productForEdit instanceof Book ||
                        !usedForEdit && rbBook.isSelected())
                    product = new Book(id, name, price, count, genre);
                else if (usedForEdit && productForEdit instanceof Clothes ||
                        !usedForEdit && rbClothes.isSelected())
                    product = new Clothes(id, name, price, count, color);
                else {
                    int weight = (int) weightObj;
                    product = new Food(id, name, price, count, weight);
                }

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

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        boolean bookChecked = rbBook.isSelected(),
                clothesChecked = rbClothes.isSelected(),
                foodChecked = rbFood.isSelected();

        lblBook.setEnabled(bookChecked);
        textFieldBook.setEnabled(bookChecked);

        lblClothes.setEnabled(clothesChecked);
        textFieldClothes.setEnabled(clothesChecked);

        lblFood.setEnabled(foodChecked);
        ftfFood.setEnabled(foodChecked);
    }

    private void fillForEdit() {
        if (productForEdit != null) {
            panelForAdd.setVisible(false);
            ftfId.setValue(productForEdit.getId());
            textFieldName.setText(productForEdit.getName());
            ftfPrice.setValue(productForEdit.getPrice());
            ftfCount.setValue(productForEdit.getQuantity());

            if (productForEdit instanceof Food) {
                lblInt.setText("Вес");
                lblInt.setVisible(true);
                ftfInt.setVisible(true);
                ftfInt.setValue(((Food) productForEdit).getWeight());
            }
            else {
                lblString.setVisible(true);
                textFieldString.setVisible(true);

                if (productForEdit instanceof Book) {
                    lblString.setText("Жанр");
                    textFieldString.setText(((Book) productForEdit).getGenre());
                }
                else if (productForEdit instanceof Clothes) {
                    lblString.setText("Цвет");
                    textFieldString.setText(((Clothes) productForEdit).getColor());
                }
            }
        }
    }
}
