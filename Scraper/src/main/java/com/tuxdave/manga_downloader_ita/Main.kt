package com.tuxdave.manga_downloader_ita

fun main() {
    search(
        "to",
        listeners = arrayOf(
            SearchProgressionListener { percentage -> println("${percentage}%") }
        )
    )
}