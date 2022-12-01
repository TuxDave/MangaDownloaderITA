package com.tuxdave.manga_downloader_ita.telegram_ui

import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import java.io.File

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("Inserire come parametro soltanto il TK del bot")
        return
    }

    File("${System.getProperty("user.home")}/.tuxdave").mkdir()
    File("${System.getProperty("user.home")}/.tuxdave/MangaDownloaderITA/").mkdir()
    File("${System.getProperty("user.home")}/.tuxdave/MangaDownloaderITA/temp/").mkdir()

    val api = TelegramBotsApi(DefaultBotSession::class.java)
    try {
        api.registerBot(MangaBot(args[0]))
    } catch (e: TelegramApiRequestException) {
        println("Bot non avviato correttamente")
        e.printStackTrace()
    }
}