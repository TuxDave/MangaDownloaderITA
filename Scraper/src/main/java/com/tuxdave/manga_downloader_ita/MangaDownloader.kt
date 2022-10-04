package com.tuxdave.manga_downloader_ita

import com.tuxdave.manga_downloader_ita.entity.Manga
import com.tuxdave.manga_downloader_ita.exception.MangaException
import com.tuxdave.manga_downloader_ita.exception.VolumeOutOfRangeException
import org.apache.commons.io.FileUtils
import org.jsoup.Jsoup
import java.io.ByteArrayInputStream
import java.io.File
import java.net.URI
import java.net.URL
import javax.imageio.stream.ImageInputStream
import javax.imageio.stream.ImageInputStreamImpl

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

@Throws(MangaException::class, VolumeOutOfRangeException::class)
fun downloadVolume(manga: Manga, vol: Int): Unit {
    if (!manga.open) {
        throw MangaException("Aprire il manga prima di tentare di scaricarlo")
    }
    if (vol <= 0 || vol > (manga.volumiTotali ?: 0)) {
        throw VolumeOutOfRangeException("Volume non disponibile per il manga selezionato (\"${manga.titolo}\")")
    }

    val page = Jsoup.connect(manga.ref.toString()).get()
    val vol = page.getElementsByClass("volume-element")[(manga.volumiTotali!! - 1) - (vol-1)]
    val temp = vol
        .getElementsByClass("volume-chapters")[0]
        .getElementsByClass("chapter")
        .reversed()
    val caps = mutableListOf<URI>()
    for(t in temp){
        caps.add(URI(t.getElementsByTag("a")[0].attr("href")))
    }
    val images: MutableList<List<ByteArray>> = mutableListOf()
    for(cap in caps){
        //TODO: Aggiungi un listener
        images.add(downloadCapitolo(cap))
    }
}

fun downloadCapitolo(cap: URI): List<ByteArray> {
    val jPage = Jsoup.connect(cap.toString()).get()
    val pagesCount = jPage
        .getElementsByClass("page")[0]
        .getElementsByTag("option")[0]
        .html().split("/")[1].toInt()
    val images: MutableList<ByteArray> = mutableListOf()
    var file: File = File(".temp")
    for(page in 1 .. pagesCount){
        val imagePage = Jsoup.connect("${cap.toString()}/$page").get()
        val link = URL(
            imagePage
                .getElementById("image-loader")!!
                .parent()!!
                .getElementsByTag("img")[0]
                .attr("src")
        )
        //TODO: Aggiungere un listener
        file = File(".temp")
        val i = FileUtils.copyURLToFile(link, file, 10000, 10000)
        images.add(FileUtils.openInputStream(file).readBytes())
    }
    file.deleteOnExit()
    return images.toList()
}

//TODO: scaricare tutto il manga (tutti i volumi)

//TODO: Vedi per la creazione in un altro file kt dei PDF (e poi EBOOK)
// https://www.baeldung.com/java-pdf-creation
// https://pdfbox.apache.org/docs/2.0.3/javadocs/org/apache/pdfbox/pdmodel/graphics/image/PDImageXObject.html
// https://pdfbox.apache.org/docs/2.0.3/javadocs/org/apache/pdfbox/pdmodel/graphics/image/JPEGFactory.html#createFromStream(org.apache.pdfbox.pdmodel.PDDocument,%20java.io.InputStream)