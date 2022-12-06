package com.tuxdave.manga_downloader_ita.desktop_ui.components;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JRangePicker extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;

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
    private int selectedSkip;

    public JRangePicker(int down, int up) {
        this.setUpBound(up);
        this.setDownBound(down);

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
    }

    private void onOK() {
        // TODO: 06/12/22 assegnare i valori
        selected = true;
        dispose();
    }

    private void onCancel() {
        selected = false;
        dispose();
    }

    public static void show(int down, int up) {
        JRangePicker dialog = new JRangePicker(down, up);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setVisible(true);
    }
}
