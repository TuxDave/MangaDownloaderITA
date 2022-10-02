package com.tuxdave.manga_downloader_ita

fun main() {
    search(
        "tokyo",
        listeners = arrayOf(
            SearchProgressionListener { percentage -> println("${percentage}%") }
        )
    )
}