package com.tuxdave.manga_downloader_ita.desktop_ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.tuxdave.manga_downloader_ita.core_shared.entity.Manga;
import com.tuxdave.manga_downloader_ita.core_shared.view.Raccolta;
import com.tuxdave.manga_downloader_ita.core_shared.view.Volume;
import com.tuxdave.manga_downloader_ita.desktop_ui.components.JBlinkingLabel;
import com.tuxdave.manga_downloader_ita.scraper.PercentageListener;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.tuxdave.manga_downloader_ita.core_shared.export.ExportPDFKt.exportPDF;
import static com.tuxdave.manga_downloader_ita.scraper.MangaDownloaderKt.downloadManga;

public class DownloaderDialog extends JDialog {
    private JPanel contentPane;
    private JButton annullaButton;
    private JProgressBar raccoltaProgressBar;
    private JProgressBar volumeProgressBar;
    private JProgressBar capitoloProgressBar;
    private JBlinkingLabel esportazioneBlinkingLabel;

    private final Manga manga;
    private final File path;

    public DownloaderDialog(
            Manga manga,
            File path,
            int from,
            int to,
            int[] skip,
            boolean split
    ) {
        this.manga = manga;
        this.path = path;

        setTitle("Scaricamento in corso...");
        setContentPane(contentPane);
        setModal(true);
        annullaButton.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel() {
        if (runner != null) runner.interrupt();
        dispose();
    }

    private Thread runner;

    public void run(
            Manga manga,
            File path,
            int from,
            int to,
            int[] skip,
            boolean split
    ) {
        DownloaderDialog self = this;
        runner = new Thread() {
            @Override
            public void run() {
                super.run();
                Raccolta r = downloadManga(
                        manga,
                        List.of(new PercentageListener(raccoltaProgressBar.getString(), (integer, s) -> {
                            raccoltaProgressBar.setString(s + Math.max(integer - 1, 0) + "%");
                            raccoltaProgressBar.setValue(integer);
                            return null; //bella per l'Unit di kotlin
                        })),
                        List.of(new PercentageListener(volumeProgressBar.getString(), (p, id) -> {
                            volumeProgressBar.setString(id + p.toString() + "%");
                            volumeProgressBar.setValue(p);

                            return null;
                        })),
                        List.of(new PercentageListener(capitoloProgressBar.getString(), (p, id) -> {
                            capitoloProgressBar.setString(id + p.toString() + "%");
                            capitoloProgressBar.setValue(p);
                            return null;
                        })),
                        from,
                        to,
                        skip
                );
                esportazioneBlinkingLabel.setBlinking(true);
                if (split) {
                    String basePath = path.getParentFile().getAbsolutePath() + "/" + r.getManga().getTitolo();
                    for (Volume sv : r.getVolumi()) {
                        List<Volume> lv = new ArrayList<>();
                        lv.add(sv);
                        Raccolta sr = new Raccolta(r.getManga(), lv);
                        exportPDF(sr, new File(basePath + "_volume" + sv.getNumero() + ".pdf"));
                    }
                } else
                    exportPDF(r, path);
                JOptionPane.showMessageDialog(self, "Scaricamento completato");
                onCancel();
            }
        };
        runner.start();
    }

    public static void show(
            Manga manga,
            File path,
            int from,
            int to,
            int[] skip,
            boolean split
    ) {
        DownloaderDialog dialog = new DownloaderDialog(manga, path, from, to, skip, split);
        dialog.setResizable(false);
        dialog.pack();
        dialog.run(manga, path, from, to, skip, split);
        dialog.setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        annullaButton = new JButton();
        annullaButton.setText("Annulla");
        panel2.add(annullaButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        raccoltaProgressBar = new JProgressBar();
        raccoltaProgressBar.setString("Raccolta: ");
        raccoltaProgressBar.setStringPainted(true);
        panel3.add(raccoltaProgressBar, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(400, -1), new Dimension(400, -1), new Dimension(400, -1), 0, false));
        volumeProgressBar = new JProgressBar();
        volumeProgressBar.setString("Volume: ");
        volumeProgressBar.setStringPainted(true);
        panel3.add(volumeProgressBar, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        capitoloProgressBar = new JProgressBar();
        capitoloProgressBar.setString("Capitolo: ");
        capitoloProgressBar.setStringPainted(true);
        panel3.add(capitoloProgressBar, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        esportazioneBlinkingLabel = new JBlinkingLabel();
        esportazioneBlinkingLabel.setBlinking(false);
        Font esportazioneBlinkingLabelFont = this.$$$getFont$$$(null, Font.BOLD, 16, esportazioneBlinkingLabel.getFont());
        if (esportazioneBlinkingLabelFont != null) esportazioneBlinkingLabel.setFont(esportazioneBlinkingLabelFont);
        esportazioneBlinkingLabel.setText("Esportazione in corso...");
        panel3.add(esportazioneBlinkingLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /** @noinspection ALL */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /** @noinspection ALL */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
