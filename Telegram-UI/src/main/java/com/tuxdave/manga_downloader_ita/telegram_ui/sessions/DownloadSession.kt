package com.tuxdave.manga_downloader_ita.telegram_ui.sessions

import com.tuxdave.manga_downloader_ita.core_shared.entity.Manga
import com.tuxdave.manga_downloader_ita.core_shared.export.exportPDF
import com.tuxdave.manga_downloader_ita.scraper.*
import com.tuxdave.manga_downloader_ita.telegram_ui.MangaBot
import java.io.File
import java.util.*

fun String.isInt(): Boolean{
    return try{
        this.toInt()
        true
    }catch (e: NumberFormatException){
        false
    }
}

class DownloadSession(_user: Long, bot: MangaBot) : Session(_user, bot) {
    override val actions: List<Pair<String, (msg: String) -> Unit>> = listOf(
        Pair("Cerca il manga da scaricare...", ::step1Search),
        Pair("Inserisci il numero del manga da scaricare...", ::step2Select),
        Pair("Quali volumi vuoi scaricare?\n" +
                "X = TUTTI (potrebbe richiedere parecchio tempo)\n" +
                "8 = solo il capitolo indicato\n" +
                "1,4,5 = solo i capitoli indicati\n" +
                "3-7 = dal capitolo 3 al 7 inclusi\n" +
                "EXIT = per uscire", ::step3SelectVolumi)
    )

    private var step1Data: List<Manga>? = null
    private fun step1Search(nome: String): Unit {
        val mangas = search(nome, SearchOrderParam.MOST_READ)
        bot.send(user, "0%")
        for(manga in mangas){
            openManga(manga)
            bot.send(user, "${(mangas.indexOf(manga)+1) * 100 / mangas.size}%")
        }
        var c = 1
        step1Data = mangas
        for(manga in mangas){
            bot.send(user, "${c++}: ${manga.titolo}")
        }
    }

    private var step2Selected: Manga? = null
    private fun step2Select(msg: String): Unit {
        try{
            val n = msg.toInt()
            if(n < 0 || n > (step1Data?.size ?: 0)){
                throw NumberFormatException("")
            }else{
                step2Selected = (step1Data!![n-1])
                openManga(step2Selected!!)
                bot.send(user, "Manga selezionato:")
                bot.send(user, step2Selected!!.toStringTGOpened())
            }
        }catch (e: NumberFormatException){
            bot.send(user, "Inserire un numero valido!")
            step--
            return
        }
    }

    private fun step3SelectVolumi(msg: String): Unit {
        fun retry(): Unit {
            bot.send(user, "Non capisco, riprova o scrivi 'EXIT' per annullare...")
            step--
            next()
            return
        }

        val msg = msg.lowercase(Locale.getDefault())
        var toDownload = mutableListOf<Int>()
        if (msg == "exit") {
            step = actions.size
            return
        } else if (msg == "x") {
            for (cap in 1..(step2Selected?.volumiTotali ?: 0)) {
                toDownload.add(cap)
            }
        } else if (msg.isInt()) {
            toDownload.add(msg.toInt())
        } else if (msg.contains("-")) {
            var bounds = msg.split("-")
            if (bounds.size == 2 && bounds[0].isInt() && bounds[1].isInt()) {
                if (bounds[0] == bounds[1]) {
                    toDownload.add(bounds[0].toInt())
                } else {
                    bounds = bounds.sortedBy { it.toInt() }
                    val l = bounds[0].toInt()
                    val h = bounds[1].toInt()
                    for (vol in l..h) {
                        toDownload.add(vol)
                    }
                }
            } else {
                retry()
                return
            }
        } else if (msg.contains(",")) {
            val temp = msg.split(",")
            for (v in temp) {
                if (!v.isInt()) {
                    retry()
                    return
                }
            }
            toDownload = List<Int>(temp.size) { temp[it].toInt() }.toMutableList()
        } else {
            retry()
            return
        }
        toDownload.sort()
        val from = toDownload.min()
        val to = toDownload.max()
        val skip = mutableListOf<Int>()
        for(v in from .. to){
            if(v !in toDownload){
                skip.add(v)
            }
        }
        bot.send(user,"Inizio lo scaricamento dei volumi selezionati...")

        val raccolta = downloadManga(
            step2Selected!!,
            listOf(
                PercentageListener("Completamento Manga") { p, _ -> bot.send(user, "Manga: ${p}%") }
            ),
            listOf(
                PercentageListener("Completamento Volume") { p, _ -> bot.send(user, "\tVolume: ${p}%") }
            ),
            from = from,
            to = to,
            skip = skip.toIntArray()
        )

        bot.send(user, "Scaricamento completato, attendi di ricevere il pdf!")

        val file = File(System.getProperty("user.home") + "/.tuxdave/MangaDownloaderITA/temp/${user}.pdf")
        file.createNewFile()

        exportPDF(raccolta, file)

        bot.send(user, file)
        file.delete()
    }
}