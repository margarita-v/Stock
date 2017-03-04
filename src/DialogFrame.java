import task.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DialogFrame extends JDialog implements KeyListener {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldId;
    private JTextField textFieldName;
    private JTextField textFieldPrice;
    private JTextField textFieldCount;
    private JTextField textFieldDescription;
    private JLabel lblError;
    private Product product;

    public DialogFrame(String title, Product productForEdit) {
        lblError.setText("");
        setContentPane(contentPane);
        setModal(true);
        setTitle(title);
        getRootPane().setDefaultButton(buttonOK);
        setPreferredSize(new Dimension(400, 300));

        if (productForEdit != null) {
            textFieldId.setText(Integer.toString(productForEdit.getId()));
            textFieldName.setText(productForEdit.getName());
            textFieldPrice.setText(Integer.toString(productForEdit.getPrice()));
            textFieldCount.setText(Integer.toString(productForEdit.getQuantity()));
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

        textFieldId.addKeyListener(this);
        textFieldName.addKeyListener(this);
        textFieldPrice.addKeyListener(this);
        textFieldCount.addKeyListener(this);
        textFieldDescription.addKeyListener(this);

        pack();
    }

    public Product getProduct() {
        return product;
    }

    private void onOK() {
        if (textFieldId.getText() != "" && textFieldName.getText() != ""
                && textFieldCount.getText() != "" && textFieldPrice.getText() != "") {
            try {
                int id = Integer.parseInt(textFieldId.getText());
                int price = Integer.parseInt(textFieldPrice.getText());
                int count = Integer.parseInt(textFieldCount.getText());

                if (id > 0 && price > 0 && count > 0 ) {
                    product = new Product(id, textFieldName.getText(), price, count, textFieldDescription.getText());
                    dispose();
                }
                else
                    lblError.setText("Введены неверные значения!");

            } catch (NumberFormatException e) {
                lblError.setText("Введены неверные значения!");
            }
        }
        else
            lblError.setText("Заполните все поля!");
    }

    private void onCancel() {
        dispose();
    }

    //region KeyListener methods
    @Override
    public void keyTyped(KeyEvent keyEvent) {
        lblError.setText("");
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
    //endregion
}
