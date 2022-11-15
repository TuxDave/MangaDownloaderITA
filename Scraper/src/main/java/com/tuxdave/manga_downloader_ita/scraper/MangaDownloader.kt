package com.tuxdave.manga_downloader_ita.scraper

import com.tuxdave.manga_downloader_ita.core_shared.entity.Manga
import com.tuxdave.manga_downloader_ita.core_shared.exception.MangaException
import com.tuxdave.manga_downloader_ita.core_shared.exception.VolumeOutOfRangeException
import com.tuxdave.manga_downloader_ita.core_shared.view.Capitolo
import com.tuxdave.manga_downloader_ita.core_shared.view.Raccolta
import com.tuxdave.manga_downloader_ita.core_shared.view.Volume
import org.apache.commons.io.FileUtils
import org.jsoup.Jsoup
import java.io.File
import java.net.URI
import java.net.URL

/*
* questo file si occupa di, dato un manga, scaricare in ram tutte le informazioni
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
        } catch (_: Exception) {
        }
    }
    try {
        vol = page.getElementsByClass("volume-name")[0].html().split("Volume ")[1].toInt()
    } catch (_: Exception) {
    }
    manga.open(cap, vol)
}

@kotlin.jvm.Throws(MangaException::class, VolumeOutOfRangeException::class)
fun downloadVolume(
    manga: Manga,
    vol: Int,
    listeners: List<PercentageListener> = listOf(),
    capitoliListeners: List<PercentageListener> = listOf()
): Volume {
    if (!manga.open) {
        throw MangaException("Aprire il manga prima di tentare di scaricarlo")
    }
    if (vol <= 0 || vol > (manga.volumiTotali ?: 0)) {
        throw VolumeOutOfRangeException("Volume non disponibile per il manga selezionato (\"${manga.titolo}\")")
    }

    listeners.pock(0)

    val page = Jsoup.connect(manga.ref.toString()).get()
    val volPage = page.getElementsByClass("volume-element")[(manga.volumiTotali!! - 1) - (vol - 1)]
    val temp = volPage
        .getElementsByClass("volume-chapters")[0]
        .getElementsByClass("chapter")
        .reversed()
    val caps = mutableListOf<URI>()
    for (t in temp) {
        caps.add(URI(t.getElementsByTag("a")[0].attr("href")))
    }
    val images: MutableList<Capitolo> = mutableListOf()
    for (cap in 0 until caps.size) {
        listeners.pock(cap * 100 / caps.size)
        images.add(downloadCapitolo(caps[cap], capitoliListeners))
    }
    listeners.pock(100)
    return Volume(images.toList(), vol)
}

fun downloadCapitolo(cap: URI, listeners: List<PercentageListener> = listOf()): Capitolo {
    val jPage = Jsoup.connect(cap.toString()).get()

    var capNumber: Int = -1
    run{
        try{
            capNumber = jPage
                .getElementsByClass("chapter")[0]
                .getElementsByTag("option")
                .select("option[selected]")[0]
                .html().split(" ")[1].toInt()
        }catch (_: Exception){
            var options = jPage
                .getElementsByClass("chapter")[0]
                .getElementsByTag("option")
            val path = cap.toString().split("read/")[1]
            for(option in options){
                if(option.attr("value") == path){
                    capNumber = option.html().split(" ")[1].toInt()
                }
            }
        }
    }

    listeners.pock(0)

    val pagesCount = jPage
        .getElementsByClass("page")[0]
        .getElementsByTag("option")[0]
        .html().split("/")[1].toInt()
    val images: MutableList<ByteArray> = mutableListOf()
    var file: File = File(".temp")
    for (page in 1..pagesCount) {
        val imagePage = Jsoup.connect("${cap.toString()}/$page").get()
        val link = URL(
            imagePage
                .getElementById("image-loader")!!
                .parent()!!
                .getElementsByTag("img")[0]
                .attr("src")
        )
        file = File(".temp")
        val i = FileUtils.copyURLToFile(link, file, 10000, 10000)
        listeners.pock(page * 100 / pagesCount)
        images.add(FileUtils.openInputStream(file).readBytes())
    }
    file.deleteOnExit()
    listeners.pock(100)
    return Capitolo(images.toList(), capNumber)
}

@kotlin.jvm.Throws(MangaException::class)
fun downloadManga(
    manga: Manga,
    listeners: List<PercentageListener> = listOf(),
    volumiListeners: List<PercentageListener> = listOf(),
    capitoliListeners: List<PercentageListener> = listOf(),
    from: Int = 1,
    to: Int = manga.volumiTotali ?: 1,
    skip: Array<Int> = arrayOf()
): Raccolta {
    if (!manga.open) {
        throw MangaException("Aprire il manga prima di tentare di scaricarlo")
    }
    if (manga.volumiTotali == null || manga.volumiTotali == 0) {
        throw VolumeOutOfRangeException("Il manga selezionato non ha volumi disponibili")
    }
    var to = to
    if(to > manga.volumiTotali!!){
        to = manga.volumiTotali!!
    }
    var from = from
    if(from < 1){
        from = 1
    }
    val skip = skip.toMutableList()
    for(vol in skip){
        if (vol !in from .. to){
            skip.remove(vol)
        }
    }

    listeners.pock(0)

    val complete = mutableListOf<Volume>()
    for (vol in from.. to) {
        if(vol in skip){
            continue
        }
        val n = vol * 100 / (to - from - skip.size + 1)
        listeners.pock(n)
        complete.add(downloadVolume(manga, vol, volumiListeners, capitoliListeners))
    }
    listeners.pock(100)
    return Raccolta(manga, complete)
}