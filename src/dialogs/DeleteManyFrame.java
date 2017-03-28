package dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class DeleteManyFrame extends JDialog {
    // Default widgets
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

    // Panel for checkboxes
    private JPanel itemsPane;
    // IDs of products which were chosen
    private List<Integer> chosenItems;

    public DeleteManyFrame(List<Integer> productsIDs) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        chosenItems = new ArrayList<>();
        List<JCheckBox> cbList = new ArrayList<>();
        for (Integer id: productsIDs) {
            cbList.add(new JCheckBox(id.toString()));
        }

        for (JCheckBox checkBox: cbList) {
            checkBox.addItemListener(itemEvent -> {
                int id = Integer.parseInt(checkBox.getText());
                if (checkBox.isSelected())
                    chosenItems.add(id);
                else if (chosenItems.contains(id))
                    chosenItems.remove(id);
            });
            itemsPane.add(checkBox);
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
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public List<Integer> getChosenIDs() {
        return chosenItems;
    }
}
