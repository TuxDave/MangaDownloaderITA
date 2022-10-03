package com.tuxdave.manga_downloader_ita

import com.tuxdave.manga_downloader_ita.entity.Manga
import org.jsoup.Jsoup

/*
* questo file si occupa di, dato un manga, scaricare in ram tutte le
* */

fun openManga(manga: Manga): Unit {
    val page = Jsoup.connect(manga.ref.toString()).get()
    var cap: Int = 0
    var vol: Int = 0

    val spans = page.getElementsByTag("span")
    for (poss in spans) {
        try {
            if (poss.html().trim() == "Capitoli totali:") {
                cap = poss.parent()?.getElementsByTag("span")?.get(1)?.html()?.toInt() ?: 0
            }
            if (poss.html().trim() == "Volumi totali:") {
                vol = poss.parent()?.getElementsByTag("span")?.get(1)?.html()?.toInt() ?: 0
            }
        } catch (_: Exception) {
        }
    }

    manga.open(cap, vol)
}