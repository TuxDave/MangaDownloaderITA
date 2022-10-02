package com.tuxdave.manga_downloader_ita

import com.tuxdave.manga_downloader_ita.entity.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.lang.Exception
import java.net.URI

enum class SearchOrderParam {
    A_Z,
    MOST_READ;

    override fun toString(): String {
        return super.toString().replace("_","-")
    }
}

class SearchProgressionListener(
    val activationFunction: (percentage: Int) -> Unit
){
    fun pock(percentage: Int){
        activationFunction(percentage)
    }
}

fun search(
    name: String,
    order: SearchOrderParam = SearchOrderParam.MOST_READ,
    listeners: Array<SearchProgressionListener> = arrayOf()
): List<Manga>{
    for(listener in listeners) listener.pock(0)

    val result = Jsoup.connect("${SITE_BASE}archive?keyword=${name}&sort=${order.toString()}")
        .get()
    val body = result.body()
    val pages = (body.toString().split("totalPages\":")[1].split(",")[0]).toInt()
    val entries = body
        .getElementsByClass("comics-grid")[0]
        .getElementsByClass("entry")

    if(pages >= 2){
        var page: Element? = null
        var pageEntries: Elements? = null
        for(i in 1 .. pages){
            page = Jsoup.connect("${SITE_BASE}archive?keyword=${name}&sort=${order.toString()}&page=$i")
                .get()
                .body()
            pageEntries = page
                .getElementsByClass("comics-grid")[0]
                .getElementsByClass("entry")
            for(entry in pageEntries){
                entries.add(entry)
            }
            if(i != pages)
                for(l in listeners){
                    l.pock(i*100/pages)
                }
        }
    }

    println(entries[1])

    val mangas = mutableListOf<Manga>();
    run{
        var manga: Manga
        for(entry in entries){
            mangas.add(parseManga(entry))
        }
    }

    for(listener in listeners){
        listener.pock(100)
    }

    return mangas
}

private fun parseManga(html: Element): Manga{
    var ref: URI? = null
    try{
        ref = URI(html.getElementsByClass("manga-title")[0].attr("href"))
    }catch (e: Exception){/*se l'url del link è mal formato, isi amen rimane null*/}
    var imgRef: URI? = null
    try{
        imgRef = URI(
            html
                .getElementsByTag("a")[0]
                .getElementsByTag("img")[0]
                .attr("src")
        )
    }catch (_: Exception){} //ignored eghein (sì in italiese)

    var refArtista: URI? = null
    try {
        refArtista = URI(html.getElementsByClass("artist")[0].getElementsByTag("a")[0].attr("href"))
    }catch (_: Exception){}
    val artista: Artista = Artista(
        html.getElementsByClass("artist")[0].getElementsByTag("a")[0].html(),
        refArtista
    )
    var refAutore: URI? = null
    try{
        refAutore = URI(html.getElementsByClass("Author")[0].getElementsByTag("a")[0].attr("href"))
    }catch (_: Exception){}
    val autore = Autore(
        html.getElementsByClass("author")[0].getElementsByTag("a")[0].html(),
        refAutore
    )
    var refGenere: URI? = null

    var generi = mutableListOf<Genere>()
    run{
        val unparsedGenres = html.getElementsByClass("genres")[0].getElementsByTag("a")
        for(genere in unparsedGenres){
            generi.add(
                Genere(
                    genere.html(),
                    try { URI(genere.attr("href")) } catch (e: Exception) { null }
            )
            )
        }
    }

    val html = html.getElementsByClass("content")[0]
    return Manga(
        titolo = html.getElementsByClass("manga-title")[0].html(),
        ref = ref,
        artista = artista,
        autore = autore,
        generi = generi.toList(),
        stato = Stato.get(html.getElementsByClass("status")[0].html()),
        tipo = html.getElementsByClass("genre")[0].getElementsByTag("a")[0].html(),
        storia = html.getElementsByClass("story")[0].html().split("</span>")[1].trim(),
        imgLink = imgRef
    )
}