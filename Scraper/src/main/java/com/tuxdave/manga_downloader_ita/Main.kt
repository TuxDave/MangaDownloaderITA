package com.tuxdave.manga_downloader_ita

fun main() {
    val mangas = search(
        "tokyo ghoul",
        SearchOrderParam.MOST_READ,
        /*listeners = arrayOf(
            SearchProgressionListener { percentage -> println("${percentage}%") }
        )*/
    )
    openManga(mangas[0])
    println(mangas[0].open)
    println(mangas[0].volumiTotali)
    println(mangas[0].capitoliTotali)
}