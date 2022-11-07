package com.tuxdave.manga_downloader_ita.telegram_ui

import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("Inserire come parametro soltanto il TK del bot")
        return
    }

    val api = TelegramBotsApi(DefaultBotSession::class.java) //TODO: vedi come funziona
    try {
        api.registerBot(MangaBot(args[0]))
    } catch (e: TelegramApiRequestException) {
        println("Bot non avviato correttamente")
        e.printStackTrace()
    }
}