package com.tuxdave.manga_downloader_ita

fun main() {
    val mangas = search(
        "to",
        listeners = arrayOf(
            SearchProgressionListener { percentage -> println("${percentage}%") }
        )
    )
}