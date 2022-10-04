package com.tuxdave.manga_downloader_ita

import java.net.URI

fun main() {
    val mangas = search(
        "tokyo ghoul",
        SearchOrderParam.MOST_READ,
        /*listeners = arrayOf(
            SearchProgressionListener { percentage -> println("${percentage}%") }
        )*/
    )
    openManga(mangas[0])

   downloadVolume(mangas[0], 1)
//    downloadCapitolo(URI("https://www.mangaworld.in/manga/678/toukyou-ghoul/read/5f77d61e15ab860853c04ad3"))
}