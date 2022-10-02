package com.tuxdave.manga_downloader_ita

import com.tuxdave.manga_downloader_ita.entity.Manga
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.net.URL

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
    println(entries[1])
    return mangas
}

private fun parseManga(html: Element): Manga{
    //TODO: comporre URL in modo che se si rompe non mi rompe tutta la ricerca
    val html = html.getElementsByClass("content")[0]
    return Manga(
        titolo = html.getElementsByClass("manga-title")[0].html(),
        ref = ref
    )
}