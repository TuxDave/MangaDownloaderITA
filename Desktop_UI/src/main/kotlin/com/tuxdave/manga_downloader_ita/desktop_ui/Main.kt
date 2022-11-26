package com.tuxdave.manga_downloader_ita.desktop_ui

import javax.swing.JFrame

fun main(){
    val f = JFrame()
    f.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    f.isResizable = false
    f.isVisible = true

    f.contentPane = MainForm()
    f.isVisible = true
    f.pack()
}