package com.tuxdave.manga_downloader_ita.telegram_ui.sessions

import com.tuxdave.manga_downloader_ita.scraper.PercentageListener
import com.tuxdave.manga_downloader_ita.scraper.SearchOrderParam
import com.tuxdave.manga_downloader_ita.scraper.search
import com.tuxdave.manga_downloader_ita.telegram_ui.MangaBot

class SearchSession(user: Long, bot: MangaBot): Session(user, bot) {

    override val actions: List<Pair<String, (data: String) -> Unit>> = listOf(
        Pair("Inserisci il nome del manga da cercare...", ::step1Search)
    )

    private val searchListener = PercentageListener(
        user.toString()
    ) { percentage, _ -> bot.send(user, "$percentage%") }

    private fun step1Search(nome: String): Unit {
        val mangas = search(
            nome,
            SearchOrderParam.MOST_READ,
            listeners = listOf(searchListener)
        )
        for(manga in mangas){
            bot.send(user, "${manga.titolo}\n${manga.ref}")
        }
    }
}