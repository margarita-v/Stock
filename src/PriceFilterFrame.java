import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
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

    PriceFilterFrame(String title) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setPreferredSize(new Dimension(320, 125));
        setTitle(title);

        NumberFormat integerFormat = NumberFormat.getIntegerInstance();
        NumberFormatter numberFormatter = new NumberFormatter(integerFormat);
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setMinimum(1);
        numberFormatter.setMaximum(Integer.MAX_VALUE);
        numberFormatter.setAllowsInvalid(false);

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
    }

    private void onOK() {
        String minStr = ftfMinPrice.getText();
        String maxStr = ftfMaxPrice.getText();

        if (!Objects.equals(minStr, "") && !Objects.equals(maxStr, "")) {
            try {
                minPrice = Integer.parseInt(minStr);
                maxPrice = Integer.parseInt(maxStr);

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

    int getMinPrice() {
        return minPrice;
    }

    int getMaxPrice() {
        return maxPrice;
    }
}