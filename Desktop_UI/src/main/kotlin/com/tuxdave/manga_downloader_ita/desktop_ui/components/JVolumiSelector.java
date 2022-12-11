package com.tuxdave.manga_downloader_ita.desktop_ui.components;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.tuxdave.manga_downloader_ita.desktop_ui.UtilsKt;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Array;
import java.util.ArrayList;

public class JVolumiSelector extends JDialog implements ActionListener {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JRadioButton intervalloRadioButton;
    private JRadioButton volumiSparsiRadioButton;
    private JRangePicker rangePicker;
    private JPanel sparsiPanel;

    private ArrayList<JCheckBox> sparsiGroup;

    @Getter
    @Setter
    private boolean selected;

    @Getter
    @Setter
    private int upBound;

    @Getter
    @Setter
    private int downBound;

    @Getter
    @Setter
    private int selectedDown;

    @Getter
    @Setter
    private int selectedUp;

    @Getter
    @Setter
    private int[] selectedSkip;

    public JVolumiSelector(int down, int up) {
        this.setUpBound(up);
        this.setDownBound(down);

        rangePicker = new JRangePicker(downBound, upBound);
        $$$setupUI$$$();

        ButtonGroup g = new ButtonGroup();
        g.add(intervalloRadioButton);
        g.add(volumiSparsiRadioButton);
        sparsiPanel.setEnabled(false);

        sparsiPanel.setLayout(new BoxLayout(sparsiPanel, BoxLayout.Y_AXIS));
        JCheckBox c;
        sparsiGroup = new ArrayList<JCheckBox>();
        for (int i = downBound; i <= upBound; i++) {
            c = new JCheckBox("" + i);
            sparsiGroup.add(c);
            sparsiPanel.add(c);
        }

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        intervalloRadioButton.addActionListener(this);
        volumiSparsiRadioButton.addActionListener(this);
    }

    private void onOK() {
        if (intervalloRadioButton.isSelected()) {
            selectedSkip = new int[]{};
            selectedDown = rangePicker.getRange()[0];
            selectedUp = rangePicker.getRange()[1];
        } else {
            ArrayList<Integer> checked = new ArrayList<Integer>();
            for (JCheckBox c : sparsiGroup) {
                if (c.isSelected()) {
                    checked.add(Integer.parseInt(c.getText()));
                }
            }
            if (checked.size() == 0) {
                selected = false;
                return;
            }
            selectedDown = checked.get(0);
            selectedUp = checked.get(checked.size() - 1);
            selectedSkip = new int[selectedUp - selectedDown - (checked.size() - 1)];
            int c = 0;
            for (int i = selectedDown + 1; i < selectedUp; i++) {
                if (!checked.contains(i)) {
                    selectedSkip[c++] = i;
                }
            }
        }
        selected = true;
        dispose();
    }

    private void onCancel() {
        selected = false;
        dispose();
    }

    public static JVolumiSelector show(int down, int up) {
        JVolumiSelector dialog = new JVolumiSelector(down, up);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setVisible(true);
        return dialog;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("OK");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        intervalloRadioButton = new JRadioButton();
        intervalloRadioButton.setSelected(true);
        intervalloRadioButton.setText("Intervallo");
        panel3.add(intervalloRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        volumiSparsiRadioButton = new JRadioButton();
        volumiSparsiRadioButton.setText("Volumi Sparsi");
        panel3.add(volumiSparsiRadioButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel3.add(rangePicker.$$$getRootComponent$$$(), new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setEnabled(false);
        panel3.add(scrollPane1, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(70, 175), new Dimension(70, 175), new Dimension(70, 175), 0, false));
        sparsiPanel = new JPanel();
        sparsiPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        scrollPane1.setViewportView(sparsiPanel);
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 20), new Dimension(-1, 20), new Dimension(-1, 20), 2, false));
        final Spacer spacer3 = new Spacer();
        panel3.add(spacer3, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /** @noinspection ALL */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    private void createUIComponents() {
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == intervalloRadioButton || actionEvent.getSource() == volumiSparsiRadioButton) {
            if (intervalloRadioButton.isSelected()) {
                rangePicker.setEnabled(true);
                sparsiPanel.setEnabled(false);
            } else {
                rangePicker.setEnabled(false);
                sparsiPanel.setEnabled(true);
            }
        }
    }
}
