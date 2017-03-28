package dialogs;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.event.*;
import java.util.Objects;

public class PriceFilterFrame extends JDialog {
    // Default widgets
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    // Components for user's input
    private JFormattedTextField ftfMinPrice;
    private JFormattedTextField ftfMaxPrice;

    // Price range which this dialog returns as a result
    private int minPrice, maxPrice;

    public PriceFilterFrame(NumberFormatter numberFormatter, String title) {
        setContentPane(contentPane);
        setModal(true);
        setTitle(title);
        getRootPane().setDefaultButton(buttonOK);

        DefaultFormatterFactory formatter = new DefaultFormatterFactory(numberFormatter);
        ftfMinPrice.setFormatterFactory(formatter);
        ftfMaxPrice.setFormatterFactory(formatter);

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
        Object minObj = ftfMinPrice.getValue();
        Object maxObj = ftfMaxPrice.getValue();

        if (!Objects.equals(minObj, null) && !Objects.equals(maxObj, null)) {
            try {
                minPrice = (int) minObj;
                maxPrice = (int) maxObj;

                if (minPrice > maxPrice) {
                    int temp = minPrice;
                    minPrice = maxPrice;
                    maxPrice = temp;
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
        // add your code here if necessary
        dispose();
    }

    public int getMinPrice() {
        return minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }
}
