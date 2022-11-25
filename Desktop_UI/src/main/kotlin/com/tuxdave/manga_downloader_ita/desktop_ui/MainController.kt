package com.tuxdave.manga_downloader_ita.desktop_ui

import javafx.animation.Animation
import javafx.animation.FadeTransition
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.util.Duration

class MainController {

    @FXML
    private lateinit var searchTextField: TextField

    @FXML
    private lateinit var searchButton: Button

    @FXML
    private lateinit var searchingLabel: Label

    @FXML
    fun initialize(): Unit {
        Platform.runLater { searchButton.requestFocus() }
    }

    @FXML
    fun search(): Unit {
        val fadeTransition = FadeTransition(Duration.seconds(0.25), searchingLabel)
        fadeTransition.fromValue = 1.0
        fadeTransition.toValue = 0.0
        fadeTransition.isAutoReverse = true
        fadeTransition.cycleCount = Animation.INDEFINITE
        fadeTransition.play()

        // TODO: fare la ricerca asynchrona e riempire la list view 

        fadeTransition.stop()
        searchingLabel.opacity = 0.0
    }
}