package com.tuxdave.manga_downloader_ita.desktop_ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.tuxdave.manga_downloader_ita.core_shared.entity.Manga;
import com.tuxdave.manga_downloader_ita.desktop_ui.components.JBlinkingLabel;
import com.tuxdave.manga_downloader_ita.desktop_ui.components.JSearchField;
import com.tuxdave.manga_downloader_ita.desktop_ui.components.JVolumiSelector;
import com.tuxdave.manga_downloader_ita.desktop_ui.components.MangaViewer;
import com.tuxdave.manga_downloader_ita.scraper.SearchOrderParam;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.tuxdave.manga_downloader_ita.desktop_ui.UtilsKt.sortByCorrispondenza;
import static com.tuxdave.manga_downloader_ita.scraper.MangaDownloaderKt.openManga;
import static com.tuxdave.manga_downloader_ita.scraper.MangaSearcherKt.search;

public class MainForm extends JPanel {

    private JPanel panel1;
    private JSearchField searchField;
    private JList<Manga> resultsList;
    private JBlinkingLabel searchingLabel;
    private MangaViewer mangaViewer;
    private JBlinkingLabel fetchingLabel;
    private JScrollPane resultScrollPane;
    private JButton downloadButton;

    private MainForm self;

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 3, new Insets(5, 5, 5, 5), -1, -1));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 300), null, null, 0, false));
        Font searchingLabelFont = this.$$$getFont$$$(null, Font.BOLD, 16, searchingLabel.getFont());
        if (searchingLabelFont != null) searchingLabel.setFont(searchingLabelFont);
        searchingLabel.setText("Searching...");
        panel2.add(searchingLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, null, null, null, 0, false));
        resultScrollPane = new JScrollPane();
        resultScrollPane.setHorizontalScrollBarPolicy(32);
        panel2.add(resultScrollPane, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        resultsList = new JList();
        resultScrollPane.setViewportView(resultsList);
        searchField = new JSearchField();
        searchField.setPlaceHolder("Cerca...");
        searchField.setText("one piece");
        panel2.add(searchField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(250, -1), new Dimension(-1, 30), null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(5, -1), null, new Dimension(5, -1), 0, false));
        panel1.add(mangaViewer.$$$getRootComponent$$$(), new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        Font fetchingLabelFont = this.$$$getFont$$$(null, Font.BOLD, 16, fetchingLabel.getFont());
        if (fetchingLabelFont != null) fetchingLabel.setFont(fetchingLabelFont);
        fetchingLabel.setText("Fetching");
        panel1.add(fetchingLabel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        downloadButton = new JButton();
        Font downloadButtonFont = this.$$$getFont$$$(null, Font.BOLD, 16, downloadButton.getFont());
        if (downloadButtonFont != null) downloadButton.setFont(downloadButtonFont);
        downloadButton.setText("Download!");
        panel1.add(downloadButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        return panel1;
    }

    private void createUIComponents() {
        searchingLabel = new JBlinkingLabel(0);
        fetchingLabel = new JBlinkingLabel();
        mangaViewer = new MangaViewer(null);
    }

    //==================================================================================================================

    public MainForm() {
        $$$setupUI$$$();

        self = this;
        this.add(panel1);

        searchingLabel.setDuration(750);
        resultsList.setFixedCellWidth(searchField.getWidth());

        Listener l = new Listener();
        searchField.addActionListener(l);
        resultsList.addListSelectionListener(l);
        downloadButton.addActionListener(l);
    }

    private class Listener implements ActionListener, ListSelectionListener {

        private Opener opener = null;

        public Listener() {
            super();
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (searchField.equals(actionEvent.getSource())) {
                if (searchField.getText().equals("")) {
                    searchField.setBackground(Color.red);
                } else {
                    searchField.setBackground(Color.white);
                    searchingLabel.setBlinking(true);
                    class MangaCellRenderer extends JLabel implements ListCellRenderer<Manga> {
                        @Override
                        public Component getListCellRendererComponent(
                                JList<? extends Manga> jList,
                                Manga manga,
                                int i,
                                boolean isSelected,
                                boolean b1
                        ) {
                            setText(manga.getTitolo());
                            if (isSelected) {
                                setForeground(Color.white);
                                setBackground(Color.blue);
                                setOpaque(true);
                            } else {
                                setForeground(Color.black);
                                setBackground(Color.white);
                                setOpaque(false);
                            }
                            return this;
                        }
                    }
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            List<Manga> results = search(
                                    searchField.getText(),
                                    SearchOrderParam.MOST_READ,
                                    new ArrayList<>()
                            );
                            // TODO: magari chiedere se si vuole ordinare per corrispondenza o visualizzazioni (spoiler non ho questo dato)
                            results = sortByCorrispondenza(results, searchField.getText());
                            DefaultListModel<Manga> model = new DefaultListModel<>();
                            for (Manga m : results) {
                                model.addElement(m);
                            }
                            resultsList.setCellRenderer(new MangaCellRenderer());
                            resultsList.setModel(model);

                            searchingLabel.setBlinking(false);
                        }
                    }.start();
                }
            } else if (downloadButton.equals(actionEvent.getSource()) && mangaViewer.getManga() != null) {
                JVolumiSelector selector = JVolumiSelector.show(1, mangaViewer.getManga().getVolumiTotali());
                if (!selector.isSelected()) {
                    return;
                }

                File target;
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setMultiSelectionEnabled(false);
                chooser.setApproveButtonText("Salva");
                chooser.addChoosableFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        if (f.isDirectory()) {
                            return true;
                        } else {
                            return f.getName().toLowerCase().endsWith(".pdf");
                        }
                    }

                    @Override
                    public String getDescription() {
                        return "Documenti PDF (.pdf)";
                    }
                }); //:)
                chooser.showSaveDialog(self);
                String targetPath = chooser.getSelectedFile().getAbsolutePath();
                if (!targetPath.substring(targetPath.length() - 4).toLowerCase().equals(".pdf")) {
                    targetPath += ".pdf";
                }
                target = new File(targetPath);

                if (mangaViewer.getManga() != null && target != null)
                    DownloaderDialog.show(
                            mangaViewer.getManga(),
                            target,
                            selector.getSelectedDown(),
                            selector.getSelectedUp(),
                            selector.getSelectedSkip()
                    );
            }
        }

        private class Opener extends Thread {
            private final Manga manga;

            public Manga getManga() {
                return manga;
            }

            public Opener(Manga manga) {
                super();
                this.manga = manga;
            }

            @Override
            public void run() {
                super.run();
                if (!manga.getOpen()) {
                    openManga(manga);
                }
                mangaViewer.setManga(manga);
                interrupt();
            }

            @Override
            public void interrupt() {
                super.interrupt();
                fetchingLabel.setBlinking(false);
            }
        }

        @Override
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            if (listSelectionEvent.getSource() == resultsList) {
                if (opener != null) {
                    opener.interrupt();
                }
                fetchingLabel.setBlinking(true);
                Manga m = ((Manga) resultsList.getSelectedValue());
                if (m == null) {
                    fetchingLabel.setBlinking(false);
                    return;
                }
                if (m.getOpen()) {
                    mangaViewer.setManga(m);
                    fetchingLabel.setBlinking(false);
                } else {
                    opener = new Opener(m);
                    opener.start();
                }
            }
        }
    }
}
