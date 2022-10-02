package com.tuxdave.manga_downloader_ita

fun main() {
    val mangas = search(
        "tokyo",
        listeners = arrayOf(
            SearchProgressionListener { percentage -> println("${percentage}%") }
        )
    )
    println(mangas[0])
}