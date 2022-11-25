package com.tuxdave.manga_downloader_ita.desktop_ui

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class MainApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(MainApplication::class.java.getResource("main-view.fxml"))
        val scene = Scene(fxmlLoader.load())
        stage.title = "MangaDownloaderITA"
        stage.scene = scene
        stage.isResizable = false
        stage.show()
    }
}

fun main() {
    Application.launch(MainApplication::class.java)
}