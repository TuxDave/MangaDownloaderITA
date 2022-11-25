package com.tuxdave.manga_downloader_ita.desktop_ui

import com.tuxdave.manga_downloader_ita.core_shared.entity.Manga
import com.tuxdave.manga_downloader_ita.scraper.PercentageListener
import com.tuxdave.manga_downloader_ita.scraper.SearchOrderParam
import com.tuxdave.manga_downloader_ita.scraper.openManga
import com.tuxdave.manga_downloader_ita.scraper.search
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.ProgressBar
import javafx.scene.control.TextField

fun ListView<MangaWrapper>.getSelected(): Manga {
    return this.selectionModel.selectedItems[0].manga
}

// Se qualcuno conosce un modo per overloddare il toString solo in questo file senza usare una classe si faccia avanti
class MangaWrapper(val manga: Manga) {

    override fun toString(): String {
        return manga.titolo
    }
}

class MainController {

    @FXML
    private lateinit var searchTextField: TextField

    @FXML
    private lateinit var searchButton: Button

    @FXML
    private lateinit var searchProgressBar: ProgressBar

    @FXML
    private lateinit var resultsListView: ListView<MangaWrapper>

    @FXML
    fun initialize(): Unit {
        Platform.runLater { searchButton.requestFocus() }
        
        resultsListView.selectionModel.selectedItemProperty().addListener(
            { _, _, _ -> openSelectedManga(); }
        )
    }

    @FXML
    fun searchManga(): Unit {
        searchProgressBar.opacity = 100.0
        resultsListView.items.removeAll(resultsListView.items)

        Thread() {
            val pl =
                PercentageListener("search") { percentage, id -> searchProgressBar.progress = percentage.toDouble(); }
            val found = search(
                searchTextField.text,
                SearchOrderParam.MOST_READ,
                listeners = listOf(
                    pl
                )
            )
            for (manga in found) {
                resultsListView.items.add(MangaWrapper(manga))
            }

            searchProgressBar.opacity = 0.0
        }.start()
    }

    fun openSelectedManga(): Unit {
        if (!resultsListView.getSelected().open)
        // TODO: Rendere questo async e nelle labels ecc durante il caricamento farle tutte lampeggiare o simili! 
            openManga(resultsListView.getSelected())
    }
}