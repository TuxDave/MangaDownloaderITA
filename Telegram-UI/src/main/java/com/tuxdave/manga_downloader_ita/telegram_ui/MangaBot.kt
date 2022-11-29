package com.tuxdave.manga_downloader_ita.telegram_ui

import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendDocument
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import java.io.File

class MangaBot(private val tk: String) : TelegramLongPollingBot() {
    init {
        messageDispatcher.bot = this
    }

    override fun getBotToken(): String {
        return tk
    }

    override fun getBotUsername(): String {
        return "MangaDownloader_ITA_BOT"
    }

    override fun onUpdateReceived(data: Update?) {
        if (data == null) return
        messageDispatcher.dispatch(data.message)
    }

    fun send(who: Long, what: String): Unit {
        execute(SendMessage(who.toString(), what))
    }

    fun send(who: Long, what: File): Unit{
        execute(SendDocument(who.toString(), InputFile(what)))
    }
}