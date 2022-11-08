package com.tuxdave.manga_downloader_ita.telegram_ui.sessions

import com.tuxdave.manga_downloader_ita.core_shared.entity.Manga
import com.tuxdave.manga_downloader_ita.openManga
import com.tuxdave.manga_downloader_ita.scraper.SearchOrderParam
import com.tuxdave.manga_downloader_ita.scraper.search
import com.tuxdave.manga_downloader_ita.telegram_ui.MangaBot
import java.util.*

fun Manga.toStringTG(): String{
    return "Titolo: $titolo\n" +
            "Link: $ref\n" +
            "Tipo: $tipo\n" +
            //"Stato: ${stato.toString().lowercase(Locale.getDefault()).split("_").joinToString(separator = " "){"${it.capitalize()}"}}\n" + TODO: fixare che non li riconosce giusti il parser
            "Autore: ${autore?.nome}, ${autore?.ref}\n" +
            "Autore: ${artista?.nome}, ${artista?.ref}\n" +
            "Generi: ${generi.joinToString { "${it.nome}" }}\n\n" +
            "Storia: $storia\n\n"
}

class InfoSession(_user: Long, bot: MangaBot) : Session(_user, bot) {
    override val actions: List<Pair<String, (msg: String) -> Unit>> = listOf(
        Pair("Cerca un manga per nome", ::step1Info)
    )

    private fun step1Info(nome: String): Unit {
        val mangas = search(nome, SearchOrderParam.MOST_READ)
        println(mangas)
        bot.send(user, "0%")
        for(manga in mangas){
            openManga(manga)
            bot.send(user, "${(mangas.indexOf(manga)+1) * 100 / mangas.size}%")
        }
        for(manga in mangas){
            bot.send(user, manga.toStringTG())
        }
    }
}